package qna.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("사용자 정보 저장 테스트")
    void saveTest(){
        User saveUser = userRepository.save(new User("admin", "admin1!", "관리자", ""));

        User findUser = userRepository.findById(saveUser.getId())
                .orElse(null);

        assertThat(saveUser.getId()).isNotNull();
        assertThat(saveUser).isEqualTo(findUser);
    }

    @Test
    @DisplayName("아이디로 사용자 정보 조회 테스트")
    void findByUserIdTest(){
        User saveUser = userRepository.save(UserTest.JAVAJIGI);
        User findUser = userRepository.findByUserId(UserTest.JAVAJIGI.getUserId())
                .orElse(null);

        assertThat(findUser).isNotNull();
        assertThat(findUser).isEqualTo(saveUser);
    }

    @Test
    @DisplayName("이름으로 사용자 정보 조회 테스트")
    void findByNameTest(){
        User saveUser = userRepository.save(UserTest.SANJIGI);
        User findUser = userRepository.findByName(UserTest.SANJIGI.getName())
                .orElse(null);

        assertThat(findUser).isNotNull();
        assertThat(findUser).isEqualTo(saveUser);
    }
}
