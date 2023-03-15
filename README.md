<br />
<div align="center">
  <a href="https://www.onjeong-app.com/">
    <img src="https://onjeong-prod.s3.ap-northeast-2.amazonaws.com/profile/24c4b2d3-be1c-4bd8-8d95-79e7a66ce23eonjeong%20logo.png?s=200&v=4" alt="Logo" width="80" height="80">
  </a>
  <h3 align="center">Onjeong-Backend</h3>

  <p align="center">
    온정의 backend 부분 github입니다.
    <br />
    <a href="https://github.com/On-jeong"><strong>1. Explore the Organization</strong></a><br>
    <a href="https://github.com/On-jeong/Onjeong-front"><strong>2. Explore Front Repository</strong></a>
    <br />
    <br />
    <!-- <a href="https://github.com/othneildrew/Best-README-Template">View Demo</a> -->
    <!-- · -->
  </p>
</div>

<!-- TABLE OF CONTENTS -->
<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <!-- <a href="#about-the-project">About The Project</a> -->
      <a href="#built-with">Built With</a>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#convention">Convention</a></li>
        <li><a href="#java-code-convention">Java Code Convention</a></li>
        <li><a href="#database-convention">Database Convention</a></li>
        <li><a href="#infra-structure">Infra Structure</a></li>
        <li><a href="#package structure">Package Structure</a></li>
        <li><a href="#commit-convention">Commit Convention</a></li>
        <li><a href="#pr-convention">PR Convention</a></li>
        <li><a href="#erd">ERD</a></li>
      </ul>
    </li>
    <li><a href="#contributing">Contributing</a></li>
  </ol>
</details>


<!--Built with -->
### Built With

<b>Backend</b>

