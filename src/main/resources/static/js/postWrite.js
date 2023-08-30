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
            window.location.href = "/";
        }, error: function(req,status,error) {
            alert("게시글 등록이 실패하였습니다");
        }
    })
}

