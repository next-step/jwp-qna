package qna.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
@EnableJpaAuditing
@Configuration
public class JpaConfig {
}
