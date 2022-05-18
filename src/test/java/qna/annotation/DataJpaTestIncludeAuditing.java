package qna.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Retention(RetentionPolicy.RUNTIME)
@DataJpaTest(includeFilters = {@ComponentScan.Filter(classes = {EnableJpaAuditing.class})})
public @interface DataJpaTestIncludeAuditing {
}
