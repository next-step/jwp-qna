package qna.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AnswerTest {

    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Autowired
    private AnswerRepository answerRepository;

    @BeforeEach
    void setUp() {
        answerRepository.save(A1);
        answerRepository.save(A2);
    }

    @AfterEach
    void clean() {
        answerRepository.deleteAll();
    }

    @Test
    void findById() {
        // given
        Answer answer = answerRepository.findAll().get(0);

        // when
        Answer result = answerRepository.findById(answer.getId()).get();

        // then
        assertThat(result).isEqualTo(answer);
    }

    @Test
    void update() {
        // given
        Answer answer = answerRepository.findAll().get(0);
        String newContents = "Update Contents";

        // when
        answer.setContents(newContents);

        // then
        List<Answer> result = answerRepository.findByContents(newContents);
        assertThat(result).containsExactly(answer);
    }

    @Test
    void remove() {
        // given
        List<Answer> prevResult = answerRepository.findAll();
        assertThat(prevResult.size()).isGreaterThan(0);

        // when
        answerRepository.deleteAll();

        // then
        List<Answer> result = answerRepository.findAll();
        assertThat(result).isEmpty();
    }
}
