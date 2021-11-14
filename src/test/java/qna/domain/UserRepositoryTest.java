package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserRepositoryTest {
    private static final String USERID = "userId";
    private static final String USERID2 = "userId2";
    private static final String PASSWORD = "PASSWORD";
    private static final String NAME = "MYNAME";
    private static final Email EMAIL = new Email("javajigi@slipp.net");

    @Autowired
    UserRepository users;

    @Test
    @DisplayName("save 후 DB 조회 동일성 검증")
    void findByUserId() {
        // given
        User saveUser = new User(USERID, PASSWORD, NAME, EMAIL);

        // when
        User actual = users.save(saveUser);
        User expect = users.findByUserId(saveUser.getUserId()).get();

        // then
        assertThat(actual).isSameAs(expect);
    }

    @Test
    @DisplayName("USER 저장 후 findById 메소드로 가져온 객체는 동일함")
    void findById_동일성_체크() {
        // given
        User saveUser = new User(USERID, PASSWORD, NAME, EMAIL);

        // when
        User actual = users.save(saveUser);
        User expect = users.findById(saveUser.getId()).get();

        // then
        assertAll(
            () -> assertThat(actual).isEqualTo(expect),
            () -> assertThat(saveUser).isEqualTo(expect)
        );
    }

    @Test
    @DisplayName("UserId 변경 후 findByUserId 를 호출하면 update 쿼리 발생 후 select 쿼리 발생함, 조회된 객체 동일성 체크")
    void findByUserId_update_flush() {
        // given
        User saveUser = new User(USERID, PASSWORD, NAME, EMAIL);
        users.save(saveUser);

        // when
        saveUser.setUserId("user_id 변경");
        User expect = users.findByUserId(saveUser.getUserId()).get();

        // then
        assertThat(saveUser).isEqualTo(expect);
    }

    @Test
    @DisplayName("동일한 사용자이름 목록 조회")
    void findByName() {
        // given
        User sameNameUser1 = new User(USERID, PASSWORD, NAME, EMAIL);
        User sameNameUser2 = new User(USERID2, PASSWORD, NAME, EMAIL);
        users.save(sameNameUser1);
        users.save(sameNameUser2);

        // when
        List<User> userList = users.findByName(NAME);

        assertAll(
            () -> assertThat(userList).contains(sameNameUser1),
            () -> assertThat(userList).contains(sameNameUser2)
        );
    }

    @Test
    @DisplayName("user_id(유니크) 동일한 유저 저장시 예외 발생 테스트")
    void unique_fail() {
        // given
        User sameUserIdUser1 = new User(USERID, PASSWORD, NAME, EMAIL);
        User sameUserIdUser2 = new User(USERID, PASSWORD, NAME, EMAIL);

        assertThatExceptionOfType(DataIntegrityViolationException.class) // then
            .isThrownBy(() -> {
                // when
                users.save(sameUserIdUser1);
                users.save(sameUserIdUser2);
            });
    }

    @Test
    @DisplayName("User 생성 후 countByUserId 로 카운트 확인")
    void countByUserId() {
        // given
        User saveUser = new User(USERID, PASSWORD, NAME, EMAIL);
        users.save(saveUser);

        // when
        Long actual = users.countByUserId(saveUser.getUserId());

        // then
        assertThat(actual).isGreaterThan(0);
    }
}
