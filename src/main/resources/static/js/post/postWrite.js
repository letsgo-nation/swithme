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

// 게시글 업로드
$(document).ready(function () {
    $('#savePost').click(function () {
        const title = $('#title').val();
        const category_id = $('#category').val();
        const content = $('#content').val();
        const imageFile = $('#image')[0].files[0];

        // 파일 크기 확인 (1MB 이상인 경우 처리)
        if (imageFile && imageFile.size > 1048576) { // 1MB 이상인 경우
            alert("파일 크기가 너무 큽니다. 1MB 보다 작은 파일을 선택해주세요.");
            return; // 업로드 중단
        }

        // 이미지 업로드를 위한 FormData 객체 생성
        const imageFormData = new FormData();
        imageFormData.append('image', imageFile);

        // 데이터를 JSON 형식으로 생성
        const jsonData = {
            title: title,
            category_id: category_id,
            content: content
        };

        // JSON 데이터를 문자열로 변환한 후 Blob으로 감싸기
        const jsonDataBlob = new Blob([JSON.stringify(jsonData)], { type: 'application/json' });

        // FormData 객체 생성하고 JSON 데이터 추가
        const dataFormData = new FormData();
        dataFormData.append('data', jsonDataBlob);

        // 두 개의 FormData 객체를 합치기
        const formData = new FormData();
        formData.append('image', imageFile);
        formData.append('data', jsonDataBlob);

        // 서버로 데이터 전송 (Ajax)
        $.ajax({
            type: 'POST',
            url: '/api/post',
            data: formData,
            contentType: false, // Content-Type 자동 설정 비활성화
            processData: false, // 데이터 처리 비활성화

            success: function (response) {
                // 성공 처리
                alert(response.message);
                window.location.href = '/view/posts';
            },
            error: function () {
                // 오류 처리
                alert('게시글 등록이 실패하였습니다');
            }
        });
    });
});


// $(document).ready(function () {
//     $('#saveButton').click(function () {
//         const title = $('#title').val();
//         const category_id = $('#category').val();
//         const content = $('#content').val();
//         const imageFile = $('#image')[0].files[0];
//
//         // FormData 객체 생성
//         const formData = new FormData();
//         formData.append('data', JSON.stringify({ title, category_id, content }));
//         formData.append('image', imageFile);
//         console.log('data')
//         console.log(imageFile)
//
//         //
//         // const json = JSON.stringify({ title, category_id, content });
//         // const blob = new Blob([json], { type: "application/json" });
//         // fd.append("dto", blob);
//
//
//         // 서버로 데이터 전송 (Ajax)
//         $.ajax({
//             type: 'POST',
//             url: '/api/post',
//             data: formData,
//             contentType: false, // Content-Type 자동 설정 비활성화
//             processData: false, // 데이터 처리 비활성화
//             success: function (response) {
//                 // 성공 처리
//                 alert(response.message);
//                 window.location.href = '/view/posts';
//             },
//             error: function () {
//                 // 오류 처리
//                 alert('게시글 등록이 실패하였습니다');
//             }
//         });
//     });
// });
// 게시물 저장 버튼
// function writePost() {
//     const title = document.querySelector("#title").value;
//     const content = document.querySelector("#content").value;
//     const category_id = document.querySelector("#category").value;
//
//     const data = {
//         title : title,
//         content : content,
//         category_id : category_id
//     }
//
//     $.ajax({
//         type:'POST',
//         url:'/api/post',
//         contentType: "application/json",
//         data: JSON.stringify(data),
//         success: function(response) {
//             console.log(response)
//             // {status: 200, message: '개인 스터디 게시물 생성 완료', data: {…}}
//             console.log(data)
//             // {title: '', content: '', category_id: '1'}
//             alert("게시글 등록이 완료되었습니다");
//             window.location.href = "/view/posts";
//         }, error: function(req,status,error) {
//             alert("게시글 등록이 실패하였습니다");
//         }
//     })
// }

