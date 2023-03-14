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





<!--Database Convention-->
###  Database Convention


<!--Infra Structure-->
###  Infra Structure



<!--Package Structure-->
###  Package Structure

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
