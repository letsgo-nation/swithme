package com.example.swithme;

import com.example.swithme.dto.MyStudyRequestDto;
import com.example.swithme.entity.*;
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
    MyStudyRepository myStudyRepository;
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
        User admin = new User("admin", passwordEncoder.encode("1234"), "관리자_채팅개설");
        userRepository.save(admin);

        //일반 로그인
        User user = new User("spring", passwordEncoder.encode("1234"), "스프링");
        userRepository.save(user);


        //카테고리 생성
        Category category1 = new Category();
        category1.setName("프론트");
        categoryRepository.save(category1);

        Category category2 = new Category();
        category2.setName("백엔드");
        categoryRepository.save(category2);

        //글 작성
        for (int i = 0; i < 10; i++) {
            MyStudyRequestDto myStudyRequestDto = new MyStudyRequestDto();
            myStudyRequestDto.setTitle("title" + i);
            myStudyRequestDto.setContent("content" + i);
            MyStudy myStudy = new MyStudy(myStudyRequestDto,user, category1);

            myStudyRepository.save(myStudy);
        }
    }

    // 채팅룸 생성 (DB에서 1번 사용자를 ADMIN으로 바꾸고 실행해야 함)
    @Test
    void saveTest2() {
        // 관리자가 1번에 있다고
        List<User> findAdmin = userRepository.findAllByRole(UserRole.ADMIN);
        User user = findAdmin.get(0);
        ChatRoom chatRoom1 = new ChatRoom("프론트엔드", "프론트에 대한 다양한 주제를 나눠요.", "front", UUID.randomUUID());
        ChatRoom chatRoom2 = new ChatRoom("백엔드", "프론트에 대한 다양한 주제를 나눠요.", "backend", UUID.randomUUID());
        ChatRoom chatRoom3 = new ChatRoom("네트워크", "네트워크에 대한 다양한 주제를 나눠요.", "network", UUID.randomUUID());
        ChatRoom chatRoom4 = new ChatRoom("게임 개발", "게임개발에 대한 다양한 주제를 나눠요.", "game", UUID.randomUUID());

        chatRoomRepository.save(chatRoom1);
        chatRoomRepository.save(chatRoom2);
        chatRoomRepository.save(chatRoom3);
        chatRoomRepository.save(chatRoom4);

        ChatGroup chatGroup1 = new ChatGroup(user, chatRoom1);
        ChatGroup chatGroup2 = new ChatGroup(user, chatRoom2);
        ChatGroup chatGroup3 = new ChatGroup(user, chatRoom3);
        ChatGroup chatGroup4 = new ChatGroup(user, chatRoom4);
        chatGroupRepository.save(chatGroup1);
        chatGroupRepository.save(chatGroup2);
        chatGroupRepository.save(chatGroup3);
        chatGroupRepository.save(chatGroup4);
    }
}
