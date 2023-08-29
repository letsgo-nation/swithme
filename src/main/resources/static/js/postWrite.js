document.addEventListener('DOMContentLoaded', function () {
    var modeSwitch = document.querySelector('.mode-switch');

    modeSwitch.addEventListener('click', function () {
        document.documentElement.classList.toggle('dark');
        modeSwitch.classList.toggle('active');
    });

    var listView = document.querySelector('.list-view');
    var gridView = document.querySelector('.grid-view');
    var projectsList = document.querySelector('.project-boxes');

    listView.addEventListener('click', function () {
        gridView.classList.remove('active');
        listView.classList.add('active');
        projectsList.classList.remove('jsGridView');
        projectsList.classList.add('jsListView');
    });

    gridView.addEventListener('click', function () {
        gridView.classList.add('active');
        listView.classList.remove('active');
        projectsList.classList.remove('jsListView');
        projectsList.classList.add('jsGridView');
    });

    document.querySelector('.messages-btn').addEventListener('click', function () {
        document.querySelector('.messages-section').classList.add('show');
    });

    document.querySelector('.messages-close').addEventListener('click', function() {
        document.querySelector('.messages-section').classList.remove('show');
    });
});

// 게시물 작성판
// $(document).ready(function () {
//     let username = [[${info_username}]]
//     showPage(username);
// });
//
// function showPage(username) {
//     $('#body').empty();
//     setHtml(username)
//     setCategory();
// }
//
// function setCategory() {
//     $.ajax({
//         type: 'GET',
//         url: '/api/categories',
//         success: function(response) {
//             let form_category = $('#category');
//             form_category.empty();
//             let data = response['data'];
//             for(let i=0;i<data.length;i++) {
//                 let category = data[i]
//                 let category_id = category['id'];
//                 let name = category['name'];
//                 let option_value = i+1;
//                 form_category.append(`<option value=${option_value}>${name}</option>`);
//             }
//         }
//     })
// }
//
// function setHtml(username) {
//     let html = `<div class="input-group mb-3">
//             <span class="input-group-text" id="inputGroup-title">게시글 제목</span>
//             <input name="title" type="text" id="title" class="form-control" aria-label="Sizing example input" aria-describedby="inputGroup-sizing">
//           </div>
//           <div class="input-group mb-3 g-2">
//             <span class="input-group-text" id="inputGroup-username">작성자</span>
//             <input name="username" type="text" id="username" class="form-control me-4" value="${username}" aria-label="Sizing example username" aria-describedby="username-sizing" readonly>
//             <span class="input-group-text" for="category">카테고리</span>
//             <select name="category" class="form-select" id="category">
//             </select>
//           </div>
//           <div class="input-group-lg">
//             <span class="input-group-text">내용</span>
//             <textarea name="content" class="form-control" id="content" aria-label="With textarea" rows="15"></textarea>
//           </div>
//           <div class="d-flex justify-content-center mt-3">
//             <button type="submit" class="btn btn-dark btn-lg me-5 w-25" onclick="postSave()">저장</button>
//             <button type="submit" class="btn btn-dark btn-lg me-5 w-25" onclick="location.href='/'">취소</button>
//           </div>`
//     $('#body').append(html);
// }
//
// function postSave() {
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
//             alert("게시글이 등록되었습니다.");
//             window.location.href = "/";
//         }, error: function(req,status,error) {
//             alert("게시글 등록이 실패하였습니다.");
//         }
//     })
// }
