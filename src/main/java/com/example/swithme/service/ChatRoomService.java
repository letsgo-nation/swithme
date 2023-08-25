package com.example.swithme.service;

import com.example.swithme.dto.chat.ChatRoomResponseDto;
import com.example.swithme.entity.ChatRoom;
import com.example.swithme.entity.User;
import com.example.swithme.enumType.UserRole;
import com.example.swithme.repository.ChatGroupRepository;
import com.example.swithme.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final UserRepository userRepository;
    private final ChatGroupRepository chatGroupRepository;


    //관리자가 설정 채팅방
    public List<ChatRoomResponseDto> findAll() {
        // 규칙을 설정해야함, 관리를 위해서, 특정 관리자 계정만 채팅방 개설기능 가능
        List<User> findAdmin = userRepository.findAllByRole(UserRole.ADMIN);

        List<ChatRoom> findChatGroup =
                chatGroupRepository.findAllByUser(findAdmin.get(0)).stream()
                        .map(chatGroup -> chatGroup.getChatRoom())
                        .collect(Collectors.toList());

        List<ChatRoomResponseDto> findAllChatRoom = findChatGroup.stream()
                .map(ChatRoomResponseDto::new)
                .collect(Collectors.toList());
        return findAllChatRoom;
    }

    // 내가 가입된 채팅방 조회
    public List<ChatRoomResponseDto> findAllById(User user) {
        List<ChatRoom> findChatGroup =
                chatGroupRepository.findAllByUser(user).stream()
                .map(chatGroup -> chatGroup.getChatRoom())
                        .collect(Collectors.toList());

        List<ChatRoomResponseDto> findAllChatRoom = findChatGroup.stream()
                .map(ChatRoomResponseDto::new)
                .collect(Collectors.toList());
        return findAllChatRoom;
    }
}
