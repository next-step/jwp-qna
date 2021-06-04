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
public class QuestionTest {
	public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
	public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

	@Autowired
	private QuestionRepository questions;

	@DisplayName("Question 저장 : save()")
	@Test
	@Order(1)
	void save() {
		//given

		//when
		Question actual = questions.save(Q1);
		Question actual2 = questions.save(Q2);

		//then
		assertAll(
			() -> assertThat(actual.getId()).isNotNull(),
			() -> assertThat(actual.isOwner(UserTest.JAVAJIGI)).isTrue(),
			() -> assertThat(actual.getTitle()).isEqualTo(Q1.getTitle()),
			() -> assertThat(actual.getContents()).isEqualTo(Q1.getContents()),
			() -> assertThat(actual2.getId()).isNotNull(),
			() -> assertThat(actual2.isOwner(UserTest.SANJIGI)).isTrue(),
			() -> assertThat(actual2.getTitle()).isEqualTo(Q2.getTitle()),
			() -> assertThat(actual2.getContents()).isEqualTo(Q2.getContents())
		);
	}

	@DisplayName("Question 조회 : findById()")
	@Test
	@Order(2)
	void findById() {
		//given
		Question expected = questions.save(Q1);
		Question expected2 = questions.save(Q2);

		//when
		Question actual = questions.findById(expected.getId()).get();
		Question actual2 = questions.findById(expected2.getId()).get();

		//then
		assertAll(
			() -> assertThat(actual.getId()).isNotNull(),
			() -> assertThat(actual.isOwner(UserTest.JAVAJIGI)).isTrue(),
			() -> assertThat(actual.getTitle()).isEqualTo(Q1.getTitle()),
			() -> assertThat(actual.getContents()).isEqualTo(Q1.getContents()),
			() -> assertThat(actual2.getId()).isNotNull(),
			() -> assertThat(actual2.isOwner(UserTest.SANJIGI)).isTrue(),
			() -> assertThat(actual2.getTitle()).isEqualTo(Q2.getTitle()),
			() -> assertThat(actual2.getContents()).isEqualTo(Q2.getContents())
		);
	}

	@DisplayName("Question Soft delete - 조회 : findByIdAndDeletedFalse(), 수정 : setDeleted()")
	@Test
	@Order(3)
	void setQuestionId() {
		//given
		Question expected = questions.save(Q1);

		//when
		Optional<Question> beforeSoftDelete = questions.findByIdAndDeletedFalse(expected.getId());
		expected.setDeleted(true);
		Optional<Question> afterSoftDelete = questions.findByIdAndDeletedFalse(expected.getId());

		//then
		assertAll(
			() -> assertThat(beforeSoftDelete.isPresent()).isTrue(),
			() -> assertThat(afterSoftDelete.isPresent()).isFalse()
		);
	}

	@DisplayName("Question 삭제 : delete()")
	@Test
	@Order(4)
	void delete() {
		//given
		Question expected = questions.save(Q1);
		Question beforeDeleteQuestion = questions.findById(expected.getId()).get();

		//when
		questions.delete(expected);
		questions.flush();
		Optional<Question> afterDeleteQuestionOptional = questions.findById(expected.getId());

		//then
		assertAll(
			() -> assertThat(beforeDeleteQuestion).isNotNull(),
			() -> assertThat(afterDeleteQuestionOptional.isPresent()).isFalse()
		);
	}
}
