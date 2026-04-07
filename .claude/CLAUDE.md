# CLAUDE.md
## 1. 규칙

### 1.1 아키텍처 규칙
- 계층 구조는 `Controller → Facade/Application Service → Domain Service → Repository` 를 따른다.
- Controller 는 요청/응답 변환과 검증만 담당한다.
- 여러 도메인이나 외부 시스템을 조합하는 트랜잭션 흐름은 Facade 에서만 조정한다.
- Repository 는 영속성 처리만 담당하며 비즈니스 로직을 넣지 않는다.
- Entity 를 API 응답으로 직접 반환하지 않는다. 반드시 DTO/Response 객체로 변환한다.

### 1.2 트랜잭션 규칙
- 트랜잭션 시작 지점은 기본적으로 Facade/Application Service 로 한정한다.
- 하위 Service/Repository 에 무분별하게 `@Transactional` 을 중복 선언하지 않는다.
- 여러 작업이 하나의 유스케이스를 이루면 하나의 트랜잭션 경계로 묶는다.
- 외부 시스템 호출은 DB 트랜잭션과의 정합성을 고려해서 배치한다.
- 외부 파일 업로드 후 DB 저장 실패로 orphan 데이터가 생길 수 있는 경우, 허용 여부를 명시적으로 판단한다.

### 1.3 코드 변경 규칙
- 기존 구조와 네이밍 스타일을 우선 존중한다.
- 큰 구조 변경은 사전 합의 없이 진행하지 않는다.
- 사용하지 않는 추상화, 과도한 인터페이스, 불필요한 디자인 패턴 추가를 지양한다.
- 한 번에 너무 많은 리팩터링을 하지 말고, 변경 목적에 필요한 범위만 수정한다.
- 하위 호환성이 중요한 API 는 기존 계약을 깨지 않는다.

### 1.4 금지 사항
- Controller 에 비즈니스 로직 작성 금지
- Service 에서 HTTP 요청/응답 객체 직접 의존 금지
- Entity 직접 직렬화 금지
- 매직 넘버/매직 문자열 방치 금지
- null 반환 남발 금지
- 로그에 민감정보 출력 금지

### 1.5 스택
Spring boot
postgresql
jpa

## 2. 아키텍처
presentation → application → domain → infrastructure

### 2.1 기본 구조
이 프로젝트는 Spring 기반의 계층형 아키텍처를 사용한다.

```text
presentation
 └─ controller
application
 ├─ facade
 ├─ service
 └─ dto
domain
 ├─ model
 ├─ enum
 └─ policy
infrastructure
 ├─ repository
 ├─ client
 └─ config
 ```

### 계층 책임
- Controller: 요청/응답 처리
- Facade: 유스케이스 + 트랜잭션
- Service: 도메인 로직
- Repository: DB 접근

## 3. 빌드 / 실행 / 테스트
### Gradle
./gradlew build
./gradlew test
./gradlew bootRun

## 4. 도메인 컨텍스트

이 프로젝트는 간단한 CRUD 중심의 Spring 애플리케이션이다.

### 핵심 목적
- 게시글을 생성, 조회, 수정, 삭제할 수 있다.
- 계층 구조와 코딩 컨벤션이 잘 지켜지는지 확인하는 것이 우선 목표다.
- 복잡한 도메인 규칙보다 유지보수 가능한 구조를 만드는 데 집중한다.

### 주요 개념
- Post
  - 사용자가 작성하는 게시글
  - title, content, createdAt, updatedAt 값을 가진다.

### 현재 범위
- 게시글 등록
- 게시글 단건 조회
- 게시글 목록 조회
- 게시글 수정
- 게시글 삭제

### 현재 제외 범위
- 인증/인가
- 파일 업로드
- 외부 API 연동
- 캐시/메시지큐
- 복잡한 상태 전이

### 도메인 규칙
- 제목과 내용은 비어 있을 수 없다.
- 삭제된 게시글은 기본 목록 조회에서 제외한다.
- API 응답에는 Entity를 직접 노출하지 않고 DTO를 사용한다.

## 5. 코딩 컨벤션
### 네이밍
- Controller: XXXController
- Service: XXXService
- Facade: XXXFacade
- Repository: XXXRepository
- DTO: XXXRequest / XXXResponse

### 메서드
- create / find / update / delete

### boolean
- isXXX, hasXXX

## 6. 트랜잭션 정책
- Facade에서만 @Transactional
- 외부 API 실패 시 rollback 여부 명시
- 파일 업로드 실패 → 전체 실패
- 파일 삭제 실패 → 허용 가능

## 7. 예외 처리
- BusinessException
- ExternalApiException
- InfrastructureException

## 8. 테스트
- given / when / then 구조
- 외부 API는 mock
- 도메인은 실제 로직 검증

## 9. 리뷰 기준
- Controller에 로직 없는가
- 트랜잭션 적절한가
- DTO/Entity 분리 되었는가

## 10. AI 코드 생성 규칙
- 기존 구조 유지
- 불필요한 리팩터링 금지
- 테스트 가능한 코드 작성
