package qna.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.Question;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class QuestionRepositoryTest {
    @Autowired
    QuestionRepository questionRepository;

    @DisplayName("저장하기")
    @Test
    void save() {
        Question question = new Question("testTitle", "testContent");

        Question saveQuestion = questionRepository.save(question);

        Question findQuestion = questionRepository.findById(saveQuestion.getId())
                .orElseThrow(() -> new IllegalStateException());

        assertThat(saveQuestion).isEqualTo(findQuestion);
        assertThat(saveQuestion).isSameAs(findQuestion);
    }

    @DisplayName("수정하기")
    @Test
    void update() {
        Question question = new Question("testTitle", "testContent");
        Question saveQuestion = questionRepository.save(question);

        saveQuestion.setContents("testUpdateContent");
        saveQuestion.setDeleted(true);

        Question findQuestion = questionRepository.findById(question.getId()).orElseThrow(() -> new IllegalStateException());
        assertThat(findQuestion.getContents()).isEqualTo("testUpdateContent");
        assertThat(findQuestion.isDeleted()).isTrue();
    }

    @DisplayName("삭제하기")
    @Test
    void delete() {
        Question question = new Question("testTitle", "testContent");
        Question saveQuestion = questionRepository.save(question);

        questionRepository.delete(saveQuestion);

        assertThat(questionRepository.findById(saveQuestion.getId())).isEqualTo(Optional.empty());
    }
}
