package com.example.swithme;

import com.example.swithme.dto.MyStudyRequestDto;
import com.example.swithme.entity.Category;
import com.example.swithme.entity.MyStudy;
import com.example.swithme.entity.User;
import com.example.swithme.repository.CategoryRepository;
import com.example.swithme.repository.MyStudyRepository;
import com.example.swithme.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

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

    @Test
    void saveTest() {

        //일반 로그인
        User user = new User("spring", passwordEncoder.encode("1234"), "스프링");
        userRepository.save(user);

        //관리자 로그인
        User admin = new User("admin", passwordEncoder.encode("1234"), "스프링");
        userRepository.save(admin);

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
}
