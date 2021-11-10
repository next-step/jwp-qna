package qna.repository;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import qna.domain.Question;
import qna.domain.User;
import qna.domain.UserTest;

@DataJpaTest
class UserRepositoryTest {

	@Autowired
	UserRepository userRepository;

	@Test
	public void save() {
		User user = new User("jerry92k", "12345678","jerrykim","jerry@gmail.com");
		User actual = userRepository.save(user);
		assertThat(user).isEqualTo(actual);
	}

	@Test
	public void findByUserId() {
		User savedUser = userRepository.save( new User("jerry92k", "12345678","jerrykim","jerry@gmail.com"));
		Optional<User> findUser = userRepository.findByUserId(savedUser.getUserId());
		assertThat(findUser.isPresent()).isTrue();
	}
}