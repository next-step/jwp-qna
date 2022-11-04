package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class QuestionTest {

    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @DisplayName("질문의 작성자를 변경할수 있다")
    @Test
    void writeBy_test() {
        Question question = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);

        question.writeBy(UserTest.SANJIGI);

        assertEquals(question.getWriterId(), UserTest.SANJIGI.getId());
    }

    @DisplayName("질문의 작성자를 확인할 수 있다")
    @Test
    void isOwner_test() {
        Question question = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
        assertTrue(question.isOwner(UserTest.JAVAJIGI));
    }

    @DisplayName("질문의 답변을 변경할 수 있다")
    @Test
    void addAnswer_test() {
        // given
        Question question1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
        Question question2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);
        Answer answer = new Answer(UserTest.JAVAJIGI, question1, "Answers Contents1");
        // when
        question2.addAnswer(answer);
        // then
        assertThat(answer.getQuestionId()).isEqualTo(question2.getId());
    }
}
