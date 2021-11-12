package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class QuestionTest {

    @Test
    @DisplayName("질문을 삭제하면 상태를 삭제 상태로 변경한다.")
    void changeDeleteState() throws CannotDeleteException {
        //given
        User user = new User(1L, "page", "pa**@word", "name", "page@abc.kr");
        Question question = new Question(1L, "title1", "contents1").writeBy(user);

        //when
        question.changeDeleteState(user);

        //then
        assertThat(question.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("질문 작성자가 아닌 경우 질문을 삭제할 수 없다.")
    void validateDeletable() {
        User user = new User(1L, "page", "pa**@word", "name", "page@abc.kr");
        User other = new User(2L, "prologue", "pa**@word", "name", "prologue@abc.kr");
        Question question = new Question(1L, "title1", "contents1").writeBy(user);

        //when //then
        assertThrows(CannotDeleteException.class,
                () -> question.changeDeleteState(other));
    }

    @Test
    @DisplayName("다른 사용자의 답변이 있는 경우 삭제할 수 없다.")
    void validateOtherAnswer() {
        //given
        User user = new User(1L, "page", "pa**@word", "name", "page@abc.kr");
        User other = new User(2L, "prologue", "pa**@word", "name", "prologue@abc.kr");
        Question question = new Question(1L, "title1", "contents1").writeBy(user);

        //when
        Answer answer = new Answer(1L, question, other, "Answers Contents1");
        question.addAnswer(answer);

        //then
        assertThrows(CannotDeleteException.class,
                () -> question.changeDeleteState(other));
    }

    @Test
    @DisplayName("질문자와 답변 글의 모든 답변자가 같은 경우 삭제할 수 있다.")
    void deleteQuestionWithAnswers() throws CannotDeleteException {
        //given
        User user = new User(1L, "page", "pa**@word", "name", "page@abc.kr");
        Question question = new Question(1L, "title1", "contents1").writeBy(user);
        question.addAnswer(new Answer(1L, question, user, "Answers Contents1"));
        question.addAnswer(new Answer(2L, question, user, "Answers Contents2"));

        //when
        question.changeDeleteState(user);

        //then
        assertThat(question.isDeleted()).isTrue();
    }
}
