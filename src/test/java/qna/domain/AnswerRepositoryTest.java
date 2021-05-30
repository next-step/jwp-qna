package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static qna.domain.AnswerTest.A1;
import static qna.domain.AnswerTest.A2;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AnswerRepositoryTest {

	private static final String EMPTY_ENTITY_MESSAGE = "찾는 Entity가 없습니다.";

	@Autowired
	private AnswerRepository answers;

	@Test
	@DisplayName("save 테스트")
	void saveTest() {
		// when
		Answer answerA1 = answers.save(A1);

		// then
		assertAll(() -> {
			assertThat(answerA1.getId()).isNotNull();
			assertThat(answerA1.getQuestionId()).isEqualTo(A1.getQuestionId());
			assertThat(answerA1.getWriterId()).isEqualTo(A1.getWriterId());
		});
	}

	@Test
	@DisplayName("findById test")
	void findByIdTest() {
		// given
		Answer answerA1 = answers.save(A1);

		// when
		assertThat(answers.findById(answerA1.getId())
						  .orElseThrow(() -> new NullPointerException(EMPTY_ENTITY_MESSAGE)))
			.isNotNull()
			.isSameAs(answerA1); // then

	}

	@Test
	@DisplayName("update 테스트")
	void updateTest() {
		// given
		String expectContents = "update";
		Answer expected = answers.save(A1);

		// when
		expected.setContents(expectContents);

		// then
		assertThat(answers.findByIdAndDeletedFalse(expected.getId())
						  .orElseThrow(() -> new NullPointerException(EMPTY_ENTITY_MESSAGE)))
			.isNotNull()
			.extracting(answer -> answer.getContents())
			.isEqualTo(expectContents);
	}

	@Test
	@DisplayName("존재하지 않는 PK로 findById test")
	void findByIdTestWithNull() {
		assertThatThrownBy(() -> answers.findById(0L).orElseThrow(() -> new NullPointerException(EMPTY_ENTITY_MESSAGE)))
			.isInstanceOf(NullPointerException.class)
			.hasMessageContaining(EMPTY_ENTITY_MESSAGE);
	}

	@Test
	@DisplayName("question_id 값으로 deleted 컬럼이 false인 Answer 확인")
	void findByQuestionIdAndDeletedFalseTest() {
		// given
		Answer answerA1 = answers.save(A1);

		A2.setDeleted(true);
		answers.save(A2);

		// when
		assertThat(answers.findByQuestionIdAndDeletedFalse(QuestionTest.Q1.getId()))
			.isNotEmpty()
			.containsOnly(answerA1); // then
	}

	@Test
	@DisplayName("PK로 deleted 컬럼이 false인 Answer 확인")
	void findByIdAndDeletedFalseTest() {
		// given
		Answer answerA1 = answers.save(A1);

		// when
		assertThat(answers.findByIdAndDeletedFalse(answerA1.getId())
						  .orElseThrow(() -> new NullPointerException(EMPTY_ENTITY_MESSAGE)))
			.isNotNull() // then
			.isSameAs(answerA1);
	}

	@Test
	@DisplayName("PK로 deleted 컬럼이 true Answer 확인")
	void findByIdAndDeletedFalseTestWithDeletedTrueEntity() {
		// given
		answers.save(A1);

		A2.setDeleted(true);
		Answer answerA2 = answers.save(A2);

		// when
		assertThatThrownBy(() -> answers.findByIdAndDeletedFalse(answerA2.getId())
										.orElseThrow(() -> new NullPointerException(EMPTY_ENTITY_MESSAGE)))
			.isInstanceOf(NullPointerException.class) // then
			.hasMessageContaining(EMPTY_ENTITY_MESSAGE);
	}


}
