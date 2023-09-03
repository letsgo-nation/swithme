// URL을 "/"로 분할하여 배열로 저장합니다.
let currentURL = window.location.href;
let urlParts = currentURL.split("/");

// 배열에서 마지막 요소를 가져옵니다.
let lastPart = urlParts[urlParts.length - 1];

// 삭제 버튼 클릭 시 게시물 삭제
document.getElementById("deleteButton").addEventListener("click", function() {
let postId = this.getAttribute("data-postid");

fetch("/api/post/" + postId, {
    method: "DELETE",
    headers: {
        "Content-Type": "application/json"
    }
})
    .then(response => response.json())
    .then(data => {
        console.log(data); // 서버의 응답 데이터 처리
        // 여기서 필요한 동작 수행 (예: 페이지 새로고침)
        alert("게시글이 삭제되었습니다");
        window.location.href = "/view/posts";
    })
    .catch(error => {
        console.error("Error:", error);
    });
});

// 댓글, 대댓글
$(document).ready(function () {
    // 댓글 작성
    $("#comment-form").submit(function (event) {
        event.preventDefault();
        // let postId = this.getAttribute("data-postid");
        // const postId = /* 게시물 ID를 가져오는 방법 */;
        // const urlParams = new URLSearchParams(window.location.search);
        // const postId = urlParams.get('id'); // 'id'는 파라미터 이름에 맞게 수정
        // console.log(postId)
        console.log(lastPart)
        const commentText = $("#comment-input").val();
        console.log(commentText)
        if (commentText.trim() === "") {
            alert("댓글 내용을 입력하세요.");
            return;
        }

        const requestData = {
            text: commentText,
        };
        console.log(requestData)
        $.ajax({
            type: "POST",
            url: `/api/post/comment/`+lastPart,
            // url: `/api/post/comment/` + postId,
            data: JSON.stringify(requestData),
            contentType: "application/json",
            success: function (response) {
                console.log(response)
                alert("댓글이 작성되었습니다.");
                $("#comment-input").val("");
                loadComments(lastPart);
            },
            error: function (error) {
                console.log(error)
                alert("댓글 작성 중 오류가 발생했습니다.");
            },
        });
    });

    // 대댓글 생성
    $(document).on("submit", ".reply-form", function (event) {
        event.preventDefault();

        const commentId = $(this).data("comment-id");
        const replyText = $(this).find("input").val();

        if (replyText.trim() === "") {
            alert("대댓글 내용을 입력하세요.");
            return;
        }

        const requestData = {
            text: replyText,
        };

        $.ajax({
            type: "POST",
            url: `/api/post/comment/${commentId}/reply`,
            data: JSON.stringify(requestData),
            contentType: "application/json",
            success: function (response) {
                console.log(response)
                alert("대댓글이 작성되었습니다.");
                loadReplies(commentId);
            },
            error: function (error) {
                console.log(error)
                alert("대댓글 작성 중 오류가 발생했습니다.");
            },
        });
    });

    // 댓글 조회
    function loadComments(lastPart) {
        $.ajax({
            type: "GET",
            url: `/api/post/comment/`+ lastPart,
            success: function (comments) {
                displayComments(comments);
            },
            error: function (error) {
                console.log(error)
                alert("댓글을 불러오는 중 오류가 발생했습니다.");
            },
        });
    }

    // 대댓글 조회
    function loadReplies(commentId) {
        $.ajax({
            type: "GET",
            url: `/api/post/comment/reply/${commentId}`,
            success: function (replies) {
                displayReplies(commentId, replies);
            },
            error: function (error) {
                console.log(error)
                alert("대댓글을 불러오는 중 오류가 발생했습니다.");
            },
        });
    }

    // 댓글을 화면에 표시하는 함수
    function displayComments(comments) {
        const commentsList = $("#comments-list");
        commentsList.html(""); // 댓글 목록 초기화

        comments.forEach(function (comment) {
            const commentElement = document.createElement("div");
            commentElement.classList.add("comment");
            commentElement.innerHTML = `
                <p>${comment.text}</p>
                <button class="edit-comment" data-id="${comment.id}">수정</button>
                <button class="delete-comment" data-id="${comment.id}" th:attr="data-postid=${post.id}">삭제</button>
                <form class="reply-form" data-comment-id="${comment.id}" style="display: none;">
                    <input type="text" placeholder="대댓글 입력">
                    <button type="submit">대댓글 작성</button>
                </form>
                <div class="replies"></div>
            `;

            commentsList.append(commentElement);
        });
    }

    // 대댓글을 화면에 표시하는 함수
    function displayReplies(commentId, replies) {
        const repliesContainer = $(`.comment[data-id="${commentId}"] .replies`);
        repliesContainer.html(""); // 대댓글 목록 초기화

        replies.forEach(function (reply) {
            const replyElement = document.createElement("div");
            replyElement.classList.add("reply");
            replyElement.innerHTML = `
                <p>${reply.text}</p>
                <button class="edit-reply" data-id="${reply.id}">수정</button>
                <button class="delete-reply" data-id="${reply.id}">삭제</button>
            `;

            repliesContainer.append(replyElement);
        });
    }

    // 댓글 수정
    $(document).on("click", ".edit-comment", function () {
        const commentId = $(this).data("id");
        const editedText = /* 수정된 댓글 내용 가져오는 방법 */ $(this).closest(".comment").find("p").text(); // 댓글 내용을 가져옵니다.

        // 댓글 수정 폼을 표시하고, 수정된 내용을 입력 필드에 채웁니다.
        const commentForm = $(this).closest(".comment").find(".comment-form");
        commentForm.find("textarea").val(editedText);
        commentForm.show();

        if (editedText.trim() === "") {
            alert("댓글 내용을 입력하세요.");
            return;
        }

        const requestData = {
            text: editedText,
        };

        $.ajax({
            type: "PUT",
            url: `/api/post/comment/${commentId}`,
            data: JSON.stringify(requestData),
            contentType: "application/json",
            success: function (response) {
                alert("댓글이 수정되었습니다.");
                loadComments(lastPart);
            },
            error: function (error) {
                alert("댓글 수정 중 오류가 발생했습니다.");
            },
        });
    });

    // 대댓글 수정
    $(document).on("click", ".edit-reply", function () {
        const replyId = $(this).data("id");
        const editedText = $(this).closest(".reply").find("p").text(); // 대댓글 내용을 가져옵니다.

        // 대댓글 수정 폼을 표시하고, 수정된 내용을 입력 필드에 채웁니다.
        const replyForm = $(this).closest(".comment").find(".reply-form");
        replyForm.find("input").val(editedText);
        replyForm.show();

        if (editedText.trim() === "") {
            alert("대댓글 내용을 입력하세요.");
            return;
        }

        const requestData = {
            text: editedText,
        };

        $.ajax({
            type: "PUT",
            url: `/api/post/comment/reply/${replyId}`,
            data: JSON.stringify(requestData),
            contentType: "application/json",
            success: function (response) {
                alert("대댓글이 수정되었습니다.");
                loadReplies(commentId);
            },
            error: function (error) {
                alert("대댓글 수정 중 오류가 발생했습니다.");
            },
        });
    });


    // 댓글 삭제
    $(document).on("click", ".delete-comment", function () {
        const commentId = $(this).data("id");
        let postId = this.getAttribute("data-postid");
        // const postId = /* 게시물 ID를 가져오는 방법 */;

        if (!confirm("댓글을 삭제하시겠습니까?")) {
            return;
        }

        $.ajax({
            type: "DELETE",
            url: `/api/post/comment/${commentId}`,
            success: function (response) {
                alert("댓글이 삭제되었습니다.");
                loadComments(lastPart); // 댓글 목록 다시 로드
            },
            error: function (error) {
                alert("댓글 삭제 중 오류가 발생했습니다.");
            },
        });
    });
    // 대댓글 삭제
    $(document).on("click", ".delete-reply", function () {
        const replyId = $(this).data("id");
        const commentId = $(this).data("id"); /* 댓글 ID를 가져오는 방법 */

        if (!confirm("대댓글을 삭제하시겠습니까?")) {
            return;
        }

        $.ajax({
            type: "DELETE",
            url: `/api/post/comment/reply/${replyId}`,
            success: function (response) {
                alert("대댓글이 삭제되었습니다.");
                loadReplies(commentId); // 대댓글 목록 다시 로드
            },
            error: function (error) {
                alert("대댓글 삭제 중 오류가 발생했습니다.");
            },
        });
    });
});
