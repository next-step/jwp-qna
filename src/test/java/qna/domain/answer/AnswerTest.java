package qna.domain.answer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.domain.question.Question;
import qna.domain.user.User;
import qna.exception.CannotDeleteException;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AnswerTest {
    private User javajigi;
    private User sanjigi;

    @BeforeEach
    void setUp() {
        javajigi = User.crate("javajigi", "password", "user1", "javajigi@slipp.net");
        sanjigi = User.crate("sanjigi", "password", "user2", "sanjigi@slipp.net");
    }

    @Test
    @DisplayName("질문자와 답변자가 다른 경우 답변을 삭제할 수 없다.")
    void changeDeleteState() throws CannotDeleteException {
        //given
        Question question = Question.create("title1", "contents1", javajigi);
        Answer answer = Answer.create(question, javajigi, "Answers Contents1");

        //when
        answer.changeDeleteState(javajigi);

        //then
        assertThat(answer.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("질문자와 답변자가 다른 경우 답변을 삭제할 수 없다.")
    void validateAnswer() throws CannotDeleteException {
        //given
        Question question = Question.create("title1", "contents1", javajigi);
        Answer answer = Answer.create(question, sanjigi, "Answers Contents1");

        //when //then
        assertThrows(CannotDeleteException.class,
                () -> answer.changeDeleteState(javajigi));
    }
}
