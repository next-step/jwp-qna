3단계-질문 삭제하기 리팩터링
===
# 미션 설명
### 영상 내용 정리
다음과 같은 layered architecture 구조에서 상태 변경과 연산 등의 핵심 비지니스 로직은 어디에 구현하는 것이 맞을까?

> Web 계층 : Presentation Layer
- controllers, exception handlers, filters, view templates ...

> DTOs 계층
- Layer간의 데이터를 이동하기 위한 연결 계층

> Service 계층 : thin Layer
(로직이 위치하는 계층이 아닌 다른 계층을 연결하면서 데이터 조회 및 도메인 객체의 로직을 호출하는 계층, 즉 요청을 처리하기 위한 여러 작업을 하나의 트랜잭션으로 묶는 것이 주 목적인 얇은 계층)
- application services and infrastructure services
- coexistence DTOs and Domain Model
- Web 계층과 Repository 계층의 의존 관계 연결 부

> Domain 계층
- domain services, entities, and value objects

> Repository 계층
- repository interfaces and their implementations
- DB 인터페이스, 외부 API 인터페이스

> 서비스 계층에 비지니스 로직을 구현하는 경우 어떤 문제가 있을까?
```
서비스 계층에는 API, DB와 같이 외부 자원과의 연동을 담당하는 부분과 의존성을 가지는데, 
해당 계층에 구현한 로직이 의도한대로 동작하는지 검증하기 위해서는 
외부 자원과 연동하여 출력 값을 받아오거나 혹은 Mocking, Stubbing과 같은 Mock 프레임워크를 활용하여 
의존성의 행위를 직접 정의하는 방법을 통해 테스트 코드를 구성해야 한다. 
즉, 테스트 코드 작성이 어려운 문제가 있다.

따라서 서비스 계층의 비지니스 로직을 연관된 도메인 계층으로 분리 함으로써 로직과 외부 의존성을 격리할 수 있고,
외부 의존성으로 부터 격리된 도메인 계층의 비지니스 로직은 테스트 하기 쉬운 환경을 가지게 된다.

또한 비지니스 로직을 도메인 계층으로 분산하는 경우 의존성과의 격리 외에 아래와 같은 이점을 가질 수 있다.
1. 외부 연동부와 격리됨에 따라 테스트를 위해 외부 자원과 연동하거나 의존성의 행위를 정의하는 과정이 불필요하다.
2. 관련 도메인이 직접 로직을 처리함에 따라 외부 계층으로 로직이 분산되지 않고 도메인에 응집되어 유지보수성을 향상 시킬 수 있으며 무분별한 Getter 사용을 억제 할 수 있다.
```

### 기능 요구 사항
> QnA 서비스를 만들어가면서 JPA로 실제 도메인 모델을 어떻게 구성하고, 객체와 테이블을 어떻게 매핑해야 하는지 알아본다.
- 질문 데이터를 완전히 삭제하는 것이 아니라 데이터의 상태를 삭제 상태(deleted - boolean type)로 변경한다.
- 로그인 사용자와 질문한 사람이 같은 경우 삭제할 수 있다.
- 답변이 없는 경우 삭제가 가능하다.
- 질문자와 답변 글의 모든 답변자 같은 경우 삭제가 가능하다.
- 질문을 삭제할 때 답변 또한 삭제해야 하며, 답변의 삭제 또한 삭제 상태(deleted)를 변경한다.
- 질문자와 답변자가 다른 경우 답변을 삭제할 수 없다.
- 질문과 답변 삭제 이력에 대한 정보를 DeleteHistory를 활용해 남긴다.

