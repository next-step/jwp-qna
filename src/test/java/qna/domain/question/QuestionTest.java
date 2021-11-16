package qna.domain.question;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.domain.answer.Answer;
import qna.domain.user.User;
import qna.exception.CannotDeleteException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class QuestionTest {
    private User javajigi;
    private User sanjigi;

    @BeforeEach
    void setUp() {
        javajigi = User.crate("javajigi", "password", "user1", "javajigi@slipp.net");
        sanjigi = User.crate("sanjigi", "password", "user2", "sanjigi@slipp.net");
    }

    @Test
    @DisplayName("질문을 삭제하면 상태를 삭제 상태로 변경한다.")
    void deleteQuestion() throws CannotDeleteException {
        //given
        Question question = Question.create("title1", "contents1", javajigi);

        //when
        question.delete(javajigi);

        //then
        assertThat(question.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("질문 작성자가 아닌 경우 질문을 삭제할 수 없다.")
    void validateDeletable() {
        Question question = Question.create("title1", "contents1", javajigi);

        //when //then
        assertThrows(CannotDeleteException.class,
                () -> question.delete(sanjigi));
    }

    @Test
    @DisplayName("다른 사용자의 답변이 있는 경우 삭제할 수 없다.")
    void validateOtherAnswer() {
        //given
        Question question = Question.create("title1", "contents1", javajigi);

        //when
        Answer answer = Answer.create(question, sanjigi, "Answers Contents1");
        question.addAnswer(answer);

        //then
        assertThrows(CannotDeleteException.class,
                () -> question.delete(sanjigi));
    }

    @Test
    @DisplayName("질문자와 답변 글의 모든 답변자가 같은 경우 삭제할 수 있다.")
    void deleteQuestionWithAnswers() throws CannotDeleteException {
        //given
        Question question = Question.create("title1", "contents1", javajigi);
        question.addAnswer(Answer.create(question, javajigi, "Answers Contents1"));
        question.addAnswer(Answer.create(question, javajigi, "Answers Contents2"));

        //when
        question.delete(javajigi);

        //then
        assertThat(question.isDeleted()).isTrue();
    }
}
