package com.example.swithme.chat;

import com.example.swithme.dto.chat.ChatRoomResponseDto;
import com.example.swithme.repository.ChatRoomRepository;
import com.example.swithme.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatRoomService chatRoomService;

    // 카테고리 페이지로 이동
    @GetMapping("/category")
    public String chatCategory(Model model) {
        List<ChatRoomResponseDto> findAllChatRoom = chatRoomService.findAll();
        model.addAttribute("chatRooms", findAllChatRoom);
        return "chat/chatCategory";
    }

    //채팅룸으로 이동 쿼리파라미터로 동적으로 방생성 구현
    @GetMapping("/room")
    public String chat() {
        return "chat/chatRoom";
    }

    @MessageMapping("/chat.sendMessage/{category}")
    @SendTo("/topic/public/{category}")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        return chatMessage;
    }

    @MessageMapping("/chat.addUser/{category}")
    @SendTo("/topic/public/{category}")
    public ChatMessage addUser(
            @Payload ChatMessage chatMessage,
            SimpMessageHeaderAccessor headerAccessor  // 이 부분은 나중에 로그인 사용자로 변환하면 될듯.
    ) {
        // Add username in web socket session
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        return chatMessage;
    }
}
