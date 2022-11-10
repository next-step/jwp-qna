package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
@DisplayName("Answer")
public class AnswerTest {
    public static final String ANSWERS_CONTENTS_1 = "Answers Contents1";
    public static final String ANSWERS_CONTENTS_2 = "Answers Contents2";
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, ANSWERS_CONTENTS_1);
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, ANSWERS_CONTENTS_2);

    @Autowired
    private AnswerRepository answerRepository;

    @DisplayName("저장 테스트")
    @Test
    void save() {

        Answer answer = answerRepository.save(A1);

        assertAll(
                () -> assertThat(answer.getId()).isNotNull(),
                () -> assertThat(answer.getWriterId()).isNotNull(),
                () -> assertThat(answer.getQuestionId()).isNotNull(),
                () -> assertThat(answer.getCreatedAt()).isNotNull(),
                () -> assertThat(answer.getUpdatedAt()).isNotNull(),
                () -> assertThat(answer.getContents()).isEqualTo(ANSWERS_CONTENTS_1)
        );
    }
}
