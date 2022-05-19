package qna.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import qna.domain.User;
import qna.domain.UserRepository;

@DataJpaTest
public class UserServiceTest {

	@Autowired
	private UserRepository userRepository;

	@Test
	@DisplayName("저장 후 조회 테스트")
	void save_find_test() {
		User eaststar1129 = new User("eaststar1129", "password", "eaststar", "eaststar1129@eamil.com");

		User saveUser = userRepository.save(new User("eaststar1129", "password", "eaststar", "eaststar1129@eamil.com"));
		Optional<User> findUser = userRepository.findByUserId(saveUser.getUserId());

		assertAll(() -> assertNotNull(saveUser), () -> assertNotNull(findUser.get().getId()),
				() -> assertEquals(findUser.get().getUserId(), "eaststar1129"),
				() -> assertTrue(findUser.get().matchPassword("password")),
				() -> assertTrue(findUser.get().equalsNameAndEmail(eaststar1129)));
	}
	
	@Test
	@DisplayName("유니크 컬럼 user_id 중복값 에러 테스트")
	void save_unique_user_id_test() {
		User eaststar1129 = new User("eaststar1129", "password", "eaststar", "eaststar1129@eamil.com");
		User eaststar1129Two = new User("eaststar1129", "password", "eaststar", "eaststar1129@eamil.com");

		userRepository.save(eaststar1129);
		assertThrows(DataIntegrityViolationException.class, () -> userRepository.save(eaststar1129Two));
	}

	@Test
	@DisplayName("업데이트 테스트")
	void update_test() {
		User eaststar1129 = new User("eaststar1129", "password", "eaststarTwo", "eaststarTwo1129@eamil.com");
		User saveUser = userRepository.save(new User("eaststar1129", "password", "eaststar", "eaststar1129@eamil.com"));

		saveUser.update(saveUser, eaststar1129);
		saveUser = userRepository.save(saveUser);
		Optional<User> findUser = userRepository.findByUserId(saveUser.getUserId());

		assertAll(() -> assertNotNull(findUser.get().getId()), () -> assertNull(eaststar1129.getId()),
				() -> assertEquals(eaststar1129.getUserId(), findUser.get().getUserId()),
				() -> assertTrue(eaststar1129.matchPassword(findUser.get().getPassword())),
				() -> assertTrue(eaststar1129.equalsNameAndEmail(findUser.get())));
	}
}
