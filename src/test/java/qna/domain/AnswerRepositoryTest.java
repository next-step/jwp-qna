package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class AnswerRepositoryTest {

	@Autowired
	private AnswerRepository answers;

	@Test
	@DisplayName("Answer 저장 테스트")
	void save() {
		Answer answer = AnswerTest.A1;
		Answer actual = answers.save(answer);
		assertThat(answers.findByIdAndDeletedFalse(actual.getId()).isPresent()).isTrue();
	}

	@Test
	@DisplayName("Answer id로 Answer 조회 테스트")
	void findById() {
		Answer answer = AnswerTest.A1;
		Answer saved = answers.save(answer);
		Answer actual = answers.findByIdAndDeletedFalse(saved.getId()).get();
		assertAll(
			() -> assertThat(actual).isNotNull(),
			() -> assertThat(actual.getWriterId()).isEqualTo(answer.getId()),
			() -> assertThat(actual.getContents()).isEqualTo(answer.getContents()),
			() -> assertThat(actual.isDeleted()).isEqualTo(false),
			() -> assertThat(actual.getCreateAt()).isNotNull()
		);
	}

	@Test
	@DisplayName("Answer 수정 테스트")
	void update() {
		Answer answer = AnswerTest.A1;
		Answer saved = answers.save(answer);
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
		Answer answer = AnswerTest.A1;
		Answer saved = answers.save(answer);
		long savedId = saved.getId();
		answers.delete(saved);
		answers.flush();
		assertThat(answers.findByIdAndDeletedFalse(savedId).isPresent()).isFalse();
	}

}
