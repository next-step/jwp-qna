package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import qna.config.JpaAuditingConfiguration;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
@Import(value = {JpaAuditingConfiguration.class})
public class UserTest {
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("저장")
    void save() {
        //given
        User user = userRepository.save(JAVAJIGI);

        //expect
        assertAll(
                () -> assertThat(user).isNotNull(),
                () -> assertThat(user.getId()).isNotNull(),
                () -> assertThat(user.getCreatedAt()).isNotNull(),
                () -> assertThat(user.getUpdatedAt()).isNotNull()
        );
    }

    @Test
    @DisplayName("아이디로 유저 검색")
    void findById() {
        //gvien
        userRepository.save(JAVAJIGI);

        //when
        Optional<User> user = userRepository.findById(JAVAJIGI.getId());

        //then
        assertThat(user).isPresent();
    }

    @Test
    @DisplayName("없는 아이디로 유저를 검색하면 Optional<User>의 값이 비어있다")
    void findById_없는_아이디_검색() {
        //gvien
        userRepository.save(JAVAJIGI);

        //when
        Optional<User> user = userRepository.findById(-1L);

        //then
        assertThat(user).isNotPresent();
    }
}