# JPA (2주차)
## DDL
### answer
``` mysql
create table answer
(
    id          bigint not null auto_increment,
    contents    longtext,
    created_at  datetime(6) not null,
    deleted     bit    not null,
    question_id bigint,
    updated_at  datetime(6),
    writer_id   bigint,
    primary key (id)
) engine=InnoDB
```
### delete_history
``` mysql
create table delete_history
(
    id            bigint not null auto_increment,
    content_id    bigint,
    content_type  varchar(255),
    create_date   datetime(6),
    deleted_by_id bigint,
    primary key (id)
) engine=InnoDB
```
### question
``` mysql
create table question
(
    id         bigint       not null auto_increment,
    contents   longtext,
    created_at datetime(6)  not null,
    deleted    bit          not null,
    title      varchar(100) not null,
    updated_at datetime(6),
    writer_id  bigint,
    primary key (id)
) engine=InnoDB
```
### user
``` mysql
create table user
(
    id         bigint      not null auto_increment,
    created_at datetime(6) not null,
    email      varchar(50),
    name       varchar(20) not null,
    password   varchar(20) not null,
    updated_at datetime(6),
    user_id    varchar(20) not null,
    primary key (id)
) engine=InnoDB

alter table user
    add constraint UK_a3imlf41l37utmxiquukk8ajc unique (user_id)
```

## 요구사항
* QnA 서비스를 만들어가면서 JPA로 실제 도메인 모델을 어떻게 구성하고 객체와 테이블을 어떻게 매핑해야 하는지 알아본다.
* 아래의 DDL(Data Definition Language)을 보고 유추하여 엔티티 클래스와 리포지토리 클래스를 작성해 본다.
* @DataJpaTest를 사용하여 학습 테스트를 해 본다.

## 힌트
### 참고 URL
[Spring Boot Docs](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#features.testing.spring-boot-applications.autoconfigured-spring-data-jpa)
### 쿼리 로그
``` 
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.show-sql=true
```
### Mysql Dialect
```
spring.datasource.url=jdbc:h2:~/test;MODE=MySQL
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL57Dialect
```

## 기능목록
### 엔티티
- [X] answer
- [X] delete_history
- [X] question
- [X] user

### 엔티티 테스트
- [X] user
- [X] question
- [X] answer
- [X] delete_history

### 서비스 테스트
- [X] user
- [X] question
- [X] answer
- [ ] delete_history
