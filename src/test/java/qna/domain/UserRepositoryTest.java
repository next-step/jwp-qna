package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository users;

    private User javajigi;
    private User sanjigi;

    @BeforeEach
    public void setUp() {
        javajigi = UserTest.JAVAJIGI;
        sanjigi = UserTest.SANJIGI;
        users.save(javajigi);
        users.save(sanjigi);
    }

    @Test
    @DisplayName("사용자 등록하기")
    public void save() {
        assertAll(
                () -> assertThat(javajigi.getId()).isNotNull(),
                () -> assertThat(javajigi.getUserId()).isEqualTo(UserTest.JAVAJIGI.getUserId()),
                () -> assertThat(sanjigi.getId()).isNotNull(),
                () -> assertThat(sanjigi.getUserId()).isEqualTo(UserTest.SANJIGI.getUserId())
        );
    }

    @Test
    @DisplayName("사용자 ID 로 찾기")
    public void findByUserId() {
        assertThat(users.findByUserId(sanjigi.getUserId()).get().getUserId()).isEqualTo(UserTest.SANJIGI.getUserId());
        assertThat(users.findByUserId(javajigi.getUserId()).get().getUserId()).isEqualTo(UserTest.JAVAJIGI.getUserId());
    }
}