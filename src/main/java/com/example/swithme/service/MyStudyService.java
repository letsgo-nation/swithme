package com.example.swithme.service;

import com.example.swithme.dto.ApiResponseDto;
import com.example.swithme.dto.MyStudyRequestDto;
import com.example.swithme.dto.MyStudyResponseDto;
import com.example.swithme.entity.Category;
import com.example.swithme.entity.MyStudy;
import com.example.swithme.entity.User;
import com.example.swithme.repository.CategoryRepository;
import com.example.swithme.repository.MyStudyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j(topic = "My Study Service")
@Service
@RequiredArgsConstructor
public class MyStudyService {

    private final MyStudyRepository myStudyRepository;
    private final CategoryRepository categoryRepository;
//    private final CommentRepository commentRepository;

    // 개인 스터디 게시물 생성
    public ApiResponseDto createMyStudy(MyStudyRequestDto requestDto, User user) {

        Optional<Category> category = categoryRepository.findById(requestDto.getCategory_id());
        // RequestDto -> Entity
        MyStudy myStudy = new MyStudy(requestDto, user, category.get());
        // DB 저장
        MyStudy saveMyStudy = myStudyRepository.save(myStudy);
        // Entity -> ResponseDto
        return new ApiResponseDto(HttpStatus.OK.value(), "개인 스터디 게시물 생성 완료", saveMyStudy);
    }

    // 전체 개인 스터디 게시글 조회
    public List<MyStudyResponseDto> getMyStudies() {
        return myStudyRepository.findAllByOrderByModifiedAtDesc().stream().map(MyStudyResponseDto::new).toList();
    }

    // 개인 스터디 단건 조회
    public MyStudyResponseDto lookupMyStudy(Long id) {
        MyStudy myStudy = findMyStudy(id);
        return new MyStudyResponseDto(myStudy);
    }

    // 개인 스터디 게시물 수정
    @Transactional
    public ResponseEntity<ApiResponseDto> updateMyStudy(Long id, MyStudyRequestDto myStudyRequestDto, User user) {
        Optional<MyStudy> myStudy = myStudyRepository.findById(id);
        Optional<Category> category = categoryRepository.findById(myStudyRequestDto.getCategory_id());

        if (!myStudy.isPresent() || !Objects.equals(myStudy.get().getUser().getUsername(), user.getUsername())) {
            log.error("게시글이 존재하지 않거나 게시글 작성자가 아닙니다.");
            return ResponseEntity.status(400).body(new ApiResponseDto("게시글 수정 실패", HttpStatus.BAD_REQUEST.value()));
        }

        if (!category.isPresent()) {
            log.error("해당하는 카테고리가 존재하지 않습니다.");
            return ResponseEntity.status(400).body(new ApiResponseDto( "게시글 수정 실패", HttpStatus.BAD_REQUEST.value()));
        }

        myStudy.get().update(myStudyRequestDto, category.get());

//        return new ApiResponseDto(HttpStatus.OK.value(), "게시글 수정 성공", new MyStudyResponseDto(myStudy.get()));
        return ResponseEntity.status(200).body(new ApiResponseDto(HttpStatus.OK.value(), "게시글 수정 성공", new MyStudyResponseDto(myStudy.get())));
    }

    // 개인 스터디 게시물 삭제
    @Transactional
    public ResponseEntity<ApiResponseDto> deleteMyStudy(Long id, User user) {
        Optional<MyStudy> myStudy = myStudyRepository.findById(id);

        if (!myStudy.isPresent() || !Objects.equals(myStudy.get().getUser().getUsername(), user.getUsername())) {
            log.error("게시글이 존재하지 않거나 게시글의 작성자가 아닙니다.");
            return ResponseEntity.status(400).body(new ApiResponseDto("게시글 삭제 실패", HttpStatus.BAD_REQUEST.value()));
        }

        myStudyRepository.delete(myStudy.get());

        return ResponseEntity.status(200).body(new ApiResponseDto("게시글 삭제 성공", HttpStatus.OK.value()));
    }

    // 카테고리별 게시물 조회
    public ResponseEntity<ApiResponseDto> getCategoryMyStudies(Long category_id) {
        List<MyStudy> myStudyList = myStudyRepository.findAllByCategory_IdOrderByModifiedAtDesc(category_id);

        List<MyStudyResponseDto> newMyStudyList = myStudyList.stream().map(MyStudyResponseDto::new).toList();

        return ResponseEntity.status(200).body(new ApiResponseDto(HttpStatus.OK.value(), "카테고리별 게시글 조회", newMyStudyList));
    }

    private MyStudy findMyStudy(Long id) {
        return myStudyRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("선택한 게시물은 존재하지 않습니다."));
    }
}