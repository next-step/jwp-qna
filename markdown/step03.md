# 3단계 - 질문 삭제하기 리팩터링

## 1. 요구사항 정의

### 1.1. 명시된 요구사항

#### 1.1.1. 기능 요구 사항

QnA 서비스를 만들어가면서 JPA로 실제 도메인 모델을 어떻게 구성하고 객체와 테이블을 어떻게 매핑해야 하는지 알아본다.

- 질문 데이터를 완전히 삭제하는 것이 아니라 데이터의 상태를 삭제 상태(deleted - boolean type)로 변경한다.
- 로그인 사용자와 질문한 사람이 같은 경우 삭제할 수 있다.
- 답변이 없는 경우 삭제가 가능하다.
- 질문자와 답변 글의 모든 답변자 같은 경우 삭제가 가능하다.
- 질문을 삭제할 때 답변 또한 삭제해야 하며, 답변의 삭제 또한 삭제 상태(deleted)를 변경한다.
- 질문자와 답변자가 다른 경우 답변을 삭제할 수 없다.
- 질문과 답변 삭제 이력에 대한 정보를 DeleteHistory를 활용해 남긴다.

#### 1.1.2. 프로그래밍 요구 사항

- `qna.service.QnaService`의 `deleteQuestion()`는 앞의 질문 삭제 기능을 구현한 코드이다. 이 메서드는 단위 테스트하기 어려운 코드와 단위 테스트 가능한 코드가 섞여 있다.
- 단위 테스트하기 어려운 코드와 단위 테스트 가능한 코드를 분리해 단위 테스트 가능한 코드에 대해 단위 테스트를 구현한다.
- 리팩터링을 완료한 후에도 `src/test/java` 디렉터리의 `qna.service.QnaServiceTest`의 모든 테스트가 통과해야 한다.

