package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1,
        "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1,
        "Answers Contents2");

    @Autowired
    private AnswerRepository answers;

    @Test
    @DisplayName("Create Answer - 저장된 ID, contents 확인")
    void saveAnswer() {
        Answer savedAnswer = answers.save(A1);

        assertAll(
            () -> assertThat(savedAnswer.getId()).isNotNull(),
            () -> assertThat(savedAnswer.getContents()).isEqualTo(A1.getContents())
        );
    }

    @Test
    @DisplayName("Read Answer - writerId로 조회한 Answer 의 Id, Question Id를 확인")
    void findAnswerByWriterId() {
        Answer savedAnswer = answers.save(A1);
        Answer foundAnswer = answers.findAnswerByWriterId(A1.getWriterId()).get(0);

        assertAll(
            () -> assertThat(foundAnswer.getId()).isNotNull(),
            () -> assertThat(foundAnswer.getQuestionId()).isEqualTo(savedAnswer.getQuestionId())
        );
    }

    @Test
    @DisplayName("Update Answer - contents 변경 후 변경된 내용 확인")
    void updateAnswer() {
        Answer savedAnswer = answers.save(A1);
        Answer foundAnswer = answers.findById(savedAnswer.getId()).get();

        foundAnswer.setContents(A2.getContents());
        Optional<Answer> foundAnswerOptional = answers.findById(savedAnswer.getId());

        assertAll(
            () -> assertThat(foundAnswerOptional).isPresent(),
            () -> assertThat(foundAnswerOptional.get().getContents()).isEqualTo(A2.getContents())
        );
    }

    @Test
    @DisplayName("Delete Answer - 삭제 후 존재하지 않는 것을 확인")
    void deleteAnswer() {
        Answer savedAnswer = answers.save(A1);
        answers.delete(savedAnswer);
        Optional<Answer> answerOptional = answers.findById(savedAnswer.getId());

        assertThat(answerOptional).isNotPresent();

    }

}
