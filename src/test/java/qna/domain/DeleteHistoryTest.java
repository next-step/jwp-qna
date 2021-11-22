package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class DeleteHistoryTest {
	public static final DeleteHistory HISTORY = new DeleteHistory(ContentType.ANSWER, AnswerTest.A1.getId(),
		UserTest.JAVAJIGI);

	@Autowired
	private DeleteHistoryRepository deleteHistoryRepository;

	@Autowired
	private UserRepository userRepository;

	@Test
	@DisplayName("삭제 이력 남기기")
	void save() {
		final DeleteHistory actual = deleteHistoryRepository.save(HISTORY);
		assertAll(
			() -> assertThat(actual.getId()).isNotNull(),
			() -> assertThat(actual.getEraser().getName()).isEqualTo(UserTest.JAVAJIGI.getName())
		);
	}

	@Test
	@DisplayName("삭제를 실행한 유저 확인")
	void findDeleteHistoryWithUser() {
		final DeleteHistory actual = deleteHistoryRepository.save(HISTORY);
		final User user = userRepository.findByUserId(actual.getEraser().getUserId()).get();
		assertThat(actual.getEraser()).isEqualTo(user);
	}
}
