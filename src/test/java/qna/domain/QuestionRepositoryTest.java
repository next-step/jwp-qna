package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class QuestionRepositoryTest {
    private QuestionRepository questionRepository;

    @Autowired
    public QuestionRepositoryTest(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    @DisplayName("Question을 저장한다")
    @Test
    void testSave() {
        User writer = new User("user1", "1234", "userName", "email");
        Question question = new Question("title1", "contents1").writeBy(writer);
        Question savedQuestion = questionRepository.save(question);
        assertAll(
                () -> assertThat(savedQuestion.getId()).isNotNull(),
                () -> assertThat(savedQuestion.getTitle()).isEqualTo(question.getTitle()),
                () -> assertThat(savedQuestion.getContents()).isEqualTo(question.getContents()),
                () -> assertThat(savedQuestion.getWriterId()).isEqualTo(question.getWriterId())
        );
    }
}
