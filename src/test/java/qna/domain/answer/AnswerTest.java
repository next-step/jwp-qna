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
        javajigi = new User(1L, "javajigi", "password", "user1", "javajigi@slipp.net");
        sanjigi = new User(1L, "sanjigi", "password", "user2", "sanjigi@slipp.net");
    }

    @Test
    @DisplayName("질문자와 답변자가 다른 경우 답변을 삭제할 수 없다.")
    void changeDeleteState() throws CannotDeleteException {
        //given
        Question question = new Question(1L, "title1", "contents1").writeBy(javajigi);
        Answer answer = new Answer(1L, question, javajigi, "Answers Contents1");

        //when
        answer.changeDeleteState(javajigi);

        //then
        assertThat(answer.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("질문자와 답변자가 다른 경우 답변을 삭제할 수 없다.")
    void validateAnswer() throws CannotDeleteException {
        //given
        Question question = new Question(1L, "title1", "contents1").writeBy(javajigi);
        Answer answer = new Answer(1L, question, sanjigi, "Answers Contents1");

        //when //then
        assertThrows(CannotDeleteException.class,
                () -> answer.changeDeleteState(javajigi));
    }
}
