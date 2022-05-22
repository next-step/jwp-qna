package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class UserTest {
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("이름, 이메일 수정")
    void updateNameAndEmail() {
        User changeUser = new User(1L, "javajigi", "password", "name2", "javajigi2@slipp.net");
        changeUser.update(JAVAJIGI, JAVAJIGI);
        assertThat(changeUser.getName()).isEqualTo(JAVAJIGI.getName());
        assertThat(changeUser.getEmail()).isEqualTo(JAVAJIGI.getEmail());
    }

    @Test
    @DisplayName("저장 후 이름, 이메일,비밀번호 동일 저장 확인")
    void saveMatch() {
        User user = userRepository.save(JAVAJIGI);
        assertThat(user.matchPassword(JAVAJIGI.getPassword())).isTrue();
        assertThat(user.equalsNameAndEmail(JAVAJIGI)).isTrue();
    }

    @Test
    @DisplayName("빈값으로 id 세팅 시 자동 입력 확인")
    void save() {
        User user = new User("", "password", "hyun", "hyun@slipp.net");
        User save = userRepository.save(user);
        assertThat(save.getId()).isNotNull();
    }

    @Test
    @DisplayName("이메일 수정")
    void updateEmail() {
        User save = userRepository.save(new User("", "password", "hyun", "hyun@slipp.net"));
        save.setEmail("hyun22@slipp.net");

        User one = userRepository.getOne(save.getId());
        assertThat(one.getEmail()).isEqualTo("hyun22@slipp.net");
    }
}
