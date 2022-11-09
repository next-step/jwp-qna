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
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("유저 저장 성공")
    void save() {
        //given
        User user = new User(null, "sangjae", "password", "name", "javajigi@slipp.net");
        User saveUser = userRepository.save(user);

        //expect
        assertAll(
                () -> assertThat(saveUser).isNotNull(),
                () -> assertThat(saveUser.getId()).isNotNull(),
                () -> assertThat(saveUser.getCreatedAt()).isNotNull(),
                () -> assertThat(saveUser.getUpdatedAt()).isNotNull()
        );
    }

    @Test
    @DisplayName("아이디로 유저 검색")
    void findById() {
        //gvien
        User user = new User(null, "sangjae", "password", "name", "javajigi@slipp.net");
        User saveUser = userRepository.save(user);

        //when
        Optional<User> optionalUser = userRepository.findById(saveUser.getId());

        //then
        assertThat(optionalUser).isPresent();
    }

    @Test
    @DisplayName("없는 아이디로 유저를 검색하면 Optional<User>의 값이 비어있다")
    void findById_없는_아이디_검색() {
        //gvien
        User user = new User(null, "sangjae", "password", "name", "javajigi@slipp.net");
        User saveUser = userRepository.save(user);

        //when
        Optional<User> optionalUser = userRepository.findById(-1L);

        //then
        assertThat(optionalUser).isNotPresent();
    }

}