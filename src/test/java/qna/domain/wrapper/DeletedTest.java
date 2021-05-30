package qna.domain.wrapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.wrapper.Deleted;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class DeletedTest {

    private Deleted deleted;
    @BeforeEach
    void setup() {
        deleted = new Deleted();
    }

    @Test
    void create() {
        assertThat(deleted.status()).isFalse();
    }

    @Test
    void 삭제상태_true_테스트() {
        deleted.setTrue();

        assertThat(deleted.status()).isTrue();
    }

    @Test
    void 삭제상태_다시_false_테스트() {
        deleted.setTrue();
        deleted.setFalse();

        assertThat(deleted.status()).isFalse();
    }

}
