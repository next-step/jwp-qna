package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.NotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    @Autowired
    private UserRepository users;
    private User user1;

    @BeforeEach
    void setUp() {
        user1 = new User("user1", "pass@1234AB", "name1", "user@nextstep.camp");
        users.save(user1);
    }

    @DisplayName("유저아이디에 해당하는 유저를 리턴한다.")
    @Test
    void findByUserId() {
        //when
        User actual = users.findByUserId(user1.getUserId())
                .orElseThrow(NotFoundException::new);

        //then
        assertThat(actual).isSameAs(user1);
    }

    @DisplayName("유저아이디에 해당하는 유저가 없다면 예외를 발생시킨다.")
    @Test
    void findByUserIdException() {
        //given
        User noneUser = new User(-1L, "", "pass@1234AB", "", "user@nextstep.camp");

        //when
        assertThatThrownBy(() -> users.findByUserId(noneUser.getUserId())
                .orElseThrow(NotFoundException::new))
                .isInstanceOf(NotFoundException.class); //then
    }
}