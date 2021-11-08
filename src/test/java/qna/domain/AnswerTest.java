package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Autowired
    private AnswerRepository answerRepository;

    @Test
    @DisplayName("answer 등록")
    public void saveTest(){
        Answer answer = answerRepository.save(A1);
        assertAll(
                () -> assertThat(A1.getId()).isNotNull(),
                () -> assertThat(A1.getWriterId()).isEqualTo(answer.getWriterId()),
                () -> assertThat(A1.getQuestionId()).isEqualTo(answer.getQuestionId()),
                () -> assertThat(A1.getContents()).isEqualTo(answer.getContents()),
                () -> assertThat(A1.isDeleted()).isFalse()
        );
    }

    @Test
    @DisplayName("question id로 deleted가 false인 answer 검색")
    public void findByQuestionIdAndDeletedFalseTest(){
        Answer savedAnswer = answerRepository.save(A1);

        List<Answer> answers = answerRepository.findByQuestionIdAndDeletedFalse(QuestionTest.Q1.getId());

        assertThat(savedAnswer).isEqualTo(answers.get(0));
        assertThat(savedAnswer.isDeleted()).isFalse();
    }

    @Test
    @DisplayName("answer id로 deleted가 false인 answer 단일검색")
    public void findByIdAndDeletedFalseTest(){
        Answer savedAnswer = answerRepository.save(A1);

        Optional<Answer> oAnswer = answerRepository.findByIdAndDeletedFalse(savedAnswer.getId());

        assertThat(oAnswer.get()).isEqualTo(savedAnswer);
        assertThat(oAnswer.get().isDeleted()).isFalse();

    }


}
