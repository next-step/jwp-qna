# 2단계 - 연관 관계 매핑

## 1. 요구사항 정의

### 1.1. 명시된 요구사항

#### 1.1.1. 요구사항

QnA 서비스를 만들어가면서 JPA로 실제 도메인 모델을 어떻게 구성하고 객체와 테이블을 어떻게 매핑해야 하는지 알아본다.

- 객체의 참조와 테이블의 외래 키를 매핑해서 객체에서는 참조를 사용하고 테이블에서는 외래 키를 사용할 수 있도록 한다.

#### 1.1.2. 힌트

- 이전 단계에서 엔티티 설계가 이상하다는 생각이 들었다면 객체 지향 설계를 의식하는 개발자고, 그렇지 않고 자연스러웠다면 데이터 중심의 개발자일 것이다. 객체 지향 설계는 각각의 객체가 맡은 역할과 책임이 있고 관련 있는 객체끼리 참조하도록 설계해야 한다.

```java
Question question = findQuestionById(questionId);
List<Answer> answers = answerRepository.findByQuestionIdAndDeletedFalse(questionId);
```
- 위 방식은 객체 설계를 테이블 설계에 맞춘 방법이다. 특히 테이블의 외래 키를 객체에 그대로 가져온 부분이 문제다. 왜냐하면 관계형 데이터베이스는 연관된 객체를 찾을 때 외래 키를 사용해서 조인하면 되지만 객체에는 조인이라는 기능이 없다. 객체는 연관된 객체를 찾을 때 참조를 사용해야 한다.
```java
Question question = findQuestionById(questionId);
List<Answer> answers = question.getAnswers();
```

아래의 DDL을 보고 유추한다.
- H2
    
```sql
alter table answer
    add constraint fk_answer_to_question
        foreign key (question_id)
            references question

alter table answer
    add constraint fk_answer_writer
        foreign key (writer_id)
            references user

alter table delete_history
    add constraint fk_delete_history_to_user
        foreign key (deleted_by_id)
            references user

alter table question
    add constraint fk_question_writer
        foreign key (writer_id)
            references user
```

- MySQL

```mysql
alter table answer
    add constraint fk_answer_to_question
        foreign key (question_id)
            references question (id)

alter table answer
    add constraint fk_answer_writer
        foreign key (writer_id)
            references user (id)

alter table delete_history
    add constraint fk_delete_history_to_user
        foreign key (deleted_by_id)
            references user (id)

alter table question
    add constraint fk_question_writer
        foreign key (writer_id)
            references user (id)
```

### 1.2. 기능 요구사항 정리

|구분 | 상세 |구현방법     |
|:----:  |:------  |:---------|
|엔티티 매핑|• QnA 서비스를 만들어가면서 JPA로 실제 도메인 모델을 어떻게 구성하고 객체와 테이블을 어떻게 매핑해야 하는지 알아본다.<br>&nbsp;&nbsp;&nbsp;• 객체의 참조와 테이블의 외래 키를 매핑해서 객체에서는 참조를 사용하고 테이블에서는 외래 키를 사용할 수 있도록 한다.|• `@Entity` 연관 관계 수정|
|엔티티 매핑|• `@DataJpaTest`를 사용하여 학습 테스트를 해 본다.|• `@DataJpaTest`를 사용|

### 1.3. 프로그래밍 요구사항

|구분|상세|구현 방법|
|:---:|:---|---|
|Convention|• 자바 코드 컨벤션을 지키면서 프로그래밍한다.<br>&nbsp;&nbsp;• https://naver.github.io/hackday-conventions-java/ <br>&nbsp;&nbsp;• https://google.github.io/styleguide/javaguide.html <br>&nbsp;&nbsp;•  https://myeonguni.tistory.com/1596 |- gradle-editorconfig 적용<br>- gradle-checkstyle 적용<br>- IntelliJ 적용<br>- Github 적용|
|테스트|• 모든 기능을 TDD로 구현해 단위 테스트가 존재해야 한다. 단, UI(System.out, System.in) 로직은 제외<br>&nbsp;&nbsp;• 핵심 로직을 구현하는 코드와 UI를 담당하는 로직을 구분한다.<br>&nbsp;&nbsp;•UI 로직을 InputView, ResultView와 같은 클래스를 추가해 분리한다.|- 핵심 로직 단위테스트|

### 1.4. 비기능 요구사항

