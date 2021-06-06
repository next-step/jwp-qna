package qna.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DeletedTest {

    @Test
    void create() {
        //when
        Deleted trueDeleted = new Deleted(true);
        Deleted falseDeleted = new Deleted(false);

        //then
        assertThat(trueDeleted.isDeleted()).isTrue();
        assertThat(falseDeleted.isDeleted()).isFalse();
    }
}