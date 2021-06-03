package qna.domain;

import org.springframework.test.util.ReflectionTestUtils;

public class UserTest {
    public static final User JAVAJIGI = new User("javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User("sanjigi", "password", "name", "sanjigi@slipp.net");

    static {
        ReflectionTestUtils.setField(JAVAJIGI, "id", 1L);
        ReflectionTestUtils.setField(SANJIGI, "id", 2L);
    }
}
