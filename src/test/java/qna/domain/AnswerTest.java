package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AnswerTest {
	public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
	public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

	@Autowired
	private AnswerRepository answers;

	@DisplayName("Answer 저장 : save()")
	@Test
	@Order(1)
	void save() {
		//given

		//when
		Answer actual = answers.save(A1);
		Answer actual2 = answers.save(A2);

		//then
		assertAll(
			() -> assertThat(actual.getId()).isNotNull(),
			() -> assertThat(actual.isOwner(UserTest.JAVAJIGI)).isTrue(),
			() -> assertThat(actual.getQuestionId()).isEqualTo(A1.getQuestionId()),
			() -> assertThat(actual.getWriterId()).isEqualTo(A1.getWriterId()),
			() -> assertThat(actual.getContents()).isEqualTo(A1.getContents()),
			() -> assertThat(actual2.getId()).isNotNull(),
			() -> assertThat(actual2.isOwner(UserTest.SANJIGI)).isTrue(),
			() -> assertThat(actual2.getQuestionId()).isEqualTo(A2.getQuestionId()),
			() -> assertThat(actual2.getWriterId()).isEqualTo(A2.getWriterId()),
			() -> assertThat(actual2.getContents()).isEqualTo(A2.getContents())
		);
	}

	@DisplayName("Answer 조회 : findById()")
	@Test
	@Order(2)
	void findById() {
		//given
		Answer expected = answers.save(A1);
		Answer expected2 = answers.save(A2);

		//when
		Answer actual = answers.findById(expected.getId()).get();
		Answer actual2 = answers.findById(expected2.getId()).get();

		//then
		assertAll(
			() -> assertThat(actual.getId()).isNotNull(),
			() -> assertThat(actual.isOwner(UserTest.JAVAJIGI)).isTrue(),
			() -> assertThat(actual.getQuestionId()).isEqualTo(A1.getQuestionId()),
			() -> assertThat(actual.getWriterId()).isEqualTo(A1.getWriterId()),
			() -> assertThat(actual.getContents()).isEqualTo(A1.getContents()),
			() -> assertThat(actual2.getId()).isNotNull(),
			() -> assertThat(actual2.isOwner(UserTest.SANJIGI)).isTrue(),
			() -> assertThat(actual2.getQuestionId()).isEqualTo(A2.getQuestionId()),
			() -> assertThat(actual2.getWriterId()).isEqualTo(A2.getWriterId()),
			() -> assertThat(actual2.getContents()).isEqualTo(A2.getContents())
		);
	}

	@DisplayName("Answer Soft delete - 조회 : findByIdAndDeletedFalse(), 수정 : setDeleted()")
	@Test
	@Order(3)
	void setAnswerId() {
		//given
		Answer expected = answers.save(A1);

		//when
		Optional<Answer> beforeSoftDelete = answers.findByIdAndDeletedFalse(expected.getId());
		expected.setDeleted(true);
		Optional<Answer> afterSoftDelete = answers.findByIdAndDeletedFalse(expected.getId());

		//then
		assertAll(
			() -> assertThat(beforeSoftDelete.isPresent()).isTrue(),
			() -> assertThat(afterSoftDelete.isPresent()).isFalse()
		);
	}

	@DisplayName("Answer 삭제 : delete()")
	@Test
	@Order(4)
	void delete() {
		//given
		Answer expected = answers.save(A1);
		Answer beforeDeleteAnswer = answers.findById(expected.getId()).get();

		//when
		answers.delete(expected);
		answers.flush();
		Optional<Answer> afterDeleteAnswerOptional = answers.findById(expected.getId());

		//then
		assertAll(
			() -> assertThat(beforeDeleteAnswer).isNotNull(),
			() -> assertThat(afterDeleteAnswerOptional.isPresent()).isFalse()
		);
	}
}
