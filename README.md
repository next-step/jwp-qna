## QnA

​	

## Step1 - 엔티티 매핑

### 요구사항

- [x] 엔티티 매핑 
  - [x] BaseEntity 생성
  - [x] Answer 엔티티 매핑
  - [x] DeleteHistory 엔티티 매핑
  - [x] Question 엔티티 매핑
  - [x] User 엔티티 매핑
- [x] Spring Data JPA Test
  - [x] AnswerRepository test
  - [x] Answer entity test
  - [x] DeleteHistoryRepository test
  - [x] DeleteHistory entity test
  - [x] QuestionRepository test
  - [x] Question entity test
  - [x] UserRepository test
  - [x] User entity test

## Step2 - 연관관계 매핑

- [x] Answer 엔티티 매핑
- [x] Delete_History 엔티티 매핑
- [x] Question 엔티티 매핑

## Step3 - 질문 삭제하기 리팩터링

- [ ] 일급 컬렉션으로 엔티티 개선

- [ ] QnaService 로직 도메인 으로 분리

  - [ ] Question delete구현

    - [x] question 작성자가 아니면 오류 발생
    - [ ] Question 내에서 answer delete요청 (일급컬렉션 구현)

  - [ ] Answer delete구현

    - [ ] Answer 작성자가 아니면 오류발생

      Answer가 없으면 Question 삭제됨

      Answer와 Question작성자가 같으면 Question까지 삭제됨

      Answer의 작성자가 Question과 다르면 오류발생으로 삭제되지않음

  - [ ] 각 Content삭제 후 DeleteHistory 생성
