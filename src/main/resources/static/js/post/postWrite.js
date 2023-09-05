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

// 게시물 저장 버튼
function writePost() {
    const title = document.querySelector("#title").value;
    const content = document.querySelector("#content").value;
    const category_id = document.querySelector("#category").value;

    const data = {
        title : title,
        content : content,
        category_id : category_id
    }

    $.ajax({
        type:'POST',
        url:'/api/post',
        contentType: "application/json",
        data: JSON.stringify(data),
        success: function(response) {
            console.log(response)
            // {status: 200, message: '개인 스터디 게시물 생성 완료', data: {…}}
            console.log(data)
            // {title: '', content: '', category_id: '1'}
            alert("게시글 등록이 완료되었습니다");
            window.location.href = "/view/posts";
        }, error: function(req,status,error) {
            alert("게시글 등록이 실패하였습니다");
        }
    })
}

// S3
// var inputFileList = new Array();     // 이미지 파일을 담아놓을 배열 (업로드 버튼 누를 때 서버에 전송할 데이터)
//
// // 파일 선택 이벤트
// $('input[name=images]').on('change', function(e) {
//     var files = e.target.files;
//     var filesArr = Array.prototype.slice.call(files);
//
//     // 업로드 된 파일 유효성 체크
//     if (filesArr.length > 3) {
//         alert("이미지는 최대 3개까지 업로드 가능합니다.");
//         $('input[name=images]')val();
//         return;
//     }
//
//     filesArr.forEach(function(f) {
//         inputFileList.push(f);    // 이미지 파일을 배열에 담는다.
//     });
// });
//
// // 업로드 수행
// $('#uploadBtn').on('click', function() {
//     console.log("inputFileList: " + inputFileList);
//     let formData = new FormData($('#uploadForm')[0]);  // 폼 객체
//
//     for (let i = 0; i < inputFileList.length; i++) {
//         formData.append("images", inputFileList[i]);  // 배열에서 이미지들을 꺼내 폼 객체에 담는다.
//     }
//
//     $.ajax({
//         type:'post'
//         , enctype:"multipart/form-data"  // 업로드를 위한 필수 파라미터
//         , url: '/upload_image'
//         , data: formData
//         , processData: false   // 업로드를 위한 필수 파라미터
//         , contentType: false   // 업로드를 위한 필수 파라미터
//         , success: function(data) {
//             alert(data);
//         }
//         , error: function(e) {
//             alert("error:" + e);
//         }
//     });
// });