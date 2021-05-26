package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureTestDatabase(replace = Replace.NONE)
@DataJpaTest
class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository questionRepository;

    @Test
    @DisplayName("Question 저장 테스트")
    void save() {
        Question question = QuestionTest.Q1;
        Question saved = questionRepository.save(question);

        assertThat(saved).isEqualTo(question);
    }

    @Test
    @DisplayName("Question 수정 테스트")
    void update() {
        Question question = QuestionTest.Q1;
        Question saved = questionRepository.save(question);

        saved.setContents("질문 내용 바꾸기");

        Optional<Question> updated = questionRepository.findById(saved.getId());

        assertThat(updated.get().getContents()).isEqualTo("질문 내용 바꾸기");
    }

    @Test
    @DisplayName("Question 제거 테스트")
    void delete() {
        Question question = QuestionTest.Q1;
        Question saved = questionRepository.save(question);

        questionRepository.delete(saved);

        List<Question> questions = questionRepository.findAll();

        assertThat(questions).isEmpty();
    }

    @Test
    void findByDeletedFalse() {
    }

    @Test
    void findByIdAndDeletedFalse() {
    }
}