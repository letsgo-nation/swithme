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

// 댓글 작성 버튼 클릭 이벤트 리스너 등록
document.getElementById("commentSubmitBtn").addEventListener("click", function() {
    let postId = this.getAttribute("data-postid");
    const commentText = document.getElementById("commentText").value;
    createComment(commentText);

    // 댓글 작성 함수
    function createComment(commentText) {
        // 댓글 데이터를 JSON 형식으로 전송
        const postData = { content: commentText };
        // 서버로 댓글 데이터를 보내는 fetch 요청
        fetch("/api/post/comment/" + postId, { // 123는 게시물 ID입니다. 실제로는 해당 게시물 ID를 동적으로 가져와야 합니다.
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(postData) // JSON 형식으로 데이터 전송
        })
            .then(response => response.json())
            .then(data => {
                console.log(data); // 서버의 응답 데이터 처리
                // 여기서 필요한 동작 수행 (예: 새로운 댓글을 화면에 추가)
                const commentList = document.getElementById("commentList");
                // 새로운 댓글을 표시할 div 요소 생성
                const commentDiv = document.createElement("div");
                commentDiv.textContent = data.text; // 서버 응답에서 댓글 텍스트를 가져옴
                // 새로운 댓글을 commentList에 추가
                commentList.appendChild(commentDiv);
            })

            .catch(error => {
                console.error("Error:", error);
            });
    }
});

// 대댓글 작성 버튼 클릭 이벤트 리스너 등록
const replyButtons = document.querySelectorAll('.replyBtn');
replyButtons.forEach(button => {
    button.addEventListener('click', () => {
        const commentId = button.getAttribute('data-comment');
        const replyText = prompt('대댓글을 작성하세요:');
        if (replyText) {
            createReply(commentId, replyText);
        }
    });
});

// 대댓글 작성 함수
function createReply(commentId, replyText) {
    // 서버로 대댓글 데이터를 보내는 fetch 요청
    fetch(`/api/post/comment/${commentId}/reply`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({ text: replyText })
    })
        .then(response => response.json())
        .then(data => {
            console.log(data); // 서버의 응답 데이터 처리
            // 여기서 필요한 동작 수행 (예: 새로운 대댓글을 화면에 추가)
            const replyList = document.getElementById(`replyList-${commentId}`);
            const replyDiv = document.createElement("div");
            replyDiv.textContent = data.text; // 서버 응답에서 대댓글 텍스트를 가져옴
            replyList.appendChild(replyDiv);
        })
        .catch(error => {
            console.error("Error:", error);
        });
}

// 대댓글 작성 함수도 동일하게 수정
// function createReply(commentId, replyText) {
//     // 대댓글 데이터를 JSON 형식으로 전송
//     const replyData = { content: replyText };
//
//     fetch(`/api/post/comment/${commentId}/reply`, {
//         method: "POST",
//         headers: {
//             "Content-Type": "application/json"
//         },
//         body: JSON.stringify(replyData) // JSON 형식으로 데이터 전송
//     })
//         .then(response => response.json())
//         .then(data => {
//             console.log(data);
//             // 대댓글 추가 로직을 여기에 추가
//         })
//         .catch(error => {
//             console.error("Error:", error);
//         });
// }