package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;


@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
public class AnswerRepositoryTest {

    @Autowired
    private UserRepository users;

    @Autowired
    private QuestionRepository questions;

    @Autowired
    private AnswerRepository answers;

    private User user;
    private Answer answer;
    private Question question;

    @BeforeEach
    void setUp() {
        user = users.save(UserTest.JAVAJIGI);
        question = questions.save(new Question("title1", "contents1").writeBy(user));
        answer = new Answer(user, question, "Answers Contents1");
    }
    
    @DisplayName("user, question 연관관계 검증")
    @Test
    void saveWithUserAndQuestion() {
        Answer result = answers.save(answer);

        assertAll(
                () -> assertThat(result.getId()).isNotNull(),
                () -> assertThat(result.isOwner(user)).isTrue(),
                () -> assertThat(result.getQuestion()).isEqualTo(question)
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

    @DisplayName("qustion 연관관계 검증")
    @Test
    void findByQuestionIdAndDeletedFalse() {
        answers.save(answer);
        Answer answer1 = new Answer(user, question, "Answers Contents2");
        answers.save(answer1);
        Question then = questions.findById(question.getId()).get();

        List<Answer> result = then.getAnswers();

        assertThat(then.isDeleted()).isFalse();
        assertThat(result.size()).isEqualTo(2);
    }
}
