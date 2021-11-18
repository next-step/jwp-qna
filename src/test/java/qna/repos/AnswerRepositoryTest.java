package qna.repos;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.Answer;
import qna.domain.Question;
import qna.domain.User;
import qna.domain.UserTest;

import javax.persistence.EntityManager;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository answers;

    @Autowired
    private QuestionRepository questions;

    @Autowired
    private UserRepository users;

    @Autowired
    EntityManager em;

    private User user;

    private Question question;

    @BeforeEach
    void setUp() {
        user = users.save(UserTest.JAVAJIGI);
        users.save(user);
        question = new Question("title1", "contents1").writeBy(user);
        questions.save(question);
    }

    @DisplayName("Answer 저장 테스트")
    @Test
    void save() {
        Answer actual = answers.save(new Answer(user, question, "contents1"));

        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getContents()).isEqualTo("contents1")
        );
    }

    @DisplayName("Answer deleted=false 경우 테스트")
    @Test
    void findByIdAndDeletedFalse() {
        //given
        Answer a1 = answers.save(new Answer(user, question, "contents1"));
        Answer a2 = answers.save(new Answer(user, question, "contents2"));

        //when
        a2.setDeleted(true);
        answers.save(a2);
        Optional<Answer> a1Result = answers.findByIdAndDeletedFalse(a1.getId());
        Optional<Answer> a2Result = answers.findByIdAndDeletedFalse(a2.getId());

        //then
        assertAll(
                () -> assertThat(a1Result).isNotEmpty(),
                () -> assertThat(a2Result).isEmpty()
        );
    }
}
