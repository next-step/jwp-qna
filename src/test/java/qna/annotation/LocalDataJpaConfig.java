package qna.annotation;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@ActiveProfiles("test")
@Retention(RetentionPolicy.RUNTIME)
@DataJpaTest
public @interface LocalDataJpaConfig {
}
