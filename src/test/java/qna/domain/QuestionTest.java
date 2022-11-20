package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static qna.domain.DomainTestFactory.*;

public class QuestionTest {

    @Test
    @DisplayName("isOwner : 작성자가 일치하면 true를 리턴한다")
    public void isOwnerTest() {
        User user = createUser("DEVELOPYO");
        Question question = createQuestion().writeBy(createUser("DEVELOPYO"));
        assertThat(question.isOwner(user)).isTrue();
    }

    @Test
    @DisplayName("toQuestion : 답변 추가시 지정한 질문글에 add 된다")
    public void addAnswerTest() {
        User user = createUser("DEVELOPYO");
        Question question = createQuestion();
        Answer answer = createAnswer(user, question);
        question.addAnswer(answer);

        assertThat(answer.getQuestion()).isEqualTo(question);
    }

    @Test
    @DisplayName("질문 삭제시 질문과 삭제되지 않은 답변의 삭제 히스토리만 리턴한다")
    public void deleteTest1() throws CannotDeleteException {
        User user = createUser("DEVELOPYO");
        Question question = createQuestion().writeBy(user);
        Answer deletedAnswer = createAnswer(user, question);
        Answer deletedAnswer2 = createAnswer(user, question);
        deletedAnswer.delete(user);
        deletedAnswer2.delete(user);
        DeleteHistories deleteHistories = question.delete(createUser("DEVELOPYO"));

        List<DeleteHistory> questionDeletedHistories = deleteHistories.getDeleteHistories().stream()
                .filter(deleteHistory -> deleteHistory.getContentType().equals(ContentType.QUESTION))
                .collect(Collectors.toList());
        assertThat(questionDeletedHistories).hasSize(1);
        List<DeleteHistory> answerDeletedHistories = deleteHistories.getDeleteHistories().stream()
                .filter(deleteHistory -> deleteHistory.getContentType().equals(ContentType.ANSWER))
                .collect(Collectors.toList());
        assertThat(answerDeletedHistories).hasSize(0);
    }

    @Test
    @DisplayName("질문 삭제시 질문과 질문의 답변을 모두 삭제한 히스토리를 리턴한다")
    public void deleteTest2() throws CannotDeleteException {
        User user = createUser("DEVELOPYO");
        Question question = createQuestion().writeBy(user);
        createAnswer(user, question);
        DeleteHistories deleteHistories = question.delete(createUser("DEVELOPYO"));
        List<DeleteHistory> questionDeletedHistories = deleteHistories.getDeleteHistories().stream()
                .filter(deleteHistory -> deleteHistory.getContentType().equals(ContentType.QUESTION))
                .collect(Collectors.toList());
        assertThat(questionDeletedHistories).hasSize(1);
        List<DeleteHistory> answerDeletedHistories = deleteHistories.getDeleteHistories().stream()
                .filter(deleteHistory -> deleteHistory.getContentType().equals(ContentType.ANSWER))
                .collect(Collectors.toList());
        assertThat(answerDeletedHistories).hasSize(1);
    }
}
