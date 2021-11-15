package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import qna.utils.StringUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

/**
 * packageName : qna.domain
 * fileName : UserRepositoryTest
 * author : haedoang
 * date : 2021-11-09
 * description :
 */
@DataJpaTest
@EnableJpaAuditing
@TestMethodOrder(MethodOrderer.MethodName.class)
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {
    private static final int MAX_COLUMN_LENGTH = 500;

    @Autowired
    private UserRepository repository;

    @Test
    @DisplayName("User 검증 테스트")
    public void T1_save() {
        //WHEN
        User savedUser = repository.save(UserTest.JAVAJIGI);
        User savedUser2 = repository.save(UserTest.SANJIGI);
        //THEN
        assertThat(savedUser.equals(savedUser2)).isFalse();
        assertThat(savedUser.equalsNameAndEmail(UserTest.JAVAJIGI)).isTrue();
        assertThat(savedUser2.equalsNameAndEmail(UserTest.SANJIGI)).isTrue();
    }

    @Test
    @DisplayName("유효성체크1_null")
    public void T2_validate() {
        //WHEN
        User userIdNull = new User(null, "password", "name", "javajigi@slipp.net");
        User passwordNull = new User("javajigi", null, "name", "javajigi@slipp.net");
        User nameNull = new User("javajigi", "password", null, "javajigi@slipp.net");
        //THEN
        assertAll(
                () -> assertThatThrownBy(() -> repository.save(userIdNull)).isInstanceOf(DataIntegrityViolationException.class),
                () -> assertThatThrownBy(() -> repository.save(passwordNull)).isInstanceOf(DataIntegrityViolationException.class),
                () -> assertThatThrownBy(() -> repository.save(nameNull)).isInstanceOf(DataIntegrityViolationException.class)
        );
    }

    @Test
    @DisplayName("유효성체크2_길이초과")
    public void T3_validate2() {
        //GIVEN
        String overLengthString = StringUtils.getRandomString(MAX_COLUMN_LENGTH);
        //WHEN
        User userIdLengthOver = new User(overLengthString, "password", "name", "javajigi@slipp.net");
        User passwordLengthOver = new User("javajigi", overLengthString, "name", "javajigi@slipp.net");
        User nameLengthOver = new User("javajigi", "password", overLengthString, "javajigi@slipp.net");
        User emailLengthOver = new User("javajigi", "password", "name", overLengthString);
        //THEN
        assertAll(
                () -> assertThatThrownBy(() -> repository.save(userIdLengthOver)).isInstanceOf(DataIntegrityViolationException.class),
                () -> assertThatThrownBy(() -> repository.save(passwordLengthOver)).isInstanceOf(DataIntegrityViolationException.class),
                () -> assertThatThrownBy(() -> repository.save(nameLengthOver)).isInstanceOf(DataIntegrityViolationException.class),
                () -> assertThatThrownBy(() -> repository.save(emailLengthOver)).isInstanceOf(DataIntegrityViolationException.class)
        );
    }
}
