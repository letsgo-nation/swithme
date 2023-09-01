package com.example.swithme.controller;

import com.example.swithme.dto.chat.ChatRoomInviteRequestDTo;
import com.example.swithme.dto.chat.ChatRoomRequestDto;
import com.example.swithme.dto.chat.ChatRoomResponseDto;
import com.example.swithme.dto.chat.ChatUserResponseDto;
import com.example.swithme.entity.chat.ChatMessage;
import com.example.swithme.entity.User;
import com.example.swithme.enumType.MessageType;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatRoomService chatRoomService;

    // 사용자 채팅 전달
    // 전달 주소는 chatUrl에 따라 달라짐
    @MessageMapping("/chat.sendMessage/{chatUrl}")
    @SendTo("/topic/public/{chatUrl}")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {

//        var chatMessage = ChatMessage.builder()
//                .type(MessageType.JOIN)
//                .sender(userDetails.getUser().getNickname())
//                .build();

        return chatMessage;
    }

    // 사용자가 접속시, 접속 종료시 정보를 헤더에 저장
    // 사용자 접속시 대화창에 정보 표시
    // 주소는 chatUrl 에 따라 달라짐
    @MessageMapping("/chat.addUser/{chatUrl}")
    @SendTo("/topic/public/{chatUrl}")
    public ChatMessage addUser(
            @Payload ChatMessage chatMessage,
            SimpMessageHeaderAccessor headerAccessor, @DestinationVariable String chatUrl,@AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        // 유저이름을 웹소켓에 추가합니다. 채팅이 끊길 때 사용합니다.
        headerAccessor.getSessionAttributes().put("username", userDetails.getUser().getNickname());

        // 채탱 URL을 웹소켓에 추가합니다. 채팅이 끊길 때 사용합니다.
        headerAccessor.getSessionAttributes().put("disconnectUrl", chatUrl);

//        var chatMessage = ChatMessage.builder()
//                .type(MessageType.JOIN)
//                .sender(userDetails.getUser().getNickname())
//                .build();


        return chatMessage;
    }

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
        return "chat/personal";
    }

    // 개인 알림 조회 페이지 이동
    @GetMapping("/alert")
    public String chatInvite(@AuthenticationPrincipal UserDetailsImpl userDetails, Model model) {
        User user = userDetails.getUser();
        List<ChatRoomResponseDto> findAllChatRoom = chatRoomService.findAllInvite(user);
        model.addAttribute("chatRooms", findAllChatRoom);
        return "chat/chat_invite_alert";
    }

    //채팅룸으로 이동
    @GetMapping("/room")
    public String chat() {
        return "chat/chatRoom";
    }


    //채팅룸 생성
    @ResponseBody
    @PostMapping("/personal/room")
    public String creatChatRoom(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        chatRoomService.create(user);
        return "ok";
    }

    //수정 페이지로 이동
    @GetMapping("/room/edit")
    public String findChatRoom(@RequestParam Long id, Model model) {

        //채팅방 조회
        ChatRoomResponseDto chatRoom = chatRoomService.find(id);
        model.addAttribute("chatRoom", chatRoom);

        //채팅방 초대 또는 참가자 명단
        List<ChatUserResponseDto> inviteUser = chatRoomService.findUser(id);
        model.addAttribute("inviteUsers", inviteUser);


        return "chat/chatRoomEdit";
    }

    //채팅방 수정 : 제목, 타이틀
    @PostMapping("/room/edit")
    public String editChatRoom(@ModelAttribute ChatRoomRequestDto chatRoomRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        chatRoomService.edit(chatRoomRequestDto, user);
        return "redirect:/chat/personal";
    }

    //채팅 초대 요청 보내기
    @PostMapping("/room/invite")
    public String inviteChatRoom(@ModelAttribute ChatRoomInviteRequestDTo chatRoomInviteRequestDTo, @AuthenticationPrincipal UserDetailsImpl userDetails, Model model) {
        User user = userDetails.getUser();
        chatRoomService.invite(chatRoomInviteRequestDTo, user);

        ChatRoomResponseDto chatRoom = chatRoomService.find(chatRoomInviteRequestDTo.getId());
        model.addAttribute("chatRoom", chatRoom);
        return "chat/chatRoomEdit";
    }

    //채팅 초대 요청 수락
    @PostMapping("/room/invite/accept")
    public String inviteAccept(@RequestParam Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        chatRoomService.inviteAccept(id, user);
        return "chat/chat_invite_alert";
    }


    //채팅 초대 요청 거절
    @PostMapping("/room/invite/decline")
    public String inviteDecline(@RequestParam Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        chatRoomService.inviteDecline(id, user);
        return "chat/chat_invite_alert";
    }

    // 채팅룸 멤버 삭제
    @DeleteMapping("/room/member")
    @ResponseBody
    public String deleteMember(@RequestBody ChatRoomRequestDto chatRoomRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        chatRoomService.delete(chatRoomRequestDto.getId(), user);
        return "ok";
    }

    // 채팅룸 삭제
    @DeleteMapping("/chatroom")
    @ResponseBody
    public String deleteChatRoom(@RequestBody ChatRoomRequestDto chatRoomRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        chatRoomService.deleteChatRoom(chatRoomRequestDto.getId(), user);
        return "ok";
    }
}
