## 요구 사항
- [x] User Entity 매핑
- [x] Question Entity 매핑
- [x] DeleteHistory Entity 매핑
- [x] Answer Entity 매핑
- [x] DeleteHistoryService의 Transactional Propagation = REQUIRES_NEW를 제거
- QnAService
  - DeleteQuestion 리팩토링
    - [x] 일급 컬렉션: List<Answer>를 Answers로 변환
    - [x] 일급 컬렉션: List<DeleteHistory>를 deleteHistories로 변환
    - [x] Answers.setDeleted 추가

## 요구 사항 2단계
- [x] Step1의 Answers.haveNotOwner 메서드 2depth로 구성되 있는 것을 2depth 미만으로 수정
- [x] Answer, Question, User Entity의 createdAt, updatedAt을 JPA Auditing을 이용하여 중복 줄이기
- [x] Question 연관 관계 매핑
- [x] DeleteHistory 연관 관계 매핑
- [x] Answer 연관 관계 매핑
- [x] DeleteHistory에 audit 적용

## 요구 사항 3단계
- [ ] 전체적으로 equals, hashcode 수정
- [ ] AuditEntityTest 테스트 메소드명 수정
- QnAService deleteQuestion 리팩토링
  - [ ] Question.delete 추가
  - [ ] Answer.delete 추가
  - [ ] Answers.delete 추가