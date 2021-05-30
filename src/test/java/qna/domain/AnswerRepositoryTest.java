package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AnswerRepositoryTest {

	@Autowired
	private AnswerRepository answerRepository;

	private Answer expected;
	private Answer saved;

	@BeforeEach
	void setup() {
		this.expected = AnswerTest.A1;
		this.saved = this.answerRepository.save(expected);
	}

	@Test
	@DisplayName("answer 저장 확인")
	void testSave() {
		this.isEqualTo(expected, saved);
	}

	@Test
	@DisplayName("answer id 에 해당하는 엔티티가 삭제되지 않았으면 Answer entity 반환확인")
	void tesst_findByIdAndDeleteFalse() {
		Answer actual = this.answerRepository.findByIdAndDeletedFalse(saved.getId())
			.orElseThrow(() -> new EntityNotFoundException("아이디에 해당하는 엔티티가없음"));
		this.isEqualTo(expected, actual);
	}

	@Test
	void test_findByIdAndDeleteTrue() {
		this.answerRepository.delete(saved);
		Optional<Answer> answerOpt = this.answerRepository.findByIdAndDeletedFalse(saved.getId());

		Assertions.assertThat(answerOpt.isPresent()).isFalse();
	}

	@Test
	@DisplayName("question id 에해당하는 answer 목록을 반환을 테스트합니다.")
	void test_findByQuestionIdAndDeleteFalse() {
		List<Answer> actual = this.answerRepository.findByQuestionIdAndDeletedFalse(
			saved.getQuestion().getId());

		assertThat(actual).containsExactly(AnswerTest.A1);
	}

	private void isEqualTo(Answer expected, Answer actual) {
		assertAll(
			() -> assertThat(actual.getId()).isNotNull(),
			() -> assertThat(actual.getContents()).isEqualTo(expected.getContents()),
			() -> assertThat(actual.getQuestion()).isEqualTo(expected.getQuestion()),
			() -> assertThat(actual.getWriter()).isEqualTo(expected.getWriter()),
			() -> assertThat(actual.getCreatedAt()).isNotNull(),
			() -> assertThat(actual.getUpdatedAt()).isNotNull()
		);
	}
}
