package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@EnableJpaAuditing
@DisplayName("QuestionRepository 테스트")
class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository questionRepository;

    private Question question;
    private Question question2;

    @BeforeEach
    void setUp() {
        question = new Question("질문", "질문 내용");
        question2 = new Question("질문2", "질문2 내용");
        question2.setDeleted(true);
    }

    @Test
    @DisplayName("Question를 저장한다.")
    void save() {
        // when
        Question savedQuestion = questionRepository.save(question);

        // then
        assertAll(
                () -> assertThat(savedQuestion.getId()).isNotNull(),
                () -> assertThat(savedQuestion.getCreatedAt()).isNotNull(),
                () -> assertThat(savedQuestion.getUpdatedAt()).isNotNull(),
                () -> assertThat(savedQuestion).isEqualTo(question)
        );
    }

    @Test
    @DisplayName("deleted가 false인 Question 리스트를 조회한다.")
    void findByDeletedFalse() {
        // given
        questionRepository.save(question);
        questionRepository.save(question2);

        // when
        List<Question> questions = questionRepository.findByDeletedFalse();

        // then
        assertThat(questions).containsExactly(question);
    }

    @Test
    @DisplayName("id와 deleted로 Question을 조회한다.")
    void findByIdAndDeletedFalse() {
        // given
        Question savedQuestion = questionRepository.save(question);

        // when
        Optional<Question> questionOptional = questionRepository.findByIdAndDeletedFalse(savedQuestion.getId());

        // then
        assertAll(
                () -> assertThat(questionOptional.isPresent()).isTrue(),
                () -> assertThat(questionOptional.get()).isEqualTo(question)
        );
    }
}
