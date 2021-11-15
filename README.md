# JWP-QNA
## 구현할 기능 목록
* Answer Entity 생성
  * 요구 ddl에 맞춰서 클래스 수정
  * @DataJpaTest를 사용하여 코드 작성
  * Question Entity와 연관 관계 매핑
  * User Entity와 연관 관계 매핑
* 요구사항에 맞춰서 DeleteHistory Entity 생성
  * 요구 ddl에 맞춰서 클래스 수정
  * @DataJpaTest를 사용하여 코드 작성
  * User Entity와 연관 관계 매핑
* 요구사항에 맞춰서 Question Entity 생성
  * 요구 ddl에 맞춰서 클래스 수정
  * @DataJpaTest를 사용하여 코드 작성
  * User Entity와 연관 관계 매핑
* 요구사항에 맞춰서 User Entity 생성
  * 요구 ddl에 맞춰서 클래스 수정
  * @DataJpaTest를 사용하여 코드 작성
* Auditing을 이용하여 공통 분리
* 질문 삭제하기 기능 리팩토링
  * 질문 삭제 기능 로직 단위 테스트 구현
  * 질문 삭제 기능 로직을 서비스 레이어에서 분리 후 도메인 모델로 이동
  * 기존 서비스 레이어 테스트 또한 리팩토링 이후에도 통과해야 한다.
