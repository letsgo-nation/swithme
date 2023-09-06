package com.example.swithme;

import com.example.swithme.dto.PostRequestDto;
import com.example.swithme.entity.*;
import com.example.swithme.entity.chat.ChatGroup;
import com.example.swithme.entity.chat.ChatRoom;
import com.example.swithme.enumType.ChatRole;
import com.example.swithme.enumType.UserRole;
import com.example.swithme.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.UUID;

@SpringBootTest
public class SaveTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    PostRepository postRepository;
    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    ChatRoomRepository chatRoomRepository;

    @Autowired
    ChatGroupRepository chatGroupRepository;

    @Test
    void saveTest() {
        //관리자 로그인
        User admin = new User("admin@naver.com", passwordEncoder.encode("1234"), "관리자_채팅개설",1);
        userRepository.save(admin);

        //일반 로그인
        User user = new User("spring@naver.com", passwordEncoder.encode("1234"), "스프링",1);
        userRepository.save(user);


//        //카테고리 생성
//        Category category1 = new Category();
//        category1.setName("프론트");
//        categoryRepository.save(category1);
//
//        Category category2 = new Category();
//        category2.setName("백엔드");
//        categoryRepository.save(category2);

//        //글 작성
//        for (int i = 0; i < 10; i++) {
//            PostRequestDto postRequestDto = new PostRequestDto();
//            postRequestDto.setTitle("title" + i);
//            postRequestDto.setContent("content" + i);
//            Post post = new Post(postRequestDto,user, category1);
//
//            postRepository.save(post);
//        }
    }

    // 채팅룸 생성 (DB에서 1번 사용자를 ADMIN으로 바꾸고 실행해야 함)
    @Test
    void saveTest2() {
        // 관리자가 1번에 있다고
        List<User> findAdmin = userRepository.findAllByRole(UserRole.ADMIN);
        User user = findAdmin.get(0);
        ChatRoom chatRoom1 = new ChatRoom("프로그래밍", "프로그래밍에 대한 다양한 정보를 나눠요.", "front", UUID.randomUUID());
        ChatRoom chatRoom2 = new ChatRoom("수능", "수능에 대한 다양한 정보를 나눠요.", "backend", UUID.randomUUID());
        ChatRoom chatRoom3 = new ChatRoom("공무원시험", "공무원시험에 대한 다양한 정보를 나눠요.", "network", UUID.randomUUID());
        ChatRoom chatRoom4 = new ChatRoom("어학", "어학에 대한 다양한 정보를 나눠요.", "game", UUID.randomUUID());
        ChatRoom chatRoom5 = new ChatRoom("자격증", "자격증에 대한 다양한 정보를 나눠요.", "game", UUID.randomUUID());

        chatRoomRepository.save(chatRoom1);
        chatRoomRepository.save(chatRoom2);
        chatRoomRepository.save(chatRoom3);
        chatRoomRepository.save(chatRoom4);
        chatRoomRepository.save(chatRoom5);

        ChatGroup chatGroup1 = new ChatGroup(user, chatRoom1, ChatRole.MANAGER);
        ChatGroup chatGroup2 = new ChatGroup(user, chatRoom2, ChatRole.MANAGER);
        ChatGroup chatGroup3 = new ChatGroup(user, chatRoom3, ChatRole.MANAGER);
        ChatGroup chatGroup4 = new ChatGroup(user, chatRoom4, ChatRole.MANAGER);
        ChatGroup chatGroup5 = new ChatGroup(user, chatRoom5, ChatRole.MANAGER);

        chatGroupRepository.save(chatGroup1);
        chatGroupRepository.save(chatGroup2);
        chatGroupRepository.save(chatGroup3);
        chatGroupRepository.save(chatGroup4);
        chatGroupRepository.save(chatGroup5);
    }
}
