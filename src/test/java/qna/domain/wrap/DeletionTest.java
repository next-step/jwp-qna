package qna.domain.wrap;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;
import static org.junit.jupiter.api.Assertions.*;

class DeletionTest {
    @Test
    @DisplayName("이미 삭제된 상태면 삭제상태로 변경시 IllegalStateException이 발생한다")
    void 이미_삭제된_상태면_삭제상태로_변경시_IllegalStateException이_발생한다() {
        Deletion deletion = new Deletion(true);

        assertThatIllegalStateException()
                .isThrownBy(() -> deletion.delete());
    }

    @Test
    @DisplayName("미삭제_상태일경우_삭제가_가능하다")
    void 미삭제_상태일경우_삭제가_가능하다() {
        Deletion deletion = new Deletion();

        assertAll(
                () -> assertDoesNotThrow(() -> deletion.delete()),
                () -> assertThat(deletion.isDeleted()).isTrue()
        );
    }
}