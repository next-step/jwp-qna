package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@SpringBootTest
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    void save() {
        final User YONG = userRepository.save(new User( "yong", "password", "name", "yong@nextstep.com"));
        assertThat(userRepository.findByUserId(YONG.getUserId()).get()).isNotNull();
    }

    @Test
    public void findByName() {
        userRepository.save(UserTest.JAVAJIGI);
        final User YONG = userRepository.findByName(UserTest.JAVAJIGI.getName()).get();
        assertThat(YONG).isNotNull();
    }

    @Test
    @DisplayName("같은 유저 아이디로 중복된 유저가 있을 수 없다.")
    public void duplication_user() {
        userRepository.save(UserTest.JAVAJIGI);
        assertThatThrownBy(() ->
            userRepository.save(new User(UserTest.JAVAJIGI.getUserId(), "password", "name", "yong@nextstep.com"))
        ).isInstanceOf(DataIntegrityViolationException.class);
    }

//    @Test
//    @DisplayName("생성일 / 수정일을 입력 확인")
//    void create_update_at_check() {
//        userRepository.save(UserTest.JAVAJIGI);
//        LocalDateTime createAt = UserTest.JAVAJIGI.getCreatedAt();
//        LocalDateTime updateAt = UserTest.JAVAJIGI.getUpdatedAt();
//
//        System.out.println(UserTest.JAVAJIGI.getCreatedAt());
//        assertThat(createAt).isAfter(LocalDateTime.now());
//        assertThat(updateAt).isAfter(LocalDateTime.now());
//
//        UserTest.JAVAJIGI.setName("김씨");
//        LocalDateTime changeUpdateAt = UserTest.JAVAJIGI.getUpdatedAt();
//        assertThat(updateAt).isNotEqualTo(changeUpdateAt);
//    }
}
