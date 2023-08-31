// 삭제 버튼 클릭 시 게시물 삭제

// let postId = [[${post.id}]]
// function deletePost() {
//     $.ajax({
//         type:'DELETE',
//         url:'/api/post/' + postId,
//         success: function(response) {
//             alert("게시글이 삭제되었습니다");
//             window.location.href = "/view/posts";
//         }, error: function(req,status,error) {
//             alert("게시글 삭제가 실패하였습니다");
//         }
//     })
// }

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