package qna.domain.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.entity.Answer;
import qna.domain.entity.Question;
import qna.domain.entity.User;

@DataJpaTest
public class AnswerRepositoryTest {

	@Autowired
	private UserRepository users;
	@Autowired
	private AnswerRepository answers;
	@Autowired
	private QuestionRepository questions;
	private User 자바지기;
	private User 산지기;
	private Answer 자바지기_답변;
	private Answer 산지기_답변;
	private User 영속화된_자바지기;
	private User 영속화된_산지기;
	private Question 영속화된_자바지기_질문;
	private Question 영속화된_산지기_질문;

	@DisplayName("테스트 초기화")
	@BeforeEach
	void setup() {
		초기_정보_저장();
	}

	private void 초기_정보_저장() {
		각_유저_영속화();
		각_질문정보_영속화();
		답변_인스턴스_생성();
	}

	private void 각_유저_영속화() {
		자바지기 = User.generate(1L, "javajigi", "password1", "name1", "javajigi@slipp.net");
		영속화된_자바지기 = users.save(자바지기);
		산지기 = User.generate(2L, "sanjigi", "password2", "name2", "sanjigi@slipp.net");
		영속화된_산지기 = users.save(산지기);
	}

	private void 각_질문정보_영속화() {
		Question 임시_질문 = Question.generate(1L, "title1", "contents1").writeBy(영속화된_자바지기);
		임시_질문.writeBy(영속화된_자바지기);
		영속화된_자바지기_질문 = questions.save(임시_질문);

		임시_질문 = Question.generate(2L, "title2", "contents2").writeBy(영속화된_산지기);
		임시_질문.writeBy(영속화된_산지기);
		영속화된_산지기_질문 = questions.save(임시_질문);
	}

	private void 답변_인스턴스_생성() {
		자바지기_답변 = Answer.generate(영속화된_자바지기, 영속화된_자바지기_질문, "Answers Contents1");
		산지기_답변 = Answer.generate(영속화된_산지기, 영속화된_산지기_질문, "Answers Contents2");
	}

	@Test
	void 저장() {
		//given

		//when
		Answer 영속화된_자바지기_답변 = answers.save(자바지기_답변);
		Answer 영속화된_산지기_답변 = answers.save(산지기_답변);

		//then
		assertAll(
			() -> assertThat(영속화된_자바지기_답변).isNotNull(),
			() -> assertThat(영속화된_산지기_답변).isNotNull()
		);
	}

	@Test
	void 답변_아아디로_조회() {
		//given
		Answer 영속화된_자바지기_답변 = answers.save(자바지기_답변);
		Answer 영속화된_산지기_답변 = answers.save(산지기_답변);

		//when
		Answer 조회한_자바지기_답변 = answers.findById(영속화된_자바지기_답변.id()).get();
		Answer 조회한_산지기_답변 = answers.findById(영속화된_산지기_답변.id()).get();

		//then
		assertAll(
			() -> assertThat(조회한_자바지기_답변.equals(영속화된_자바지기_답변)).isTrue(),
			() -> assertThat(조회한_산지기_답변.equals(영속화된_산지기_답변)).isTrue()
		);
	}

	@Test
	void 삭제되지_않은_답변_아이디로_조회() {
		//given
		Answer 영속화된_자바지기_답변 = answers.save(자바지기_답변);

		//when
		Optional<Answer> 조회한_자바지기_답변 = answers.findByIdAndDeletedFalse(영속화된_자바지기_답변.id());

		//then
		assertThat(조회한_자바지기_답변.isPresent()).isTrue();
	}

	@Test
	void 논리삭제() {
		//given
		Answer 영속화된_자바지기_답변 = answers.save(자바지기_답변);

		//when
		Optional<Answer> 삭제전_자바지기_답변 = answers.findByIdAndDeletedFalse(영속화된_자바지기_답변.id());
		영속화된_자바지기_답변.delete(영속화된_자바지기);
		Optional<Answer> 삭제후_자바지기_답변 = answers.findByIdAndDeletedFalse(영속화된_자바지기_답변.id());

		//then
		assertAll(
			() -> assertThat(삭제전_자바지기_답변.isPresent()).isTrue(),
			() -> assertThat(삭제후_자바지기_답변.isPresent()).isFalse()
		);
	}

	@Test
	void 작성자_변경() {
		//given
		Answer 영속화된_자바지기_답변 = answers.save(자바지기_답변);

		//when
		영속화된_자바지기_답변.writtenBy(영속화된_산지기);
		Optional<Answer> 조회한_자바지기_답변 = answers.findById(영속화된_자바지기_답변.id());

		//then
		assertAll(
			() -> assertThat(조회한_자바지기_답변.get().writer().equals(영속화된_산지기)).isTrue(),
			() -> assertThat(조회한_자바지기_답변.get().writer().equals(영속화된_자바지기)).isFalse()
		);
	}

	@Test
	void 질문_변경() {
		//given
		Answer 영속화된_자바지기_답변 = answers.save(자바지기_답변);

		//when
		영속화된_자바지기_답변.changeQuestion(영속화된_산지기_질문);
		Optional<Answer> 조회한_자바지기_답변 = answers.findById(영속화된_자바지기_답변.id());

		//then
		assertAll(
			() -> assertThat(조회한_자바지기_답변.get().question().equals(영속화된_산지기_질문)).isTrue(),
			() -> assertThat(조회한_자바지기_답변.get().question().equals(영속화된_자바지기_질문)).isFalse()
		);
	}

	@Test
	void 물리삭제() {
		//given
		Answer 영속화된_자바지기_답변 = answers.save(자바지기_답변);
		Answer 삭제전_조회한_자바지기_답변 = answers.findById(영속화된_자바지기_답변.id()).get();

		//when
		answers.delete(영속화된_자바지기_답변);
		Optional<Answer> 삭제후_조회한_자바지기_답변 = answers.findById(영속화된_자바지기_답변.id());

		//then
		assertAll(
			() -> assertThat(삭제전_조회한_자바지기_답변).isNotNull(),
			() -> assertThat(삭제후_조회한_자바지기_답변.isPresent()).isFalse()
		);
	}
}
