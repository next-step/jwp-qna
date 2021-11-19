package qna.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class DeletedTest {
    @DisplayName("Deleted 생성")
    @Test
    void init() {
        Deleted deleted = new Deleted();

        boolean isDeleted = deleted.isDeleted();

        assertThat(isDeleted).isFalse();
    }

    @DisplayName("삭제")
    @Test
    void delete() {
        Deleted deleted = new Deleted(true);

        boolean isDeleted = deleted.isDeleted();

        assertThat(isDeleted).isTrue();
    }

    @DisplayName("Deleted 비교")
    @Test
    void equalToDeleted() {
        Deleted standard = new Deleted();
        Deleted target = new Deleted();

        assertThat(standard).isEqualTo(target);
    }
}
