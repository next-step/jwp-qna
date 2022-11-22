package qna.domain;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Test
    @DisplayName("질문 삭제시 작성자가 다르면 삭제 불가")
    public void question_delete_exception_writer_not_same() {
        Question question = Q1.writeBy(UserTest.JAVAJIGI);
        assertThatThrownBy(() -> question.delete(UserTest.SANJIGI))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("질문자가 같은 경우 가능")
    public void question_delete() {
        Question question = Q1.writeBy(UserTest.JAVAJIGI);
        question.delete(UserTest.JAVAJIGI);
        assertThat(question.isDeleted()).isTrue();
    }

}