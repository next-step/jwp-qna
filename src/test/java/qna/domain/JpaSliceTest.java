package qna.domain;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import qna.config.JpaConfig;

@DataJpaTest
@Import(JpaConfig.class)
public abstract class JpaSliceTest {
}
