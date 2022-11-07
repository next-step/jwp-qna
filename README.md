# JPA

## 1단계 - 엔티티 매핑
### 요구사항
- [x] 엔티티 클래스, 리포지토리 클래스 생성
  - [x] Answer
  - [x] Question
  - [x] User
  - [x] DeleteHistory
- [x] DataJpaTest를 사용한 테스트를 코드 작성
  - [x] AnswerRepositoryTest
  - [x] QuestionRepositoryTest
  - [x] UserRepositoryTest
  - [x] DeleteHistoryRepositoryTest

---

## 2단계 - 연관 관계 매핑
### 요구사항
- [x] Answer -> Question refactor
- [x] Answer -> User refactor
- [x] DeleteHistory-> User refactor
- [x] Question-> User refactor
- [x] 테스트 코드 리팩토링
  - [x] DeleteHistoryRepository 테스트
  - [x] UserRepository 테스트
  - [x] AnswerRepository 테스트
  - [x] QuestionRepository 테스트
- [x] equals method, hashcode override 
- [x] 연관 entity 필요시에만 조회하도록 fatch 타입을 LAZY로 설정
- [x] 코드 포매팅

---

## 3단계 - 질문 삭제 리팩토링
### 기능 요구사항
 - 질문
  - [x] 질문 데이터를 삭제하지 않고, 데이터 삭제 상태를 (deleted - false)로 변경한다.
  - [x] 로그인 사용자와 질문한 사람이 같은 경우 삭제할 수 있다.
  - [x] 답변이 없는 경우 삭제.
  - [x] 질문자와 답변 글의 모든 답변자 같은 경우 삭제.
  - [x] 질문을 삭제할 때 답변 또한 삭제해야 하며, 답변의 삭제 또한 삭제 상태(deleted)를 변경한다.
  - [x] 질문 삭제 이력 정보를 DeleteHistory에 남긴다.
- 삭제
  - [x] 질문자와 답변자가 다른 경우 답변을 삭제할 수 없다.
  - [x] 답변 삭제 이력 정보를 DeleteHistory에 남긴다. 
- 할 일
- [ ] service 리팩토링

### 프로그래밍 요구사항
- `qna.service.QnaService`의 `deleteQuestion()`는 앞의 질문 삭제 기능을 구현한 코드이다.  
  이 메서드는 단위 테스트하기 어려운 코드와 단위 테스트 가능한 코드가 섞여 있다.
- 단위 테스트하기 어려운 코드와 단위 테스트 가능한 코드를 분리해 단위 테스트 가능한 코드에 대해 단위 테스트를 구현한다.
- 리팩터링을 완료한 후에도 `src/test/java` 디렉터리의 `qna.service.QnaServiceTest`의 모든 테스트가 통과해야 한다.
- 자바 코드 컨벤션을 지키면서 프로그래밍한다.
  기본적으로 Google Java Style Guide을 원칙으로 한다.  
  단, 들여쓰기는 '2 spaces'가 아닌 '4 spaces'로 한다.    
  indent(인덴트, 들여쓰기) depth는 1까지만 허용한다.  
  (while문 안에 if문이 있으면 들여쓰기는 2이다)  
- 3항 연산자를 쓰지 않는다.
- else와 switch/case 예약어를 쓰지 않는다.  
  (if문에서 값을 반환하는 방식으로 구현하면 else 예약어를 사용하지 않아도 된다.)
- 모든 기능을 TDD로 구현해 단위 테스트가 존재해야 한다. 단, UI(System.out, System.in) 로직은 제외.  
  핵심 로직을 구현하는 코드와 UI를 담당하는 로직을 구분한다.  
- UI 로직을 InputView, ResultView와 같은 클래스를 추가해 분리한다.
- 함수(또는 메서드)의 길이가 10라인을 넘어가지 않도록 구현한다.
- 함수(또는 메소드)가 한 가지 일만 하도록 최대한 작게 만들어라.
- 배열 대신 컬렉션을 사용한다.
- 모든 원시 값과 문자열을 포장한다
- 줄여 쓰지 않는다(축약 금지).
- 일급 컬렉션을 쓴다.
- 모든 엔티티를 작게 유지한다.
- 3개 이상의 인스턴스 변수를 가진 클래스를 쓰지 않는다.
