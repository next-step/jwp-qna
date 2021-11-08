package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

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
                () -> assertFalse(A1.isDeleted())
        );
    }

    @Test
    @DisplayName("question id로 deleted가 false인 answer 검색")
    public void findByQuestionIdAndDeletedFalseTest(){
        Answer savedAnswer = answerRepository.save(A1);

        List<Answer> answers = answerRepository.findByQuestionIdAndDeletedFalse(QuestionTest.Q1.getId());

        assertAll(
                () -> assertFalse(savedAnswer.isDeleted()),
                () -> assertEquals(savedAnswer, answers.get(0))
        );
    }

    @Test
    @DisplayName("answer id로 deleted가 false인 answer 단일검색")
    public void findByIdAndDeletedFalseTest(){
        Answer savedAnswer = answerRepository.save(A1);

        Optional<Answer> oAnswer = answerRepository.findByIdAndDeletedFalse(savedAnswer.getId());

        assertAll(
                () -> assertEquals(oAnswer.get(), savedAnswer),
                () -> assertFalse(oAnswer.get().isDeleted())
        );

    }


}