|구분 |상세 |구현방법     |
|:----:  |:------  |:---------|
|요구사항|• 기능을 구현하기 전에 README.md 파일에 구현할 기능 목록을 정리해 추가한다.|- 요구사항 정의 정리|
|Convention|• git의 commit 단위는 앞 단계에서 README.md 파일에 정리한 기능 목록 단위로 추가한다.<br>&nbsp;&nbsp;• 참고문서 : [AngularJS Commit Message Conventions](https://gist.github.com/stephenparish/9941e89d80e2bc58a153)|- git commit 시 해당 convention 적용|

#### 1.4.1. AngularJS Commit Message Conventions 중

- commit message 종류를 다음과 같이 구분

```
feat (feature)
 fix (bug fix)
 docs (documentation)
 style (formatting, missing semi colons, …)
 refactor
 test (when adding missing tests)
 chore (maintain)
 ```

# 1.4.2. editorConfig setting
```
Execution failed for task ':editorconfigCheck'.
> There are .editorconfig violations. You may want to run

* Try:
Run with --stacktrace option to get the stack trace. Run with --info or --debug option to get more log output. Run with --scan to get full insights.
```

- 위와 같은 에러가 뜨면 다음을 실행한다. `./gradlew editorconfigFormat`

## 2. 분석 및 설계

### 2.1. 이번 Step 핵심 목표

#### 2.1.1. TDD (Test-Driven Developmet) : production code보다 test code를 먼저 작성한다.

> Cycle : Test Fail ---> Test Passes ---> Refactor ---> Test Fail ---> ...

#### 2.1.2. 연관 관계 매핑

> 출처 : 자바 ORM 표준 JPA 프로그래밍 (저: 김영한)

- 방향 : 단방향/양방향 중 하나
  - 단방향 : A -> B, B -> A 처럼 한 쪽만 참조하는 것
  - 양방향 : A <-> B 처럼 서로 참조하는 것
  - 객체 vs. 테이블
    - 객체 연관 관계 : 단방향 관계, 참조(주소)로 연관 관계를 맺음
    - 테이블 연관 관계 : 양방향 관계, 외래 키로 연관 관계를 맺음
- 연관 관계의 주인 : 객체를 양방향 연관 관계로 만들면 연관 관계의 주인을 정해야 한다.
  - 연관 관계의 주인을 정하는 기준 : **항상 외래 키가 있는 곳은 기준으로 매핑**
    - 단방향 : 항상 외래 키가 있는 곳은 기준으로 매핑
    - 양방향 : 외래 키가 있는 곳은 기준으로 매핑 - 비즈니스 로직상 중요도(X), 외래 키 관리자 정도의 의미(O)
- 다중성
  - 다대일
  - 일대다
  - 일대일
  - 다대다  


### 2.2. Todo List

- [ ] 0.기본 세팅
    - [x] 0-1.git fork/clone
        - [x] 0-1-1.NEXTSTEP 내 과제로 이동 및 '미션시작'
        - [x] 0-1-2.실습 github으로 이동
        - [x] 0-1-3.branch 'gregolee'로 변경
        - [x] 0-1-4.fork
        - [x] 0-1-5.clone : `git clone -b gregolee --single-branch https://github.com/gregolee/jwp-qna.git`
        - [x] 0-1-6.branch : `git checkout -b step1`
    - [x] 0-2.요구사항 정리
    - [x] 0-3.[AngularJS Commit Message Conventions](https://gist.github.com/stephenparish/9941e89d80e2bc58a153#generating-changelogmd) 참고
    - [x] 0-4.Slack을 통해 merge가 되는지 확인한 후에 코드 리뷰 2단계 과정으로 다음 단계 미션을 진행
        - [x] 0-4-1.gregolee(master) branch로 체크아웃 : `git checkout gregolee`
        - [x] 0-4-2.step1 branch 삭제 : `git branch -D step1`
        - [x] 0-4-3.step1 branch 삭제 확인 : `git branch -a`
        - [x] 0-4-4.원본(next-step) git repository를 remote로 연결 (미션 당 1회) : `git remote add -t gregolee upstream https://github.com/next-step/jwp-qna`
        - [x] 0-4-5.원본(next-step) git repository를 remote로 연결 확인 : `git remote -v`
        - [x] 0-4-6.원본(next-step) git repository에서 merge된 나의 branch(gregolee)를 fetch : `git fetch upstream gregolee`
        - [x] 0-4-7.remote에서 가져온 나의 branch로 rebase : `git rebase upstream/gregolee`
        - [x] 0-4-7.gregolee -> step2로 체크아웃 : `git checkout -b step2`
    - [x] 0-5.리뷰어님의 리뷰를 반영한 코드로 수정
        - [x] 0-5-1.추가 변경 사항 없음
- [x] 1.자바 코드 컨벤션을 위한 세팅
    - [x] 1-1.[gradle-editorconfig](https://naver.github.io/hackday-conventions-java/#editorconfig) 적용
    - [x] 1-2.[gradle-checkstyle](https://naver.github.io/hackday-conventions-java/#checkstyle) 적용
    - [x] 1-3.[IntelliJ](https://naver.github.io/hackday-conventions-java/#_intellij) 적용
    - [x] 1-4.[Github](https://naver.github.io/hackday-conventions-java/#_github) 적용
- [x] 2.학습
    - [x] 2-1. Hands-on JPA 정리
    - [x] 2-2. Hands-on JPA Test code 작성
- [x] 3.분석 및 설계
    - [x] 3-1.step02.md 초안 작성
- [x] 4.구현
    - [x] 4-1.테스트 코드 작성
        - [x] 4-1-1.`AnswerTest`
        - [x] 4-1-2.`DeleteHistoryTest`
        - [x] 4-1-3.`QuestionTest`
        - [x] 4-1-4.`UserTest`
    - [x] 4-2.연관 관계 매핑
        - [x] 4-2-1.`Answer`
        - [x] 4-2-2.`DeleteHistory`
        - [x] 4-2-3.`Question`
        - [x] 4-2-4.`User`
    - [x] 4-3.리뷰어님 피드백 반영
        - [x] 4-3-1.생성자 접근제어자 `public` -> `protected` 변경
        - [x] 4-3-2.getter/setter 제거
        - [x] 4-3-3.반복적인 필드 추상클래스 활용
            - [x] 4-3-3-1.중복된 필드 모아놓기
            - [x] 4-3-3-2.jpa auditing 적용
        - [x] 4-3-4.테스트 영역 구분
            - [x] 4-3-4-1.`AnswerTest` -> `AnswerTest`, `AnswerRepositoryTest`
            - [x] 4-3-4-2.`DeleteHistoryTest` -> `DeleteHistoryTest`, `DeleteHistoryRepositoryTest`
            - [x] 4-3-4-3.`QuestionTest` -> `QuestionTest`, `QuestionRepositoryTest`
            - [x] 4-3-4-4.`UserTest` -> `UserTest`, `UserRepositoryTest`
        - [x] 4-3-5.테스트 초기화 수정
            - [x] 4-3-5-1.`AnswerTest`
            - [x] 4-3-5-2.`AnswerRepositoryTest`
            - [x] 4-3-5-3.`DeleteHistoryTest`
            - [x] 4-3-5-4.`DeleteHistoryRepositoryTest`
            - [x] 4-3-5-5.`QuestionTest`
            - [x] 4-3-5-6.`QuestionRepositoryTest`
            - [x] 4-3-5-7.`UserTest`
            - [x] 4-3-5-8.`UserRepositoryTest`
        - [x] 4-3-6.불필요한 테스트 코드 정리
            - [x] 4-3-6-1.`AnswerTest`
            - [x] 4-3-6-2.`AnswerRepositoryTest`
            - [x] 4-3-6-3.`DeleteHistoryTest`
            - [x] 4-3-6-4.`DeleteHistoryRepositoryTest`
            - [x] 4-3-6-5.`QuestionTest`
            - [x] 4-3-6-6.`QuestionRepositoryTest`
            - [x] 4-3-6-7.`UserTest`
            - [x] 4-3-6-8.`UserRepositoryTest`
- [x] 5.테스트
    - [x] 5-1.Gradle build Success 확인
    - [x] 5-2.요구사항 조건들 충족했는지 확인
        - [x] 5-2-1.핵심 단위 로직 테스트
- [ ] 6.인수인계
    - [x] 6-1.소감 및 피드백 정리
        - [x] 6-1-1.느낀점 & 배운점 작성
        - [x] 6-1-2.피드백 요청 정리
    - [ ] 6-2.코드리뷰 요청 및 피드백
        - [ ] 6-1-1.step2를 gregolee/jwp-qna로 push : `git push origin step2`
        - [ ] 6-1-2.pull request(PR) 작성
    - [ ] 6-3.Slack을 통해 merge가 되는지 확인한 후에 미션 종료

## 3. 인수인계

### 3.1. 느낀점 & 배운점

#### 3.1.1. 느낀점

- 객체의 연관 관계 vs. 테이블의 연관 관계
  - 테이블 연관 관계을 주로 개발하다보니 생각보다 쉽지 않았습니다.
  - 과거 연관 관계 매핑에 대한 지식을 알고 있었으나, 패러다임에 대한 이해를 할 수 있었습니다.
  - 객체 지향적 프로그래밍을 위해서는 객체의 연관 관계와 테이블의 연관 관계를 이해하고 있어야 함을 알았습니다.  

#### 3.1.2. 배운점

- 방향 : 단방향/양방향 중 하나
    - 단방향 : A -> B, B -> A 처럼 한 쪽만 참조하는 것
    - 양방향 : A <-> B 처럼 서로 참조하는 것
    - 객체 vs. 테이블
        - 객체 연관 관계 : 단방향 관계, 참조(주소)로 연관 관계를 맺음
        - 테이블 연관 관계 : 양방향 관계, 외래 키로 연관 관계를 맺음
- 연관 관계의 주인 : 객체를 양방향 연관 관계로 만들면 연관 관계의 주인을 정해야 한다.
    - 연관 관계의 주인을 정하는 기준 : **항상 외래 키가 있는 곳은 기준으로 매핑**
        - 단방향 : 항상 외래 키가 있는 곳은 기준으로 매핑
        - 양방향 : 외래 키가 있는 곳은 기준으로 매핑 - 비즈니스 로직상 중요도(X), 외래 키 관리자 정도의 의미(O)
- 다중성
    - 다대일
    - 일대다
    - 일대일
    - 다대다

### 3.2. 피드백 요청

- 이번 PR에는 피드백 요청 드릴 것이 없습니다.
