package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    private User user;
    private User otherUser;
    private Question question;
    private Answer answer;

    @BeforeEach
    void setUp() {
        //Given
        user = new User("AppleMango", "password", "name", "email@email.com");
        otherUser = new User("Conan", "password", "name", "conan@email.com");
        question = new Question("JPA Question", "Question Contents").writeBy(user);
        answer = new Answer(user, question, "First Answer Contents");
    }

    @DisplayName("답변 작성자와 삭제자가 다르면 exception을 던진다")
    @Test
    void throw_exception_when_other_user_is_deleting() {
        //When + Then
        assertThatThrownBy(() -> answer.deleteBy(otherUser))
                .isInstanceOf(CannotDeleteException.class);
    }
}
