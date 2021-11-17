package qna.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class ContentsTest {
    @Test
    void create() {
        Contents contents = new Contents("즐거운 코딩", UserTest.JAVAJIGI);
        assertThat(contents.getContents()).isEqualTo("즐거운 코딩");
        assertThat(contents.getWriter().equalsNameAndEmail(UserTest.JAVAJIGI)).isTrue();
    }

    @Test
    void isWrittenBy() {
        Contents contents = new Contents("즐거운 코딩", UserTest.JAVAJIGI);
        assertThat(contents.isWrittenBy(UserTest.JAVAJIGI)).isTrue();
    }
}
