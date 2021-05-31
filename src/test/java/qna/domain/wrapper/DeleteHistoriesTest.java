package qna.domain.wrapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.*;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class DeleteHistoriesTest {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AnswerRepository answerRepository;

    private DeleteHistories deleteHistories;
    private Question question;
    private User user;

    @BeforeEach
    void setup() {
        deleteHistories = new DeleteHistories();
        user = userRepository.save(UserTest.JAVAJIGI);
        question = questionRepository.save(new Question("title1", "contents1").writtenBy(user));
    }

    @Test
    void create() {
        assertThat(deleteHistories.histories()).hasSize(0);
    }

    @Test
    void add_Test() {
        deleteHistories.add(question.deleteAndReturnHistories());

        assertThat(deleteHistories.histories()).hasSize(1);
    }

    @Test
    void addAll_Test() {
        DeleteHistories deleteHistories = QuestionTest.Q1.deleteAndReturnHistories();
        deleteHistories.add(QuestionTest.Q2.deleteAndReturnHistories());

        assertThat(deleteHistories.histories()).hasSize(2);
    }

}
