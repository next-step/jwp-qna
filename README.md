## 요구 사항
- [x] User Entity 매핑
- [x] Question Entity 매핑
- [x] DeleteHistory Entity 매핑
- [x] Answer Entity 매핑
- [x] DeleteHistoryService의 Transactional Propagation = REQUIRES_NEW를 제거
- QnAService 매핑
  - [ ] 일급 컬렉션: List<Answer>를 Answers로 변환
  - [ ] 일급 컬렉션: List<DeleteHistory>를 deleteHistories로 변환