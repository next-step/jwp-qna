# QnA
## 요구 사항
* QnA 서비스를 만들어가면서 JPA로 실제 도메인 모델을 어떻게 구성하고 객체와 테이블을 어떻게 매핑해야 하는지 알아본다.
* DDL을 보고 유추하여 엔티티 클래스와 레포지토리 클래스를 작성해 본다.
* @DataJpaTest를 사용하여 학습 테스트를 해 본다.

## 진행상황
- [ ] Answer Entity 구현
- [ ] DeleteHistory Entity 구현
- [ ] Question Entity 구현
- [ ] User Entity 구현
- [ ] Entity 구현 주어진 DDL스펙과 일치하는지 확인
- [ ] AnswerTest 구현 (저장 후 동일성 체크, 저장 후 찾기 테스트, 저장 후 수정 테스트, 저장 후 삭제 테스트)
- [x] QuestionTest 구현 (저장 후 동일성 체크, 저장 후 찾기 테스트, 저장 후 수정 테스트, 저장 후 삭제 테스트)
- [x] UserTest 구현 (저장 후 동일성 체크, 저장 후 찾기 테스트, 저장 후 수정 테스트)