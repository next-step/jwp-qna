# JWP-QNA
## step1 - 엔티티 매핑
- [x] Answer Entity 클래스 수정 및 테스트
    - [x] 요구 ddl에 맞춰서 클래스 수정
    - [x] @DataJpaTest를 사용하여 코드 작성
- [x] DeleteHistory Entity 클래스 수정 및 테스트
    - [x] 요구 ddl에 맞춰서 클래스 수정
    - [x] @DataJpaTest를 사용하여 코드 작성
- [x] Question Entity 클래스 수정 및 테스트
    - [x] 요구 ddl에 맞춰서 클래스 수정
    - [x] @DataJpaTest를 사용하여 코드 작성
- [x] User Entity 클래스 수정 및 테스트
    - [x] 요구 ddl에 맞춰서 클래스 수정
    - [x] @DataJpaTest를 사용하여 코드 작성
- [x] Auditing을 이용하여 공통 분리

## step2 - 연관 관계 매핑
객체의 참조와 테이블의 외래 키를 매핑해서 객체에서는 참조를 사용하고 테이블에서는 외래 키를 사용할 수 있도록 한다.

아래의 DDL을 보고 유추한다.
- H2

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

## step3 - 질문 삭제하기 리팩토링

### QnaService.java 리팩토링 목록
- [x] Question 작성자와 로그인 유저의 일치 여부 검증 => Question 도메인 객체로 이동
- [x] Answer 작성자와 로그인 유저의 일치 여부 검증 => Answer 도메인 객체로 이동
- [x] List<DeleteHistory> 일급 콜렉션 리펙토링
- [] List<Answer> 일급 콜렉션 리펙토링