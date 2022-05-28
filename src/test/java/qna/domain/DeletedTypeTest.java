package qna.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class DeletedTypeTest {

    @Test
    void create() {
        assertThat(DeletedType.valueOf(true)).isEqualTo(DeletedType.YES);
        assertThat(DeletedType.valueOf(false)).isEqualTo(DeletedType.NO);
    }
}
