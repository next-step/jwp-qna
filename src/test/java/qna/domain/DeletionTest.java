package qna.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class DeletionTest {

    @Test
    void delete() {
        Deletion deletion = new Deletion();

        deletion.delete();

        assertThat(deletion.isDeleted()).isTrue();
    }
}