```java
@Transactional
public void deleteQuestion(User loginUser, Long questionId) {
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
   - 기본적으로 [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html) 을 원칙으로 한다.
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

#### 1.1.2. 힌트

- 테스트하기 쉬운 부분과 테스트하기 어려운 부분을 분리해 테스트 가능한 부분만 단위 테스트한다.
   - `QnaService.delete()`에서 Mock을 활용하지 않고 단위 테스트가 가능한 부분을 발라내어 활용한다. 

### 1.2. 기능 요구사항 정리

|구분 | 상세 |구현방법     |
|:----:  |:------  |:---------|
|삭제 형식 변경|• 질문 데이터를 완전히 삭제하는 것이 아니라 데이터의 상태를 삭제 상태(deleted - boolean type)로 변경한다.|• Hard Delete -> Soft Delete|
|삭제 조건|• 로그인 사용자와 질문한 사람이 같은 경우 삭제할 수 있다.|• 삭제 유효성 검사<br>&nbsp;&nbsp;• Question - 사용자 동일 유효성 검사<br>&nbsp;&nbsp;• Answer - 사용자 동일 유효성 검사|
|삭제 조건|• 답변이 없는 경우 삭제가 가능하다.|• 삭제 유효성 검사<br>&nbsp;&nbsp;• Question - Answer 없음 유효성 검사|
|삭제 조건|• 질문자와 답변 글의 모든 답변자 같은 경우 삭제가 가능하다.|• 삭제 유효성 검사<br>&nbsp;&nbsp;• Question - 사용자 동일 유효성 검사, 연관 Answer 사용자 동일 유효성 검사<br>&nbsp;&nbsp;• Answer - 사용자 동일 유효성 검사|
|삭제 조건|• 질문자와 답변자가 다른 경우 답변을 삭제할 수 없다.|• 질문과 연관된 답변 중 하나라도 작성자가 일치하지 않는 경우 질문과 답변을 모두 삭제하지 않는다.|
|삭제 결과|• 질문을 삭제할 때 답변 또한 삭제해야 하며, 답변의 삭제 또한 삭제 상태(deleted)를 변경한다.|• CASCADE 적용(validation 통과에 한하여)<br>• **_2.1.3. 영속성 전이 : CASCADE_** 참조|
|삭제 로깅|• 질문과 답변 삭제 이력에 대한 정보를 DeleteHistory를 활용해 남긴다.|• 삭제가 성공적으로 완료된 경우 로깅 처리|

### 1.3. 프로그래밍 요구사항

|구분|상세|구현 방법|
|:---:|:---|---|
|Convention|• 자바 코드 컨벤션을 지키면서 프로그래밍한다.<br>&nbsp;&nbsp;• [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html) <br>&nbsp;&nbsp;• 단, 들여쓰기는 '2 spaces'가 아닌 '4 spaces'로 한다.|- gradle-editorconfig 적용<br>- gradle-checkstyle 적용<br>- IntelliJ 적용<br>- Github 적용|
|enum|• java enum을 적용해 프로그래밍을 구현한다.|• Code값들은 enum을 활용하여 구현한다.|
|객체지향 생활 체조|• 규칙 3: 모든 원시값과 문자열을 포장한다.|• 원시값 또는 문자열을 객체(VO)로 포장한다.|
|객체지향 생활 체조|• 규칙 5: 줄여쓰지 않는다(축약 금지).|• 네이밍에 약어를 사용하지 않는다.|
|객체지향 생활 체조|• 규칙 8: 일급 콜렉션을 쓴다.<br>• 배열 대신 컬렉션을 사용한다.|• 값 객체(VO) 또는 엔티티(Entity)의 Collection을 일급 콜렉션으로 구현한다.|
|예외 처리|• 예외 처리를 통해 에러가 발생하지 않도록 한다.|• **_2.1.2. Exception Control_** 참조|
|메소드|• indent(인덴트, 들여쓰기) depth를 2가 넘지 않도록 구현한다. 1까지만 허용한다.<br>&nbsp;&nbsp;• 예를 들어 while문 안에 if문이 있으면 들여쓰기는 2이다.<br>&nbsp;&nbsp;• **힌트: indent(인덴트, 들여쓰기) depth를 줄이는 좋은 방법은 함수(또는 메소드)를 분리**하면 된다.|- < 2depth 규칙|
|메소드|• 함수(또는 메소드)의 길이가 10라인을 넘어가지 않도록 구현한다.<br>&nbsp;&nbsp;• 함수(또는 메소드)가 한 가지 일만 잘 하도록 구현한다.|- 함수(또는 메서드) 10라인 이하|
|메소드|• 3항 연산자를 쓰지 않는다.|- 3항 연산자 금지|
|메소드|• else 예약어를 쓰지 않는다.<br>&nbsp;&nbsp;• 힌트: if 조건절에서 값을 return하는 방식으로 구현하면 else를 사용하지 않아도 된다.<br>&nbsp;&nbsp;• else를 쓰지 말라고 하니 switch/case로 구현하는 경우가 있는데 switch/case도 허용하지 않는다.|- else 예약어 금지|
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

#### 2.1.0. 클린 코드 (강현구 리뷰어님 조언 코멘트)

> 클린코드 25p
> 
> 의미 있게 구분하라
> 컴파일러나 인터프리터만 통과하려는 생각으로 코드를 구현하는 프로그래머는 스스로 문제를 일으킨다.
> 예를 들어, 동일한 범위 안에서 다른 두 개념에 같은 이름을 사용하지 못한다.
> ...
> 
> 컴파일러를 통과할지라도 연속된 숫자를 덧붙이거나 불용어(noise word)를 추가하는 방식은 적절하지 못하다.
> 이름이 달라야 한다면 의미도 달라져야 한다.
> 
> 연속적인 숫자를 덧붙인 이름(a1, a2 ... aN)은 의도적인 이름과 정반대다.
> 이런 이름은 그릇된 정보를 제공하는 이름도 아니며, 아무런 정보를 제공하지 못하는 이름일 뿐이다.
> 저자 의도가 전혀 드러나지 않는다.

> 클린코드 157p
> 
> 테스트 코드는 실제 코드 못지 않게 중요하다. 테스트 코드는 이류 시민이 아니다.
> 테스트 코드는 사고와 설계와 주의가 필요하다.
> 실제 코드 못지 않게 깨끗하게 짜야한다.
> 
> 테스트는 유연성, 유지보수성, 재사용성을 제공한다. 테스트 케이스가 있으면 변경이 쉬워지기 때문이다.
> 따라서 테스트 코드가 지저분하면 코드를 변경하는 능력이 떨어지며 코드 구조를 개선하는 능력도 떨어진다.
> 테스트 코드가 지저분할수록 실제 코드도 지저분해진다.
> 결국 테스트 코드를 잃어버리고 실제 코드도 망가진다.

#### 2.1.1. TDD (Test-Driven Developmet) : production code보다 test code를 먼저 작성한다.

> Cycle : Test Fail ---> Test Passes ---> Refactor ---> Test Fail ---> ...

#### 2.1.2. Exception Control

##### 2.1.2.1. Checked-Unchecked Exception

> 출처 : [Checked Exception을 대하는 자세](https://cheese10yun.github.io/checked-exception/) 참조

|구분   |Checked Exception|Unchecked Exception|
|:---: |:---             |:---               |
|처리 여부|반드시 예외 처리 해야함|예외 처리 하지 않아도 됨 |
|Transaction<br>Rollback 여부|Rollback 안됨|Rollback 진행|
|대표 Exception|IOException,<br>SQLException|NullPointerException,<br>IllegalArgumentException|

**결론**

- 예외 복구 전략이 명확하고 그것이 가능하다면 `Checked Exception`을 `try`, `catch`로 잡고 해당 복구를 하는 것이 좋습니다.
- 하지만 그러한 경우는 흔하지 않으며 `Checked Exception`이 발생하면 **더 구체적인 `Unchecked Exception`**을 발생시키고 예외에 대한 메시지를 명확하게 전달하는 것이 효과적입니다.
- **무책임하게 상위 메서드로 `throw`를 던지는 행위는 하지 않는 것**이 좋습니다. 상위 메서드들의 책임이 그만큼 증가하기 때문입니다.
- `Checked Exception`은 기본 트랜잭션의 속성에서는 `rollback`을 진행하지 않는 점도 알고 있어야 실수를 방지할 수 있습니다.

#### 2.1.3. 영속성 전이 : CASCADE

> 출처 : 자바 ORM 표준 JPA 프로그래밍 (저 : 김영한)

- 영속성 전이(Transitive Persistence)
    - 특정 엔티티를 영속 상태로 만들 때 연관된 엔티티도 함께 영속 상태로 만들고 싶을때 사용
    - JPA는 `CASCADE`옵션으로 영속성 전이를 제공
    - 부모 엔티티를 저장할 때 자식 엔티티도 함께 저장
- 사용 전
    - 엔티티를 저장할 때 연관된 모든 엔티티는 영속 상태여야 한다.
    - 부모 엔티티를 영속 상태로 만들고 자식 엔티티도 각각 영속 상태로 만든다.

```java
void 영속성_전이_미활용(EntityManager em) {
    //저녁식사
    Dinner dinner = new Dinner();
    em.persist(dinner);
    
    //요리1 객체
    Dish dish1 = new Dish();
    dish1.setDinner(dinner);
    dinner.getDishes().add(dish1);
    em.persist(dish1);
    
    //요리2 객체
    Dish dish2 = new Dish();
    dish2.setDinner(dinner);
    dinner.getDishes().add(dish2);
    em.persist(dish2);
}
```    

- 사용 후
    - 부모만 영속 상태로 만들면 연관된 자식까지 한 번에 영속 상태로 만들 수 있다.

```java
@Entity
public class Dinner {
	...
    @OneToMany(mappedBy = "dinner", cascade = CascadeType.PERSIST)
    private List<Dish> dishes = new ArrayList<>();
    ...
}
```

```java
void 영속성_전이_활용(EntityManager em) {
    Dish dish1 = new Dish();
    Dish dish2 = new Dish();

    Dinner dinner = new Dinner();
    dish1.setDinner(dinner);
    dish2.setDinner(dinner);
    dinner.getDishes().add(dish1);
    dinner.getDishes().add(dish2);
    
    em.persist(dish2);
}
```    

- `CASCADE` 종류
    - `CascadeType` 코드

```java
public enum CascadeType {
	ALL,        //모두 적용
	PERSIST,    //영속 
	MERGE,      //병합 
	REMOVE,     //삭제
	REFRESH,    //REFRESH
	DETACH,     //DETACH
}
```

> 다음처럼 여러 속성을 같이 활용 가능
> <br>`cascade = {CascadeType.PERSIST, CascadeType.REMOVE}`

> 참고로 `CascadeType.PERSIST`, `CascadeType.REMOVE`는 `em.persist()`, `em.remove()`를 실행할 때 바로 전이가 발생하지 않고 플러시를 호출할 때 전이가 발생한다.

#### 2.1.4. 래거시 코드 리팩토링하기

![image](../documents/step03/kisspng-spring-framework-web-application-modelviewco.png)
<br>출처 : https://www.cleanpng.com/png-spring-framework-web-application-modelviewco-2844098/

Q. Layered Architecture 기반 하에서 핵심 비즈니스 로직은 어디에 구현하는 것이 옮은가?<br>
A. Domain Model 영역에서 하는 것이 옳다.

> Mockito는 최대한 늦게 하는 것을 권장(포비님 말씀)

- 래거시 코드 리팩토링
    - 응용 애플리케이션(웹, 모바일 애플리케이션)을 개발할 때 TDD, OOP를 적용하려면 핵심 비즈니스 로직을 도메인 객체가 담당하도록 구현하는 것
    - 테스트하기 쉬운 부분과 테스트하기 어려운 부분을 분리해 테스트하기 쉬운 부분에 대한 단위테스트를 구현하고, 지속적인 리팩토링
- 단계별 진행 방법
    - 1단계
        - Service Layer 에 단위테스트를 추가한 후 비즈니스 로직을 도메인 객체로 이동하는 리팩토링
        - Acceptance Test를 추가한 후 리팩토링

#### 2.1.5. JPA 복합키 @GenerateValued

> 출처 : https://kihoonkim.github.io/2017/01/27/JPA(Java%20ORM)/3.%20JPA-%EC%97%94%ED%8B%B0%ED%8B%B0%20%EB%A7%A4%ED%95%91/

> 복합키 매핑
> - @IdClass 와 @EmbeddedId 를 통해 두가지 방식으로 매핑할 수 있다.
> - 두방식 모두 복합키를 필드로 가지고 있는 식별자 클래스를 만들어야 된다.
> - 두 방식중 무엇을 사용하든 복합키에는 @GeneratedValue를 통해 기본키를 자동 생성할 수 없다. 반드시 직접 할당 전략을 사용해야 된다.

### 2.2. Todo List

- [x] 0.기본 세팅
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
        - [x] 0-4-2.step2 branch 삭제 : `git branch -D step2`
        - [x] 0-4-3.step2 branch 삭제 확인 : `git branch -a`
        - [x] 0-4-4.원본(next-step) git repository를 remote로 연결 (미션 당 1회) : `git remote add -t gregolee upstream https://github.com/next-step/jwp-qna`
        - [x] 0-4-5.원본(next-step) git repository를 remote로 연결 확인 : `git remote -v`
        - [x] 0-4-6.원본(next-step) git repository에서 merge된 나의 branch(gregolee)를 fetch : `git fetch upstream gregolee`
        - [x] 0-4-7.remote에서 가져온 나의 branch로 rebase : `git rebase upstream/gregolee`
        - [x] 0-4-7.gregolee -> step3로 체크아웃 : `git checkout -b step3`
    - [x] 0-5.리뷰어님의 리뷰를 반영한 코드로 수정
        - [x] 0-5-1.테스트 메서드 실행 순서가 서로 의존하게 작성하지 않기
            - [x] 0-5-1-1.`AnswerTest`
            - [x] 0-5-1-2.`AnswerRepositoryTest`
            - [x] 0-5-1-3.`DeleteHistoryTest`
            - [x] 0-5-1-4.`DeleteHistoryRepositoryTest`
            - [x] 0-5-1-5.`QuestionTest`
            - [x] 0-5-1-6.`QuestionRepositoryTest`
            - [x] 0-5-1-7.`UserTest`
            - [x] 0-5-1-8.`UserRepositoryTest`
        - [x] 0-5-2.클린코드 - 의미있게 구분하라. 그리고 테스트 코드는 이류 시민이 아니다.
            - [x] 0-5-2-1.`AnswerTest`
            - [x] 0-5-2-2.`AnswerRepositoryTest`
            - [x] 0-5-2-3.`DeleteHistoryTest`
            - [x] 0-5-2-4.`DeleteHistoryRepositoryTest`
            - [x] 0-5-2-5.`QuestionTest`
            - [x] 0-5-2-6.`QuestionRepositoryTest`
            - [x] 0-5-2-7.`UserTest`
            - [x] 0-5-2-8.`UserRepositoryTest`
