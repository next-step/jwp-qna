package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
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
                () -> assertThat(question.getAnswers()).hasSize(1),
                () -> assertThat(question.getAnswers().get(0)).isSameAs(answer),
                () -> assertThat(answer.getQuestion()).isSameAs(question)
        );
    }

}
