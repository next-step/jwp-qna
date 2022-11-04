package qna.repository;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.Question;
import qna.domain.QuestionTest;

@DataJpaTest
class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository questionRepository;

    @DisplayName("삭제되지 않은 답변을 전부 조회할 수 있다")
    @Test
    void findByDeletedFalse_test() {

        Question q1 = questionRepository.save(QuestionTest.Q1);
        Question q2 = questionRepository.save(QuestionTest.Q2);

        List<Question> beforeDeleted = questionRepository.findByDeletedFalse();

        q1.setDeleted(true);

        List<Question> afterDeleted = questionRepository.findByDeletedFalse();

        assertAll(
                () -> assertTrue(q1.isDeleted()),
                () -> assertFalse(q2.isDeleted()),
                () -> assertEquals(beforeDeleted.size(), 2),
                () -> assertEquals(afterDeleted.size(), 1)
        );

    }

    @DisplayName("삭제되지 않은 답변을 아이디로 조회할 수 있다.")
    @Test
    void findByIdAndDeletedFalse_test() {
        Question q1 = questionRepository.save(QuestionTest.Q1);
        Question q2 = questionRepository.save(QuestionTest.Q2);

        q1.setDeleted(true);

        Optional<Question> expect1 = questionRepository.findByIdAndDeletedFalse(q1.getId());
        Optional<Question> expect2 = questionRepository.findByIdAndDeletedFalse(q2.getId());

        assertAll(
                () -> assertTrue(q1.isDeleted()),
                () -> assertFalse(q2.isDeleted()),
                () -> assertFalse(expect1.isPresent()),
                () -> assertTrue(expect2.isPresent())
        );


    }
}