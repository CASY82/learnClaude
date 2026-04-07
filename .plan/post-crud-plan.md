# Post CRUD 전체 구현 계획

## Context

CLAUDE.md에 정의된 계층형 아키텍처(presentation → application → domain → infrastructure)를
기반으로 게시글(Post) CRUD 기능을 처음부터 구현한다.
목적은 컨벤션과 계층 책임이 올바르게 지켜지는 구조를 만드는 것이다.

---

## 패키지 구조

```
src/main/java/{basePackage}/
├── presentation/
│   └── controller/
│       └── PostController.java
├── application/
│   ├── facade/
│   │   └── PostFacade.java
│   ├── service/
│   │   └── PostService.java
│   └── dto/
│       ├── PostCreateRequest.java
│       ├── PostUpdateRequest.java
│       └── PostResponse.java
├── domain/
│   ├── model/
│   │   └── Post.java
│   └── policy/
│       └── PostPolicy.java          (선택: 도메인 규칙 분리 시)
└── infrastructure/
    ├── repository/
    │   └── PostRepository.java
    └── config/
        └── JpaConfig.java           (필요 시)
```

---

## 구현 순서 및 상세

### 1. Domain — `Post` Entity
**파일:** `domain/model/Post.java`
- 필드: `id`, `title`, `content`, `createdAt`, `updatedAt`, `deleted(boolean)`
- `@Entity`, `@Table(name = "posts")`
- 생성자는 정적 팩토리 메서드 `Post.create(title, content)` 사용
- 수정 메서드: `update(title, content)` — 엔티티 내부에서 검증
- 삭제 메서드: `delete()` — `deleted = true`
- 도메인 규칙: 제목/내용이 비어 있으면 `BusinessException` 던짐

### 2. Infrastructure — `PostRepository`
**파일:** `infrastructure/repository/PostRepository.java`
- `JpaRepository<Post, Long>` 상속
- 삭제 제외 목록 조회: `findAllByDeletedFalse()`
- 단건 조회: `findByIdAndDeletedFalse(Long id)` — Optional 반환

### 3. Application — `PostService`
**파일:** `application/service/PostService.java`
- `@Transactional` 없음 (Facade에서 관리)
- 메서드:
  - `createPost(Post post)` → Post
  - `findPost(Long id)` → Post (없으면 BusinessException)
  - `findAllPosts()` → List<Post>
  - `updatePost(Long id, String title, String content)` → Post
  - `deletePost(Long id)` → void

### 4. Application — DTOs
**파일:** `application/dto/`

```java
// PostCreateRequest: title, content (검증 어노테이션 포함)
// PostUpdateRequest: title, content
// PostResponse: id, title, content, createdAt, updatedAt
//   + 정적 팩토리: PostResponse.from(Post post)
```

### 5. Application — `PostFacade`
**파일:** `application/facade/PostFacade.java`
- `@Transactional` 여기서만 선언
- 유스케이스별 메서드:
  - `createPost(PostCreateRequest)` → PostResponse
  - `getPost(Long id)` → PostResponse
  - `getPosts()` → List<PostResponse>
  - `updatePost(Long id, PostUpdateRequest)` → PostResponse
  - `deletePost(Long id)` → void

### 6. Presentation — `PostController`
**파일:** `presentation/controller/PostController.java`
- `@RestController`, `@RequestMapping("/api/posts")`
- 요청/응답 변환만 담당, 로직 없음
- 엔드포인트:

| 메서드 | 경로            | 설명        |
|--------|-----------------|-------------|
| POST   | /api/posts      | 게시글 등록 |
| GET    | /api/posts      | 목록 조회   |
| GET    | /api/posts/{id} | 단건 조회   |
| PUT    | /api/posts/{id} | 수정        |
| DELETE | /api/posts/{id} | 삭제        |

### 7. 예외 처리
**파일:** `domain/` 또는 공통 패키지
- `BusinessException` (RuntimeException) — 도메인 규칙 위반
- `ErrorCode` enum — 에러 코드 + 메시지 관리
- `GlobalExceptionHandler` (`@RestControllerAdvice`) — 예외를 통일된 응답으로 변환

---

## 핵심 규칙 체크리스트 (리뷰 기준)

- [ ] Controller에 비즈니스 로직 없음
- [ ] `@Transactional`은 Facade에만 선언
- [ ] Entity 직접 반환 없음 — 반드시 PostResponse로 변환
- [ ] null 반환 없음 — Optional 또는 예외 처리
- [ ] 매직 문자열/숫자 없음

---

## 검증 방법

1. `./gradlew bootRun` 으로 서버 기동
2. curl 또는 HTTP 클라이언트로 각 엔드포인트 호출
3. `./gradlew test` — given/when/then 구조로 PostService, PostFacade 단위 테스트
4. DB(PostgreSQL)에서 실제 데이터 확인 — deleted 플래그 동작 포함
