
## 1단계 요구사항
- DDL을 보고 유추하여 엔티티 클래스와 리포지토리 클래스 작성
  - 아래 로그 설정을 통해 과제 DDL과 동일한 동작 쿼리가 호출되는지 확인
    - spring.jpa.properties.hibernate.format_sql=true
    - spring.jpa.show-sql=true
  - [ ] answer entity, repository 클래스 작성
  - [ ] delete_history entity, repository 클래스 작성
  - [ ] question entity, repository 클래스 작성
  - [ ] user entity, repository 클래스 작성
    - [ ] user entity의 user_id 컬럼 UniqueConstraint 지정
- H2 데이터베이스 사용
  - 아래 설정 사용
    - spring.data.source.url=jdbc:h2:~/test;MODE=MySQL
    - spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
