package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class QuestionRepositoryTest {
    @Autowired
    QuestionRepository questionRepository;
    Question question1;
    Question question2;

    @BeforeEach
    void deleteAll() {
        questionRepository.deleteAll();

        // TODO: 테스트 한번에 실행해도 성공하도록 수정 필요
        question1 = new Question(1L, "hi", "hello~", new Answers());
        question1.setDeleted();
        question2 = new Question(2L, "wow", "yeah~", new Answers());
        question2.setDeleted();
    }

    @Test
    @DisplayName("질문을 등록할 수 있다.")
    void save() {
        Question saveQuestion = questionRepository.save(question1);

        Optional<Question> findQuestion = questionRepository.findById(saveQuestion.getId());

        assertThat(findQuestion.isPresent()).isTrue();
        assertThat(findQuestion.get().getId()).isEqualTo(question1.getId());
    }

    @Test
    @DisplayName("deleted 가 false 인 질문을 찾는다.")
    void findByDeletedFalse() {
        questionRepository.save(question1);
        questionRepository.save(question2);

        List<Question> findQuestions = questionRepository.findByDeletedFalse();

        assertThat(findQuestions.size()).isEqualTo(1);
        assertThat(findQuestions.get(0).getId()).isEqualTo(question1.getId());
    }

    @Test
    @DisplayName("id로 삭제되지 않은 질문을 찾는다.")
    void findByIdAndDeletedFalse() {
        questionRepository.save(question1);

        Optional<Question> question = questionRepository.findByIdAndDeletedFalse(question1.getId());

        assertThat(question.isPresent()).isTrue();
        assertThat(question.get().getId()).isEqualTo(question1.getId());
    }
}