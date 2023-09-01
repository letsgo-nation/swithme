document.addEventListener('DOMContentLoaded', function () {
    var modeSwitch = document.querySelector('.mode-switch');

    modeSwitch.addEventListener('click', function () {
        document.documentElement.classList.toggle('dark');
        modeSwitch.classList.toggle('active');
    });

// 카테고리 불러오기
    $.ajax({
        type: 'GET',
        url: '/api/categories',
        success: function(response) {
            let form_category = $('#category');
            form_category.empty();
            let data = response['data'];
            for(let i=0;i<data.length;i++) {
                let category = data[i]
                let category_id = category['id'];
                let name = category['name'];
                let option_value = i+1;
                form_category.append(`<option value=${option_value}>${name}</option>`);
            }
        }
    })
});

// 게시물 수정 버튼
function updatePost() {
    // var postId = /* 게시글 ID를 가져오는 코드 */;
    var urlParts = window.location.href.split("/");
    var postId = urlParts[urlParts.length - 1]; // 맨 마지막 부분이 게시글 ID일 것으로 가정
    console.log(postId)
    var title = document.getElementById("title").value;
    var content = document.getElementById("content").value;
    var categoryId = document.getElementById("category").value;/* 카테고리 ID를 가져오는 코드 */;

    // 업데이트할 데이터 객체를 구성
    var postData = {
        title: title,
        content: content,
        category_id: categoryId
    };

    // AJAX 요청 설정
    $.ajax({
        type: "PUT",
        url: "/api/post/" + postId,
        contentType: "application/json",
        data: JSON.stringify(postData),
        success: function(response) {
            // 업데이트 성공 시 실행할 코드
            console.log(response)
            console.log("게시글 업데이트 성공");
            window.location.href = "/view/post/detail/" + postId;
        },
        error: function(xhr, status, error) {
            // 업데이트 실패 시 실행할 코드
            console.error("게시글 업데이트 실패");
            console.log("에러 상태 코드:", status);
            console.log("에러 메시지:", error);
        }
    });
}

// function updatePost() {
//     // 여기에 AJAX 요청을 통한 업데이트 로직을 구현합니다.
//     // var postId = /* 여기에 게시글의 ID를 가져오는 코드를 작성하세요 */;
//     var urlParts = window.location.href.split("/");
//     var postId = urlParts[urlParts.length - 1]; // 맨 마지막 부분이 게시글 ID일 것으로 가정
//     console.log(postId)
//     // 요청 본문 데이터를 생성합니다.
//     // var postData = /* 여기에 업데이트할 데이터를 JSON 형태로 작성하세요 */;
//     // 제목, 내용, 카테고리 ID를 가져온다
//     var title = document.getElementById("title").value;
//     var content = document.getElementById("content").value;
//     // var categoryId = document.getElementById("category").value;/* ... */ // 카테고리 ID를 가져오는 방법
//
//     // 업데이트할 데이터 객체를 구성한다
//     var postData = {
//         title: title,
//         content: content,
//         // category_id: categoryId
//     };
//
//     // XMLHttpRequest 객체를 생성합니다.
//     var xhr = new XMLHttpRequest();
//
//     // PUT 요청을 생성합니다.
//     xhr.open("PUT", "/api/post/" + postId, true);
//     xhr.setRequestHeader("Content-Type", "application/json");
//
//     // 요청 완료 시 실행될 콜백 함수
//     xhr.onreadystatechange = function() {
//         if (xhr.readyState === XMLHttpRequest.DONE) {
//             if (xhr.status === 200) {
//                 // 업데이트 성공 시 실행할 코드를 여기에 작성합니다.
//                 console.log("게시글 업데이트 성공");
//                 window.location.href = "/view/post/detail/" + postId;
//             } else {
//                 // 업데이트 실패 시 실행할 코드를 여기에 작성합니다.
//                 console.error("게시글 업데이트 실패");
//             }
//         }
//     };
//     console.log(postData);
//     // 요청을 보냅니다.
//     xhr.send(JSON.stringify(postData));
//     console.log(postId)
//     console.log(postData);
// }