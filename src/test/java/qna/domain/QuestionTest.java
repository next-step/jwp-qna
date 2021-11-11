package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

public class QuestionTest {

    @Test
    @DisplayName("자신의 질문만 삭제할 수 있다")
    void delete1() throws CannotDeleteException {
        User writer = new User(1L, "1", "password", "user1", "test@email.com");
        TestDummy.QUESTION1.setWriter(writer);

        Question deletedQuestion = TestDummy.QUESTION1.delete(writer);

        assertThat(deletedQuestion.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("자신의 질문이 아닌 경우 삭제 권한이 없다.")
    void delete2() {
        User writer = new User(1L, "1", "password", "user1", "test@email.com");
        TestDummy.QUESTION1.setWriter(writer);

        User other = new User(2L, "2", "password", "user2", "test2@email.com");

        assertThatExceptionOfType(CannotDeleteException.class).isThrownBy(
                () -> TestDummy.QUESTION1.delete(other))
            .withMessage("질문을 삭제할 권한이 없습니다.");
    }

}
