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
- [x] Question 연관 관계 매핑
- [x] DeleteHistory 연관 관계 매핑
- [x] Answer 연관 관계 매핑
- [x] DeleteHistory에 audit 적용