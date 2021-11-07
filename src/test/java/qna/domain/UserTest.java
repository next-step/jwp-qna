package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
public class UserTest {
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    @Autowired
    private UserRepository repository;

    @Test
    @DisplayName("사용자를 저장한다.")
    void save() {
        //given //when
        User saved = repository.save(JAVAJIGI);

        //then
        assertAll(
                () -> assertThat(saved.getId()).isNotNull(),
                () -> assertThat(saved.getUserId()).isEqualTo(JAVAJIGI.getUserId()),
                () -> assertThat(saved.getPassword()).isEqualTo(JAVAJIGI.getPassword()),
                () -> assertThat(saved.getName()).isEqualTo(JAVAJIGI.getName()),
                () -> assertThat(saved.getEmail()).isEqualTo(JAVAJIGI.getEmail())
        );
    }

    @Test
    @DisplayName("사용자 한 건을 조회한다.")
    void findByUserId() {
        //given
        User saved = repository.save(SANJIGI);

        //when
        Optional<User> findUser = repository.findByUserId(saved.getUserId());

        //then
        assertThat(findUser.isPresent()).isTrue();
        User user = findUser.get();
        assertAll(
                () -> assertThat(user.getId()).isNotNull(),
                () -> assertThat(user.getUserId()).isEqualTo(SANJIGI.getUserId()),
                () -> assertThat(user.getPassword()).isEqualTo(SANJIGI.getPassword()),
                () -> assertThat(user.getName()).isEqualTo(SANJIGI.getName()),
                () -> assertThat(user.getEmail()).isEqualTo(SANJIGI.getEmail())
        );

    }
}
