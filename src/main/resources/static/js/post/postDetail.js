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

// 댓글, 대댓글 관련 JS
$(document).ready(function () {

    // 댓글 작성
    $("#commentSubmitBtn").click(function () {
        const commentText = $("#commentText").val();
        if (commentText.trim() === "") {
            alert("댓글 내용을 입력하세요.");
            return;
        }

        const requestData = {
            content: commentText,
        };

        fetch(`/api/post/comment/` + lastPart, {
        // fetch(`/api/post/comment/${postId}` + lastPart, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(requestData),
        })
            .then((response) => response.json())
            .then((data) => {
                console.log(data);
                alert("댓글이 작성되었습니다.");
                console.log(requestData)
                $("#commentText").val("");
                loadComments(lastPart);
            })
            .catch((error) => {
                console.log(error);
                console.log(requestData)
                alert("댓글 작성 중 오류가 발생했습니다.");
            });
    });

    // 대댓글 작성 버튼 클릭
    $(document).on("click", ".replyBtn", function () {
        const commentId = $(this).data("comment");
        const replyText = prompt("대댓글 내용을 입력하세요.");
        if (replyText === null || replyText.trim() === "") {
            return;
        }

        const requestData = {
            content: replyText,
        };

        fetch(`/api/post/comment/${commentId}/reply`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(requestData),
        })
            .then((response) => response.json())
            .then((data) => {
                console.log(data);
                alert("대댓글이 작성되었습니다.");
                loadReplies(commentId);
            })
            .catch((error) => {
                console.log(error);
                alert("대댓글 작성 중 오류가 발생했습니다.");
            });
    });

    // 댓글 조회
    function loadComments(lastPart) {
        fetch(`/api/post/comment/`+ lastPart)
            .then((response) => response.json())
            .then((comments) => {
                displayComments(comments);
                loadInitialReplies();
                console.log(comments)
            })
            .catch((error) => {
                console.log(error);
                alert("댓글을 불러오는 중 오류가 발생했습니다.");
            });
    }

    // 대댓글 조회
    function loadReplies(commentId) {
        fetch(`/api/post/comment/reply/${commentId}`)
            .then((response) => response.json())
            .then((replies) => {
                displayReplies(commentId, replies);
            })
            .catch((error) => {
                console.log(error);
                alert("대댓글을 불러오는 중 오류가 발생했습니다.");
            });
    }

    // 댓글을 화면에 표시하는 함수
    function displayComments(comments) {
        const commentList = $("#commentList");
        commentList.html(""); // 댓글 목록 초기화
        console.log("comments : " , comments);
        comments.forEach(function (comment) {
            console.log("comment : " , comment);
            const commentElement = document.createElement("div");
            commentElement.id = `comment-${comment.id}`;
            // const replies = comment.replyResponseDtoList;
            // const currentNickname = currentNickname;
            //     console.log(replies);
            //     console.log("comment.id :", comment.id);

                // console.log("currentNickname :", currentNickname);

                // if (replies!=null) {
                // displayReplies(comment.id, replies);}
            // if (comment.id == currentNickname) {
            //     console.log(currentNickname);
            //     commentElement.innerHTML = `
            //     <p>${comment.userNickname}</p>
            //     <p>${comment.content}</p>
            //     <button class="replyBtn" data-comment="${comment.id}">대댓글 작성</button>
            //     <button class="edit-comment" data-comment="${comment.id}">댓글 수정</button>
            //     <button class="delete-comment" data-comment="${comment.id}">댓글 삭제</button>
            //     <div id="replyList-${comment.id}" style="margin-left: 5%;">
            //         <!-- 대댓글이 여기에 동적으로 추가됩니다 -->
            //     </div>
            //     <hr>
            // `;
            // } else {
            //     commentElement.innerHTML = `
            //     <p>${comment.userNickname}</p>
            //     <p>${comment.content}</p>
            //     <button class="replyBtn" data-comment="${comment.id}">대댓글 작성</button>
            //     <div id="replyList-${comment.id}" style="margin-left: 5%;">
            //         <!-- 대댓글이 여기에 동적으로 추가됩니다 -->
            //     </div>
            //     <hr>
            // `;
            // }

            commentElement.innerHTML = `
                <hr>
                <p>${comment.userNickname}</p>
                <p>${comment.content}</p>
                <button class="replyBtn" data-comment="${comment.id}">대댓글 작성</button>
                <button class="edit-comment" data-comment="${comment.id}">댓글 수정</button>
                <button class="delete-comment" data-comment="${comment.id}">댓글 삭제</button>
                <div id="replyList-${comment.id}" style="margin-left: 5%;">
                    <!-- 대댓글이 여기에 동적으로 추가됩니다 -->
                </div>
            `;

            commentList.append(commentElement);
        });
    }

    // 대댓글을 화면에 표시하는 함수
    function displayReplies(commentId, replies) {
        if (replies==null) {
            return;
        }
        const replyList = $(`#replyList-${commentId}`);
        replyList.html(""); // 대댓글 목록 초기화

        replies.forEach(function (reply) {
            console.log("reply : " , reply);
            const replyElement = document.createElement("div");
            replyElement.innerHTML = `
                <hr>
                <p>${reply.userNickname}</p>
                <p id="reply-${reply.id}">${reply.content} </p>
                <button class="edit-reply" data-reply="${reply.id}">대댓글 수정</button>
                <button class="delete-reply" data-reply="${reply.id}" data-comment="${commentId}">대댓글 삭제</button>
            `;

            replyList.append(replyElement);
        });
    }
    // 대댓글 수정만 하는 방법
    function updateReply(reply) {
        const replyContent = $(`#reply-${reply.id}`);
        replyContent.html(reply.content);
    }

    // 댓글 수정
    $(document).on("click", ".edit-comment", function () {
        const commentId = $(this).data("comment");
        const editedText = prompt("수정된 댓글 내용을 입력하세요.");

        if (editedText === null || editedText.trim() === "") {
            return;
        }

        const requestData = {
            content: editedText,
        };

        fetch(`/api/post/comment/${commentId}`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(requestData),
        })
            .then((response) => response.json())
            .then((data) => {
                console.log(data);
                alert("댓글이 수정되었습니다.");
                loadComments(lastPart);
            })
            .catch((error) => {
                console.log(error);
                alert("댓글 수정 중 오류가 발생했습니다.");
            });
    });

    // 대댓글 수정
    $(document).on("click", ".edit-reply", function () {
        const replyId = $(this).data("reply");
        const editedText = prompt("수정된 대댓글 내용을 입력하세요.");

        if (editedText === null || editedText.trim() === "") {
            return;
        }

        const requestData = {
            content: editedText,
        };

        fetch(`/api/post/comment/reply/${replyId}`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(requestData),
        })
            .then((response) => response.json())
            .then((data) => {
                console.log(data);
                alert("대댓글이 수정되었습니다.");
                const commentId = $(this).data("comment"); // 댓글 ID 가져오기
                console.log(commentId)
                // loadReplies(commentId);
                const reply = {
                    id:replyId,
                    content:editedText
                }
                // displayReplies(commentId, replies)
                updateReply(reply)
            })
            .catch((error) => {
                console.log(error);
                alert("대댓글 수정 중 오류가 발생했습니다.");
            });
    });

    // 댓글 삭제
    $(document).on("click", ".delete-comment", function () {
        const commentId = $(this).data("comment");

        if (!confirm("댓글을 삭제하시겠습니까?")) {
            return;
        }

        fetch(`/api/post/comment/${commentId}`, {
            method: "DELETE",
        })
            .then((response) => {
                if (response.ok) {
                    alert("댓글이 삭제되었습니다.");
                    loadComments(lastPart);
                } else {
                    throw new Error("댓글 삭제 중 오류가 발생했습니다.");
                }
            })
            .catch((error) => {
                console.log(error);
                alert("댓글 삭제 중 오류가 발생했습니다.");
            });
    });

    // 대댓글 삭제
    $(document).on("click", ".delete-reply", function () {
        const replyId = $(this).data("reply");

        if (!confirm("대댓글을 삭제하시겠습니까?")) {
            return;
        }

        fetch(`/api/post/comment/reply/${replyId}`, {
            method: "DELETE",
        })
            .then((response) => {
                if (response.ok) {
                    alert("대댓글이 삭제되었습니다.");
                    const commentId = $(this).data("comment"); // 댓글 ID 가져오기
                    loadReplies(commentId);
                } else {
                    throw new Error("대댓글 삭제 중 오류가 발생했습니다.");
                }
            })
            .catch((error) => {
                console.log(error);
                alert("대댓글 삭제 중 오류가 발생했습니다.");
            });
    });

    // 초기 댓글 로드
    loadComments(lastPart);
    console.log("lastPart;" , lastPart);

    // 페이지 로드 시 대댓글 불러오기
    loadInitialReplies();

    // 페이지 로드 시 대댓글 불러오기 함수
    function loadInitialReplies() {
        const commentButtons = $(".replyBtn");
        commentButtons.each(function () {
            const commentId = $(this).data("comment");
            console.log("const commentId = $(this).data(\"comment\");" , commentId);
            loadReplies(commentId);
        });
    }
});
