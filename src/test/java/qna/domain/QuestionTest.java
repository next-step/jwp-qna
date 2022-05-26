package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Test
    @DisplayName("질문의 작성자가 맞는지 확인한다.")
    void isOwner() {
        assertThat(Q1.isOwner(UserTest.JAVAJIGI))
                .isTrue();
        assertThat(Q2.isOwner(UserTest.SANJIGI))
                .isTrue();
    }

    @Test
    @DisplayName("질문에 답변을 추가한다.")
    void addAnswer() {
        Answer answer = new Answer(UserTest.JAVAJIGI, Q2, "new answer");
        Q2.addAnswer(answer);

        assertThat(answer.getQuestionId())
                .isEqualTo(Q2.getId());
    }

}
