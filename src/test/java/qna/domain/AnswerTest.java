package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Autowired
    private AnswerRepository answerRepository;

    private Answer savedAnswer;

    @BeforeEach
    private void beforeAll(){
        savedAnswer = answerRepository.save(A1);
    }

    @Test
    @DisplayName("answer 등록")
    public void saveTest(){
        assertAll(
                () -> assertThat(A1.getId()).isNotNull(),
                () -> assertThat(A1.getWriterId()).isEqualTo(savedAnswer.getWriterId()),
                () -> assertThat(A1.getQuestionId()).isEqualTo(savedAnswer.getQuestionId()),
                () -> assertThat(A1.getContents()).isEqualTo(savedAnswer.getContents()),
                () -> assertFalse(A1.isDeleted())
        );
    }

    @Test
    @DisplayName("question id로 deleted가 false인 answer 검색")
    public void findByQuestionIdAndDeletedFalseTest(){
        List<Answer> answers = answerRepository.findByQuestionIdAndDeletedFalse(QuestionTest.Q1.getId());

        assertAll(
                () -> assertFalse(savedAnswer.isDeleted()),
                () -> assertEquals(savedAnswer, answers.get(0))
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
