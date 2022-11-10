package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

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


    @DisplayName("findByQuestionIdAndDeletedFalse 조회 테스트")
    @Test
    void findByQuestionIdAndDeletedFalse() {

        Answer answer1 = answerRepository.save(AnswerTest.A1);
        Answer answer2 = answerRepository.save(AnswerTest.A2);

        List<Answer> answers = answerRepository.findByQuestionIdAndDeletedFalse(answer1.getQuestionId());

        assertAll(
                () -> assertThat(answers).hasSize(2),
                () -> assertThat(answers).containsExactly(answer1, answer2)
        );
    }

    @DisplayName("findByIdAndDeletedFalse 조회 테스트")
    @Test
    void findByIdAndDeletedFalse() {

        Answer answer = answerRepository.save(AnswerTest.A1);
        Answer findAnswer = answerRepository.findByIdAndDeletedFalse(answer.getQuestionId()).orElse(null);

        assertAll(
                () -> assertThat(findAnswer).isNotNull(),
                () -> assertThat(findAnswer).isEqualTo(answer)
        );
    }
}
