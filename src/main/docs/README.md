
# JPA

## 1단계 - 엔티티 매핑
### 구현 필요기능
- [x] JPA Auditing 적용
- [x] Answer Entity, Repository 객체 테스트 작성
- [x] DeleteHistory Entity, Repository 객체 테스트 작성
- [x] User Entity, Repository 객체 테스트 작성
- [x] Question Entity, Repository 객체 테스트 작성
 
## 2단계 - 연관관계 매핑
### 구현 필요 기능
- Answer
  - [x] Answer : Question = N:1 양방향 객체 관계로 변경
  - [x] Answer : User = N:1 단방향 관계로 변경
- Question
  - [x] Question : User = N:1 단방향 관계로 변경
- DeleteHistory
  - [x] DeleteHistory : User = N:1 단방향 관계로 변경

## 3단계 - 질문 삭제하기 리팩터링
### 구현 필요 기능
- Question
  - [] 질문 데이터 삭제시 데이터의 상태를 삭제 상태로 변경
  - 질문을 삭제할 수 있는 조건
    - [] 로그인 사용자와 질문한 사람이 같은 경우 삭제할 수 있다.
    - [] 답변이 없는 경우 삭제가 가능하다.
    - [] 질문자와 답변 글의 모든 답변자 같은 경우 삭제가 가능하다.
- Answer
  - [] 질문을 삭제할 때 답변 또한 삭제해야 하며, 답변의 삭제 또한 삭제 상태(deleted)를 변경한다.
  - [] 질문자와 답변자가 다른 경우 답변을 삭제할 수 없다.
- DeleteHistory
  - [] 질문과 답변 삭제 이력에 대한 정보를 DeleteHistory를 활용해 남긴다. 