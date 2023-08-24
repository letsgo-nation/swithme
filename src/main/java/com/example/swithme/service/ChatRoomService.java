package com.example.swithme.service;

import com.example.swithme.dto.chat.ChatRoomResponseDto;
import com.example.swithme.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;


    public List<ChatRoomResponseDto> findAll() {
         List<ChatRoomResponseDto> findAllChatRoom = chatRoomRepository.findAll().stream()
                .map(ChatRoomResponseDto::new)
                .collect(Collectors.toList());
         return findAllChatRoom;
    }
}
