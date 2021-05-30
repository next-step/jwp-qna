package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DeleteHistoryRepositoryTest {

	@Autowired
	private DeleteHistoryRepository deleteHistoryRepository;

	private DeleteHistory expected;
	private DeleteHistory saved;

	@BeforeEach
	void setup() {
		this.expected = new DeleteHistory(ContentType.ANSWER, 1L, UserTest.JAVAJIGI, null);
		this.saved = this.deleteHistoryRepository.save(this.expected);
	}

	@Test
	@DisplayName("deleteHistory save 테스트")
	void test() {
		this.isEqualTo(expected, saved);
	}

	@Test
	@DisplayName("deleteHistory 제거 후 아이디로 찾았을때 값이 없음을 확인")
	void test_deleteAndFind() {
		this.deleteHistoryRepository.delete(saved);
		Optional<DeleteHistory> delHistoryOpt = this.deleteHistoryRepository.findById(saved.getId());
		assertThat(delHistoryOpt.isPresent()).isFalse();
	}

	@Test
	@DisplayName("deleteHistory 목록을 찾아 반환확인")
	void test_deleteHistoryList() {
		List<DeleteHistory> actual = this.deleteHistoryRepository.findAll();
		assertThat(actual.size()).isEqualTo(1);
	}

	private void isEqualTo(DeleteHistory expected, DeleteHistory actual) {
		assertAll(
			() -> assertThat(actual.getId()).isNotNull(),
			() -> assertThat(actual.getContentId()).isEqualTo(expected.getContentId()),
			() -> assertThat(actual.getDeletedBy()).isEqualTo(expected.getDeletedBy()),
			() -> assertThat(actual.getCreateDate()).isEqualTo(expected.getCreateDate()),
			() -> assertThat(actual.getContentType()).isEqualTo(expected.getContentType())
		);
	}
}