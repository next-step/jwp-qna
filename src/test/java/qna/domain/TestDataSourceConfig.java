package qna.domain;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@TestConfiguration
public class TestDataSourceConfig {

    @PersistenceContext
    private EntityManager entityManager;
}
