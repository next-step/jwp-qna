package qna.domain;

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

    @Test
    @DisplayName("Question 저장")
    void save(){
        Question saved = questionRepository.save(QuestionTest.Q1);
        assertThat(saved.getId()).isNotNull();
    }

    @Test
    @DisplayName("Question을 DeletedFalse로 조회")
    void Question_조회_byDeletedFalse(){
        QuestionTest.Q1.setDeleted(true);
        questionRepository.save(QuestionTest.Q1);
        Question questionDeletedFalse = questionRepository.save(QuestionTest.Q2);
        List<Question> questions = questionRepository.findByDeletedFalse();
        assertThat(questions).containsExactly(questionDeletedFalse);
    }

    @Test
    @DisplayName("Question을 Id, DeletedFalse로 조회")
    void Question_조회_byId_DeletedFalse(){
        Question saved = questionRepository.save(QuestionTest.Q1);
        Optional<Question> question = questionRepository.findByIdAndDeletedFalse(saved.getId());
        assertThat(question.get()).isEqualTo(saved);
    }
}