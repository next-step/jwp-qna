package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
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
public class DeleteHistoryTest {
	public static DeleteHistory DELETE_HISTORY_1 = new DeleteHistory(ContentType.QUESTION,
		QuestionTest.Q1.getId(), QuestionTest.Q1.getWriterId(), LocalDateTime.now());
	public static DeleteHistory DELETE_HISTORY_2 = new DeleteHistory(ContentType.ANSWER,
		AnswerTest.A2.getId(), AnswerTest.A2.getWriterId(), LocalDateTime.now());

	@Autowired
	private DeleteHistoryRepository deleteHistories;

	@DisplayName("DeleteHistory 저장 : save()")
	@Test
	@Order(1)
	void save() {
		//given

		//when
		DeleteHistory actual = deleteHistories.save(DELETE_HISTORY_1);
		DeleteHistory actual2 = deleteHistories.save(DELETE_HISTORY_2);

		//then
		assertAll(
			() -> assertThat(actual.equals(DELETE_HISTORY_1)).isTrue(),
			() -> assertThat(actual2.equals(DELETE_HISTORY_2)).isTrue()
		);
	}

	@DisplayName("DeleteHistory 조회 : findById()")
	@Test
	@Order(2)
	void findById() {
		//given
		DeleteHistory expected = deleteHistories.save(DELETE_HISTORY_1);
		DeleteHistory expected2 = deleteHistories.save(DELETE_HISTORY_2);

		//when
		DeleteHistory actual = deleteHistories.findById(expected.getId()).get();
		DeleteHistory actual2 = deleteHistories.findById(expected2.getId()).get();

		//then
		assertAll(
			() -> assertThat(actual.equals(expected)).isTrue(),
			() -> assertThat(actual2.equals(expected2)).isTrue()
		);
	}

	@DisplayName("DeleteHistory 삭제 : delete()")
	@Test
	@Order(3)
	void delete() {
		//given
		DeleteHistory expected = deleteHistories.save(DELETE_HISTORY_1);
		DeleteHistory beforeDeleteDeleteHistory = deleteHistories.findById(expected.getId()).get();

		//when
		deleteHistories.delete(expected);
		deleteHistories.flush();
		Optional<DeleteHistory> afterDeleteDeleteHistoryOptional = deleteHistories.findById(expected.getId());

		//then
		assertAll(
			() -> assertThat(beforeDeleteDeleteHistory).isNotNull(),
			() -> assertThat(afterDeleteDeleteHistoryOptional.isPresent()).isFalse()
		);
	}
}
