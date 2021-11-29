package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import qna.CannotDeleteException;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Autowired
    private QuestionRepository questions;

    @Autowired
    private UserRepository users;

    @Autowired
    private AnswerRepository answers;

    @Autowired
    private EntityManager entityManager;

    @Test
    void save() {
        final User user = users.save(UserTest.JAVAJIGI);
        final Question question = questions.save(
                Q1.writeBy(user)
        );
        assertThat(question.getId()).isNotNull();
        assertThat(question.getTitle()).isEqualTo(Q1.getTitle());
    }

    @Test
    void findByDeletedFalse_Empty() {
        List<Question> questions = this.questions.findByDeletedFalse();
        assertThat(questions).isEmpty();
    }

    @Test
    void findByDeletedFalse() {
        final User user1 = users.save(UserTest.JAVAJIGI);
        final User user2 = users.save(UserTest.SANJIGI);
        questions.save(Q1.writeBy(user1));
        questions.save(Q2.writeBy(user2));

        List<Question> questions = this.questions.findByDeletedFalse();
        assertThat(questions.size()).isEqualTo(2);
    }

    @Test
    void findByDeletedFalse_WithAnswer() { // TODO: 2021/11/25 setAnswer가 되어야지!!!
        final User user = users.save(UserTest.JAVAJIGI);
        final Question question = questions.save(QuestionTest.Q1.writeBy(user));
        answers.save(new Answer(user, question, "Answers Contents1"));
        entityManager.clear();

        List<Question> result = questions.findByDeletedFalse();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getAnswers()).hasSize(1);
        assertThat(result.get(0).getAnswers().get(0).getContents()).isEqualTo("Answers Contents1");
    }

    @Test
    void nullableFalse() {
        Question question = new Question(null, "contents");

        assertThatThrownBy(() -> questions.save(question))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void validateDeletableBy_성공() throws CannotDeleteException {
        final User user = users.save(UserTest.JAVAJIGI);
        final Question question = questions.save(QuestionTest.Q1.writeBy(user));

        question.validateDeletableBy(user);
    }

    @Test
    void validateDeletableBy_질문_작성자가아닌경우() throws CannotDeleteException {
        final User user = users.save(UserTest.JAVAJIGI);
        final Question question = questions.save(QuestionTest.Q1.writeBy(user));

        assertThatThrownBy(() -> question.validateDeletableBy(UserTest.SANJIGI))
                .isInstanceOf(CannotDeleteException.class);
    }

    @Test
    void validateDeletableBy_댓글_작성자가아닌경우() {
        final User javajigi = users.save(UserTest.JAVAJIGI);
        final User sanjigi = users.save(UserTest.SANJIGI);
        final Question question = questions.save(QuestionTest.Q1.writeBy(javajigi));
        final Answer answer = answers.save(new Answer(sanjigi, question, "Answers Contents1"));
        question.addAnswer(answer);

        assertThatThrownBy(() -> question.validateDeletableBy(javajigi))
                .isInstanceOf(CannotDeleteException.class);
    }
}
