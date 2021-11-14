package qna.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class UserRepositoryTest {
	@Autowired
	UserRepository userRepository;

	@Test
	@DisplayName("User 생성 테스트")
	public void UserRepositoryCreateTest() {
		//given
		//when
		User savedUser = userRepository.save(UserTest.JAVAJIGI);

		//then
		User findUser = userRepository.findById(savedUser.getId()).get();
		assertThat(findUser.getName()).isEqualTo(savedUser.getName());
	}

	@Test
	@DisplayName("User 동일성 테스트")
	public void UserRepositoryEqualsTest() {
		//given
		//when
		User savedUser = userRepository.save(UserTest.JAVAJIGI);
		User findUser = userRepository.findById(savedUser.getId()).get();

		//then
		assertThat(savedUser).isEqualTo(findUser);
		assertThat(savedUser).isSameAs(findUser);
	}

	@Test
	@DisplayName("User 변경 테스트")
	public void UserRepositoryUpdateTest() {
		//given
		User savedUser = userRepository.save(UserTest.JAVAJIGI);
		//when
		savedUser.setName("우테캠");
		userRepository.flush();

		//then
		User findUser = userRepository.findById(savedUser.getId()).get();
		assertThat(findUser.getName()).isEqualTo(savedUser.getName());
	}

	@Test
	@DisplayName("User 삭제 테스트")
	public void UserRepositoryDeleteTest() {
		//given
		User savedOne = userRepository.save(UserTest.JAVAJIGI);
		User savedTwo = userRepository.save(UserTest.SANJIGI);

		//when
		userRepository.deleteById(savedTwo.getId());
		System.out.println(savedTwo.getId());

		//then
		assertThat(userRepository.findAll()).hasSize(1);
		assertThat(userRepository.findAll()).contains(savedOne);
	}
}