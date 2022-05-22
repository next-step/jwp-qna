
### JPA 기능 구현 목록

- QNA 서비스를 만들어가기 위해 제시된 Model 을 Entity 로 만들어라
- Entity 의 공통조건인 시간값은 BaseEntity 를 이용하여 관리하라
- Entity 에서 setter 와 public 기본 생성자는 지양하라
- Answer, Question, DeleteHistory, User 에 대한 @DataJpaTest 를 작성하라


### JPA 연관 관계 매핑

- Long Type 으로 맺어져있는 연관관계를 객체지향적으로 작성하기 위해 객체로 변경하라
- 객체에 변경됨에 따른 테스트 코드를 작성해서 검증하라