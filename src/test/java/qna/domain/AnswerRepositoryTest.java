package qna.domain;

import org.junit.jupiter.api.BeforeEach;
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
    private UserRepository users;

    @Autowired
    private AnswerRepository answers;

    private User user;
    private Answer answer;

    @BeforeEach
    void setUp() {
        user = users.save(UserTest.JAVAJIGI);
        answer = new Answer(user, QuestionTest.Q1, "Answers Contents1");
    }

    @Test
    void saveWithUser() {

        Answer result = answers.save(answer);

        assertAll(
                () -> assertThat(result.getId()).isNotNull(),
                () -> assertThat(result.getContents()).isEqualTo(result.getContents()),
                () -> assertThat(result.getWriterId()).isNotNull()
        );

    }

    @DisplayName("저장 후 id를 이용하여 찾아오는지 확인")
    @Test
    void findById() {
        answers.save(answer);

        Answer result = answers.findById(answer.getId()).get();

        assertThat(result).isEqualTo(answer);
        assertThat(result == answer).isTrue();
    }

    @DisplayName("변경 감지 테스트")
    @Test
    void update() {
        answers.save(answer);

        answer.setContents("update Contents");
        Answer answer2 = answers.findByContents("update Contents");

        assertThat(answer2).isNotNull();
    }

    @DisplayName("삭제 되는지 테스트")
    @Test
    void delete() {
        answers.save(answer);

        answers.deleteAll();
        answers.flush();

        assertThat(answers.findAll()).isEmpty();
    }
}
