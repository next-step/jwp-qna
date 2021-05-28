package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository questionRepository;

    Question question;
    Question savedQuestion;

    @BeforeEach
    void setUp() {
        question = questionRepository.save(QuestionTest.Q1);
        savedQuestion = questionRepository.save(question);
    }

    @DisplayName("DB에 저장된 Entity와 저장하기전 Entity가 동일한지 확인")
    @Test
    void insertTest() {
        Question findQuestion = questionRepository.getOne(savedQuestion.getId());

        assertThat(findQuestion).isEqualTo(savedQuestion);
    }

    @DisplayName("삭제되지 않은 질문리스트조회")
    @Test
    void findByDeletedFalse() {
        Question savedQuestion1 = questionRepository.save(QuestionTest.Q2);
        savedQuestion1.setDeleted(true);
        questionRepository.flush();

        List<Question> findQuestion = questionRepository.findByDeletedFalse();

        assertAll(
                () -> assertThat(findQuestion).hasSize(1),
                () -> assertThat(findQuestion).containsExactly(savedQuestion)
        );


    }

    @DisplayName("삭제상태가 아닌 질문을 질문ID로 조회 테스트")
    @Test
    void findByIdAndDeletedFalse() {
        Optional<Question> findQuestion = questionRepository.findByIdAndDeletedFalse(savedQuestion.getId());

        assertAll(
                () -> assertThat(findQuestion).contains(savedQuestion),
                () -> assertTrue(findQuestion.isPresent())
        );

    }


    @DisplayName("삭제상태인 질문을 질문ID로 조회했을때 조회안됨 테스트")
    @Test
    void notFoundByIdAndDeletedTrue() {
        savedQuestion.setDeleted(true);
        questionRepository.flush();

        Optional<Question> findQuestion = questionRepository.findByIdAndDeletedFalse(savedQuestion.getId());

        assertAll(
                () -> assertThat(findQuestion).isEmpty(),
                () -> assertFalse(findQuestion.isPresent())
        );

    }

    @DisplayName("존재하지 않는 질문ID로 조회 테스트")
    @Test
    void findByInvalidIdAndDeletedFalse() {
        Long questionId = 0L;
        Optional<Question> findQuestion = questionRepository.findByIdAndDeletedFalse(questionId);

        assertFalse(findQuestion.isPresent());
    }
    
    @DisplayName("질문 수정 테스트")
    @Test
    void updateQuestionTest() {
        String contests = "질문수정내용";
        savedQuestion.setContents(contests);

        questionRepository.flush();

        Question findQuestion = questionRepository.getOne(savedQuestion.getId());

        assertAll(
                () -> assertThat(findQuestion).isEqualTo(savedQuestion),
                () -> assertThat(findQuestion.getContents()).isEqualTo(contests)
        );

    }
}
