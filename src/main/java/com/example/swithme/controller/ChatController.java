package com.example.swithme.controller;

import com.example.swithme.dto.chat.ChatRoomResponseDto;
import com.example.swithme.entity.ChatMessage;
import com.example.swithme.entity.User;
import com.example.swithme.security.UserDetailsImpl;
import com.example.swithme.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    // 전체 채팅룸 조회페이지로 이동
    @GetMapping("/category")
    public String chatCategory(Model model) {
        List<ChatRoomResponseDto> findAllChatRoom = chatRoomService.findAll();
        model.addAttribute("chatRooms", findAllChatRoom);
        return "chat/chatCategory";
    }

    // 개인 채팅룸 조회 페이지 이동
    @GetMapping("/personal")
    public String chatPersonal(@AuthenticationPrincipal UserDetailsImpl userDetails, Model model) {
        User user = userDetails.getUser();
        List<ChatRoomResponseDto> findAllChatRoom = chatRoomService.findAllById(user);
        model.addAttribute("chatRooms", findAllChatRoom);
        return "chat/chatPersonal";
    }

    //채팅룸으로 이동 쿼리파라미터로 동적으로 방생성 구현
    @GetMapping("/room")
    public String chat() {
        return "chat/chatRoom";
    }


    // 사용자가 채팅시 채팅 전달 주소는 chatUrl에 따라 달라짐
    @MessageMapping("/chat.sendMessage/{chatUrl}")
    @SendTo("/topic/public/{chatUrl}")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        return chatMessage;
    }


    // 사용자가 최초 접속시 사용됨 주소는 chatUrl에 따라 달라짐
    @MessageMapping("/chat.addUser/{chatUrl}")
    @SendTo("/topic/public/{chatUrl}")
    public ChatMessage addUser(
            @Payload ChatMessage chatMessage,
            SimpMessageHeaderAccessor headerAccessor, @DestinationVariable String chatUrl  // 이 부분은 나중에 로그인 사용자로 변환하면 될듯.
    ) {
        // 유저이름을 웹소켓에 추가합니다. 채팅이 끊길 때 사용합니다.
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());

        // 채탱 URL을 웹소켓에 추가합니다. 채팅이 끊길 때 사용합니다.
        headerAccessor.getSessionAttributes().put("disconnectUrl", chatUrl);
        return chatMessage;
    }
}
