package qna.repository;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import qna.domain.Question;
import qna.domain.QuestionRepository;
import qna.domain.QuestionTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository questionRepository;

    @Test
    @DisplayName("save 검증 테스트")
    void saveTest() {
        Question question = QuestionTest.Q1;
        Question savedQuestion = questionRepository.save(question);

        assertAll(
            () -> assertNotNull(savedQuestion.getId()),
            () -> assertEquals(savedQuestion.getTitle(), question.getTitle()),
            () -> assertEquals(savedQuestion.getContents(), question.getContents()),
            () -> assertEquals(savedQuestion.getWriterId(), question.getWriterId())
        );
    }

    @Test
    @DisplayName("저장한 question 와 조회한 question 이 같은지 동등성 테스트")
    void read() {
        Question savedQuestion = questionRepository.save(QuestionTest.Q1);
        Optional<Question> question = questionRepository.findById(savedQuestion.getId());

        assertAll(
            () -> assertThat(question).isPresent(),
            () -> assertThat(savedQuestion).isSameAs(question.get())
        );
    }


    @Test
    @DisplayName("findByDeletedFalse 테스트")
    void findByDeletedFalseTest() {
        questionRepository.save(QuestionTest.Q1);
        questionRepository.save(QuestionTest.Q2);

        List<Question> questions = questionRepository.findByDeletedFalse();

        assertAll(
            () -> assertThat(questions).hasSize(2),
            () -> assertTrue(questions.stream().noneMatch(Question::isDeleted))
        );
    }


    @Test
    @DisplayName("findByIdAndDeletedFalse 테스트")
    void findByIdAndDeletedFalse() {
        Question questionSaved = questionRepository.save(QuestionTest.Q1);
        Question questionFound = questionRepository.findByIdAndDeletedFalse(questionSaved.getId()).get();

        assertFalse(questionFound.isDeleted());
    }
}
