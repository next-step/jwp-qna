package qna.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import qna.CannotDeleteException;

@DataJpaTest
public class AnswerTest {
    @Autowired
    private AnswerRepository answers;

    @Autowired
    private UserRepository users;

    @Autowired
    private QuestionRepository questions;

    @AfterEach
    void tearDown() {
        answers.deleteAll();
        questions.deleteAll();
        users.deleteAll();
    }

    @Test
    void save() {
        User user = users.save(new User("javajigi", "password", "name", "javajigi@slipp.net"));
        Answer expected = new Answer(user, "Answers Contents1");

        Answer actual = answers.save(expected);

        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getContents()).isEqualTo(expected.getContents());
        assertThat(actual.getContents().getWriter()).isSameAs(user);
    }

    @Test
    @DisplayName("JPA가 식별자가 같은 엔티티에 대한 동일성을 보장하는지 테스트")
    void identity() {
        Answer expected = saveNewDefaultAnswer();

        Answer actual = answers.findById(expected.getId()).get();

        assertThat(actual).isSameAs(expected);
    }

    @Test
    @DisplayName("ID로 삭제 후, 조회가 되지 않는지 테스트")
    void delete() {
        Answer expected = saveNewDefaultAnswer();
        answers.deleteById(expected.getId());

        assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(
            () -> answers.findById(expected.getId()).get()
        );
    }

    @Test
    void deleteBy() {
        Answer answer = saveNewDefaultAnswer();
        User writer = answer.getContents().getWriter();

        DeleteHistory deleteHistory = answer.deleteBy(writer);

        assertThat(deleteHistory).isEqualTo(new DeleteHistory(ContentType.ANSWER, answer.getId(), writer));
        assertThat(answer.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("답변자와 로그인 유저가 다를 때 예외 발생")
    void deleteByInvalidUser() {
        Answer answer = saveNewDefaultAnswer();
        User invalidUser = users.save(new User("minseoklim", "1234", "임민석", "mslim@slipp.net"));

        assertThatThrownBy(() -> answer.deleteBy(invalidUser))
            .isInstanceOf(CannotDeleteException.class);
    }

    private Answer saveNewDefaultAnswer() {
        User user = users.save(new User("javajigi", "password", "name", "javajigi@slipp.net"));
        Answer defaultAnswer = new Answer(user, "Answers Contents1");
        return answers.save(defaultAnswer);
    }
}
