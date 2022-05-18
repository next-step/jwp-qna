package qna.user.repository;

import config.annotation.LocalDataJpaConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import qna.user.domain.User;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@LocalDataJpaConfig
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void 유저_아이디로_조회_테스트() {
        User user = new User("userId", "password", "name", "email");
        User savedUser = userRepository.save(user);

        Optional<User> notFindResult = userRepository.findByUserId(UUID.randomUUID().toString());
        Optional<User> findResult = userRepository.findByUserId(user.getUserId());

        assertThat(notFindResult.isPresent()).isFalse();
        assertThat(findResult.isPresent()).isTrue();
        assertThat(findResult.get().getUserId()).isEqualTo(savedUser.getUserId());
        assertThat(findResult.get().getPassword()).isEqualTo(savedUser.getPassword());
        assertThat(findResult.get().getName()).isEqualTo(savedUser.getName());
        assertThat(findResult.get().getEmail()).isEqualTo(savedUser.getEmail());
    }
}