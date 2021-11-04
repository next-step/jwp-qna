package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@DisplayName("사용자 데이터")
public class UserTest {

    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    @Autowired
    private UserRepository users;

    @Test
    @DisplayName("저장")
    void save() {
        User actual = users.save(JAVAJIGI);
        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getName()).isEqualTo(JAVAJIGI.getName())
        );
    }

    @Test
    @DisplayName("아이디로 검색")
    void findByUserId() {
        String expected = "sanjigi";
        users.save(SANJIGI);
        String actual = userByUserId(expected).getUserId();
        assertThat(actual).isEqualTo(expected);
    }

    private User userByUserId(String userId) {
        return users.findByUserId(userId)
            .orElseThrow(() -> new EntityNotFoundException(String.format("%s is not found", userId)));
    }
}
