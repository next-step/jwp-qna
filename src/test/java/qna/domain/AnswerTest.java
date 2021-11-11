package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import static org.junit.jupiter.api.Assertions.assertThrows;

class AnswerTest {

    @Test
    @DisplayName("질문자와 답변자가 다른 경우 답변을 삭제할 수 없다.")
    void validateAnswer() throws CannotDeleteException {
        //given
        User user1 = new User(1L, "page", "pa**@word", "name", "page@abc.kr");
        Question question = new Question(1L, "title1", "contents1").writeBy(user1);

        User user2 = new User(2L, "prologue", "pa**@word", "name", "prologue@abc.kr");
        Answer answer = new Answer(1L, question, user2, "Answers Contents1");

        //when //then
        assertThrows(CannotDeleteException.class,
                () -> answer.changeStateDelete(user1));
    }
}
