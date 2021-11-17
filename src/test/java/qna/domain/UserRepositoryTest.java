package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import qna.domain.repository.UserRepository;

@DataJpaTest
public class UserRepositoryTest {

    private static final String USERID = "userId";
    private static final String USERID2 = "userId2";
    private static final String PASSWORD = "PASSWORD";
    private static final String NAME = "MYNAME";
    private static final Email EMAIL = new Email("javajigi@slipp.net");

    @Autowired
    UserRepository users;

    private User USER1;
    private User USER2;

    @BeforeEach
    public void setUp() throws Exception {
        USER1 = UserTest.createUser(USERID, PASSWORD, NAME, EMAIL);
        USER2 = UserTest.createUser(USERID2, PASSWORD, NAME, EMAIL);
    }

    @Test
    @DisplayName("USER 저장 후 findById 메소드로 가져온 객체는 동일함")
    void findById_동일성_체크() {
        // given
        // when
        User actual = users.save(USER1);
        User expect = users.findById(USER1.getId()).get();

        // then
        assertAll(
            () -> assertThat(actual).isEqualTo(expect),
            () -> assertThat(USER1).isEqualTo(expect)
        );
    }

    @Test
    @DisplayName("UserId 변경 후 findByUserId 를 호출하면 update 쿼리 발생 후 select 쿼리 발생함, 조회된 객체 동일성 체크")
    void findByUserId_update_flush() {
        // given
        users.save(USER1);

        // when
        USER1.changeUserId("user_id 변경");
        User expect = users.findByUserAuthUserId(USER1.getUserId()).get();

        // then
        assertThat(USER1).isEqualTo(expect);
    }

    @Test
    @DisplayName("동일한 사용자이름 목록 조회")
    void findByName() {
        // given
        users.save(USER1);
        users.save(USER2);

        // when
        List<User> userList = users.findByUserDataName(NAME);

        assertAll(
            () -> assertThat(userList).contains(USER1),
            () -> assertThat(userList).contains(USER2)
        );
    }

    @Test
    @DisplayName("user_id(유니크) 동일한 유저 저장시 예외 발생 테스트")
    void unique_fail() {
        // given
        USER2.changeUserId(USERID);

        assertThatExceptionOfType(DataIntegrityViolationException.class) // then
            .isThrownBy(() -> {
                // when
                users.save(USER1);
                users.save(USER2);
            });
    }

    @Test
    @DisplayName("User 생성 후 countByUserId 로 카운트 확인")
    void countByUserId() {
        // given
        users.save(USER1);

        // when
        Long actual = users.countByUserAuthUserId(USER1.getUserId());

        // then
        assertThat(actual).isGreaterThan(0);
    }
}
