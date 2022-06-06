package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.CannotDeleteException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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

    @Test
    void 답변_삭제() throws CannotDeleteException {
        DeleteHistory deleteHistory = A1.delete(UserTest.JAVAJIGI);
        assertThat(deleteHistory).isNotNull();

    }

    @Test
    void 답변_삭제_실패() {
        assertThatThrownBy(() -> A1.delete(UserTest.SANJIGI)).isInstanceOf(CannotDeleteException.class);
    }

    @Test
    void 답변_삭제_실패2(){
        A1.setDeleted(true);
        assertThatThrownBy(() -> A1.delete(UserTest.JAVAJIGI)).isInstanceOf(CannotDeleteException.class);
    }
}

