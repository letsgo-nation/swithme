'use strict';

// 카테고리 주소를 읽어옴.
const urlParams= new URLSearchParams(window.location.search);
const category= urlParams.get("category");

// Html에서 특정 부분을 선택하는 코드
var usernamePage = document.querySelector('#username-page');
var chatPage = document.querySelector('#chat-page');

var usernameForm = document.querySelector('#usernameForm');

var messageForm = document.querySelector('#messageForm');

var messageInput = document.querySelector('#message');

var messageArea = document.querySelector('#messageArea');
var connectingElement = document.querySelector('.connecting');

var stompClient = null;
var username = null;

var colors = [
    '#2196F3', '#32c787', '#00BCD4', '#ff5652',
    '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
];

function connect(event) {
    username = document.querySelector('#name').value.trim();

    if(username) {

        // 화면 보여지고 안 보여지는 것 부분 조작
        usernamePage.classList.add('hidden');
        chatPage.classList.remove('hidden');


        // ockJS와 같이 웹 소켓을 지원하지 않는 환경에서 SockJS 기반으로 STOMP 클라이언트를 생성하는데 사용됩니다. 웹 소켓을 지원하는 환경에서는 Stomp.client를 사용하여 STOMP 클라이언트를 생성하는 것이 보다 직관적입니다.
        var socket = new SockJS('/ws');
        stompClient = Stomp.over(socket);

        stompClient.connect({}, onConnected, onError);
    }
    event.preventDefault();
}


// 최초 연결시 실행되는 함수
function onConnected() {
    // Subscribe to the Public Topic
    stompClient.subscribe(`/topic/public/${category}`, onMessageReceived); // 수정

    // Tell your username to the server
    stompClient.send(`/app/chat.addUser/${category}`,  // 수정
        {},
        JSON.stringify({sender: username, type: 'JOIN'})
    )

    connectingElement.classList.add('hidden');
}


function onError(error) {
    connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    connectingElement.style.color = 'red';
}


function sendMessage(event) {
    var messageContent = messageInput.value.trim();
    if(messageContent && stompClient) {
        var chatMessage = {
            sender: username,
            content: messageInput.value,
            type: 'CHAT'
        };
        stompClient.send(`/app/chat.sendMessage/${category}`, {}, JSON.stringify(chatMessage)); // 수정
        messageInput.value = '';
    }
    event.preventDefault();
}


function onMessageReceived(payload) {
    var message = JSON.parse(payload.body);

    var messageElement = document.createElement('li');

    if(message.type === 'JOIN') {
        messageElement.classList.add('event-message');
        message.content = message.sender + ' joined!';
    } else if (message.type === 'LEAVE') {
        messageElement.classList.add('event-message');
        message.content = message.sender + ' left!';
    } else {
        messageElement.classList.add('chat-message');

        var avatarElement = document.createElement('i');
        var avatarText = document.createTextNode(message.sender[0]);
        avatarElement.appendChild(avatarText);
        avatarElement.style['background-color'] = getAvatarColor(message.sender);

        messageElement.appendChild(avatarElement);

        var usernameElement = document.createElement('span');
        var usernameText = document.createTextNode(message.sender);
        usernameElement.appendChild(usernameText);
        messageElement.appendChild(usernameElement);
    }

    var textElement = document.createElement('p');
    var messageText = document.createTextNode(message.content);
    textElement.appendChild(messageText);

    messageElement.appendChild(textElement);

    messageArea.appendChild(messageElement);
    messageArea.scrollTop = messageArea.scrollHeight;
}


function getAvatarColor(messageSender) {
    var hash = 0;
    for (var i = 0; i < messageSender.length; i++) {
        hash = 31 * hash + messageSender.charCodeAt(i);
    }
    var index = Math.abs(hash % colors.length);
    return colors[index];
}

usernameForm.addEventListener('submit', connect, true)
messageForm.addEventListener('submit', sendMessage, true)
