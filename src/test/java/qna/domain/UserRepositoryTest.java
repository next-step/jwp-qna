package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static qna.domain.UserTest.JAVAJIGI;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("user_id로 검색 테스트")
    void find_by_user_id(){
        //given
        userRepository.save(JAVAJIGI);
        //when
        Optional<User> user = userRepository.findByUserId(JAVAJIGI.getUserId());
        //then
        assertThat(user.get()).usingRecursiveComparison()
                .ignoringFields("id","createdAt","updatedAt")
                .isEqualTo(JAVAJIGI);
    }
}
