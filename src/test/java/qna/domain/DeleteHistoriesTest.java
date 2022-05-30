package qna.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class DeleteHistoriesTest {
    @Test
    void DeleteHistories_를_생성할_수_있다() {
        assertDoesNotThrow(() -> new DeleteHistories());
    }

    @Test
    void DeleteHistories_는_사이즈가_같고_구성요소를_모두_포함하고_있으면_같은_객체로_본다() {
        //given
        DeleteHistory d1 = new DeleteHistory(ContentType.QUESTION, 0L, User.GUEST_USER, LocalDateTime.now());
        DeleteHistory d2 = new DeleteHistory(ContentType.ANSWER, 0L, User.GUEST_USER, LocalDateTime.now());

        //when
        DeleteHistories deleteHistories = new DeleteHistories();
        deleteHistories.add(d1);
        deleteHistories.add(d2);

        //then
        DeleteHistories sameDeleteHistories = new DeleteHistories();
        deleteHistories.add(d2);
        deleteHistories.add(d1);
        assertThat(deleteHistories).isEqualTo(sameDeleteHistories);
    }

    @Test
    void DeleteHistories_는_Iterable_객체이다() {
        //given
        DeleteHistory d1 = new DeleteHistory(ContentType.QUESTION, 0L, User.GUEST_USER, LocalDateTime.now());
        DeleteHistory d2 = new DeleteHistory(ContentType.ANSWER, 0L, User.GUEST_USER, LocalDateTime.now());

        //when
        DeleteHistories deleteHistories = new DeleteHistories();
        deleteHistories.add(d1);
        deleteHistories.add(d2);

        //then
        assertThat(deleteHistories).containsExactlyInAnyOrder(d2, d1);
    }

}
