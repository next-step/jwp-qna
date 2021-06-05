package qna.domain.wrappers;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DeletedTest {

    @Test
    void 삭제_여부_원시값_객체_생성() {
        Deleted deleted = new Deleted();
        assertThat(deleted).isEqualTo(new Deleted());
    }

    @Test
    void 삭제_여부_삭제로_변경() {
        Deleted deleted = new Deleted();
        deleted.delete(true);
        assertThat(deleted).isEqualTo(new Deleted(true));
    }
}
