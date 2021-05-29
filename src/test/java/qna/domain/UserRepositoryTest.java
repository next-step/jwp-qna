package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository users;

    @Test
    @DisplayName("유저 저장")
    void save() {
        // given
        User user1 = new User("USER1", "123456", "LDS", "lds@test.com");

        // when
        users.save(user1);
        User actual = users.findByUserId("USER1").orElseThrow(IllegalArgumentException::new);

        // then
        assertThat(actual).isSameAs(user1);
    }

    @Test
    @DisplayName("변경감지 실습 - 원래의 이름으로 다시 세팅 시, 업데이트 쿼리를 실행하지 않음")
    void study_dirtyCheck() {
        // given
        User user1 = new User("USER1", "123456", "LDS", "lds@test.com");

        // when then
        User actual = users.save(user1);
        actual.modifyName("LJS");
        actual.modifyName("LDS");
    }
}