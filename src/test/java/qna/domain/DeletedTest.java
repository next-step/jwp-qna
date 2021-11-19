package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class DeletedTest {
    @DisplayName("삭제 테스트")
    @Test
    void delete() {
        // given
        Deleted deleted = new Deleted();

        // when
        deleted.delete();

        // then
        assertThat(deleted.getDeleted()).isTrue();
    }
}