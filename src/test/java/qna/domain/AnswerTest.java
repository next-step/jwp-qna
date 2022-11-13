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
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Test
    @DisplayName("isOwner 테스트 : 답변 작성자가 유저와 같은지 테스트")
    public void isOwnerTest() {
        User answerUser = createUser("DEVELOPYO");
        Question question = createQuestion().writeBy(createUser("TESTER"));
        Answer answer = createAnswer(answerUser, question);

        assertThat(answer.isOwner(answerUser)).isTrue();
    }

    @Test
    @DisplayName("answer 생성자 테스트 : 작성자 없을 경우 예외 처리 테스트")
    public void createAnswerTest() {
        assertThatThrownBy(() -> new Answer(null, createQuestion(), "contents")).isInstanceOf(UnAuthorizedException.class);
    }

    @Test
    @DisplayName("answer 생성자 테스트 : 질문글 없을 경우 예외 처리 테스트")
    public void createAnswerTest2() {
        assertThatThrownBy(() -> new Answer(createUser("DEVELOPYO"), null, "contents")).isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("작성자가 일치하지 않을 경우 삭제 테스트")
    public void deleteByOtherUserTest(){
        User user = createUser("DEVELOPYO");
        Question question = createQuestion();
        Answer answer = createAnswer(user, question);
        assertThatThrownBy(() -> answer.delete(createUser("OTHERUSER"))).isInstanceOf(CannotDeleteException.class);
    }

    @Test
    @DisplayName("삭제되지않은 삭제글을 삭제한 경우 삭제히스토리 리턴 테스트")
    public void deleteReturnTest(){
        User user = createUser("DEVELOPYO");
        Question question = createQuestion();
        Answer answer = createAnswer(user, question);

        assertThat(answer.delete(user)).isPresent();
    }

    @Test
    @DisplayName("이미 삭제된 삭제글을 삭제한 경우 삭제히스토리 리턴 테스트")
    public void deleteReturnTest2(){
        User user = createUser("DEVELOPYO");
        Question question = createQuestion();
        Answer answer = createAnswer(user, question);
        answer.setDeleted(true);

        assertThat(answer.delete(user)).isNotPresent();
    }
}
