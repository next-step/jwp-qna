package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Date;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class AnswerRepositoryTest {

	public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
	public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

	@Autowired
	private AnswerRepository answers;

	@Test
	@DisplayName("Answer 저장 테스트")
	void save() {
		Answer expected = A1;
		Answer actual = answers.save(expected);
		assertThat(answers.findByIdAndDeletedFalse(actual.getId()).isPresent()).isTrue();
	}

	@Test
	@DisplayName("Answer id로 Answer 조회 테스트")
	void findById() {
		Answer expected = A1;
		Answer saved = answers.save(expected);
		Answer actual = answers.findByIdAndDeletedFalse(saved.getId()).get();
		assertAll(
			() -> assertThat(actual).isNotNull(),
			() -> assertThat(actual.getWriterId()).isEqualTo(expected.getId()),
			() -> assertThat(actual.getContents()).isEqualTo(expected.getContents()),
			() -> assertThat(actual.isDeleted()).isEqualTo(false),
			() -> assertThat(actual.getCreateAt()).isNotNull()
		);
	}

	@Test
	@DisplayName("Answer 수정 테스트")
	void update() {
		Answer expected = A1;
		Answer saved = answers.save(expected);
		Answer actual = answers.findByIdAndDeletedFalse(saved.getId()).get();
		Date updateAt = new Date();

		actual.setContents("수정했어요");
		actual.setUpdateAt(updateAt);
		answers.flush();
		assertThat(saved.getContents()).isEqualTo("수정했어요");
		assertThat(saved.getUpdateAt()).isEqualTo(updateAt);
	}

	@Test
	@DisplayName("Answer 삭제 테스트")
	void delete() {
		Answer expected = A1;
		Answer saved = answers.save(expected);
		answers.delete(saved);
		answers.flush();
		assertThat(answers.findByIdAndDeletedFalse(expected.getId()).isPresent()).isFalse();
	}

}
