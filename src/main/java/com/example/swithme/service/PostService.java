package com.example.swithme.service;

import com.example.swithme.S3.S3UploadService;
import com.example.swithme.dto.ApiResponseDto;
import com.example.swithme.dto.PostRequestDto;
import com.example.swithme.dto.PostResponseDto;
import com.example.swithme.entity.Category;
import com.example.swithme.entity.Post;
import com.example.swithme.entity.User;
import com.example.swithme.repository.CategoryRepository;
import com.example.swithme.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j(topic = "Post Service")
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final S3UploadService s3UploadService;
//    private final CommentRepository commentRepository;

    // 개인 스터디 게시물 생성
    public ApiResponseDto createPost(PostRequestDto requestDto, User user, MultipartFile image) {

        String postImg = null; //url받을 변수를 초기화

        if (!image.isEmpty()) {//매개변수로 받은 값이 있으면
            try {
                postImg = s3UploadService.uploadFiles(image, "images");//post name : images에 mulipartfile을 올린다
                System.out.println(postImg);// 확인하기
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Optional<Category> category = categoryRepository.findById(requestDto.getCategory_id());
        // RequestDto -> Entity
        Post post = new Post(requestDto, user, category.get(), postImg);
        // DB 저장
        Post savePost = postRepository.save(post);
        // Entity -> ResponseDto
        return new ApiResponseDto(HttpStatus.OK.value(), "개인 스터디 게시물 생성 완료", savePost);
    }

    // 전체 개인 스터디 게시글 조회
    public List<PostResponseDto> getPosts() {
        return postRepository.findAllByOrderByModifiedAtDesc().stream().map(PostResponseDto::new).toList();
    }

    // 개인 스터디 단건 조회
    public PostResponseDto lookupPost(Long id) {
        Post post = findPost(id);
        return new PostResponseDto(post);
    }

    public String lookupPostUserNickname(Long id) {
        Post post = findPost(id);
        String postUserNickname = post.getUser().getNickname();
        return postUserNickname;
    }

    // 개인 스터디 게시물 수정
    @Transactional
    public ResponseEntity<ApiResponseDto> updatePost(Long id, PostRequestDto postRequestDto, User user, MultipartFile image) {
        Optional<Post> post = postRepository.findById(id);
        Optional<Category> category = categoryRepository.findById(postRequestDto.getCategory_id());

        if (!post.isPresent() || !Objects.equals(post.get().getUser().getUsername(), user.getUsername())) {
            log.error("게시글이 존재하지 않거나 게시글 작성자가 아닙니다.");
            return ResponseEntity.status(400).body(new ApiResponseDto("게시글 수정 실패", HttpStatus.BAD_REQUEST.value()));
        }

        if (!category.isPresent()) {
            log.error("해당하는 카테고리가 존재하지 않습니다.");
            return ResponseEntity.status(400).body(new ApiResponseDto( "게시글 수정 실패", HttpStatus.BAD_REQUEST.value()));
        }

        String postImg = post.get().getPostImg(); //일단 기존 사진 넣어주고
        log.info("기존 사진 : " + postImg);

        if (!image.isEmpty()) { //매개변수로 받은 게 있다면
            try {
                s3UploadService.fileDelete(postImg); // 기존사진을 S3에서 삭제하고
                postImg = s3UploadService.uploadFiles(image, "images"); //새로 받은걸 업로드해주고
                log.info("새로운 사진 : " + postImg);
                System.out.println(postImg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // url도 바꿔서 db에 저장
        post.get().update(postRequestDto, category.get(), postImg);
        return ResponseEntity.status(200).body(new ApiResponseDto(HttpStatus.OK.value(), "게시글 수정 성공", new PostResponseDto(post.get())));
    }

    // 개인 스터디 게시물 삭제
    @Transactional
    public ResponseEntity<ApiResponseDto> deletePost(Long id, User user) {
        Optional<Post> post = postRepository.findById(id);

        if (!post.isPresent() || !Objects.equals(post.get().getUser().getUsername(), user.getUsername())) {
            log.error("게시글이 존재하지 않거나 게시글의 작성자가 아닙니다.");
            return ResponseEntity.status(400).body(new ApiResponseDto("게시글 삭제 실패", HttpStatus.BAD_REQUEST.value()));
        }

        postRepository.delete(post.get());
        return ResponseEntity.status(200).body(new ApiResponseDto("게시글 삭제 성공", HttpStatus.OK.value()));
    }

    // 카테고리별 게시물 조회
    public ResponseEntity<ApiResponseDto> getCategoryPosts(Long category_id) {
        List<Post> postList = postRepository.findAllByCategory_IdOrderByModifiedAtDesc(category_id);

        List<PostResponseDto> newPostList = postList.stream().map(PostResponseDto::new).toList();

        return ResponseEntity.status(200).body(new ApiResponseDto(HttpStatus.OK.value(), "카테고리별 게시글 조회", newPostList));
    }

    private Post findPost(Long id) {
        return postRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("선택한 게시물은 존재하지 않습니다."));
    }
}