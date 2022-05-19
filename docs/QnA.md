# 요구사항 정리
  - 주어진 DDL에 맞게 엔티티 클래스 작성
  - 주어진 DDL에 맞게 리포지토리 클래스 작성
  - DataJpaTest로 테스트를 작성한다.

# 1단계 리뷰 반영
  - [X] BaseEntity에서 id제외하기
  - [X] Exception 패키지 생성 
  - [X] Test 클래스에 @DirtiesContext 제거
  - [X] 테스트 실행시 @EnableJpaAuditing 적용되도록 변경
  - 