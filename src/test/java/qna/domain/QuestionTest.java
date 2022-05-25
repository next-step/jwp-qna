package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Test
    @DisplayName("질문 작성자 동일한지 확인")
    void isOwner() {
        assertAll(
                () -> assertThat(Q1.isOwner(UserTest.JAVAJIGI)).isTrue(),
                () -> assertThat(Q2.isOwner(UserTest.SANJIGI)).isTrue()
        );
    }
    
    @Test
    @DisplayName("답변 추가 후 질문자 id로 변경되었지 확인")
    void addAnswer() {
        Answer answer = AnswerTest.A2;
        Question question = Q1;
        question.addAnswer(answer);

        assertThat(answer.getQuestionId()).isEqualTo(question.getId());
    }
}
