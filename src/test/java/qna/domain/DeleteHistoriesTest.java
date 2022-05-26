package qna.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class DeleteHistoriesTest {
    @Test
    void Question_으로_DeleteHistories_를_생성할_수_있다() {
        assertDoesNotThrow(() -> new DeleteHistories(new Question()));
    }

    @Test
    void DeleteHistories_는_사이즈가_같고_구성요소를_모두_포함하고_있으면_같은_객체로_본다() {
        //given
        DeleteHistory d1 = new DeleteHistory(ContentType.QUESTION, 0L, User.GUEST_USER, LocalDateTime.now());
        DeleteHistory d2 = new DeleteHistory(ContentType.ANSWER, 0L, User.GUEST_USER, LocalDateTime.now());

        //when
        DeleteHistories deleteHistories = new DeleteHistories(Arrays.asList(d2, d1));

        //then
        assertThat(deleteHistories).isEqualTo(new DeleteHistories(Arrays.asList(d1, d2)));
    }

    @Test
    void DeleteHistories_는_Iterable_객체이다() {
        //given
        DeleteHistory d1 = new DeleteHistory(ContentType.QUESTION, 0L, User.GUEST_USER, LocalDateTime.now());
        DeleteHistory d2 = new DeleteHistory(ContentType.ANSWER, 0L, User.GUEST_USER, LocalDateTime.now());

        //when
        DeleteHistories deleteHistories = new DeleteHistories(Arrays.asList(d1, d2));

        //then
        assertThat(deleteHistories).containsExactlyInAnyOrder(d2, d1);
    }

}
