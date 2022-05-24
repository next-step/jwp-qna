package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AnswersTest {

    public static final Answers answers = new Answers(Collections.singletonList(AnswerTest.A1));
    public static final Answers answers2 = new Answers(Arrays.asList(AnswerTest.A1, AnswerTest.A2));

    @Test
    @DisplayName("isOwner의 결과 답변 작성자와 삭제 유저의 일치 여부를 판단하여 반환한다")
    void isOwner() {
        // given & when
        boolean actual = answers.isOwner(UserTest.JAVAJIGI);

        // then
        assertThat(actual).isTrue();
    }

    @Test
    @DisplayName("deleteAnswers의 결과 삭제 히스토리가 반환된다")
    void deleteAnswers() {
        // given & when
        List<DeleteHistory> actual = answers2.deleteAnswers();

        // then
        assertThat(actual).hasSize(2);
    }
}
