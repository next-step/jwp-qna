package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.repository.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class UserTest {

    public static final User JAVAJIGI = new User("javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User("sanjigi", "password", "name", "sanjigi@slipp.net");

    private static User user;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        user = new User("eunhye", "password", "name", "eunhye@slipp.net");
    }

    @Test
    @DisplayName("User 객체를 저장하면 Id가 자동생성 되어 Not Null 이다.")
    void save() {
        assertThat(user.getId()).isNull();

        User actual = userRepository.save(user);
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getUpdatedAt()).isNull()
        );
    }

    @Test
    @DisplayName("User 객체를 조회하면 데이터 여부에 따라 Optional 존재 여부가 다르다." +
            "또한 동일한 객체면 담긴 값도 동일하다.")
    void findByUserId() {
        User actual = userRepository.save(SANJIGI);

        assertAll(
                () -> assertThat(userRepository.findByUserId(SANJIGI.getUserId()))
                        .isPresent().get().extracting(User::getName).isEqualTo(actual.getName()),
                () -> assertThat(userRepository.findByUserId("eunhye")).isEmpty()
        );
    }

    @Test
    @DisplayName("User 객체를 수정하면 수정된 데이터와 일치해야 하고 업데이트 날짜가 Not Null 이다.")
    void update() {
        User actual = userRepository.save(SANJIGI);

        String id = "Updated Id";
        actual.setUserId(id);

        User updated = userRepository.findByUserId(id).get();
        assertAll(
                () -> assertThat(updated.getUpdatedAt()).isNotNull(),
                () -> assertThat(updated.getUserId()).isEqualTo(id)
        );
    }

}
