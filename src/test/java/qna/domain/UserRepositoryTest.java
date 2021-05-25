package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertSame;

@DataJpaTest
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    public void setUp() {
        this.user = userRepository.save(UserTest.JAVAJIGI); // persist가 아닌 merge로 동작하기에 return값을 써야함.
    }

    @Test
    @DisplayName("저장을 하고, 다시 가져왔을 때 원본 객체와 같아야 한다")
    @Transactional
    public void 저장을_하고_다시_가져왔을_때_원본_객체와_같아야_한다() {
        User foundQuestion = userRepository.findById(user.getId()).orElseThrow(EntityNotFoundException::new);

        assertSame(user, foundQuestion);
    }

    @Test
    @DisplayName("유저 아이디로 유저를 찾을 수 있다.")
    public void 유저_아이디로_유저를_찾을_수_있다() {
        assertThat(userRepository.findByUserId(user.getUserId()).orElseThrow(EntityNotFoundException::new))
                .isSameAs(user);
    }

}