### 프로그래밍 요구 사항
- qna.service.QnaService의 deleteQuestion()는 앞의 질문 삭제 기능을 구현한 코드이다. 이 메서드는 단위 테스트하기 어려운 코드와 단위 테스트 가능한 코드가 섞여 있다.
- 단위 테스트하기 어려운 코드와 단위 테스트 가능한 코드를 분리해 단위 테스트 가능한 코드에 대해 단위 테스트를 구현한다.
- 리팩터링을 완료한 후에도 src/test/java 디렉터리의 qna.service.QnaServiceTest의 모든 테스트가 통과해야 한다.
```
@Transactional
public void deleteQuestion(User loginUser, Long questionId) throws CannotDeleteException {
    Question question = findQuestionById(questionId);
    if (!question.isOwner(loginUser)) {
        throw new CannotDeleteException("질문을 삭제할 권한이 없습니다.");
    }

    List<Answer> answers = question.getAnswers();
    for (Answer answer : answers) {
        if (!answer.isOwner(loginUser)) {
            throw new CannotDeleteException("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
        }
    }

    List<DeleteHistory> deleteHistories = new ArrayList<>();
    question.setDeleted(true);
    deleteHistories.add(new DeleteHistory(ContentType.QUESTION, questionId, question.getWriterId(), LocalDateTime.now()));
    for (Answer answer : answers) {
        answer.setDeleted(true);
        deleteHistories.add(new DeleteHistory(ContentType.ANSWER, answer.getId(), answer.getWriterId(), LocalDateTime.now()));
    }
    deleteHistoryService.saveAll(deleteHistories);
}
```

- 자바 코드 컨벤션을 지키면서 프로그래밍한다.
  - 기본적으로 Google Java Style Guide을 원칙으로 한다.
  - 단, 들여쓰기는 '2 spaces'가 아닌 '4 spaces'로 한다.
- indent(인덴트, 들여쓰기) depth를 2를 넘지 않도록 구현한다. 1까지만 허용한다.
  - 예를 들어 while문 안에 if문이 있으면 들여쓰기는 2이다.
  - 힌트: indent(인덴트, 들여쓰기) depth를 줄이는 좋은 방법은 함수(또는 메서드)를 분리하면 된다.
- 3항 연산자를 쓰지 않는다.
- else 예약어를 쓰지 않는다.
  - else 예약어를 쓰지 말라고 하니 switch/case로 구현하는 경우가 있는데 switch/case도 허용하지 않는다.
  - 힌트: if문에서 값을 반환하는 방식으로 구현하면 else 예약어를 사용하지 않아도 된다.
- 모든 기능을 TDD로 구현해 단위 테스트가 존재해야 한다. 단, UI(System.out, System.in) 로직은 제외
  - 핵심 로직을 구현하는 코드와 UI를 담당하는 로직을 구분한다.
  - UI 로직을 InputView, ResultView와 같은 클래스를 추가해 분리한다.
- 함수(또는 메서드)의 길이가 10라인을 넘어가지 않도록 구현한다.
  - 함수(또는 메소드)가 한 가지 일만 하도록 최대한 작게 만들어라.
- 배열 대신 컬렉션을 사용한다.
- 모든 원시 값과 문자열을 포장한다
- 줄여 쓰지 않는다(축약 금지).
- 일급 컬렉션을 쓴다.
- 모든 엔티티를 작게 유지한다.
- 3개 이상의 인스턴스 변수를 가진 클래스를 쓰지 않는다.

### 힌트
- 테스트하기 쉬운 부분과 테스트하기 어려운 부분을 분리해 테스트 가능한 부분만 단위 테스트한다.
- 객체의 상태데이터를 꺼내지 말고(get) 메시지를 보낸다.
- 규칙 8: 일급 콜렉션을 쓴다
  - Question의 List<Answer>를 일급 콜렉션으로 구현해 본다.
- 규칙 7: 3개 이상의 인스턴스 변수를 가진 클래스를 쓰지 않는다.
  - 인스턴스 변수의 수를 줄이기 위해 도전한다.

---
# 구현
### 레거시 리팩토링
1단계
- 서비스 레이어에 단위 테스트를 추가한 후 비지니스 로직을 도메인 객체로 이동하는 리팩토링을 진행
  - Service Layer의 비지니스 로직 TC 구성을 위해 DB 의존부를 Mocking하고 있는 코드를, 비지니스로직을 도메인 객체로 이동함으로써 제거 할 수있다. 
  즉, 테스트하기 어려운 부분을 로직을 도메인 객체로 이동하여 외부 연동부와의 의존도를 제거함으로써 테스트하기 쉽도록 구성하며, 
  부가적으로 도메인 객체에 해당 도메인이 표현가능한 비지니스 로직을 구성하여 도메인에 대해 풍부하게 표현할 수 있게되었으며 로직에대한 응집도가 높아졋다. 
  추가적으로 Getter를 이용해서 도메인의 속성을 외부 Layer에서 끄집어 내서 로직을 구현하는 부분을 제거해서 로직이 파편화 되는 것을 막을 수 있다. 
  즉 도메인 계층으로 핵심 비지니스 로직을 이동하였을때 유지보수성이 향상, 도메인 응집력이 향상, 가독성이 향상, 서비스레이어가 얇아짐, 외부 의존성없이 테스트 가능 등의 이점을 가진다. 관련된 도메인을 파라미터로 하는 서비스레이어에서 로직을 처리하는 부분을 분리하여, 각 도메인에 책임을 분산시킬 수 있다.
