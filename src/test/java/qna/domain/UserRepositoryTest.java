package qna.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepositories;

    @Test
    @DisplayName("UserRepository 저장 후 ID not null 체크")
    void save() {
        // when
        User actual = userRepositories.save(UserTest.JAVAJIGI);

        // then
        assertThat(actual.getId()).isNotNull();
    }

    @Test
    @DisplayName("UserRepository 저장 후 DB조회 객체 동일성 체크")
    void identity() {
        // when
        User actual = userRepositories.save(UserTest.SANJIGI);
        User expect = userRepositories.findById(actual.getId()).get();

        // then
        assertThat(actual).isEqualTo(expect);
    }

    @Test
    @DisplayName("userId 로 User 단일 조회")
    void findByUserId() {
        // when
        User actual = userRepositories.save(UserTest.SANJIGI);
        User expect = userRepositories.findByUserId(actual.getUserId()).get();

        // then
        assertThat(actual).isEqualTo(expect);
    }
}
