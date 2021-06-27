## 요구사항
- DDL(Data Definition Language)을 보고 유추하여 엔티티 클래스와 리포지토리 클래스를 작성해 본다.
- @DataJpaTest를 사용하여 학습 테스트를 해 본다.

## 구현 리스트
- [x] Question의 참조, 외래키를 구현하고 테스트한다
- [x] DeleteHistory의 참조, 외래키를 구현하고 테스트한다
- [x] Answer의 참조, 외래키를 구현하고 테스트한다

## 이전 단계 피드백
- [x] ID 는 제어가 되지 않는 요소이니 ID를 주입하는 생성자는 전부 제거하는게 좋지 않을까요 ?
- [x] repository.deleteAll();
- [x] 사용하지 않는 getter는 지우고, setter 들은 도메인 언어로 표현하는게 좋을 것 같아요.  
- [x] @EnableJpaAuditing  
