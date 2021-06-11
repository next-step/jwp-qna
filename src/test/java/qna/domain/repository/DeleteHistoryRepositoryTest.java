package qna.domain.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import qna.domain.entity.Answer;
import qna.domain.entity.DeleteHistory;
import qna.domain.entity.Question;
import qna.domain.entity.User;

@DataJpaTest
public class DeleteHistoryRepositoryTest {

	@Autowired
	private DeleteHistoryRepository deleteHistories;
	@Autowired
	private UserRepository users;
	@Autowired
	private AnswerRepository answers;
	@Autowired
	private QuestionRepository questions;
	private User 자바지기;
	private User 산지기;
	private User 영속화된_자바지기;
	private User 영속화된_산지기;
	private Question 영속화된_자바지기_질문;
	private Answer 영속화된_산지기_답변;
	private DeleteHistory 자바지기_질문_이력;
	private DeleteHistory 산지기_답변_이력;

	@BeforeEach
	void 초기화() {
		초기_정보_저장();
	}

	private void 초기_정보_저장() {
		작성자정보_영속화();
		질문정보_영속화();
		답변정보_영속화();
		삭제이력_객체_생성();
	}

	private void 작성자정보_영속화() {
		자바지기 = User.generate(1L, "javajigi", "password1", "name1", "javajigi@slipp.net");
		영속화된_자바지기 = users.save(자바지기);
		산지기 = User.generate(2L, "sanjigi", "password2", "name2", "sanjigi@slipp.net");
		영속화된_산지기 = users.save(산지기);
	}

	private void 질문정보_영속화() {
		영속화된_자바지기_질문 = questions.save(Question.generate(1L, "title1", "contents1")
			.writeBy(영속화된_자바지기));
	}

	private void 답변정보_영속화() {
		영속화된_산지기_답변 = answers
			.save(Answer.generate(영속화된_산지기, 영속화된_자바지기_질문, "Answers Contents1"));
	}

	private void 삭제이력_객체_생성() {
		자바지기_질문_이력 = DeleteHistory.ofQuestion(영속화된_자바지기_질문.id(), 영속화된_자바지기_질문.writer());
		산지기_답변_이력 = DeleteHistory.ofAnswer(영속화된_산지기_답변.id(), 영속화된_산지기_답변.writer());
	}

	@Test
	void 저장() {
		//given

		//when
		DeleteHistory 영속화된_자바지기_질문_이력 = deleteHistories.save(자바지기_질문_이력);
		DeleteHistory 영속화된_산지기_답변_이력 = deleteHistories.save(산지기_답변_이력);

		//then
		assertAll(
			() -> assertThat(영속화된_자바지기_질문_이력.equals(자바지기_질문_이력)).isTrue(),
			() -> assertThat(영속화된_산지기_답변_이력.equals(산지기_답변_이력)).isTrue()
		);
	}

	@Test
	void 아이디로_조회() {
		//given
		DeleteHistory 영속화된_자바지기_질문_이력 = deleteHistories.save(자바지기_질문_이력);
		DeleteHistory 영속화된_산지기_답변_이력 = deleteHistories.save(산지기_답변_이력);

		//when
		DeleteHistory 조회한_자바지기_질문_이력 = deleteHistories.findById(영속화된_자바지기_질문_이력.id()).get();
		DeleteHistory 조회한_산지기_답변_이력 = deleteHistories.findById(영속화된_산지기_답변_이력.id()).get();

		//then
		assertAll(
			() -> assertThat(조회한_자바지기_질문_이력.equals(영속화된_자바지기_질문_이력)).isTrue(),
			() -> assertThat(조회한_산지기_답변_이력.equals(영속화된_산지기_답변_이력)).isTrue()
		);
	}

	@Test
	void 물리삭제() {
		//given
		DeleteHistory 영속화된_자바지기_질문_이력 = deleteHistories.save(자바지기_질문_이력);
		DeleteHistory 삭제전_조회한_자바지기_질문_이력 = deleteHistories.findById(영속화된_자바지기_질문_이력.id()).get();

		//when
		deleteHistories.delete(영속화된_자바지기_질문_이력);
		Optional<DeleteHistory> 삭제후_조회한_자바지기_질문_이력 = deleteHistories.findById(영속화된_자바지기_질문_이력.id());

		//then
		assertAll(
			() -> assertThat(삭제전_조회한_자바지기_질문_이력).isNotNull(),
			() -> assertThat(삭제후_조회한_자바지기_질문_이력.isPresent()).isFalse()
		);
	}
}
