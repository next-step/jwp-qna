package qna.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static qna.domain.UserTest.*;

@DataJpaTest
@DisplayName("UserRepository 테스트")
class UserRepositoryTest {
    @Autowired
    private UserRepository users;

    private User userJavajigi;
    private User userSanjigi;
    private User userJordy;

    @BeforeEach
    void setUp() {
        userJavajigi = users.save(JAVAJIGI);
        userSanjigi = users.save(SANJIGI);
        userJordy = users.save(JORDY);
    }
    
    @AfterEach
    void setDown() {
        userJavajigi.setId(null);
        userSanjigi.setId(null);
        userJordy.setId(null);
    }

    @Test
    @DisplayName("findById_정상_저장_전_후_동일한_객체_조회")
    void findById_정상_저장_전_후_동일한_객체_조회() {
        // Given
        User expectedResult = userJavajigi;

        // When
        Optional<User> actualResult = users.findById(expectedResult.getId());

        // Then
        assertThat(actualResult).containsSame(expectedResult);
    }

    @Test
    @DisplayName("findByUserId_정상")
    void findByUserId_정상() {
        // Given
        User expectedResult = userJordy;

        // When
        Optional<User> actualResult = users.findByUserId(expectedResult.getUserId());

        // Then
        assertThat(actualResult).containsSame(expectedResult);
    }
}