- [x] 1.자바 코드 컨벤션을 위한 세팅
    - [x] 1-1.[gradle-editorconfig](https://naver.github.io/hackday-conventions-java/#editorconfig) 적용
    - [x] 1-2.[Google Java Style Guide](https://google.github.io/styleguide/javaguide.html) 적용
    - [x] 1-3.[Github](https://naver.github.io/hackday-conventions-java/#_github) 적용
- [x] 2.학습
    - [x] 2-1. "래거시 코드 리팩토링 미션 소개" 영상 시청 [링크](https://edu.nextstep.camp/s/ffeVDScX/ls/x4dF36as)
- [x] 3.분석 및 설계
    - [x] 3-1.step03.md 초안 작성
    - [x] 3-2.ATDD 초안 작성
- [x] 4.구현
    - [x] 4-1.TDD
        - [x] 4-1-1.일급컬렉션 적용
            - [x] 4-1-1-1.`AnswerGroup`
            - [x] 4-1-1-2.`DeleteHistoryGroup`
        - [x] 4-1-2.CASCADE 적용
            - [x] 4-1-2-1.`AnswerGroup` : `Answer`-`Question` 양방향 연관 관계 매핑
            - [x] 4-1-2-2.`AnswerGroup` : CASCADE 적용
        - [x] 4-1-3.validation
            - [x] 4-1-3-1.`Question` : 삭제 유효성 검사
                - [x] 4-1-3-1-1.`Question` : 사용자 동일 유효성 검사
                - [x] 4-1-3-1-2.`Question` : 전체 답변 사용자 동일 유효성 검사
            - [x] 4-1-3-2.`Answer` : 삭제 유효성 검사
                - [x] 4-1-3-2-1.`Answer` : 사용자 동일 유효성 검사
            - [x] 4-1-3-3.`AnswerGroup` : 삭제 유효성 검사
                - [x] 4-1-3-3-1.`AnswerGroup` : 사용자 동일 유효성 검사
                - [x] 4-1-3-3-2.`AnswerGroup` : 전체 답변 사용자 동일 유효성 검사
        - [x] 4-1-4.`repository.delete()` -> soft delete로 변경
            - [x] 4-1-4-1.`Question` : 질문 삭제
            - [x] 4-1-4-2.`Answer` : 답변 삭제
            - [x] 4-1-4-3.`AnswerGroup` : 답변 그룹 삭제
        - [x] 4-1-5.`DeleteHistory` : 삭제 완료 후 로깅 처리
    - [x] 4-2.추가 리팩터링
        - [x] 4-2-1.불필요한 메서드 제거
    - [x] 4-3.리뷰어님 코멘트 반영
        - [x] 4-3-1.불필요한 원시값 포장 해제하기 : `Deleted`
        - [x] 4-3-2.2중 적용된 소스 코드 수정 : `Answer.changeQuestion()`
        - [x] 4-3-3.Checked Exception -> Unchecked Exception 적용
            - [x] 4-3-3-1.`CannotDeleteException`
            - [x] 4-3-3-2.`Answer.validateIsOwner()`
            - [x] 4-3-3-3.`Question.validateCouldDelete()`
        - [x] 4-3-4.생성자 -> 정적 팩토리 메서드
            - [x] 4-3-4-1.`AnswerGroup.generateDeleteHIstoryAllOfAnswers()` 내 `new DeleteHistory`
        - [x] 4-3-5.기능수정
            - [x] 4-3-5-1.`Answer.delete()` : 상태 값 변경 -> 상태 값 변경 + 삭제한 이력을 리턴
        - [x] 4-3-6.래핑클래스 -> 원시값으로 변경
            - [x] 4-3-6-1.`Question.deleted`
        - [x] 4-3-7.필드의 기본값 설정과 초기화를 혼동하지 말자.
            - [x] 4-3-7-1.`Question.answers` : AnswerGroup.generate() -> 제거
        - [x] 4-3-8.접근제어자 제대로 사용하기
            - [x] 4-3-8-1.`Question.validateCouldDelete()`
            - [x] 4-3-8-2.`Answer.validateIsOwner()`
            - [x] 4-3-8-3.그 외에도 찾아보기
        - [x] 4-3-9.Value Object Test 코드 작성
    - [x] 4-4.리뷰어님 코멘트 반영 2차
        - [x] 4-4-1.답변 삭제 권한 체크는 AnswerGroup 내부 또는 Answer에서 하도록 변경
            - [x] 4-4-1-1.`AnswerGroup.deleteAll()` -> `public DeleteHistoryGroup deleteAll(User loginUser)`
        - [x] 4-4-2.불필요한 상수 적용 제거
            - [x] 4-4-2-1."DELETED", "NOT_DELETED" -> `true`, `false`
        - [x] 4-4-3.테스트 코드 리펙토링
            - [x] 4-4-3-1.다양한 테스트 메서드 작성
                - [x] 4-4-3-1-1.정상 케이스 외에 의도한 예외마다 발생하는 테스트 코드 작성
                - [x] 4-4-3-1-2.정상 케이스임에도 다른 의도가 담긴 테스트 메서드로 분리
                - [x] 4-4-3-1-3.정상 수행임에도 여러가지 결과를 도출한다면 각 결과에 맞춰서 테스트 코드 작성
                - [x] 4-4-3-1-4.if절은 활용하지 않을 것
            - [x] 4-4-3-2.네이밍
                - [x] 4-4-3-2-1.타입은 명시하지 않도록 한다.
                - [x] 4-4-3-2-2.변수의 의미만 부여한다.
                - [x] 4-4-3-2-3.한글 변수명을 사용해보자.
            - [x] 4-4-3-3.`DeleteHistoryTest` : 팩터리 메서드가 의도한 컨텐츠 타입으로 생성되는지 테스트
- [x] 5.테스트
    - [x] 5-1.Gradle build Success 확인
    - [x] 5-2.Google Java Style 적용 (indent : 2 -> 4 spaces)
    - [x] 5-3.요구사항 조건들 충족했는지 확인
        - [x] 5-3-1.핵심 단위 로직 테스트
        - [x] 5-3-2.< 2 Depth 규칙
        - [x] 5-3-3.함수(또는 메서드) 10라인 이하
        - [x] 5-3-4.else 예약어 금지
        - [x] 5-3-5.Code값들은 `enum`을 활용한다.
        - [x] 5-3-6.모든 원시값과 문자열을 포장한다.
        - [x] 5-3-7.일급콜렉션 활용
        - [x] 5-3-8.축약 금지
        - [x] 5-3-9.예외 처리
        - [x] 5-3-10.3항 연산자 금지
- [ ] 6.인수인계
    - [ ] 6-1.소감 및 피드백 정리
        - [ ] 6-1-1.느낀점 & 배운점 작성
        - [ ] 6-1-2.피드백 요청 정리
    - [ ] 6-2.코드리뷰 요청 및 피드백
        - [ ] 6-1-1.step3를 gregolee/jwp-qna로 push : `git push origin step3`
        - [ ] 6-1-2.pull request(PR) 작성
    - [ ] 6-3.Slack을 통해 merge가 되는지 확인한 후에 미션 종료

### 2.3. 인수 조건 (ATDD)

- Feature: 질문 삭제 기능 변경(Hard Delete -> Soft Delete)
    - Scenario: 사용자와 작성자가 동일하지 않은 질문을 삭제 시도한다.
        - Given
            - 사용자A, 사용자B 2명의 사용자가 등록되어 있다.
            - 사용자B가 작성자로 된 질문이 등록되어 있다.
        - When
            - 사용자A가 사용자B가 등록한 질문을 삭제 시도한다.
        - Then
            - 질문 삭제에 실패한다.
    - Scenario: 사용자와 작성자가 동일한 질문을 삭제 시도한다. 질문의 답변은 존재하지 않는다.
        - Given
            - 사용자A가 등록되어 있다.
            - 사용자A가 작성자로 된 질문이 등록되어 있다.
            - 해당 질문의 답변은 등록되어 있지 않다.
        - When
            - 사용자A가 사용자A가 등록한 질문글을 삭제 시도한다.
        - Then
            - 질문 삭제에 성공한다.
            - 삭제된 질문의 히스토리가 저장된다.
    - Scenario: 사용자와 작성자가 동일한 질문을 삭제 시도한다. 질문의 답변은 존재하며 본인의 답변이다.
        - Given
            - 사용자A가 등록되어 있다.
            - 사용자A가 작성자로 된 질문이 등록되어 있다.
            - 해당 질문의 답변은 사용자A가 작성자로 등록된 답변이 있다.
        - When
            - 사용자A가 사용자A가 등록한 질문글을 삭제 시도한다.
        - Then
            - 질문 삭제 및 질문과 연관된 답변 삭제에 성공한다.
            - 삭제된 질문과 연관된 답변이 히스토리에 저장된다.
    - Scenario: 사용자와 작성자가 동일한 질문을 삭제 시도한다. 질문의 답변은 존재하며 타인이 작성한 답변도 있다.
        - Given
            - 사용자A, 사용자B 2명의 사용자가 등록되어 있다.
            - 사용자A가 작성자로 된 질문이 등록되어 있다.
            - 해당 질문의 답변 중 사용자B가 작성자로 등록된 답변이 있다.
        - When
            - 사용자A가 사용자A가 등록한 질문글을 삭제 시도한다.
        - Then
            - 질문 삭제 및 질문과 연관된 답변 삭제에 실패한다.


## 3. 인수인계

### 3.1. 느낀점 & 배운점

#### 3.1.1. 느낀점

- 배우는 것이 많을수록 기초를 많이 잊어버리기 쉬워졌습니다.
  - 원시값을 포장, 일급 콜렉션 활용이 정말 좋은 방법이긴 하나 너무도 쉽게 원시값을 사용해버리는 저를 보면서 많이 반성하게 되었습니다.
- JPA를 활용하는 것은 편리한 것이지만, 잘 활용하기 위해서는 정확하게 알아야 한다.
  - JPA를 활용하면서 느낀점은 '객제지향을 위한 좋은 도구'이지만, 조금이라도 '데이터 지향 코드'로 작성하는 순간 객체지향에서 벗어나는 것을 알게 되었습니다.
  - 일례로 `@Embedded`, `@Embeddable`을 몰라 원시값을 사용했다면 객체 지향적인 코드를 작성하지 못했을 겁니다.
- 테스트의 범위를 구분하여 매우 작은 단위 -> 작은 단위 -> 핵심 로직 -> 서비스 -> 인수 -> ... 구분해야 함을 알았습니다.
  - 다만 그 범위를 직관적으로 정하는 것은 참 어려운 일이었습니다.

#### 3.1.2. 배운점

- 복합키를 활용하면 @id의 값이 자동생성(auto increment)이 되지 않는다는 것을 알았습니다.
- JPA를 통하여 한층 더 객체 지향적인 코드를 활용할 수 있다는 점을 알았습니다.

### 3.2. 피드백 요청

- 원시값 포장 내 validation 작성과 @Column 내 조건
  - 참조한 기록들
    - https://jeong-pro.tistory.com/233
    - https://pythonq.com/so/java/76163  
  - @Column 내 작성한 조건들은 validation을 하는 것이 아닌 DDL의 조건을 작성을 위한 것임을 알았습니다.     
  - 원시값을 포장하면서 validation을 검증하는 로직을 하는 것이 옳은 것인지 좋은 습관인지 여쭤봅니다.
  - spring-boot-starter-validation, hibernate-validator 등 을 활용한 어노테이션(@Size, @NotNull)을 활용하는 것이 나은 것인지 여쭤봅니다.
    (validation 코드 작성 간 빠른 코드를 작성할 수 있습니다만, 제가 알지 못하는 사이드 이펙트가 있을 것 같습니다..)

> `@Column`은 `persistence layer`에 포함됩니다. db와 관련된 조건을 작성하시면 됩니다.
> `@Size`, `@NotNull`은 어느 구현체를 쓰느냐에 따라 달라지겠지만 `spring validation`을 쓴다면 `presentation layer`와 `application layer` 사이에서 데이터를 전달해주는 `dto`에 사용합니다. 주로 http request 요청값을 검증할 때 씁니다.
> 원시값을 포장한 객체는 `domain layer`입니다.
> `domain layer`는 순수 자바로 동작 가능해야 합니다.
> 외부 라이브러리에 의존한 유효성 검증 대신 생성자에서 의도한 인자를 전달 받고 있는지 검사하도록 구현하시는 걸 추천합니다.

- 객체지향 패러다임

> Q. public 으로 유효성 검증 가능하도록 열어두는 것은 옳지 않은가요?
> A. 필요하다면 public으로 둬야겠죠. 하지만 지금 구현하신 내용을 보면 delete 메서드에서만 필요한 상태죠. 그리고 삭제할 때 본인만 삭제 가능하도록 강제해야 의도한 기능이 맞지 않을까요? 삭제 정책이 본인만 가능하다면 기능도 제한을 줘야하는데 지금처럼 해당 기능 밖에서 처리하도록 만들면 유효성 검증을 놓쳐서 다른 사람이 삭제할 가능성이 생기겠죠
> A. 객체 내부에서 관리하는 인스턴스 변수에 대한 처리는 클래스 안에서만 처리하도록 강제해야 캡슐화를 달성할 수 있습니다 


