package qna.domain;

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

    @Test
    @DisplayName("저장을 하고, 다시 가져왔을 때 원본 객체와 같아야 한다")
    @Transactional
    public void 저장을_하고_다시_가져왔을_때_원본_객체와_같아야_한다() {
        User savedUser = userRepository.save(UserTest.JAVAJIGI);
        User foundQuestion = userRepository.findById(savedUser.getId()).orElseThrow(EntityNotFoundException::new);

//        assertSame(UserTest.JAVAJIGI, savedUser); -> 다른 케이스와 다르게 Merge되어 새로운 객체가 Return되기에 테스트가 깨진다.
        assertSame(savedUser, foundQuestion);
    }

    @Test
    @DisplayName("유저 아이디로 유저를 찾을 수 있다.")
    public void 유저_아이디로_유저를_찾을_수_있다() {
        User savedUser = userRepository.save(UserTest.JAVAJIGI);

        assertThat(userRepository.findByUserId(savedUser.getUserId()).orElseThrow(EntityNotFoundException::new))
                .isSameAs(savedUser);
    }

}
