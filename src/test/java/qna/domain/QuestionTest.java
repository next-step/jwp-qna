package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static qna.domain.AnswerTest.A1;
import static qna.domain.UserTest.JAVAJIGI;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @DisplayName("isOwner 확인")
    @Test
    void isOwner() {
        final boolean actual = Q1.isOwner(JAVAJIGI);

        assertThat(actual).isTrue();
    }

    @DisplayName("Question 삭제시 DeleteHistory 반환")
    @Test
    void delete() {
        final Question question = new Question("title3", "contents3").writeBy(JAVAJIGI);
        final Answer answer = new Answer(JAVAJIGI, question, "Answers Contents3");

        question.addAnswer(answer);
        List<DeleteHistory> actual = question.delete(JAVAJIGI);

        assertThat(actual).hasSize(2);
    }
}
