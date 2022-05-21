package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import qna.UnAuthorizedException;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("사용자 저장 성공 케이스")
    void save() {
        // when
        final User user = userRepository.save(new User("test", "1234", "이름", "test@email.com"));
        // then
        assertThat(user).isNotNull();
    }

    @ParameterizedTest
    @MethodSource("saveExceptionArgs")
    @DisplayName("사용자 저장시 제약조건 으로 인한 저장 실패 케이스")
    void save_exception(User expected) {
        // when & then
        assertThatThrownBy(() -> userRepository.save(expected))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    static Stream<Arguments> saveExceptionArgs() {
        return Stream.of(
                Arguments.of(new User("aheadtonightstiffen19p1ocBnLB60K9B9S3Fu", "12345", "이름", "test@email.com")),
                Arguments.of(new User("test", "aheadtonightstiffen19p1ocBnLB60K9B9S3Fu", "이름", "test@email.com")),
                Arguments.of(new User("test", "12345", "aheadtonightstiffen19p1ocBnLB60K9B9S3Fu", "test@email.com")),
                Arguments.of(new User("test", "12345", "이름",
                        "htvxfmpqhocuxcbnzcjvsuaiugucnfmmstrnaqvqbcipkvnrmfwzpyemyfgsxqqqsiinbmivuvrqpzvnwdilfnhnohuwcrewigxyzzhtjbrdcywdvsnuvmjspwycmogvdmbkfuamczyoncupyixmivoyefvpwwvgfzmdhvmrdydkvyeuttwqqkpkylniakpxuxnajfzuxawqhspwwpijcskfoynjabpcrjntdgigduasptxssjppojqdnu@email.com"))
        );
    }

    @Test
    @DisplayName("useer_id 가 중복되어 데이터 저장 불가")
    void save_duplicate_user_id() {
        // given
        final User user = userRepository.save(new User("userId1", "1234", "이름", "test@email.com"));
        userRepository.flush();

        // when & then
        assertThatThrownBy(() -> userRepository.save(new User(user.getUserId(), "1234", "이름", "test@email.com")))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("useerId로 찾기")
    void findByUserId() {
        // given
        final User expected = userRepository.save(new User("userId1", "1234", "이름", "test@email.com"));
        userRepository.flush();

        // when
        final User actual = userRepository.findByUserId(expected.getUserId()).get();

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("이름과 이메일 변경")
    void update() {
        // given
        final User expected = userRepository.save(new User("userId1", "1234", "이름", "test@email.com"));
        final User actual = new User(expected.getUserId(), expected.getPassword(), "변경하는 이름", "change@email.com");

        // when
        expected.update(expected, actual);
        userRepository.flush();

        // then
        assertThat(expected.getName()).isEqualTo("변경하는 이름");
        assertThat(expected.getEmail()).isEqualTo("change@email.com");
    }

    @ParameterizedTest
    @CsvSource(value = {"userId2;1234", "userId1;12345", "userId2;12345"}, delimiterString = ";")
    @DisplayName("아이디 혹은 패스워드가 다른경우 변경 불가")
    void update_exception(String userId, String password) {
        // given
        final User expected = userRepository.save(new User("userId1", "1234", "이름", "test@email.com"));
        final User actual = new User(userId, password, "변경하는 이름", "change@email.com");

        // when & then
        assertThatThrownBy(() -> expected.update(actual, actual))
                .isInstanceOf(UnAuthorizedException.class);
    }

    @Test
    void delete() {
        // given
        final User expected = userRepository.save(new User("userId1", "1234", "이름", "test@email.com"));

        // when
        userRepository.delete(expected);
        userRepository.flush();

        // then
        assertThat(userRepository.findById(expected.getId())).isEmpty();
    }
}