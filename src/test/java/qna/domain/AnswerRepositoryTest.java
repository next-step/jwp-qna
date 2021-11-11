package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class AnswerRepositoryTest {
    private AnswerRepository answerRepository;

    @Autowired
    public AnswerRepositoryTest(AnswerRepository answerRepository) {
        this.answerRepository = answerRepository;
    }

    @DisplayName("Answer가 저장된다")
    @Test
    void testSave() {
        Question question = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
        Answer answer = new Answer(UserTest.JAVAJIGI, question, "Answers Contents1");
        Answer savedAnswer = answerRepository.save(answer);
        assertAll(
                () -> assertThat(savedAnswer.getId()).isNotNull(),
                () -> assertThat(savedAnswer.getWriterId()).isEqualTo(answer.getWriterId()),
                () -> assertThat(savedAnswer.getQuestionId()).isEqualTo(answer.getQuestionId()),
                () -> assertThat(savedAnswer.getContents()).isEqualTo(answer.getContents())
        );
    }
}
