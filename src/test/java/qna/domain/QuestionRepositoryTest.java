package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository repository;

    private Question question;

    private Question actual;

    @BeforeEach
    void setUp() {
        question = Question.of("title", "contents");
        actual = repository.save(question);
    }

    @Test
    @DisplayName("정상적으로 전 후 데이터가 들어가 있는지 확인한다.")
    void save() {
        assertAll(
            () -> assertThat(actual.id()).isNotNull(),
            () -> assertThat(actual).isSameAs(question));
    }

    @Test
    @DisplayName("update 확인")
    void updata() {
        question.updateContents("change content");
        repository.saveAndFlush(question);
        Question finedQuestion = repository.findById(question.id()).get();
        assertThat(finedQuestion).isSameAs(question);
    }
}
