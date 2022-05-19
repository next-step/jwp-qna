package qna.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    public static Question createQuestion(User writer, boolean deleted) {
        return createQuestion(null, writer, deleted);
    }

    public static Question createQuestion(Long id, User writer, boolean deleted) {
        Question question = new Question(id, "title1", "contents1").writeBy(writer);
        question.setDeleted(deleted);
        return question;
    }

    @Test
    void delete_성공() {
        Question question = createQuestion(1L, UserTest.JAVAJIGI, false);

        assertThat(question.delete()).containsExactly(
                new DeleteHistory(ContentType.QUESTION, question.getId(), UserTest.JAVAJIGI, LocalDateTime.now())
        );
        assertThat(question.isDeleted()).isTrue();
    }

    @Test
    void delete_성공_질문자_답변자_같음() {
        Question question = createQuestion(1L, UserTest.JAVAJIGI, false);
        Answer answer = createAnswer(2L, question, false);
        question.addAnswer(answer);

        assertThat(question.delete()).containsExactly(
                new DeleteHistory(ContentType.QUESTION, question.getId(), UserTest.JAVAJIGI, LocalDateTime.now()),
                new DeleteHistory(ContentType.ANSWER, answer.getId(), UserTest.JAVAJIGI, LocalDateTime.now())
        );
        assertThat(question.isDeleted()).isTrue();
        assertThat(answer.isDeleted()).isTrue();
    }

    @Test
    void validateDelete_성공() throws CannotDeleteException {
        Question question = createQuestion(1L, UserTest.JAVAJIGI, false);

        question.addAnswer(new Answer(UserTest.JAVAJIGI, question, "테스트1"));
        question.addAnswer(new Answer(UserTest.JAVAJIGI, question, "테스트2"));

        question.validateDelete(UserTest.JAVAJIGI);
    }

    @Test
    void validateDelete_다른사람이_쓴_글은_삭제할_수_없다() {
        Question question = createQuestion(1L, UserTest.JAVAJIGI, false);

        Assertions.assertThatExceptionOfType(CannotDeleteException.class)
                .isThrownBy(() -> question.validateDelete(UserTest.SANJIGI));
    }

    @Test
    void validateDelete_답변_중_다른_사람이_쓴_글은_삭제할_수_없다() {
        Question question = createQuestion(1L, UserTest.JAVAJIGI, false);

        question.addAnswer(new Answer(UserTest.SANJIGI, question, "테스트"));

        Assertions.assertThatExceptionOfType(CannotDeleteException.class)
                .isThrownBy(() -> question.validateDelete(UserTest.JAVAJIGI));

    }

    @Test
    void getNotDeletedAnswers() {
        Question question = createQuestion(1L, UserTest.JAVAJIGI, false);
        Answer answer = createAnswer(2L, question, false);
        Answer deletedAnswer = createAnswer(3L, question, true);

        question.addAnswer(answer);
        question.addAnswer(deletedAnswer);

        assertThat(question.getNotDeletedAnswers()).containsExactly(answer);
    }

    private Answer createAnswer(Long id, Question question, boolean deleted) {
        Answer answer = new Answer(id, UserTest.JAVAJIGI, question, "answer content1");
        answer.setDeleted(deleted);
        return answer;
    }

}
