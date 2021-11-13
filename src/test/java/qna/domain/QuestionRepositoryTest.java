package qna.domain;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
public class QuestionRepositoryTest {
    @Autowired
    private QuestionRepository questions;

    @DisplayName("저장 테스트")
    @Test
    void save() {
        Question question = questions.save(QuestionTest.Q1);

        assertThat(question.getId()).isNotNull();
    }

    @DisplayName("검색 테스트")
    @Test
    void findById() {
        Question question = questions.save(QuestionTest.Q1);
        Question result = questions.findById(question.getId()).get();

        assertThat(result).isEqualTo(question);
    }

    @DisplayName("삭제 테스트")
    @Test
    void delete() {
        Question question = questions.save(QuestionTest.Q1);

        questions.delete(question);
        questions.flush();

        Question result = questions.findById(question.getId()).orElseGet(() -> null);
        assertThat(result).isNull();
    }
}
