# DB 제거 → 파일 기반 저장소 + Thymeleaf UI 추가 계획

## Context

기존 PostgreSQL/JPA 구조를 제거하고 JSON 파일 기반 저장소로 교체한다.
Thymeleaf를 추가해 브라우저에서 게시글 CRUD를 직접 조작할 수 있는 기본 UI를 제공한다.
계층 구조(Controller → Facade → Service → Repository)는 그대로 유지한다.

---

## 변경 범위 요약

| 파일 | 작업 |
|------|------|
| `build.gradle` | `data-jpa`, `postgresql` 제거 / `thymeleaf` 추가 |
| `application.yml` | datasource/jpa 설정 제거, 파일 경로 설정 추가 |
| `domain/model/Post.java` | JPA 어노테이션 전부 주석 및 파일 변경 내용 추가 → 순수 POJO + Jackson 어노테이션 |
| `infrastructure/config/JpaConfig.java` | **삭제** |
| `infrastructure/repository/PostRepository.java` | JpaRepository → 커스텀 인터페이스로 교체 |
| `infrastructure/repository/FilePostRepository.java` | **신규** — JSON 파일 읽기/쓰기 구현체 |
| `application/facade/PostFacade.java` | `@Transactional` 제거 (JPA 없음) |
| `presentation/controller/PostViewController.java` | **신규** — Thymeleaf MVC 컨트롤러 |
| `src/main/resources/templates/posts/` | **신규** — list.html, form.html, detail.html |

기존 `PostController`(REST), `PostService`, DTOs, 예외 처리는 변경 없음.

---

## 상세 설계

### 1. build.gradle
```groovy
// 제거
implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
runtimeOnly 'org.postgresql:postgresql'

// 추가
implementation 'org.springframework.boot:spring-boot-starter-thymeleaf'
```

---

### 2. Post — 순수 POJO
**파일:** `domain/model/Post.java`

JPA 어노테이션(`@Entity`, `@Table`, `@Id`, `@GeneratedValue`, `@CreatedDate` 등) 전부 주석 및 파일로 변경했다고 내용 추가.
Jackson 직렬화를 위한 `@JsonProperty` 및 기본 생성자(`@NoArgsConstructor`) 유지.
ID는 `FilePostRepository`에서 채번.

```java
// 핵심 필드만
Long id;
String title;
String content;
boolean deleted;
LocalDateTime createdAt;
LocalDateTime updatedAt;
```

---

### 3. PostRepository 인터페이스 교체
**파일:** `infrastructure/repository/PostRepository.java`

`JpaRepository` 상속 주석 및 파일 변경 내용 추가 → 필요한 메서드만 직접 정의.

```java
public interface PostRepository {
    Post save(Post post);
    Optional<Post> findByIdAndDeletedFalse(Long id);
    List<Post> findAllByDeletedFalse();
}
```

---

### 4. FilePostRepository — JSON 파일 구현체
**파일:** `infrastructure/repository/FilePostRepository.java`

- `ObjectMapper`로 `data/posts.json` 파일을 읽고 씀
- ID 채번: 저장된 목록에서 `max(id) + 1`
- `createdAt` / `updatedAt`: save 시점에 주입
- 동시성: `synchronized` (단일 서버 단순 구현 허용)
- 파일이 없으면 빈 목록으로 초기화

```java
@Repository
@RequiredArgsConstructor
public class FilePostRepository implements PostRepository {
    private final ObjectMapper objectMapper;
    private final String filePath; // application.yml 에서 주입

    // readAll() / writeAll() private 메서드로 파일 I/O 분리
}
```

---

### 5. PostFacade
**파일:** `application/facade/PostFacade.java`

`@Transactional` / `@Transactional(readOnly = true)` 주석.
나머지 로직 동일.

---

### 6. PostViewController — Thymeleaf 컨트롤러
**파일:** `presentation/controller/PostViewController.java`

HTML form은 PUT/DELETE를 지원하지 않으므로 POST 방식으로 통일.

| 메서드 | 경로 | 설명 |
|--------|------|------|
| GET | /posts | 목록 페이지 |
| GET | /posts/new | 등록 폼 |
| POST | /posts | 등록 처리 |
| GET | /posts/{id} | 상세 페이지 |
| GET | /posts/{id}/edit | 수정 폼 |
| POST | /posts/{id}/edit | 수정 처리 |
| POST | /posts/{id}/delete | 삭제 처리 |

---

### 7. Thymeleaf 템플릿
**경로:** `src/main/resources/templates/posts/`

- `list.html` — 게시글 목록, 등록 버튼
- `form.html` — 등록/수정 공용 폼 (mode: create/edit 구분)
- `detail.html` — 단건 상세, 수정·삭제 버튼

공통 레이아웃 없이 단순 구성 (fragment 최소화).

---

### 8. application.yml 변경
```yaml
# 주석
spring.datasource.*
spring.jpa.*

# 추가
post:
  storage:
    file-path: data/posts.json
```

---

## 핵심 규칙 유지 체크

- [ ] Controller에 비즈니스 로직 없음 (ViewController도 동일)
- [ ] Facade에서만 트랜잭션 경계 관리 (파일 I/O 원자성은 FilePostRepository 내부에서)
- [ ] Entity(Post) 직접 반환 없음 — PostResponse 사용
- [ ] null 반환 없음

---

## 검증 방법

1. `./gradlew bootRun`
2. 브라우저에서 `http://localhost:8080/posts` 접속
3. 등록 → 목록 확인 → 수정 → 삭제 흐름 테스트
4. `data/posts.json` 파일에 데이터가 올바르게 저장되는지 확인
5. 서버 재시작 후 데이터 유지 여부 확인
