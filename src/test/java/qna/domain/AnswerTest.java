package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Test
    @DisplayName("답변에 질문 연결")
    void toQuestion() {
        //given
        User writer = new User(null, "sangjae", "password", "name", "javajigi@slipp.net");
        Question question = new Question("title1", "contents1").writeBy(writer);
        Answer answer = new Answer(writer, question, "Answers Contents");

        //when
        answer.toQuestion(question);
        answer.toQuestion(question);
        answer.toQuestion(question);

        //then
        assertAll(
                () -> assertThat(question.getAnswers().size()).isEqualTo(1),
                () -> assertThat(question.getAnswers().contains(answer)).isTrue(),
                () -> assertThat(answer.getQuestion()).isSameAs(question)
        );
    }

    @Test
    @DisplayName("글쓴이와 답변자가 다른 경우 삭제 에러")
    void delete_error() {
        //given
        User writer = new User(1L, "sangjae", "password", "name", "javajigi@slipp.net");
        User other = new User(2L, "coco", "password", "coco", "coco@slipp.net");
        Question question = new Question("title1", "contents1").writeBy(writer);
        Answer answer = new Answer(writer, question, "Answers Contents");

        //except
        assertThatThrownBy(()-> answer.delete(other)).isInstanceOf(CannotDeleteException.class);
    }

    @Test
    @DisplayName("글쓴이와 답변자가 같은 경우 삭제 가능하며 DeleteHistory 반환")
    void delete_success() throws CannotDeleteException {
        //given
        User writer = new User(1L, "sangjae", "password", "name", "javajigi@slipp.net");
        Question question = new Question("title1", "contents1").writeBy(writer);
        Answer answer = new Answer(writer, question, "Answers Contents");

        //except
        assertThat(answer.delete(writer)).isInstanceOf(DeleteHistory.class);
    }

}
