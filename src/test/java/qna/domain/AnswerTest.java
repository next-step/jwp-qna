package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class AnswerTest {


    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Test
    void 답변_작성자_확인() {
        assertThat(A1.isOwner(UserTest.JAVAJIGI)).isTrue();
    }

    @Test
    void 답변_작성자_확인2() {
        assertThat(A2.isOwner(UserTest.JAVAJIGI)).isFalse();
    }

    @Test
    void 답변_추가() {
        A1.toQuestion(QuestionTest.Q1);

        assertThat(A1.getQuestion()).isEqualTo(QuestionTest.Q1);

    }

}
