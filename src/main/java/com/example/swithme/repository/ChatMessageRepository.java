package com.example.swithme.repository;

import com.example.swithme.entity.chat.ChatMessage;
import com.example.swithme.entity.chat.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    List<ChatMessage> findAllByChatRoom(ChatRoom chatRoom);
}
