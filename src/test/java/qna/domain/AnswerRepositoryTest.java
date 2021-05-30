package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class AnswerRepositoryTest {

	@Autowired
	private AnswerRepository repository;

	@Autowired
	private UserRepository userRepository;

	private Answer save;

	@BeforeEach
	void setUp() {
		User javaJigi = userRepository.save(UserTest.JAVAJIGI);
		User sanjigi = userRepository.save(UserTest.SANJIGI);
		Answer a1 = AnswerTest.A1;
		a1.setWriter(javaJigi);
		this.save = repository.save(a1);
	}

	@Test
	@DisplayName("정답을 저장된 후, 원본은 같다")
	void findByIdTest() {
		Answer find = repository.findById(save.getId()).orElse(null);
		assertAll(
			() -> assertThat(find).isNotNull(),
			() -> assertThat(find).isEqualTo(save)
		);
	}

	@Test
	void findByIdAndDeletedFalseTest() {
		this.save.setDeleted(false);
		Answer answer = repository.findByIdAndDeletedFalse(this.save.getId()).orElse(Answer.NONE);
		assertThat(answer).isEqualTo(answer);

		this.save.setDeleted(true);
		repository.save(this.save);
		answer = repository.findByIdAndDeletedFalse(this.save.getId()).orElse(Answer.NONE);
		assertThat(answer).isEqualTo(Answer.NONE);
	}
}
