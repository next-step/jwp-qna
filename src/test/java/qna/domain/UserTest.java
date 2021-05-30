package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class UserTest {
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name1", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name2", "sanjigi@slipp.net");

    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("유저 생성")
    void create() {
        //given
        //when
        userRepository.save(JAVAJIGI);
        //then
        assertThat(JAVAJIGI.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("유저 조회")
    void find() {
        //given
        //when
        userRepository.save(JAVAJIGI);
        Optional<User> javajigi = userRepository.findByUserId("javajigi");
        //then
        assertThat(javajigi.isPresent()).isTrue();
        assertThat(javajigi.get()).isNotNull();
        assertThat(javajigi.get().getUserId()).isEqualTo("javajigi");
    }

    @Test
    @DisplayName("유저 삭제")
    void delete() {
        //given
        //when
        userRepository.save(JAVAJIGI);
        userRepository.deleteByUserId("javajigi");
        Optional<User> javajigi = userRepository.findByUserId("javajigi");
        //then
        assertThat(javajigi.isPresent()).isFalse();
    }

    @Test
    @DisplayName("유저 수정")
    void update() {
        //given
        //when
        userRepository.save(JAVAJIGI);
        userRepository.save(SANJIGI);
        JAVAJIGI.update(JAVAJIGI, SANJIGI);
        //then
        assertThat(JAVAJIGI.getName()).isEqualTo("name2");
        assertThat(JAVAJIGI.getEmail()).isEqualTo("sanjigi@slipp.net");
    }
}
