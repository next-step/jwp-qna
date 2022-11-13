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

## Answers

- [x] 답변을 추가한다.
- [ ] 답변을 삭제한다.
    - [x] 질문자와 답변자가 다른 경우 답을 삭제할 수 없다.

## DeleteHistory

- [x] 삭제 내역을 생성한다.
- [x] delete_history 테이블의 엔티티 클래스를 작성한다.
- [x] delete_history 테이블의 레파지토리 클래스를 작성한다.
    - [x] delete_history 저장 테스트
    - [x] delete_history 조회 테스트

## DeleteHistories

- [x] 삭제 내역을 추가한다.
- [x] 삭제 내역 리스트를 추가한다.

## Question

- [x] 질문을 생성한다.
- [x] question 테이블의 엔티티 클래스를 작성한다.
- [x] question 테이블의 레파지토리 클래스를 작성한다.
    - [x] question 저장 테스트
    - [x] question 삭제한다.
        - [x] 삭제 시 삭제 내역을 반환한다.
    - [x] findByDeletedFalse 조회 테스트
    - [x] findByIdAndDeletedFalse 조회 테스트
- [x] 질문을 삭제한다.
    - [x] 로그인 사용자와 질문한 사람이 다르면 삭제할 수 없다.
    - [x] 질문을 삭제할 때 답변도 삭제한다.
    - [x] 질문과 답변 삭제 이력에 대한 정보를 DeleteHistory를 활용해 남긴다.

## User

- [x] 유저을 생성한다.
- [x] user 테이블의 엔티티 클래스를 작성한다.
- [x] user 테이블의 레파지토리 클래스를 작성한다.
    - [x] user 저장 테스트
    - [x] findByUserId 조회 테스트

- [ ] 인덴트
- [ ] 메서드 순서