-   [Spring Boot](https://spring.io/projects/spring-boot)
-   [Spring Security](https://spring.io/projects/spring-security)
-   [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
-   [Spring Data Redis](https://spring.io/projects/spring-data-redis)
-   [Spring Batch](https://spring.io/projects/spring-batch)
-   [Springfox Swagger UI](http://springfox.github.io/springfox/docs/current/)
-   [JSON Web Token](https://jwt.io)
-   [Firebase](https://firebase.google.com)
-   [MySQL](https://www.mysql.com)
-   [Redis](https://redis.io/)
-   [Amazon Web Services](https://aws.amazon.com/)
-   [Nginx](https://www.nginx.com/)
-   [Jenkins](https://www.jenkins.io/)
-   [Docker Hub](https://hub.docker.com/)

<p align="right">(<a href="#top">back to top</a>)</p>


<!-- GETTING STARTED -->

## Getting Started

### Convention

1. 통일된 Error Response 객체
    - Error Response JSON
      ```json
        {
            "status": 500,
            "message": "DATETYPE CONVERSE FAIL",
            "code": "DATETYPE-ERR-500"
        }
        ```
      - status : http status code를 작성합니다.
      - message : 에러에 대한 내용을 작성합니다.
      - code : 에러에 할당되는 유니크한 코드 값입니다.

    - Error Response 객체
        ```java
        @Getter
        @Setter
        public class ErrorResponse {
            private int status;
            private String message;
            private String code;

            public ErrorResponse(ErrorCode errorCode){
                this.status = errorCode.getStatus();
                this.message = errorCode.getMessage();
                this.code = errorCode.getErrorCode();
            }
        }
        ```
    - Error Code 정의
        ```java
        @Getter
        @AllArgsConstructor
        public enum ErrorCode {
            // Common
            NOT_FOUND(404,"COMMON-ERR-404","PAGE NOT FOUND"),
            INTER_SERVER_ERROR(500,"COMMON-ERR-500","INTER SERVER ERROR"),
            ....
    
            // User
            USER_NOTEXIST(500,"U001","LOGIN USER NOT EXIST"),
            USER_UNAUTHORIZED(401,"U002","USER UNAUTHORIZED"),
            USER_NICKNAME_DUPLICATION(400,"U003","USER NICKNAME DUPLICATION"),
            ....
            ;
            
            final private int status;
            final private String errorCode;
            final private String message;
        }
        ```
    - @RestControllerAdvice로 모든 예외를 핸들링
        ```java
        @Slf4j
        @RestControllerAdvice
        public class GlobalExceptionHandler {
          @ExceptionHandler(Exception.class)
          public ResponseEntity<ErrorResponse> handleException(Exception ex){
              log.error("handleException",ex);
              final ErrorResponse response = new ErrorResponse(ErrorCode.INTER_SERVER_ERROR);
              return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
          }
          
          @ExceptionHandler(UserNotExistException.class)
          public ResponseEntity<ErrorResponse> handleUserNotExistException(UserNotExistException ex){
              log.error("handleUserNotExistException",ex);
              final ErrorResponse response = new ErrorResponse(ex.getErrorCode());
              return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getErrorCode().getStatus()));
          }
          
          @ExceptionHandler(UserUnauthorizedException.class)
          public ResponseEntity<ErrorResponse> handleUserUnauthorizedException(UserUnauthorizedException ex){
              log.error("handleUserUnauthorizedException",ex);
              final ErrorResponse response = new ErrorResponse(ex.getErrorCode());
              return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getErrorCode().getStatus()));
          }
          
          @ExceptionHandler(UserNicknameDuplicationException.class)
          public ResponseEntity<ErrorResponse> handleUserNicknameDuplicationException(UserNicknameDuplicationException ex){
              log.error("handleUserNicknameDuplicationException",ex);
              final ErrorResponse response = new ErrorResponse(ex.getErrorCode());
              return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getErrorCode().getStatus()));
          }
          ....
        }
        ```
        
2. 통일된 Error Response 객체
    - Result Response JSON
      ```json
        {
          "status": 200,
          "code": "U001",
          "message": "회원가입에 성공했습니다.",
          "data": ""
        }
        ```
      - status : http status code를 작성합니다.
      - code : 결과에 할당되는 유니크한 코드 값입니다.
      - message : 결과에 대한 내용을 작성합니다.
      - data : 결과 객체를 JSON 형태로 반환합니다.

    - Result Response 객체
      ```java
      @Getter
      public class ResultResponse {
          private final int status;
          private final String code;
          private final String message;
          private final Object data;

          public ResultResponse(ResultCode resultCode, Object data) {
              this.status = resultCode.getStatus();
              this.code = resultCode.getCode();
              this.message = resultCode.getMessage();
              this.data = data;
          }

          public static ResultResponse of(ResultCode resultCode, Object data) {
              return new ResultResponse(resultCode, data);
          }

          public static ResultResponse of(ResultCode resultCode) {
              return new ResultResponse(resultCode, "");
          }
      }
      ```
    - @RestController에서 통일된 응답 사용
    ```java
    @RestController
    @RequiredArgsConstructor
    public class UserController {
        private final UserService userService;
        
        @ApiOperation(value="가족회원이 없는 회원 가입")
        @PostMapping(value = "/accounts", produces = MediaType.APPLICATION_JSON_VALUE)
        public ResponseEntity<ResultResponse> signUp(@RequestBody UserJoinDto userJoinDto){
            ...
            
            return ResponseEntity.ok(ResultResponse.of(ResultCode.REGISTER_SUCCESS));
        }
        ...
    }
    ```
    

<!--Java Code Convention-->
###  Java Code Convention
<b>[Reference]</b>
- [캠퍼스 핵데이 Java 코딩 컨벤션](https://naver.github.io/hackday-conventions-java/)


<!--Database Convention-->
###  Database Convention
<b>[Common]</b>
- 소문자 사용
- 단어를 임의로 축약 x
  > ex) register_date O reg_date X
- 동사는 능동태 사용
  > ex) register_date O registered_date X
- 이름을 구성하는 각각의 단어를 `underscore(_)`로 연결 **(snake case)**

<b>[Table]</b>
- 복수형 사용

<b>[Column]</b>
- PK는 `테이블 명 단수형_id`으로 사용
- boolean 유형의 컬럼은 `is_` 접미어 사용
- date, datetime 유형의 컬럼은 `_date` 접미어 사용

<b>[Reference]</b>
- [DB 네이밍 컨벤션](https://purumae.tistory.com/200)



<!--Infra Structure-->
###  Infra Structure



<!--Package Structure-->
###  Package Structure

- api와 batch 작업은 서로 독립적으로 이뤄지기때문에 모듈별로 분리하여 재사용, 유지보수, 에러파악 측면에서의 이점을 얻고자 `멀티모듈(Multi Module)`을 도입했습니다.
- 저희는 **api 모듈**, **batch 모듈**, api와 batch가 공통으로 사용하고 있는 **common 모듈**로 3개의 모듈로 분리했습니다.

```txt
└── module-api
    ├── src
    │   ├── main
    │   │   └── java.com.example.onjeong
    │   │       ├── anniversary
    │   │       │   ├── controller
    │   │       │   └── service
    │   │       ├── board
    │   │       │   ├── controller
    │   │       │   └── service
    │   │       ├── coin
    │   │       │   └── service
    │   │       ├── home
    │   │       │   ├── controller
    │   │       │   └── service
    │   │       ├── infra
    │   │       │   └── controller
    │   │       ├── mail
    │   │       │   ├── controller
    │   │       │   └── service
    │   │       ├── notification
    │   │       │   ├── controller
    │   │       │   └── service
    │   │       ├── profile
    │   │       │   ├── controller
    │   │       │   └── service
    │   │       ├── question
    │   │       │   ├── controller
    │   │       │   └── service
    │   │       ├── user
    │   │       │   ├── controller
    │   │       │   └── service
    │   │       └── OnjeongApplication.java
    │   │     
    |   |    └── resources
    |   |           ├── application.properties
    |   |           ├── application-dev.properties
    |   |           ├── application-prod.properties
    |   |           ├── server-application.yml
    |   |           └── firebase
    |   |           |      └── firebase_service_key.json
    |   |           └── static
    |   |                  └── index.html
    │   └── test.java.com.example.onjeong
    |
└── module-batch
    ├── src.main.java.com.example.onjeong
    │   ├── config
    │   │     └── JobConfig.java
    |   ├── scheduler
    │   │     └── JobScheduler.java
    │   └── BatchApplication.java
    |
└── module-common
    ├── src.main.java.com.example.onjeong
    │   ├── Config
    │   │      └── ....
    │   ├── S3
    │   │      └── ....
    │   ├── anniversary
    │   │      ├── domain
    │   │      ├── dto
    │   │      ├── repository
    │   │      └── exception
    │   ├── board
    │   │      ├── domain
    │   │      ├── dto
    │   │      ├── repository
    │   │      └── exception
    │   ├── coin
    │   │      ├── domain
    │   │      ├── dto
    │   │      ├── repository
    │   │      └── service
    │   ├── family
    │   │      ├── domain
    │   │      ├── repository
    │   │      └── exception
    │   ├── home
    │   │      ├── domain
    │   │      ├── dto
    │   │      ├── repository
    │   │      └── exception
    │   ├── mail
    │   │      ├── domain
    │   │      ├── dto
    │   │      ├── repository
    │   │      └── exception
    │   ├── notification
    │   │      ├── domain
    │   │      ├── dto
    │   │      └── repository
    │   ├── profile
    │   │      ├── domain
    │   │      ├── dto
    │   │      ├── repository
    │   │      └── exception
    │   ├── question
    │   │      ├── domain
    │   │      ├── dto
    │   │      ├── repository
    │   │      └── exception
    │   ├── user
    │   │      ├── Auth
    │   │      ├── redis
    │   │      ├── domain
    │   │      ├── dto
    │   │      ├── repository
    │   │      ├── service
    │   │      └── exception
    │   ├── error
    │   │      ├── ErrorCode.java
    │   │      ├── ErrorController.java
    │   │      ├── ErrorResponse.java
    │   │      └── GlobalExceptionHandler.java
    │   ├── result
    │   │      ├── ResultCode.java
    │   │      └── ResultResponse.java
    │   └── util
               ├── AuthUtil.java
               └── ProfileUtil.java
```

<b>[Reference]</b>
- [멀티모듈 설계](https://techblog.woowahan.com/2637/)
- [멀티모듈 사용 이유](https://velog.io/@cha-sung-soo/Multi-Module-%EC%82%AC%EC%9A%A9-%EC%9D%B4%EC%9C%A0)


<!--Commit Convention-->
### Commit Convention

```txt
Type: Subject
ex) Feat: 로그인 로직 추가
```
- <b>Type</b>
  - feat: 새로운 기능 추가
  - fix: 버그 수정
  - docs : 문서 수정
  - style : 코드 포맷팅, 세미콜론 누락, 코드 변경이 없는 경우
  - refactor : 코드 리펙토링
  - test : 테스트 코드, 리펙토링 테스트 코드 추가
  - chore : 빌드 업무 수정, 패키지 매니저 수정
- <b>Reference</b>


  - [Git Commit Message Convention](https://velog.io/@shin6403/Git-git-%EC%BB%A4%EB%B0%8B-%EC%BB%A8%EB%B2%A4%EC%85%98-%EC%84%A4%EC%A0%95%ED%95%98%EA%B8%B0)
<p align="right">(<a href="#top">back to top</a>)</p>


<!--PR Convention-->
### PR Convention

```txt
## 개요
## 작업사항
## 변경로직
### 변경전
### 변경후
## 사용방법
## 기타
```
- <b>Reference</b>
  - [Git PR Convention](https://medium.com/prnd/%ED%97%A4%EC%9D%B4%EB%94%9C%EB%9F%AC-%EA%B0%9C%EB%B0%9C%ED%8C%80-%EB%AA%A8%EB%91%90%EA%B0%80-%ED%96%89%EB%B3%B5%ED%95%9C-%EA%B0%9C%EB%B0%9C-pr%EA%B4%80%EB%A6%AC-%EB%B0%A9%EB%B2%95-7%EA%B0%80%EC%A7%80-1d4cd5d091f0)


<p align="right">(<a href="#top">back to top</a>)</p>



<!--ERD-->
### ERD
<p align="center"><a href="https://www.erdcloud.com/d/9JxX7unXDjeZN5XHC"><img src="https://img.shields.io/badge/ERD Cloud-946CEE?style=for-the-badge"/></a></p>



<!--Contributors -->
### Contributors

<table>
  <tr>
    <td align="center">
      <a href="https://github.com/haeun-i">
        <img src="https://onjeong-prod.s3.ap-northeast-2.amazonaws.com/profile/24c4b2d3-be1c-4bd8-8d95-79e7a66ce23eonjeong%20logo.png?v=4" width="110px;" alt=""/><br />
        <sub><b>김하은</b></sub></a><br />
        <sub><b>Back-end</b></sub></a><br />
        <a href="https://github.com/haeun-i" title="Code">💻</a>
    </td>
    <td align="center">
      <a href="https://github.com/Junhui0u0">
        <img src="https://onjeong-prod.s3.ap-northeast-2.amazonaws.com/profile/24c4b2d3-be1c-4bd8-8d95-79e7a66ce23eonjeong%20logo.png?v=4" width="110px;" alt=""/><br />
        <sub><b>박준희</b></sub></a><br />
        <sub><b>Back-end</b></sub></a><br />
        <a href="https://github.com/Junhui0u0" title="Code">💻</a>
    </td>
  </tr>
</table>  

<p align="right">(<a href="#top">back to top</a>)</p>
