package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Test
    @DisplayName("로그인 사용자와 질문한 사람이 같지 않으면 에러를 반환한다.")
    void no_owner_delete_question_test() {
        assertThatThrownBy(() -> {
            Q1.deleteAll(UserTest.SANJIGI);
        }).isInstanceOf(CannotDeleteException.class)
                .hasMessageContainingAll("질문을 삭제할 권한이 없습니다.");
    }

    @Test
    @DisplayName("질문을 삭제하면 삭제 여부는 true 가 되고, 삭제히스토리를 생성한다.")
    void delete_question_test() throws CannotDeleteException {
        DeleteHistory actual = Q1.delete(UserTest.JAVAJIGI);
        DeleteHistory expected = new DeleteHistory(ContentType.QUESTION,
                Q1.getId(),
                Q1.getWriter(),
                Q1.getUpdatedAt());
        assertAll(
                () -> assertThat(actual).isEqualTo(expected),
                () -> assertThat(Q1.isDeleted()).isTrue()
        );
    }
}
