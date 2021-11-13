package qna.repos;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.Answer;
import qna.domain.Question;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class QuestionRepositoryTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserRepositoryTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserRepositoryTest.SANJIGI);

    @Autowired
    private QuestionRepository questions;

    @Autowired
    private AnswerRepository answers;

    @DisplayName("Question 저장 테스트")
    @Test
    void save() {
        Question expected = Q1;
        Question actual = questions.save(Q1);

        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getContents()).isEqualTo(expected.getContents())
        );
    }

    @DisplayName("Question deleted=false 경우 테스트")
    @Test
    void findByIdAndDeletedFalse() {
        Question q1 = questions.save(Q1);
        Question q2 = questions.save(Q2);

        Q2.setDeleted(true);

        questions.save(Q2);

        Optional<Question> a1Result = questions.findByIdAndDeletedFalse(q1.getId());
        Optional<Question> a2Result = questions.findByIdAndDeletedFalse(q2.getId());
        List<Question> result = questions.findByDeletedFalse();

        assertAll(
                () -> assertThat(a1Result).isNotEmpty(),
                () -> assertThat(a2Result).isEmpty(),
                () -> assertThat(result.size()).isEqualTo(1)
        );
    }

    @DisplayName("Question Answer 연관관계 테스트")
    @Test
    void saveQuestionAndAnswer() {
        Question q1 = questions.save(Q1);
        q1.addAnswer(answers.save(new Answer(UserRepositoryTest.JAVAJIGI, q1, "Answers Contents1")));
        Question actual = questions.save(q1);
        List<Answer> answers = actual.getAnswers();
        assertThat(answers).hasSize(1);
    }
}
