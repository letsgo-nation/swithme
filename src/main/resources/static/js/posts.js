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

    // document.querySelector('.messages-btn').addEventListener('click', function () {
    //     document.querySelector('.messages-section').classList.add('show');
    // });
    //
    // document.querySelector('.messages-close').addEventListener('click', function() {
    //     document.querySelector('.messages-section').classList.remove('show');
    // });
});

$(document).ready(function () {
    showPage();
});

function showPage() {
//게시글 출력 페이지
    setPosts();

//User 식별 & 포맷 설정
// setUserType();
}

function setPosts() {
    $.ajax({
        type: 'GET',
        url: '/api/posts',
        success: function (response) {
            $('#post-cards').empty();
            for (let i = 0; i < response.length; i++) {
                let post_title = response[i]['title'];
                let post_nickname = response[i]['userNickname'];
                let post_createdDate = response[i]['modifiedAt']
                let post_id = response[i]['id'];

                setHtml(post_title, post_id, post_nickname, post_createdDate);
            }
        }
    });
}

function setHtml(post_title, post_id, post_nickname, post_createdDate) {
    console.log(post_createdDate)
    console.log(post_nickname)
    let html = `
<div class="project-box-wrapper" onclick="location.href='/api/post-page/' + ${post_id}">
<div class="project-box" style="background-color: #e9e7fd;">
                        <div class="project-box-header">
                            <span>${post_createdDate}</span>
                            <div class="more-wrapper">
                                <button class="project-btn-more">
                                </button>
                            </div>
                        </div>
        <p class="box-content-header">${post_title}</p>
        <p class="box-content-subheader">${post_nickname}</p>
<!--        post_nickname이 post id로 찍힘, 왜?-->
        <span hidden="hidden">${post_id}</span>
</div>
</div>`;
    $('#post-cards').append(html);
}

function setCategory(category_id) {
// 카테고리별 조회
    $.ajax({
        type: 'GET',
        url: `/api/posts/${category_id}`,
        success: function (response) {
            $('#post-cards').empty();
            let data = response['data']
            for (let i = 0; i < data.length; i++) {
                let post = data[i]
                let post_title = post['title'];
                let post_id = post['id'];
                setHtml(post_title, post_id);
            }
        }
    });
}

function getUsername() {
    let auth = getToken();
    return getUsernameFromToken(auth);
}

function setUserType() {
    let username = getUsername();

    if (username == 'Guest') {
        $('button.login').removeClass('inactive');
        $('button.signup').removeClass('inactive');
        $('button.logout').addClass('inactive');
        $('button.mypage').addClass('inactive');
    } else {
        $('button.login').addClass('inactive');
        $('button.signup').addClass('inactive');
        $('button.logout').removeClass('inactive');
        $('button.mypage').removeClass('inactive');

        $('.welcome-msg').text(username + '님, 환영합니다.');
    }

}

function getToken() {
    let auth = Cookies.get('Authorization');

    if (auth === undefined) {
        return '';
    }
    return auth;
}

function getUsernameFromToken(auth) {
    if (auth === '') {
        return 'Guest';
    }
    let token = auth.substring(7);
    let base64Payload = token.split('.')[1];
    let payload = atob(base64Payload);
    let result = JSON.parse(payload.toString())

    return result['sub']
}

function upload() {
    let username = getUsername();

    if (username == 'Guest') {
        alert("로그인이 필요합니다.");
        return;
    }
    window.location.href = '/api/post/write';
}

$(document).ready(function(){
    var currentPosition = parseInt($(".category").css("top"));
    $(window).scroll(function() {
        var position = $(window).scrollTop();
        $(".category").stop().animate({"top":position+currentPosition+"px"},1000);
    });
});

function logout() {
// 토큰 삭제
    Cookies.remove('Authorization', {path: '/'});
    window.location.href = host;
}