- Acceptance Test(인수테스트)를 추가한 후 리팩토링

# 미션 진행

### 구현 목표 : 질문의 삭제 상태 변경
### 구현 조건
- 질문을 삭제하는 사용자와 질문 작성자는 같아야 한다.
- 답변이 없는 질문은 삭제 가능하다.
- 질문 작성자가 모든 답변을 작성한 경우, 해당 질문은 삭제 가능하다.
  - 질문 삭제 시, 해당 질문의 모든 답변 삭제 상태 또한 변경한다.
- 질문과 답변 삭제 시 이력을 남긴다.

### 유효성 검증
- 질문작성자가 아닌 경우 삭제할 수 없다.
- 질문작성자와 답변작성자가 다른 경우 삭제할 수 없다.

### TODO List
질문 삭제
Domain
- [x] 질문의 삭제 상태 변경
  - [x] 질문 삭제 TC 작성
  - [x] 질문 삭제 시 예외 발생 TC 작성
    - [x] 질문 삭제 요청자와 질문 작성자가 다른 경우
    - [x] 질문 작성자와 답변 작성자가 다른 경우
- [x] 질문과 답변의 삭제 이력 추가
  - [x] 답변이 없는 질문 삭제 시, 삭제 이력 생성
  - [x] 답변이 포함된 질문 삭제 시, 삭제 이력 생성
  - [x] 답변이 포함된 질문을 삭제하는 경우, 일급 컬렉션을 활용하여 삭제이력 목록 객체를 생성
- [x] 일급 컬렉션을 활용하여 질문의 답변 목록 객체 생성
  - [x] 질문 삭제 조건 만족시, 질문 삭제 처리와 질문에 대한 답변 목록의 각 삭제 상태 변환을 일괄 처리하는 메서드 구현
- [x] domain/repository를 구분하여 패키지 구조 정리

Service
- [x] 기존 비지니스 로직을 도메인 계층에 분리한 각 비지니스 로직 호출부로 변경 

Step2 피드백 TODO List
- [x] `Question`에 대한 모든 답변이 아닌 삭제되지 않은 답변 조회로 변경
- [x] 더 이상 사용하지 않는 의존성 제거
- [x] 참조가 없는 getter 메서드 삭제

---
### 구현시 주안점
- 서비스 계층의 비지니스 로직을 도메인 계층으로 이동하여 비지니스 로직과 외부 의존성을 격리하고, 테스트 코드를 통해 리팩토링 여부를 검증

### 구현 고려사항
- soft-delete : Hibernate의 @SqlDelete와 @OnDelete(action = OnDeleteAction.CASCADE)를 이용한 영속성 전이를 통해 delete 상태를 변경
  - 결국 서비스 계층에 비지니스 로직을 구현하게 되어 고려하지 않았음

### step3 코드 리뷰 피드백 내용 정리
- [ ] 사용하지 않는 불필요 메서드 제거
- [ ] 서비스 계층의 삭제 이력 생성 비지니스 로직 이동
  - `Quesion`, `Answer` 삭제 시, 삭제 이력을 반환하도록 서비스 계층의 비지니스 로직을 도메인 계층으로 분산 
- [ ] 답변과 질문의 의존성 끊기, 답변이 질문의 일부 역할을 가지고 있는 부분을 분리
  - 답변 삭제 시, 질문 작성자에 대한 유효성 검사 부 수정
- [ ] 답변만 삭제하는 경우에 대한 처리 및 TC 작성
- [ ] JPQL fetch join에 대한 추가 학습테스트
  - e.g. 질문 5개와 각 질문의 답변을 모두 조회하는 경우, 
    - [ ] `fetch join + limit 5 + distinct`를 이용한 조회 쿼리의 동작 살펴 보기
    - [ ] `fetch join + limit 5`를 이용한 조회 쿼리의 동작 살펴 보기
    - [ ] limit 유무에 따른 동작 방식 살펴보기
    - [ ] distinct 유무에 따른 동작 방식 살펴보기
