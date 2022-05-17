package qna.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.repository.entity.Question;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static qna.repository.entity.QuestionTest.Q1;
import static qna.repository.entity.QuestionTest.Q2;

@DataJpaTest
class QuestionRepositoryTest {
    @Autowired
    private QuestionRepository questionRepository;
    private Question question;

    @BeforeEach
    void setup() {
        question = questionRepository.save(Q1);
        questionRepository.save(Q2);
    }

    @Test()
    @DisplayName("정상 상태의 질문을 ID로 찾는다")
    void findByIdAndDeletedFalse() {
        Long questionId = question.getId();
        Optional<Question> find = questionRepository.findByIdAndDeletedFalse(questionId);
        Question question = find.orElse(null);

        assertAll(
                () -> assertEquals(questionId, question.getId()),
                () -> assertEquals("contents1", question.getContents()),
                () -> assertEquals("title1", question.getTitle()),
                () -> assertEquals(1L, question.getWriterId()),
                () -> assertThat(question.isDeleted()).isFalse()
        );
    }

    @DisplayName("정상 상태의 질문을 모두 찾는다")
    @Test
    void findByDeletedFalse() {
        List<Question> questionList = questionRepository.findByDeletedFalse();

        assertEquals(2, questionList.size());
    }
}
