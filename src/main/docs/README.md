
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
- User
  - [ ] User : Answer = 1:N 단방향 관계로 변경
  - [ ] User : Question = 1:N 단방향 관계로 변경
  - [ ] User : DeleteHistory = 1:N 단방향 관계로 변경
- Question
  - [x] Question : User = N:1 단방향 관계로 변경
- DeleteHistory
  - DeleteHistory : User = N:1 단방향 관계로 변경