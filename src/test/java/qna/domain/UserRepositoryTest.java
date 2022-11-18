package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;

@Sql("classpath:user.sql")
@Import(TestDataSourceConfig.class)
@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("User 저장후 동일한지 확인한다.")
    void whenSave_thenSuccess() {
        User savedUser = userRepository.save(new User("hongGilUserId", "aaa", "hong", "hongGil@naver.com"));

        User searchedUser = userRepository.findByUserId(savedUser.getUserId()).orElse(null);

        assertThat(searchedUser).isNotNull();
        assertThat(savedUser).isEqualTo(searchedUser);
    }

    @ParameterizedTest
    @ValueSource(strings = {"aaUserId", "bbUserId", "ccUserId", "ddUserId"})
    @DisplayName("저장되어있는 userId 로 조회시 정상적으로 한개가 조회된다.")
    void givenValidName_whenFindByName_thenSuccess(String userId) {
        User user = userRepository.findByUserId(userId).orElse(null);

        assertThat(user).isNotNull();
    }
}