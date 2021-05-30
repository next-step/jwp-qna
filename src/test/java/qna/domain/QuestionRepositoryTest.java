package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static qna.domain.QuestionTest.Q1;
import static qna.domain.QuestionTest.Q2;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class QuestionRepositoryTest {

	private static final String EMPTY_ENTITY_MESSAGE = "찾는 Entity가 없습니다.";

	@Autowired
	private QuestionRepository questions;

	@Test
	@DisplayName("save 테스트")
	void saveTest() {
		Question questionQ1 = questions.save(Q1);

		assertAll(() -> {
			assertThat(questionQ1).isNotNull();
			assertThat(questionQ1.getId()).isNotNull();
			assertThat(questionQ1.getTitle()).isEqualTo(Q1.getTitle());
			assertThat(questionQ1.getContents()).isEqualTo(Q1.getContents());
			assertThat(questionQ1.getWriterId()).isEqualTo(Q1.getWriterId());
		});

	}

	@Test
	@DisplayName("update 테스트")
	void updateTest() {
		// given
		String expectedTitle = "update";
		Question expected = questions.save(Q1);

		// when
		expected.setTitle(expectedTitle);

		// then
		assertThat(questions.findByIdAndDeletedFalse(expected.getId())
							.orElseThrow(() -> new NullPointerException(EMPTY_ENTITY_MESSAGE)))
			.isNotNull()
			.extracting(value -> value.getTitle())
			.isEqualTo(expectedTitle);
	}

	@Test
	@DisplayName("findById 테스트")
	void findByIdTest() {
		Question questionQ1 = questions.save(Q1);

		// when
		assertThat(questions.findById(questionQ1.getId())
						  .orElseThrow(() -> new NullPointerException(EMPTY_ENTITY_MESSAGE)))
			.isNotNull()
			.isSameAs(questionQ1); // then
	}

	@Test
	@DisplayName("delete 컬럼이 false인 모든 question 조회 테스트")
	void findByDeletedFalseTest() {
		// given
		Question questionQ1 = questions.save(Q1); // default false

		Q2.setDeleted(true);
		Question questionQ2 = questions.save(Q2); // default false

		// when
		assertThat(questions.findByDeletedFalse())
			.isNotEmpty()
			.contains(questionQ1)
			.doesNotContain(questionQ2); // then
	}

	@Test
	@DisplayName("delete 컬럼이 false인 question Id 조회 테스트")
	void findByIdAndDeletedFalseTest() {
		// given
		Question questionQ1 = questions.save(Q1); // default false

		// when
		assertThat(questions.findByIdAndDeletedFalse(questionQ1.getId())
							.orElseThrow(() -> new NullPointerException(EMPTY_ENTITY_MESSAGE)))
			.isNotNull()
			.isSameAs(questionQ1); // then
	}

	@Test
	@DisplayName("delete 컬럼이 true인 question Id 조회 테스트")
	void findByIdAndDeletedFalseTestWithDeleteTure() {
		// given
		Q2.setDeleted(true);
		Question questionQ2 = questions.save(Q2); // default false

		// when
		assertThatThrownBy(() -> questions.findByIdAndDeletedFalse(questionQ2.getId())
										  .orElseThrow(() -> new NullPointerException(EMPTY_ENTITY_MESSAGE)))
			.isInstanceOf(NullPointerException.class)
			.hasMessageContaining(EMPTY_ENTITY_MESSAGE); // then
	}
}
