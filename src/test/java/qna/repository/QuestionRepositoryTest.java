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
    QuestionRepository questions;

    @DisplayName("저장하기")
    @Test
    void save() {
        Question question = new Question("testTitle", "testContent");

        Question saveQuestion = questions.save(question);

        assertThat(saveQuestion).isEqualTo(question);
        assertThat(saveQuestion).isSameAs(question);
    }

    @DisplayName("수정하기")
    @Test
    void update() {
        Question question = new Question("testTitle", "testContent");
        Question saveQuestion = questions.save(question);

        saveQuestion.setContents("testUpdateContent");
        saveQuestion.setDeleted(true);

        Question findQuestion = questions.findById(question.getId()).orElseThrow(() -> new IllegalStateException());
        assertThat(findQuestion.getContents()).isEqualTo("testUpdateContent");
        assertThat(findQuestion.isDeleted()).isTrue();
    }

    @DisplayName("삭제하기")
    @Test
    void delete() {
        Question question = new Question("testTitle", "testContent");
        Question saveQuestion = questions.save(question);

        questions.delete(saveQuestion);

        assertThat(questions.findById(saveQuestion.getId())).isEqualTo(Optional.empty());
    }
}
