package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;


@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
public class AnswerRepositoryTest {
    
    @Autowired
    private AnswerRepository answers;

    @Test
    void save() {
        // given
        Answer answer = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");

        // when
        Answer result = answers.save(answer);
        assertAll(
                () -> assertThat(result.getId()).isNotNull(),
                () -> assertThat(result.getContents()).isEqualTo(result.getContents())
        );

    }
}
