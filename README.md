# KEKI_Server
>  Server for keki app 🍰
<br>

## Tech Stack
### Backend
<img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white"> <img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white"> ![JWT](https://img.shields.io/badge/JWT-black?style=for-the-badge&logo=JSON%20web%20tokens) <img src="https://img.shields.io/badge/spring data jpa-6DB33F?style=for-the-badge&logoColor=white"> <img src="https://img.shields.io/badge/querydsl-6DB33F?style=for-the-badge&logoColor=white"> <img src="https://img.shields.io/badge/hibernate-59666C?style=for-the-badge&logo=hibernate&logoColor=white"> <img src="https://img.shields.io/badge/gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white"> 

### DB
<img src="https://img.shields.io/badge/amazon rds-527FFF?style=for-the-badge&logo=amazonrds&logoColor=white"> <img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white"> <img src="https://img.shields.io/badge/jasypt-0769AD?style=for-the-badge&logoColor=white"> <img src="https://img.shields.io/badge/redis-DC382D?style=for-the-badge&logo=redis&logoColor=white"> 

### CI/CD
<img src="https://img.shields.io/badge/jenkins-D24939?style=for-the-badge&logo=jenkins&logoColor=white"> <img src="https://img.shields.io/badge/docker-2496ED?style=for-the-badge&logo=docker&logoColor=white"> <img src="https://img.shields.io/badge/docker hub-2496ED?style=for-the-badge&logo=docker&logoColor=white"> 

### Deploy
<img src="https://img.shields.io/badge/amazon ec2-FF9900?style=for-the-badge&logo=amazon ec2&logoColor=white"> 

### Develop Tool
<img src="https://img.shields.io/badge/intelliJ-000000?style=for-the-badge&logo=intellij idea&logoColor=white"> <img src="https://img.shields.io/badge/postman-FF6C37?style=for-the-badge&logo=postman&logoColor=white"> <img src="https://img.shields.io/badge/swagger-85EA2D?style=for-the-badge&logo=swagger&logoColor=white"> <img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white"> <img src="https://img.shields.io/badge/git-F05032?style=for-the-badge&logo=git&logoColor=white">
<br> 
<br>

## Project Architecture
![project architecture](https://user-images.githubusercontent.com/90203250/225488610-1f3b1b4b-e2ec-454b-9ab0-035788414cbf.png)


<br>

## Project Structure

<details>
<summary>Details</summary>

```jsx
.
│  .gitignore
│  build.gradle
│  Dockerfile
│  gradlew
│  gradlew.bat
│  result.txt
│  settings.gradle
│                      
├─gradle
│  └─wrapper
│          gradle-wrapper.jar
│          gradle-wrapper.properties
│          
└─src
    ├─main
    │   ├─java
    │   │  └─com
    │   │      └─codepatissier
    │   │          └─keki
    │   │               ├── KekiApplication.java
    │   │               ├── TestController.java
    │   │               ├── auth
    │   │               ├── calendar
    │   │               │   ├── CalendarCategory.java
    │   │               │   ├── DateCountCategory.java
    │   │               │   ├── contoller
    │   │               │   │   └── CalendarController.java
    │   │               │   ├── dto
    │   │               │   │   ├── CalendarDateReturn.java
    │   │               │   │   ├── CalendarEditRes.java
    │   │               │   │   ├── CalendarHashTag.java
    │   │               │   │   ├── CalendarListRes.java
    │   │               │   │   ├── CalendarReq.java
    │   │               │   │   ├── CalendarRes.java
    │   │               │   │   ├── HomePostRes.java
    │   │               │   │   ├── HomeRes.java
    │   │               │   │   ├── HomeTagRes.java
    │   │               │   │   ├── PopularTagRes.java
    │   │               │   │   ├── TagRes.java
    │   │               │   │   └── TagStatus.java
    │   │               │   ├── entity
    │   │               │   │   ├── Calendar.java
    │   │               │   │   └── CalendarTag.java
    │   │               │   ├── repository
    │   │               │   │   ├── Calendar
    │   │               │   │   │   ├── CalendarCustom.java
    │   │               │   │   │   ├── CalendarRepository.java
    │   │               │   │   │   └── CalendarRepositoryImpl.java
    │   │               │   │   └── CalendarTag
    │   │               │   │       ├── CalendarTagCustom.java
    │   │               │   │       ├── CalendarTagRepository.java
    │   │               │   │       └── CalendarTagRepositoryImpl.java
    │   │               │   └── service
    │   │               │       └── CalendarService.java
    │   │               ├── common
    │   │               │   ├── BaseEntity.java
    │   │               │   ├── BaseException.java
    │   │               │   ├── BaseResponse.java
    │   │               │   ├── BaseResponseStatus.java
    │   │               │   ├── BeanUtils.java
    │   │               │   ├── Constant.java
    │   │               │   ├── EmptyStringToNullConverter.java
    │   │               │   ├── Role.java
    │   │               │   ├── config
    │   │               │   │   ├── JasyptConfig.java
    │   │               │   │   ├── QueryDslConfig.java
    │   │               │   │   ├── RedisConfig.java
    │   │               │   │   ├── SwaggerConfig.java
    │   │               │   │   └── WebSecurityConfig.java
    │   │               │   ├── entityListener
    │   │               │   │   ├── CalendarEntityListener.java
    │   │               │   │   ├── DessertEntityListener.java
    │   │               │   │   ├── PostEntityListener.java
    │   │               │   │   ├── StoreEntityListener.java
    │   │               │   │   └── UserEntityListener.java
    │   │               │   └── tag
    │   │               │       ├── Tag.java
    │   │               │       └── TagRepository.java
    │   │               ├── cs
    │   │               │   ├── controller
    │   │               │   │   └── CsController.java
    │   │               │   ├── dto
    │   │               │   │   ├── GetNoticeListRes.java
    │   │               │   │   └── GetNoticeRes.java
    │   │               │   ├── entity
    │   │               │   │   ├── Hide.java
    │   │               │   │   ├── Notice.java
    │   │               │   │   ├── Report.java
    │   │               │   │   └── ReportCategory.java
    │   │               │   ├── repository
    │   │               │   │   ├── HideRepository.java
    │   │               │   │   ├── NoticeRepository.java
    │   │               │   │   └── ReportRepository.java
    │   │               │   └── service
    │   │               │       └── CsService.java
    │   │               ├── dessert
    │   │               │   ├── controller
    │   │               │   │   └── DessertController.java
    │   │               │   ├── dto
    │   │               │   │   ├── GetDessertRes.java
    │   │               │   │   ├── GetStoreDessertRes.java
    │   │               │   │   ├── GetStoreDessertsRes.java
    │   │               │   │   ├── OptionDTO.java
    │   │               │   │   ├── PatchDessertReq.java
    │   │               │   │   └── PostDessertReq.java
    │   │               │   ├── entity
    │   │               │   │   ├── Dessert.java
    │   │               │   │   └── Option.java
    │   │               │   ├── repository
    │   │               │   │   ├── DessertRepository.java
    │   │               │   │   └── OptionRepository.java
    │   │               │   └── service
    │   │               │       └── DessertService.java
    │   │               ├── history
    │   │               │   ├── controller
    │   │               │   │   └── HistoryController.java
    │   │               │   ├── dto
    │   │               │   │   ├── HistorySearchRes.java
    │   │               │   │   ├── PostSearchRes.java
    │   │               │   │   └── SearchRes.java
    │   │               │   ├── entity
    │   │               │   │   ├── PostHistory.java
    │   │               │   │   └── SearchHistory.java
    │   │               │   ├── repository
    │   │               │   │   ├── PostHistoryCustom.java
    │   │               │   │   ├── PostHistoryRepository.java
    │   │               │   │   ├── PostHistoryRepositoryImpl.java
    │   │               │   │   ├── SearchHistoryCustom.java
    │   │               │   │   ├── SearchHistoryRepository.java
    │   │               │   │   └── SearchHistoryRepositoryImpl.java
    │   │               │   └── service
    │   │               │       ├── PostHistoryService.java
    │   │               │       └── SearchHistoryService.java
    │   │               ├── order
    │   │               │   ├── controller
    │   │               │   │   └── OrderController.java
    │   │               │   ├── entity
    │   │               │   │   ├── OptionOrder.java
    │   │               │   │   ├── Order.java
    │   │               │   │   ├── OrderImg.java
    │   │               │   │   └── OrderStatus.java
    │   │               │   ├── repository
    │   │               │   │   └── OrderRepository.java
    │   │               │   └── service
    │   │               │       └── OrderService.java
    │   │               ├── post
    │   │               │   ├── controller
    │   │               │   │   └── PostController.java
    │   │               │   ├── dto
    │   │               │   │   ├── DessertsRes.java
    │   │               │   │   ├── GetLikePostRes.java
    │   │               │   │   ├── GetLikePostsRes.java
    │   │               │   │   ├── GetMakePostRes.java
    │   │               │   │   ├── GetModifyPostRes.java
    │   │               │   │   ├── GetPostRes.java
    │   │               │   │   ├── GetPostsRes.java
    │   │               │   │   ├── PatchPostReq.java
    │   │               │   │   ├── PostPostReq.java
    │   │               │   │   └── PostReportReq.java
    │   │               │   ├── entity
    │   │               │   │   ├── Post.java
    │   │               │   │   ├── PostImg.java
    │   │               │   │   ├── PostLike.java
    │   │               │   │   └── PostTag.java
    │   │               │   ├── repository
    │   │               │   │   ├── PostCustom.java
    │   │               │   │   ├── PostImgRepository.java
    │   │               │   │   ├── PostLikeRepository.java
    │   │               │   │   ├── PostRepository.java
    │   │               │   │   ├── PostRepositoryImpl.java
    │   │               │   │   ├── PostTagCustom.java
    │   │               │   │   ├── PostTagRepository.java
    │   │               │   │   └── PostTagRepositoryImpl.java
    │   │               │   └── service
    │   │               │       └── PostService.java
    │   │               ├── store
    │   │               │   ├── controller
    │   │               │   │   └── StoreController.java
    │   │               │   ├── dto
    │   │               │   │   ├── GetMyPageStoreProfileRes.java
    │   │               │   │   ├── GetStoreInfoRes.java
    │   │               │   │   ├── GetStoreProfileRes.java
    │   │               │   │   ├── PatchProfileReq.java
    │   │               │   │   ├── PostStoreReq.java
    │   │               │   │   └── PostStoreRes.java
    │   │               │   ├── entity
    │   │               │   │   └── Store.java
    │   │               │   ├── repository
    │   │               │   │   └── StoreRepository.java
    │   │               │   └── service
    │   │               │       └── StoreService.java
    │   │               └── user
    │   │                   ├── controller
    │   │                   │   └── UserController.java
    │   │                   ├── dto
    │   │                   │   ├── GetProfileRes.java
    │   │                   │   ├── PatchProfileReq.java
    │   │                   │   ├── PostCustomerReq.java
    │   │                   │   ├── PostNicknameReq.java
    │   │                   │   ├── PostTokenReq.java
    │   │                   │   ├── PostUserReq.java
    │   │                   │   └── PostUserRes.java
    │   │                   ├── entity
    │   │                   │   ├── Provider.java
    │   │                   │   └── User.java
    │   │                   ├── repository
    │   │                   │   └── UserRepository.java
    │   │                   └── service
    │   │                       └── AuthService.java
    │   └── resources
    │       ├── application-oauth.properties
    │       ├── application-redis.properties
    │       └── application.properties
    └─test
        └─java
            └─com
                └─codepatissier
                    └─keki
                         KekiApplicationTests.java
```
<br>
</details>
<br><br>


## DB 
![keki-erd](https://user-images.githubusercontent.com/80838501/218660483-b06e9835-7dd5-4292-aa94-073ae466c235.png)

<br>

## Commit/PR Convention
**Commit**
```
#1 feat: 일정 등록 API 추가
```
- #이슈번호 타입: 커밋 설명
<br>

**Pull Request**
```
[feature/1-create-calender] 일정 등록
```
- [브랜치명]  설명
<br>

## Branch Strategy
- main
    - 배포 이력 관리 목적
- develop
    - feature 병합용 브랜치
    - 배포 전 병합 브랜치
- feature
    - develop 브랜치를 베이스로 기능별로 feature 브랜치 생성해 개발
- test
    - 테스트가 필요한 코드용 브랜치
- hotfix
    - 배포 후 버그 발생 시 버그 수정 
<br>

- feature branch의 경우, 기능명/이슈번호-기능설명 형태로 작성
```md
feature/7-desserts-patchDessert
```
<br>

## API
[API 명세서]( https://broadleaf-mist-919.notion.site/API-72e2b7a446544ee18493354baa0cda17 )
<br>
<br>

## Member
|[박소정](https://github.com/sojungpp)|[장채은](https://github.com/chaerlo127)|[김중현](https://github.com/JoongHyun-Kim)|[박서연](https://github.com/psyeon1120)|
|:---:|:---:|:---:|:---:|
|<img src="https://github.com/sojungpp.png" width="180" height="180" >|<img src="https://github.com/chaerlo127.png" width="180" height="180" >|<img src="https://github.com/JoongHyun-Kim.png" width="180" height="180" >|<img src="https://github.com/psyeon1120.png" width="180" height="180">|
| **PM & Backend Developer** | **PL & Backend Developer**| **Backend Developer** | **Backend Developer** |
