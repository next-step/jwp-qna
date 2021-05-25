package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Autowired
    AnswerRepository answerRepository;

    @Test
    @DisplayName("답변 생성")
    public void createAnswer() {
        Answer answer = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
        assertThat(answer.equals(A1)).isTrue();
    }

    @Test
    @DisplayName("답변 저장")
    public void saveAnswer() {
        String content = "Save Answer Content";
        Answer answer = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, content);
        Answer saveAnswer = answerRepository.save(answer);

        assertAll(
                () -> assertThat(saveAnswer.getId()).isNotNull(),
                () -> assertThat(saveAnswer.getWriterId()).isEqualTo(1L),
                () -> assertThat(saveAnswer.getContents()).isEqualTo(content),
                () -> assertThat(saveAnswer.isDeleted()).isFalse()
        );
    }
}
