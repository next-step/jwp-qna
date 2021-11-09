package qna.domain;

import org.junit.jupiter.api.BeforeEach;
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

    private Answer savedAnswer;

    @BeforeEach
    private void beforeEach(){
        savedAnswer = answerRepository.save(A1);
    }

    @Test
    @DisplayName("answer 등록")
    public void saveAnswerTest(){
        assertAll(
                () -> assertThat(savedAnswer.getId()).isNotNull(),
                () -> assertThat(savedAnswer.getWriterId()).isEqualTo(A1.getWriterId()),
                () -> assertThat(savedAnswer.getQuestionId()).isEqualTo(A1.getQuestionId()),
                () -> assertThat(savedAnswer.getContents()).isEqualTo(A1.getContents()),
                () -> assertFalse(savedAnswer.isDeleted())
        );
    }

    @Test
    @DisplayName("question id로 deleted가 false인 answer 검색")
    public void findByQuestionIdAndDeletedFalseTest(){
        List<Answer> answers = answerRepository.findByQuestionIdAndDeletedFalse(QuestionTest.Q1.getId());

        assertAll(
                () -> assertThat(answers).contains(savedAnswer)
        );
    }

    @Test
    @DisplayName("answer id로 deleted가 false인 answer 단일검색")
    public void findByIdAndDeletedFalseTest(){
        Optional<Answer> oAnswer = answerRepository.findByIdAndDeletedFalse(savedAnswer.getId());

        assertAnswerDeleted(oAnswer, false);

    }

    @Test
    @DisplayName("answer에 delete를 true로 수정")
    public void updateAnswerDeletedTrue(){
        savedAnswer.setDeleted(true);

        Optional<Answer> oAnswer = answerRepository.findById(savedAnswer.getId());

        assertAnswerDeleted(oAnswer, true);
    }

    private void assertAnswerDeleted(Optional<Answer> oAnswer, boolean isDeleted) {
        assertAll(
                () -> assertEquals(oAnswer.get(), savedAnswer),
                () -> assertThat(oAnswer.get().isDeleted()).isEqualTo(isDeleted)
        );
    }


}
