package qna.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Test
    void deleteQuestion() {
        //when
        DeleteHistory actual = Q1.delete();

        //then
        assertThat(actual.getDeletedBy()).isSameAs(UserTest.JAVAJIGI);
    }
}
