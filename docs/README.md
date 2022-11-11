# 엔티티 매핑

## BaseEntity

- [x] BaseEntity 클래스를 작성한다.

## Answer

- [X] answer 테이블의 엔티티 클래스를 작성한다.
- [x] 답변을 생성한다.
    - [x] 작성자가 없을 수 없다.
    - [x] 질문이 없을 수 없다.
- [x] answer 테이블의 레파지토리 클래스를 작성한다.
    - [x] answer 저장 테스트
    - [x] findByQuestionIdAndDeletedFalse 조회 테스트
    - [x] findByIdAndDeletedFalse 조회 테스트
    - [x] answer 삭제 테스트

## DeleteHistory

- [x] 삭제 내역을 생성한다.
- [x] delete_history 테이블의 엔티티 클래스를 작성한다.
- [x] delete_history 테이블의 레파지토리 클래스를 작성한다.
    - [x] delete_history 저장 테스트
    - [x] delete_history 조회 테스트

## Question

- [x] 질문을 생성한다.
- [x] question 테이블의 엔티티 클래스를 작성한다.
- [x] question 테이블의 레파지토리 클래스를 작성한다.
    - [x] question 저장 테스트
    - [x] question 삭제 테스트
    - [x] findByDeletedFalse 조회 테스트
    - [x] findByIdAndDeletedFalse 조회 테스트

## User

- [x] 유저을 생성한다.
- [x] user 테이블의 엔티티 클래스를 작성한다.
- [x] user 테이블의 레파지토리 클래스를 작성한다.
    - [x] user 저장 테스트
    - [x] user 삭제 테스트
    - [x] findByUserId 조회 테스트

# 연관관계 매핑

- [ ] User : Question = 1 : n
- [ ] User : Answer = 1 : n
- [ ] User : DeleteHistory = 1 : 1
- [x] Question : Answer = 1 : n
