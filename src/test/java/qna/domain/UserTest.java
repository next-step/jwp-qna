package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserTest {
    public static final User JAVAJIGI = new User("javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User("sanjigi", "password", "name", "sanjigi@slipp.net");

    @Autowired
    private UserRepository userRepository;

    @DisplayName("Create 및 ID 생성 테스트")
    @Test
    void save() {
        //when
        User save = userRepository.save(user());

        //then
        assertThat(save.getId()).isNotNull();
    }

    @DisplayName("Read 테스트")
    @Test
    void read() {
        //when
        User save = userRepository.save(user());
        User found = userRepository.findById(save.getId()).orElse(null);

        //then
        assertThat(found).isEqualTo(save);
    }

    @DisplayName("Update 테스트")
    @Test
    void update() {
        //when
        User save = userRepository.save(user());
        save.setUserId("steadyjin");
        User found = userRepository.findById(save.getId()).orElseThrow(() -> new NullPointerException("테스트실패"));

        //then
        assertThat(found.getUserId()).isEqualTo("steadyjin");
    }

    @DisplayName("Delete 테스트")
    @Test
    void delete() {
        //when
        User save = userRepository.save(user());
        userRepository.delete(save);
        User found = userRepository.findById(save.getId()).orElse(null);

        //then
        assertThat(found).isNull();
    }

    static User user() {
        return new User("steadyJin", "password", "changjin", "pcjin118@gmail.com");
    }

    static User admin() {
        return new User("javajigi", "password", "name", "javajigi@slipp.net");
    }
}
