package qna.domain;

import org.junit.jupiter.api.DisplayName;
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
        Answer answer = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");

        Answer result = answers.save(answer);
        assertAll(
                () -> assertThat(result.getId()).isNotNull(),
                () -> assertThat(result.getContents()).isEqualTo(result.getContents())
        );

    }

    @DisplayName("저장 후 id를 이용하여 찾아오는지 확인")
    @Test
    void findById() {
        Answer answer = answers.save(new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents1"));

        Answer result = answers.findById(answer.getId()).get();

        assertThat(result).isEqualTo(answer);
        assertThat(result == answer).isTrue();
    }

    @DisplayName("변경 감지 테스트")
    @Test
    void update() {
        Answer answer = answers.save(new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents1"));

        answer.setContents("update Contents");

        Answer answer2 = answers.findByContents("update Contents");

        assertThat(answer2).isNotNull();
    }

    @DisplayName("삭제 되는지 테스트")
    @Test
    void delete() {
        Answer answer = answers.save(new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents1"));

        answers.deleteAll();
        answers.flush();

        assertThat(answers.findAll()).isEmpty();
    }
}
