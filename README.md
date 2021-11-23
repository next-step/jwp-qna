## 1단계 - 엔티티 매핑
#### 기능 요구사항
**공통사항: `@DataJpaTest` 사용하여 학습테스트**
- [X] 작성된 코드 동작하도록 변경
- [X] answer 엔티티 클래스와 리포지토리 클래스를 작성
- [X] delete_history 엔티티 클래스와 리포지토리 클래스를 작성
- [X] question 엔티티 클래스와 리포지토리 클래스를 작성
- [X] user 엔티티 클래스와 리포지토리 클래스를 작성
- [X] 생성시간, 수정시간 공통부분 작성

#### 코드리뷰 사항
- [X] 줄바꿈을 통해 가독성 향상
- [X] 생성일자 컬럼에 length 추가
- [X] 테스트 코드에 @DisplayName을 활용하여 설명 추가

## 2단계 - 연관 관계 매핑
#### 기능 요구사항
- [X] 답변 테이블에 질문 id로 외래키로 답변 - 질문(N:1) 연관관계 매핑
- [X] 답변 테이블에 작성자 id(유저 id) 외래키로 답변 - 유저(N:1) 연관관계 매핑
- [X] 삭제이력 테이블에 유저 id로 외래키로 삭제이력 - 유저(N:1) 연관관계 매핑
- [X] 질문 테이블에 작성자 id(유저 id) 외래키로 질문 - 유저(N:1) 연관관계 매핑
- [X] 질문 테이블에 질문-답변(1:N) 연관관계 매핑

#### 코드리뷰 사항
- [X] question Lob 타입 사용
- [X] Answer를 조회시 항상 Question, Writer를 같이 조회해야 하는 경우가 아니라면 지연로딩 속성 추
- [X] entity의 경우 equals보다는 id로 같은지 비교
- [X] 메시지와 함께 예외상황 표시

## 3단계 - 질문 삭제하기 리팩터링
#### 기능 요구사항
- [X] 질문 데이터를 완전히 삭제하는 것이 아니라 데이터의 상태를 삭제 상태(deleted - boolean type)로 변경한다.
    - [X] 데이터의 상태를 삭제 상태로 변경
- [X] 로그인 사용자와 질문한 사람이 같은 경우 삭제할 수 있다.
    - [X] 로그인 사용자와 질문한 사람이 같은지 확인하는 메서드를 도메인에 작성
- [X] 답변이 없는 경우 삭제가 가능하다.
    - [X] 답변이 없는 경우에만 삭제가능하도록 수정
- [X] 질문자와 답변 글의 모든 답변자 같은 경우 삭제가 가능하다.
    - [X] 질문자와 답변 글의 모든 답변자가 같은지 확인
    - [X] 질문자와 답변 글의 모든 답변자가 같은 경우에만 삭제 가능하도록 변경
- [X] 질문을 삭제할 때 답변 또한 삭제해야 하며, 답변의 삭제 또한 삭제 상태(deleted)를 변경한다.
    - [X] 답변의 삭제 또한 삭제 상태 변경
- [X] 질문자와 답변자가 다른 경우 답변을 삭제할 수 없다.
    - [X] 질문자와 답변자가 같은지 확인
    - [X] 질문자와 답변자가 다른 경우 답변 삭제 불가능하도록 변경
- [X] 질문과 답변 삭제 이력에 대한 정보를 DeleteHistory를 활용해 남긴다.
    - [X] 질문 삭제시 deleteHistory에 질문 삭제 이력에 대한 정보 추가
    - [X] 질문 삭제시 deleteHistory 질문에 대한 정보 생성
    - [X] 답 삭제시 deleteHistory에 질문 삭제 이력에 대한 정보 추가
    - [X] 답 삭제시 deleteHistory에 답에 대한 정보 생성
- [X] delete 관련 메서드 한 곳에 모으기
- [X] 테스트코드 단건 호출시 에러 수정
- [X] 삭제이력모음 일급컬렉션 추가
#### 코드 리뷰사항
- [X] 정적 팩토리 메서드를 추가하여 인자도 줄이고 가독성 증가
- [ ] 모든 원시 값과 문자열 포장
- [X] Answer 작성자에 대한 검증은 Answer 내에서 삭제시 수행
- [X] 검증이 없어 불필요한 테스트 제거

## 참고 링크
- [JPA @where 어노테이션](https://cheese10yun.github.io/jpa-where/)
- [요청과 응답으로 엔티티(Entity) 대신 DTO를 사용하자](https://tecoble.techcourse.co.kr/post/2020-08-31-dto-vs-entity/)
- [JPA 양방향 Entity 무한재귀 문제해결](https://thxwelchs.github.io/JPA%20%EC%96%91%EB%B0%A9%ED%96%A5%20Entity%20%EB%AC%B4%ED%95%9C%20%EC%9E%AC%EA%B7%80%20%EB%AC%B8%EC%A0%9C%20%ED%95%B4%EA%B2%B0/)
- [JPA cascade 종류](https://data-make.tistory.com/668)
- [JPA CascadeType.REMOVE vs orphanRemoval = true](https://tecoble.techcourse.co.kr/post/2021-08-15-jpa-cascadetype-remove-vs-orphanremoval-true/)
- [JPA 임베디드 타입](https://velog.io/@conatuseus/JPA-%EC%9E%84%EB%B2%A0%EB%94%94%EB%93%9C-%ED%83%80%EC%9E%85embedded-type-8ak3ygq8wo)
- [JPA @Embedded 사용시 주의사항](https://jojoldu.tistory.com/559)
- [우테코 JPA 참고](https://tecoble.techcourse.co.kr/tags/jpa/)


