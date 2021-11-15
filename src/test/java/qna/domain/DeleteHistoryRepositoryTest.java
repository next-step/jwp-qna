package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import qna.fixture.QuestionFixture;

@DataJpaTest
public class DeleteHistoryRepositoryTest {

	@Autowired
	private DeleteHistoryRepository deleteHistoryRepository;

	@Test
	@DisplayName("저장하기 전후의 객체가 서로 동일한 객체인가")
	void save() {
		final Question question = QuestionFixture.식별자가_userId인_유저가_작성한_질문("writer.id");
		final DeleteHistory expected = new DeleteHistory(ContentType.QUESTION, question.getId(), question.getWriter());
		final DeleteHistory actual = deleteHistoryRepository.save(expected);
		assertAll(
			() -> assertThat(actual).isSameAs(expected),
			() -> assertThat(actual.getId()).isNotNull(),
			() -> assertThat(actual.getCreateDate()).isNotNull()
		);
	}
}
