package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;
import qna.UnAuthorizedException;
import qna.fixture.TestAnswerFactory;
import qna.fixture.TestQuestionFactory;
import qna.fixture.TestUserFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);

    @DisplayName("질문은 삭제될 수 있다")
    @Test
    void delete_test() throws CannotDeleteException {
        User writer = TestUserFactory.create("서정국");
        Question question = TestQuestionFactory.create(writer);

        question.delete(writer);

        assertThat(question.isDeleted()).isTrue();
    }

    @DisplayName("질문이 삭제되면 답변도 삭제된다")
    @Test
    void delete_with_answer_test() throws CannotDeleteException {
        User writer = TestUserFactory.create("서정국");
        Question question = TestQuestionFactory.create(writer);
        Answer answer = TestAnswerFactory.create(writer, question);

        question.delete(writer);

        assertThat(question.isDeleted()).isTrue();
        assertThat(answer.isDeleted()).isTrue();
    }

    @DisplayName("질문이 삭제되면 삭제이력이 반환된다")
    @Test
    void delete_history_test() throws CannotDeleteException {
        User writer = TestUserFactory.create("서정국");
        Question question = TestQuestionFactory.create(writer);

        assertThat(question.delete(writer)).isInstanceOf(DeleteHistories.class);
    }
}
