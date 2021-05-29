package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class QuestionRepositoryTest {

	@Autowired
	private QuestionRepository questions;

	@Test
	void save() {
		Question expected = new Question("title1", "contents1");
		Question actual = questions.save(expected);
		assertAll(
			() -> assertThat(actual.getId()).isNotNull(),
			() -> assertThat(actual.getTitle()).isEqualTo(expected.getTitle()),
			() -> assertThat(actual.getContents()).isEqualTo(expected.getContents())
		);
	}
	@Test
	void findByDeletedFalse() {
		Question expected1 = new Question("title1", "contents1");
		Question expected2 = new Question("title2", "contents2");
		Question expected3 = new Question("title3", "contents3");

		expected3.setDeleted(true);

		questions.save(expected1);
		questions.save(expected2);
		questions.save(expected3);

		List<Question> actual = questions.findByDeletedFalse();
		assertThat(actual).contains(expected1, expected2);
	}

	@Test
	void findByIdAndDeletedFalse() {
		Question expected1 = new Question("title1", "contents1");
		Question expected2 = new Question("title2", "contents2");

		expected2.setDeleted(true);

		questions.save(expected1);
		questions.save(expected2);

		Question actual1 = questions.findByIdAndDeletedFalse(expected1.getId()).orElse(null);
		Question actual2 = questions.findByIdAndDeletedFalse(expected2.getId()).orElse(null);
		assertThat(actual1).isEqualTo(expected1);
		assertThat(actual2).isNull();
	}
}