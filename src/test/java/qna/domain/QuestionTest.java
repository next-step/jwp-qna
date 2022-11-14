package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.exception.CannotDeleteException;
import qna.message.QuestionMessage;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class QuestionTest {

    private User writer;

    public static Question questionSample(Long id, User writer) {
        return new Question(id, "title1", "Contents").writeBy(writer);
    }

    @BeforeEach
    void setUp() {
        writer = UserTest.userSample(1L);
    }

    @Test
    @DisplayName("질문을 생성한다")
    void create_question_test() {
        Question question = questionSample(1L, writer);
        assertThat(question).isEqualTo(new Question(1L, "title1", "Contents").writeBy(writer));
    }

    @Test
    @DisplayName("질문 생성시 제목이 누락되면 [IllegalArgumentException] 예외처리한다")
    void create_question_without_title_test() {
        assertThatThrownBy(() -> new Question(null, "Contents"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(QuestionMessage.ERROR_TITLE_SHOULD_BE_NOT_NULL.message());
    }

    @Test
    @DisplayName("질문 생성시 내용이 누락되면 [IllegalArgumentException] 예외처리한다")
    void create_question_without_contents_test() {
        assertThatThrownBy(() -> new Question("title", null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(QuestionMessage.ERROR_CONTENTS_SHOULD_BE_NOT_NULL.message());
    }

    @Test
    @DisplayName("주어진 owner와 질문 owner를 비교하여 동일 여부를 반환한다 ")
    void is_match_owner_with_other_owner() {
        User owner = new User(1L, "shshon", "password", "손상훈", "shshon@naver.com");
        Question question = new Question(1L, "title1", "Contents").writeBy(writer);

        boolean isEqualOwner = question.isOwner(owner);

        assertThat(isEqualOwner).isTrue();
    }

    @Test
    @DisplayName("질문을 삭제하면 논리 삭제 처리한다")
    void delete_question_test() throws CannotDeleteException {
        Question question = questionSample(1L, writer);
        question.delete(writer);
        assertThat(question.isDeleted()).isTrue();
    }
}
