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
    AnswerRepository answerRepository;

    @DisplayName("저장하기")
    @Test
    void save() {
        User writer = new User("testUserId", "testPassword", "testName", "test@email.com");
        Question question = new Question("testTitle", "testContent");
        Answer answer = new Answer(writer, question, "save test");

        Answer savedAnswer = answerRepository.save(answer);

        Answer findAnswer = answerRepository.findById(savedAnswer.getId())
                .orElseThrow(() -> new IllegalStateException());

        assertThat(savedAnswer).isEqualTo(findAnswer);
        assertThat(savedAnswer).isSameAs(findAnswer);
    }

    @DisplayName("수정하기")
    @Test
    void update() {
        User writer = new User("testUserId", "testPassword", "testName", "test@email.com");
        Question question = new Question("testTitle", "testContent");
        Answer answer = new Answer(writer, question, "save test");
        Answer savedAnswer = answerRepository.save(answer);

        savedAnswer.setContents("testUpdateContent");

        Answer findAnswer = answerRepository.findById(answer.getId()).orElseThrow(() -> new IllegalStateException());
        assertThat(findAnswer.getContents()).isEqualTo("testUpdateContent");
    }

    @DisplayName("삭제하기")
    @Test
    void delete() {
        User writer = new User("testUserId", "testPassword", "testName", "test@email.com");
        Question question = new Question("testTitle", "testContent");
        Answer answer = new Answer(writer, question, "save test");
        Answer savedAnswer = answerRepository.save(answer);

        answerRepository.delete(savedAnswer);

        assertThat(answerRepository.findById(savedAnswer.getId())).isEqualTo(Optional.empty());
    }
}
