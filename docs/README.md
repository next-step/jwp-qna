# JPA

## 1단계 - 엔티티 매핑
### 구현 필요기능
- [x] 도메인 Entity 클래스 작성
- [x] JPA Auditing 적용
- [x] Entity 객체 테스트 작성
- [x] Repository 객체 테스트 작성

## 2단계 - 연관 관계 매핑
### 구현 필요기능
- [x] Answer Entity 와 User, Question 간의 연관관계 정의
- [x] Question Entity 와 User, Answer 간의 연관관계 정의
- [x] DeleteHistory Entity 와 User 간의 연관관계 정의
- [x] User Entity 의 equals 및 hash 구현
- [x] 연관관계 관련 테스트 추가 및 수정

## 3단계 - 질문 삭제하기 리팩터링
### 구현 필요기능
- [ ] 질문 데이터를 완전히 삭제하지 않고 데이터의 상태를 삭제 상태(deleted - boolean type)로 변경.
- [ ] 로그인 사용자와 질문한 사람이 같은 경우 삭제할 수 있다.
- [ ] 답변이 없는 경우 삭제가 가능하다.
- [ ] 질문자와 답변 글의 모든 답변자 같은 경우 삭제가 가능하다.
- [ ] 질문을 삭제할 때 답변 또한 삭제해야 하며, 답변의 삭제 또한 삭제 상태(deleted)를 변경한다.
- [ ] 질문자와 답변자가 다른 경우 답변을 삭제할 수 없다.
- [ ] 질문과 답변 삭제 이력에 대한 정보를 DeleteHistory를 활용해 남긴다.
