package com.example.swithme.service;

import com.example.swithme.dto.ApiResponseDto;
import com.example.swithme.dto.CategoryResponseDto;
import com.example.swithme.dto.CommentRequestDto;
import com.example.swithme.dto.CommentResponseDto;
import com.example.swithme.entity.Category;
import com.example.swithme.entity.Comment;
import com.example.swithme.entity.MyStudy;
import com.example.swithme.entity.User;
import com.example.swithme.repository.CommentRepository;
import com.example.swithme.repository.MyStudyRepository;
import com.example.swithme.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Slf4j(topic = "댓글 Service")
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final MyStudyRepository myStudyRepository;

    // 댓글 작성
    public ApiResponseDto createComment(Long myStudy_id, CommentRequestDto requestDto, User user) {
        // 게시글 유무 확인
        Optional<MyStudy> myStudy = myStudyRepository.findById(myStudy_id);
        // 게시글이 없을 경우
//        myStudyRepository.findById(myStudy_id).orElseThrow(() -> new IllegalArgumentException("선택한 게시물이 존재하지 않습니다."))
        if (!myStudy.isPresent()) {
            log.error("선택한 게시물이 존재하지 않습니다.");
            return new ApiResponseDto("선택한 게시물이 존재하지 않습니다.", HttpStatus.BAD_REQUEST.value());
        }
            // 게시물이 있을 경우
        Comment comment = new Comment(requestDto, myStudy.get(), user);

        // DB 저장하기
        commentRepository.save(comment);
        log.info("댓글 저장 완료");
        return new ApiResponseDto("댓글이 저장되었습니다.", HttpStatus.OK.value());
    }

    // 댓글 조회
    public List<CommentResponseDto> commentList(Long id) {
        List<Comment> getCommentList = commentRepository.findAllByMyStudyIdOrderByCreatedAt(id);
        List<CommentResponseDto> newCommentList = getCommentList.stream().map(CommentResponseDto::new).toList();
        return newCommentList;
    }

    // 댓글 수정
    @Transactional
    public ApiResponseDto updateComment(Long id, CommentRequestDto requestDto, User user) {
    // 댓글 확인
    Optional<Comment> findComment = commentRepository.findById(id);
    // 해당 댓글이 없을 경우
    if(!findComment.isPresent()) {
        log.error("댓글이 존재하지 않습니다.");
        return new ApiResponseDto("댓글이 존재하지 않습니다.", HttpStatus.BAD_REQUEST.value());
    }

    // 작성자 확인
    Comment comment = findComment.get();
    Long commentUserId = comment.getUser().getUserId();
    if(!commentUserId.equals(user.getUserId())) {
        log.error("댓글의 작성자가 아니므로 수정이 불가합니다.");
        return new ApiResponseDto("댓글의 작성자가 아니므로 수정이 불가합니다.", HttpStatus.BAD_REQUEST.value());
    }

    // 댓글 수정하기
    comment.update(requestDto);
    log.info("댓글 수정 완료");
    return new ApiResponseDto("댓글 수정이 완료되었습니다.", HttpStatus.OK.value());
    }

    // 댓글 삭제
    public ApiResponseDto deleteComment(Long id, User user) {
        // 댓글 확인
        Optional<Comment> findComment = commentRepository.findById(id);
        // 해당 댓글이 없을 경우
        if(!findComment.isPresent()) {
            log.error("댓글이 존재하지 않습니다.");
            return new ApiResponseDto("댓글이 존재하지 않습니다.", HttpStatus.BAD_REQUEST.value());
        }

        // 작성자 확인
        Comment comment = findComment.get();
        Long commentUserId = comment.getUser().getUserId();
        if(!commentUserId.equals(user.getUserId())) {
            log.error("댓글의 작성자가 아니므로 삭제가 불가합니다.");
            return new ApiResponseDto("댓글의 작성자가 아니므로 삭제가 불가합니다.", HttpStatus.BAD_REQUEST.value());
        }

        // 댓글 삭제하기
        commentRepository.delete(comment);
        log.info("댓글 삭제 완료");
        return new ApiResponseDto("댓글 삭제가 완료되었습니다.", HttpStatus.OK.value());
    }
}
