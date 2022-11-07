package qna.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class UserTest {

    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name",
        "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name",
        "sanjigi@slipp.net");

}
