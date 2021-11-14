package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Autowired
    private AnswerRepository answerRepository;

    @DisplayName("Create 및 ID 생성 테스트")
    @Test
    void save() {
        Answer save = answerRepository.save(A1);
        assertThat(save.getId()).isNotNull();
    }

    @DisplayName("Read 테스트")
    @Test
    void read() {
        Answer save = answerRepository.save(A1);
        Answer found = answerRepository.findById(save.getId()).orElse(null);
        assertThat(found).isEqualTo(save);
    }

    @DisplayName("Update 테스트")
    @Test
    void update() {
        Answer save = answerRepository.save(A1);
        save.setContents("update!!");
        Answer found = answerRepository.findById(save.getId()).orElseThrow(() -> new NullPointerException("테스트실패"));
        assertThat(found.getContents()).isEqualTo("update!!");
    }

    @DisplayName("Delete 테스트")
    @Test
    void delete() {
        Answer save = answerRepository.save(A1);
        answerRepository.delete(save);
        answerRepository.flush();
        Answer found = answerRepository.findById(save.getId()).orElse(null);
        assertThat(found).isNull();
    }

}
