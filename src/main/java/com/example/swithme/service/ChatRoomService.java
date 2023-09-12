package com.example.swithme.service;

import com.example.swithme.dto.chat.*;
import com.example.swithme.entity.User;
import com.example.swithme.entity.chat.ChatGroup;
import com.example.swithme.entity.chat.ChatMessage;
import com.example.swithme.entity.chat.ChatRoom;
import com.example.swithme.enumType.ChatRole;
import com.example.swithme.enumType.Invite;
import com.example.swithme.repository.ChatGroupRepository;
import com.example.swithme.repository.ChatMessageRepository;
import com.example.swithme.repository.ChatRoomRepository;
import com.example.swithme.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final UserRepository userRepository;
    private final ChatGroupRepository chatGroupRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;


    // 내가 가입된 채팅방 조회
    public List<ChatRoomResponseDto> findAllById(User user) {
        List<ChatRoom> findChatGroup =
                chatGroupRepository.findAllByUserAndInvite(user, Invite.YES).stream()
                        .map(chatGroup -> chatGroup.getChatRoom())
                        .collect(Collectors.toList());

        List<ChatRoomResponseDto> findAllChatRoom = findChatGroup.stream()
                .map(ChatRoomResponseDto::new)
                .collect(Collectors.toList());
        return findAllChatRoom;
    }

    public List<ChatRoomResponseDto> findAllInvite(User user) {
        List<ChatRoom> findChatGroup =
                chatGroupRepository.findAllByUserAndInvite(user, Invite.WAIT).stream()
                        .map(chatGroup -> chatGroup.getChatRoom())
                        .collect(Collectors.toList());

        List<ChatRoomResponseDto> findAllChatRoom = findChatGroup.stream()
                .map(ChatRoomResponseDto::new)
                .collect(Collectors.toList());
        return findAllChatRoom;
    }

    //채팅룸 생성
    public void create(User user) {
        ChatRoom chatRoom = new ChatRoom("제목", "내용", UUID.randomUUID());
        chatRoomRepository.save(chatRoom);

        ChatGroup chatGroup = new ChatGroup(user, chatRoom, ChatRole.MANAGER, Invite.YES);
        chatGroupRepository.save(chatGroup);
    }

    //채팅룸 조회
    public ChatRoomResponseDto find(Long id) {
        ChatRoom chatRoom = chatRoomRepository.findById(id).orElseThrow();
        return new ChatRoomResponseDto(chatRoom);
    }

    @Transactional
    public void edit(ChatRoomRequestDto chatRoomRequestDto, User user) {
        Optional<ChatGroup> findChatGroup = chatGroupRepository.findByChatRoom_IdAndAndUser(chatRoomRequestDto.getId(), user);
        if (findChatGroup.isPresent()) {
            ChatRoom findChatRoom = chatRoomRepository.findById(chatRoomRequestDto.getId()).orElseThrow();
            findChatRoom.update(chatRoomRequestDto);
        } else {

        }
    }

    public void invite(ChatRoomInviteRequestDTo chatRoomInviteRequestDTo, User user) {
        // 채팅방 번호와 유저로 내가 속한 채팅그룹을 찾음
        Optional<ChatGroup> findChatGroup = chatGroupRepository.findByChatRoom_IdAndAndUser(chatRoomInviteRequestDTo.getId(), user);
        ChatGroup chatGroup = findChatGroup.orElseThrow();

        String username = chatRoomInviteRequestDTo.getUsername(); // 초대할 유저 이름을 찾음
        User inviteUser = userRepository.findByUsername(username).orElseThrow();

        // 초대할 유저가 이미 초대되었는지 확인
        Optional<ChatGroup> findInviteUser = chatGroupRepository.findByChatRoom_IdAndAndUser(chatRoomInviteRequestDTo.getId(), inviteUser);

        // 채팅그룹에 권한을 조회해서, 내가 MANAGER이면 초대 권한을 가짐
        if (chatGroup.getChatRole().equals(ChatRole.MANAGER)) {
            if (!findInviteUser.isPresent()) {
                ChatGroup chatGroup1 = new ChatGroup(inviteUser, chatGroup.getChatRoom(), ChatRole.MEMBER, Invite.WAIT);
                chatGroupRepository.save(chatGroup1);
            }
        }
    }

    @Transactional
    public void inviteAccept(Long id, User user) {
        Optional<ChatGroup> findChatGroup = chatGroupRepository.findByChatRoom_IdAndAndUser(id, user);
        if (findChatGroup.isPresent()) {
            findChatGroup.get().setInvite(Invite.YES);
        }
    }

    @Transactional
    public void inviteDecline(Long id, User user) {
        Optional<ChatGroup> findChatGroup = chatGroupRepository.findByChatRoom_IdAndAndUser(id, user);
        if (findChatGroup.isPresent()) {
            chatGroupRepository.delete(findChatGroup.get());
        }
    }

    //채팅방 초대 또는 참가자 명단
    public List<ChatUserResponseDto> findUser(Long id) {
        ChatRoom chatRoom = chatRoomRepository.findById(id).orElseThrow();
        List<ChatGroup> findChatGroup = chatGroupRepository.findAllByChatRoom(chatRoom);


        //유저이름, 초대여부(승낙, 대기)
        List<ChatUserResponseDto> ChatUsers = findChatGroup.stream()
                .map(ChatUserResponseDto::new)
                .collect(Collectors.toList());

        return ChatUsers;

    }

    //채팅멤버 삭제
    public String delete(Long chatGroupId, User user) {
        // 내가 매니저이거나, 아니면 나 자신이 경우 삭제 가능
        ChatGroup findChatGroup = chatGroupRepository.findById(chatGroupId).get();
        ChatRoom chatRoom = findChatGroup.getChatRoom();

        ChatGroup chatGroup = chatGroupRepository.findByChatRoom_IdAndAndUser(chatRoom.getId(), user).get();
        if (chatGroup.getChatRole().equals(ChatRole.MANAGER)) {
            chatGroupRepository.deleteById(chatGroupId);
            return "삭제되었습니다.";
        }

        if(findChatGroup.getUser().getUserId() == user.getUserId()) {
            chatGroupRepository.deleteById(chatGroupId);
            return "삭제되었습니다.";
        } else {
            return "권한이 없습니다.";
        }

    }

    @Transactional
    //채팅방 삭제
    public void deleteChatRoom(Long id, User user) {
        Optional<ChatGroup> findChatGroup = chatGroupRepository.findByChatRoom_IdAndAndUser(id, user);
        ChatGroup chatGroup = findChatGroup.get();
        if (findChatGroup.isPresent() && (chatGroup.getChatRole().equals(ChatRole.MANAGER))) {
            chatRoomRepository.delete(findChatGroup.get().getChatRoom());
        } else {
            throw new IllegalArgumentException("삭제가 실패했습니다.");
        }
    }

    //전체 채팅 메시지 저장
    public void save(ChatMessage chatMessage) {
        chatMessageRepository.save(chatMessage);
    }

    public List<ChatMessageResponseDto> findMessage() {
        List<ChatMessageResponseDto> allMessage = chatMessageRepository.findAll().stream()
                .map(ChatMessageResponseDto::new)
                .collect(Collectors.toList());
        return allMessage ;
    }
}
