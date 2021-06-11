package qna.domain.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.entity.Question;
import qna.domain.entity.User;

@DataJpaTest
public class QuestionRepositoryTest {

	@Autowired
	private UserRepository users;
	@Autowired
	private QuestionRepository questions;
	private User 자바지기;
	private User 산지기;
	private User 영속화된_자바지기;
	private User 영속화된_산지기;
	private Question 자바지기_질문;
	private Question 산지기_질문;

	@DisplayName("테스트 초기화")
	@BeforeEach
	void setup() {
		초기_정보_저장();
	}

	private void 초기_정보_저장() {
		사용자정보_영속화();
		질문_인스턴스_생성();
	}

	private void 사용자정보_영속화() {
		자바지기 = User.generate(1L, "javajigi", "password1", "name1", "javajigi@slipp.net");
		영속화된_자바지기 = users.save(자바지기);
		산지기 = User.generate(2L, "sanjigi", "password2", "name2", "sanjigi@slipp.net");
		영속화된_산지기 = users.save(산지기);
	}

	private void 질문_인스턴스_생성() {
		자바지기_질문 = Question.generate("title1", "contents1").writeBy(영속화된_자바지기);
		산지기_질문 = Question.generate("title2", "contents2").writeBy(영속화된_산지기);
	}

	@Test
	void 저장() {
		//given

		//when
		Question 영속화된_자바지기_질문 = questions.save(자바지기_질문);
		Question 영속화된_산지기_질문 = questions.save(산지기_질문);

		//then
		assertAll(
			() -> assertThat(영속화된_자바지기_질문).isNotNull(),
			() -> assertThat(영속화된_산지기_질문).isNotNull()
		);
	}

	@Test
	void 아이디로_조회() {
		//given
		Question 영속화된_자바지기_질문 = questions.save(자바지기_질문);
		Question 영속화된_산지기_질문 = questions.save(산지기_질문);

		//when
		Question 조회한_자바지기_질문 = questions.findById(영속화된_자바지기_질문.id()).get();
		Question 조회한_산지기_질문 = questions.findById(영속화된_산지기_질문.id()).get();

		//then
		assertAll(
			() -> assertThat(조회한_자바지기_질문.equals(영속화된_자바지기_질문)).isTrue(),
			() -> assertThat(조회한_산지기_질문.equals(영속화된_산지기_질문)).isTrue()
		);
	}

	@Test
	void 삭제되지않은_질문_아이디로_조회() {
		//given
		Question 영속화된_자바지기_질문 = questions.save(자바지기_질문);

		//when
		Optional<Question> 조회한_자바지기_질문 = questions.findByIdAndDeletedFalse(영속화된_자바지기_질문.id());

		//then
		assertThat(조회한_자바지기_질문.isPresent()).isTrue();
	}

	@Test
	void 논리삭제() {
		//given
		Question 영속화된_자바지기_질문 = questions.save(자바지기_질문);
		Optional<Question> 조회한_자바지기_질문 = questions.findByIdAndDeletedFalse(영속화된_자바지기_질문.id());

		//when
		영속화된_자바지기_질문.delete(영속화된_자바지기);
		Optional<Question> 삭제후_조회한_자바지기_질문 = questions.findByIdAndDeletedFalse(영속화된_자바지기_질문.id());

		//then
		assertAll(
			() -> assertThat(조회한_자바지기_질문.isPresent()).isTrue(),
			() -> assertThat(삭제후_조회한_자바지기_질문.isPresent()).isFalse()
		);
	}

	@Test
	void 물리삭제() {
		//given
		Question 영속화된_자바지기_질문 = questions.save(자바지기_질문);
		Question 조회한_자바지기_질문 = questions.findById(영속화된_자바지기_질문.id()).get();

		//when
		questions.delete(영속화된_자바지기_질문);
		Optional<Question> 삭제후_조회한_자바지기_질문 = questions.findById(영속화된_자바지기_질문.id());

		//then
		assertAll(
			() -> assertThat(조회한_자바지기_질문).isNotNull(),
			() -> assertThat(삭제후_조회한_자바지기_질문.isPresent()).isFalse()
		);
	}
}
