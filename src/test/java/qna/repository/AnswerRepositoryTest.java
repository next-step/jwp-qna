package qna.repository;


import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.NotFoundException;
import qna.domain.Answer;
import qna.domain.AnswerTest;
import qna.domain.QuestionTest;

@DataJpaTest
class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository answerRepository;

    @DisplayName("답변을 새로 추가할 수 있다")
    @Test
    void save_test() {
        Answer saved = answerRepository.save(AnswerTest.A1);

        assertAll(
                () -> assertNotNull(saved.getId()),
                () -> assertEquals(saved.getWriterId(), AnswerTest.A1.getWriterId()),
                () -> assertEquals(saved.getQuestionId(), AnswerTest.A1.getQuestionId()),
                () -> assertEquals(saved.getContents(), AnswerTest.A1.getContents())
        );
    }


    @DisplayName("전체 답변을 조회할 수 있다")
    @Test
    void findAll_test() {
        answerRepository.save(AnswerTest.A1);
        answerRepository.save(AnswerTest.A2);

        List<Answer> answers = answerRepository.findAll();

        assertEquals(answers.size(), 2);
    }

    @DisplayName("특정 답변을 조회할 수 있다")
    @Test
    void find_test() {

        Answer saved = answerRepository.save(AnswerTest.A1);

        Optional<Answer> answer = answerRepository.findById(saved.getId());

        assertTrue(answer.isPresent());
    }

    @DisplayName("답변을 변경할 수 있다")
    @Test
    void update_test() {
        Answer saved = answerRepository.save(AnswerTest.A1);

        saved.toQuestion(QuestionTest.Q2);

        Answer answer = answerRepository.findById(saved.getId()).orElseThrow(NotFoundException::new);

        assertEquals(answer.getQuestionId(), QuestionTest.Q2.getId());
    }

    @DisplayName("답변을 삭제할 수 있다")
    @Test
    void delete_test() {
        Answer saved = answerRepository.save(AnswerTest.A1);

        answerRepository.delete(saved);

        Optional<Answer> answer = answerRepository.findById(saved.getId());

        assertFalse(answer.isPresent());
    }

    @DisplayName("삭제되지 않은 답변을 질문아이디로 조회할 수 있다")
    @Test
    void findByQuestionIdAndDeletedFalse_test() {
        Answer saved = answerRepository.save(AnswerTest.A1);

        List<Answer> beforeDeleted = answerRepository.findByQuestionIdAndDeletedFalse(QuestionTest.Q1.getId());

        saved.setDeleted(true);

        List<Answer> afterDeleted = answerRepository.findByQuestionIdAndDeletedFalse(QuestionTest.Q1.getId());

        assertAll(
                () -> assertEquals(beforeDeleted.size(), 1),
                () -> assertEquals(afterDeleted.size(), 0)
        );

    }

    @DisplayName("삭제되지 않은 특정 답변을 조회할 수 있다")
    @Test
    void findByIdAndDeletedFalse_test() {
        Answer saved1 = answerRepository.save(AnswerTest.A1);
        Answer saved2 = answerRepository.save(AnswerTest.A2);

        saved2.setDeleted(true);

        Optional<Answer> expect1 = answerRepository.findByIdAndDeletedFalse(saved1.getId());
        Optional<Answer> expect2 = answerRepository.findByIdAndDeletedFalse(saved2.getId());

        assertAll(
                () -> assertTrue(expect1.isPresent()),
                () -> assertFalse(expect2.isPresent())
        );

    }


}