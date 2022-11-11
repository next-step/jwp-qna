package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("삭제 여부 테스트")
class DeleteFlagTest {

    @DisplayName("삭제 상태")
    @Test
    void delete_flag_success() {
        assertThat(DeleteFlag.deleted().getDeleted()).isTrue();
    }

    @DisplayName("삭제되지 않은 상태")
    @Test
    void notDelete_flag_success() {
        assertThat(DeleteFlag.notDeleted().getDeleted()).isFalse();
    }
}
