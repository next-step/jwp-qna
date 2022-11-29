package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.NotFoundException;
import qna.UnAuthorizedException;

@DataJpaTest
public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Autowired
    AnswerRepository answers;

    @Autowired
    QuestionRepository questions;

    @Autowired
    UserRepository users;

    User user;
    Question question;
    Answer answer;

    @BeforeEach
    void setUp() {
        user = users.save(new User("userId", "password", "name", "email"));
        question = questions.save(new Question("title", "contents"));
        answer = answers.save(new Answer(user, question, "contents"));
    }

    @Test
    @DisplayName("save 테스트")
    void saveTest() {
        assertThat(answer.getId()).isNotNull();
        assertThat(answer.getContents()).isEqualTo("contents");
    }

    @Test
    @DisplayName("save 후 findById테스트")
    void saveFindByIdTest() {
        Optional<Answer> ans = answers.findById(answer.getId());
        assertThat(ans.isPresent()).isTrue();
        assertThat(ans.get()).isEqualTo(answer);
    }

    @Test
    @DisplayName("save 후 delete 테스트")
    void saveDeleteTest() {
        answers.delete(answer);
        Optional<Answer> maybe = answers.findById(answer.getId());
        assertThat(maybe.isPresent()).isFalse();
    }

    @Test
    @DisplayName("작성자가 없이 답변을 남길 수 없다")
    void noWriterAnswer() {
        assertThatThrownBy(() -> new Answer(null, new Question("Hello", "contents"), "contents"))
            .isInstanceOf(UnAuthorizedException.class);
    }

    @Test
    @DisplayName("질문없이 답변을 남길 수 없다")
    void noAnswerNoQuestion() {
        assertThatThrownBy(() ->
            new Answer(new User("userId", "password", "sj", "mail"), null, "Jun"))
            .isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("답변작성자인지 아닌지 확인한다")
    void checkAnswerWriter() {
        User other = users.save(new User("userId1", "password", "name", "esesmail"));
        assertThat(answer.isOwner(user)).isTrue();
        assertThat(answer.isOwner(other)).isFalse();
    }
}

