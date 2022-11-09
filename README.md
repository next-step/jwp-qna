# JPA
> 객체지향프로그래밍과 관계형 데이터베이스는 근본적으로 괴리가 존재한다.  
> JPA는 객체지향을 좀 더 객체지향답게 사용하기 위한 기술로서, 데이터베이스에 의존적이지 않은 객체설계가 가능하게 만들어 준다.

## 1단계 - 엔티티 매핑
- QnA 서비스를 만들어가면서 JPA로 실제 도메인 모델을 어떻게 구성하고 객체와 테이블을 어떻게 매핑해야 하는지 알아본다.
- `DDL`을 보고 유추하여 엔티티 클래스와 리포지토리 클래스를 작성해 본다.
- `@DataJpaTest`를 사용하여 `학습 테스트`를 해 본다.

### Entity
- User
  - [x] User 엔티티 매핑
  - [x] UserRepository 저장 및 find
- Question
  - [x] Question 엔티티 매핑
  - [x] QuestionRepository 저장 및 find
- Answer
  - [x] Answer 엔티티 매핑
  - [x] AnswerRepository 저장 및 find
- DeleteHistory
  - [x] DeleteHistory 엔티티 매핑

### 1단계 피드백
- [ ] @Embeddable 타입을 @MappedSuperclass로 정의
