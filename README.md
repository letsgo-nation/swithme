# swithme
안녕하세요, 스윗미 입니다.

기술스택

- 프론트
    - HTML, CSS, javascript
    - Bootstrap : 간단한 적용으로 프론트 구성 가능
- 백엔드
    - 기술 스택
        - Spring web
        - Spring Boot : 프레임워크
        - Spring Data JPA : 데이터 접근
        - Thymeleaf : 템플릿 엔진
        - MySql : 데이터베이스
    - 라이브러리
        - Spring Security : 로그인, 보안
        - jwt : jwt 토큰 생성
        - OAuth2 Client : 카카오톡, 구글 Oauth2 인증에 사용
        - Spring Validation : 회원가입 등 검증기능시 사용
        - Websocket : 채팅 기능, 실시간 양방향 통신 구현
        - Lombok : 코드 간소화
        - fullcalendar : 캘린더 기능
        - reactor : 알림 기능(sse사용 시 필요한 의존성)
코드 컨벤션 정리
    1. 패키지 이름
        1. 소문자, 숫자로 작성
        2. 요소를 .으로 구분
    2. class name
        1. 명사
        2. 첫  문자는 대문자
        3. 캐멀 스타일로 작성
        4. 숫자 포함 가능(첫 문자는 숫자X)
        5. 특수문자 ($, _ 일반적으로 포함 가능하지만 쓰지 말기)
    3. variable name
        1. 첫 문자는 소문자
        2. 캐멀 스타일로 작성
        3. 어떤 값을 저장하고 있는지 쉽게 알 수 있도록 의미 있는 이름을 짓기
        4. 한글은 포함하지 않기
    4. method
        1. 동사로 작성
        2. 첫 문자는 소문자
        3. 캐멀 스타일(firstname)
    5. 상수 
        1. 모두 대문자로 작성하는 것이 관례 
        2. 서로 다른 단어가 혼합된 이름 _로 단어들을 연결
    6. html 파일
        1. 소문자로 작성
        2. 특수문자 사용X(단, -은 가능)
        3. 공백 금지
