package qna;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import qna.domain.Answer;
import qna.domain.Question;
import qna.domain.User;

@EnableJpaAuditing
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
    }
}
