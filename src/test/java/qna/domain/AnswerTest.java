package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class AnswerTest {
    @Autowired
    private AnswerRepository answerRepository;

    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Test
    public void save(){
        final Answer javajigi = answerRepository.save(A1);

        assertThat(javajigi.id()).isNotNull();
    }

    @Test
    public void findByIdAndDeletedFalse(){
        final Answer javajigi = answerRepository.save(A1);

        Optional<Answer> find = answerRepository.findByIdAndDeletedFalse(javajigi.id());

        assertThat(find).isNotEmpty();
    }
}
