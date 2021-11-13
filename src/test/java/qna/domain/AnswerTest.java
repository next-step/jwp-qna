package qna.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Autowired
    private AnswerRepository answerRepository;


    @Test
    @DisplayName("Answer Entity Create 및 ID 생성 테스트")
    void save() {
        final Answer answer = answerRepository.save(A1);
        assertThat(answer.getId()).isNotNull();
    }

    @Test
    @DisplayName("Answer Entity Read 테스트")
    void findById() {
        final Answer saved = answerRepository.save(A1);
        final Answer found = answerRepository.findById(saved.getId()).orElseGet(()->null);
        assertThat(found).isEqualTo(saved);
    }

    @Test
    @DisplayName("Answer Entity Update 테스트")
    void update() {
        final Answer saved = answerRepository.save(A1);
        saved.setContents("updated!");
        final Answer found = answerRepository.findById(saved.getId()).orElseThrow(() -> new RuntimeException("테스트실패"));
        assertThat(found.getContents()).isEqualTo("updated!");
    }

    @Test
    @DisplayName("Answer Entity Delete 테스트")
    void delete() {
        final Answer saved = answerRepository.save(A1);
        answerRepository.delete(saved);
        answerRepository.flush();
        final Answer found = answerRepository.findById(saved.getId()).orElseGet(() -> null);
        assertThat(found).isNull();
    }
}
