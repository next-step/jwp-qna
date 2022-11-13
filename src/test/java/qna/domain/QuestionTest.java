package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static qna.domain.DomainTestFactory.*;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Test
    @DisplayName("작성자가 일치하는지 테스트")
    public void isOwnerTest() {
        User user = createUser("DEVELOPYO");
        Question question = createQuestion().writeBy(createUser("DEVELOPYO"));
        assertThat(question.isOwner(user)).isTrue();
    }

    @Test
    @DisplayName("질문글에 추가한 답변의 질문글 값이 질문글과 동일 객체인지 테스트")
    public void addAnswerTest() {
        User user = createUser("DEVELOPYO");
        Question question = createQuestion();
        Answer answer = createAnswer(user, question);
        question.addAnswer(answer);

        assertThat(answer.getQuestion()).isEqualTo(question);
    }

    @Test
    @DisplayName("답변 글쓴이들이 질문글 작성자와 동일한지 테스트 : 다른경우 false 리턴")
    public void isAnswersOwnerFalse() {
        User user = createUser("DEVELOPYO");
        Question question = createQuestion().writeBy(user);
        question.addAnswer(createAnswer(question.getWriter(), question));
        question.addAnswer(createAnswer(createUser("JAVAJIGI"), question));

        assertThat(question.getAnswers().isOwner(question.getWriter())).isFalse();
    }

    @Test
    @DisplayName("답변 글쓴이들이 질문글 작성자와 동일한지 테스트 : 같은 경우 true 리턴")
    public void isAnswersOwnerTrue() {
        User user = createUser("DEVELOPYO");
        Question question = createQuestion().writeBy(user);
        question.addAnswer(createAnswer(question.getWriter(), question));
        question.addAnswer(createAnswer(createUser("DEVELOPYO"), question));
        assertThat(question.getAnswers().isOwner(question.getWriter())).isTrue();
    }

    @Test
    @DisplayName("삭제시 질문글과 모든 답변 삭제히스토리 반환 테스트 : 질문글 1개에 답변글 2개(2개 모두 삭제된상태)")
    public void deleteTest1() throws CannotDeleteException {
        User user = createUser("DEVELOPYO");
        Question question = createQuestion().writeBy(user);
        Answer deletedAnswer = new Answer(user, question, "testcontents");
        Answer deletedAnswer2 = new Answer(user, question, "testcontents");
        deletedAnswer.setDeleted(true);
        deletedAnswer2.setDeleted(true);
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
    @DisplayName("삭제시 질문글과 모든 답변 삭제히스토리 반환 테스트 : 질문 글 1개에 답변글 2개")
    public void deleteTest2() throws CannotDeleteException {
        User user = createUser("DEVELOPYO");
        Question question = createQuestion().writeBy(user);
        createAnswer(user, question);
        createAnswer(user, question);
        DeleteHistories deleteHistories = question.delete(createUser("DEVELOPYO"));

        List<DeleteHistory> questionDeletedHistories = deleteHistories.getDeleteHistories().stream()
                .filter(deleteHistory -> deleteHistory.getContentType().equals(ContentType.QUESTION))
                .collect(Collectors.toList());
        assertThat(questionDeletedHistories).hasSize(1);
        List<DeleteHistory> answerDeletedHistories = deleteHistories.getDeleteHistories().stream()
                .filter(deleteHistory -> deleteHistory.getContentType().equals(ContentType.ANSWER))
                .collect(Collectors.toList());
        assertThat(answerDeletedHistories).hasSize(2);
    }
}
