package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;
import qna.NotFoundException;
import qna.UnAuthorizedException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static qna.domain.DomainTestFactory.*;

public class AnswerTest {
    @Test
    @DisplayName("isOwner : 답변작성자와 유저가 같을 경우 true 리턴")
    public void isOwnerTest() {
        User answerUser = createUser("DEVELOPYO");
        Question question = createQuestion().writeBy(createUser("TESTER"));
        Answer answer = createAnswer(answerUser, question);

        assertThat(answer.isOwner(answerUser)).isTrue();
    }

    @Test
    @DisplayName("answer 생성자 테스트 : 작성자 없을 경우 예외를 던진다")
    public void createAnswerTest() {
        assertThatThrownBy(() -> createAnswer(null, createQuestion()))
                .isInstanceOf(UnAuthorizedException.class);
    }

    @Test
    @DisplayName("answer 생성자 테스트 : 질문글 없을 경우 예외를 던진다")
    public void createAnswerTest2() {
        assertThatThrownBy(() -> createAnswer(createUser("DEVELOPYO"), null))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("답변 작성자가 일치하지 않을 경우 삭제시 예외를 던진다")
    public void deleteByOtherUserTest() {
        User user = createUser("DEVELOPYO");
        Question question = createQuestion();
        Answer answer = createAnswer(user, question);
        assertThatThrownBy(() -> answer.delete(createUser("OTHERUSER"))).isInstanceOf(CannotDeleteException.class);
    }

    @Test
    @DisplayName("삭제되지않은 삭제글을 삭제한 경우 삭제히스토리를 리턴한다")
    public void deleteReturnTest() {
        User user = createUser("DEVELOPYO");
        Question question = createQuestion();
        Answer answer = createAnswer(user, question);

        assertThat(answer.delete(user).getContentType()).isEqualTo(ContentType.ANSWER);
    }

    @Test
    @DisplayName("이미 삭제된 삭제글을 삭제한 경우 예외를 던진다")
    public void deleteReturnTest2() {
        User user = createUser("DEVELOPYO");
        Question question = createQuestion();
        Answer answer = createAnswer(user, question);
        answer.delete(user);
        assertThatThrownBy(() -> answer.delete(user))
                .isInstanceOf(CannotDeleteException.class);
    }

    @Test
    @DisplayName("답변 삭제시 질문의 삭제리스트에서도 삭제된다")
    public void deleteTest() {
        User user = createUser("DEVELOPYO");
        Question question = createQuestion();
        Answer answer = createAnswer(user, question);
        answer.delete(user);
        assertThat(question.getAnswers().getAnswers()).isEmpty();
    }
}
