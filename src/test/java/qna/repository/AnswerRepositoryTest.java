package qna.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.Answer;
import qna.domain.Question;
import qna.domain.User;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class AnswerRepositoryTest {
    @Autowired
    AnswerRepository answers;

    @DisplayName("저장하기")
    @Test
    void save() {
        User writer = new User("testUserId", "testPassword", "testName", "test@email.com");
        Question question = new Question("testTitle", "testContent");
        Answer answer = new Answer(writer, question, "save test");

        Answer savedAnswer = answers.save(answer);

        assertThat(savedAnswer).isEqualTo(answer);
        assertThat(savedAnswer).isSameAs(answer);
    }

    @DisplayName("수정하기")
    @Test
    void update() {
        User writer = new User("testUserId", "testPassword", "testName", "test@email.com");
        Question question = new Question("testTitle", "testContent");
        Answer answer = new Answer(writer, question, "save test");
        Answer savedAnswer = answers.save(answer);

        savedAnswer.setContents("testUpdateContent");

        Answer findAnswer = answers.findById(answer.getId()).orElseThrow(() -> new IllegalStateException());
        assertThat(findAnswer.getContents()).isEqualTo("testUpdateContent");
    }

    @DisplayName("삭제하기")
    @Test
    void delete() {
        User writer = new User("testUserId", "testPassword", "testName", "test@email.com");
        Question question = new Question("testTitle", "testContent");
        Answer answer = new Answer(writer, question, "save test");
        Answer savedAnswer = answers.save(answer);

        answers.delete(savedAnswer);

        assertThat(answers.findById(savedAnswer.getId())).isEqualTo(Optional.empty());
    }
}
