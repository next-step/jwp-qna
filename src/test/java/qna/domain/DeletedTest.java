package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class DeletedTest {
    @DisplayName("삭제 테스트")
    @Test
    void delete() {
        Deleted deleted = new Deleted();
        deleted.delete();

        assertThat(deleted.getDeleted()).isTrue();
    }
}