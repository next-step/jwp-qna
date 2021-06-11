package qna.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import qna.domain.entity.Answer;
import qna.domain.entity.Question;
import qna.domain.entity.User;
import qna.domain.repository.AnswerRepository;
import qna.domain.repository.QuestionRepository;
import qna.domain.repository.UserRepository;
import qna.exception.CannotDeleteException;

@SpringBootTest
class QnaServiceWithoutMockitoTest {

	@Autowired
	private QuestionRepository questionRepository;
	@Autowired
	private AnswerRepository answerRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private QnaService qnaService;

	private User 자바지기;
	private User 산지기;
	private Question 자바지기_질문;

	@BeforeEach
	public void setUp() {
		사용자_영속화();
		질문_영속화();
	}

	private void 사용자_영속화() {
		User 임시유저 = User.generate(1L, "javajigi", "password1", "name1", "javajigi@slipp.net");
		자바지기 = userRepository.save(임시유저);
		임시유저 = User.generate(2L, "sanjigi", "password2", "name2", "sanjigi@slipp.net");
		산지기 = userRepository.save(임시유저);
	}

	private void 질문_영속화() {
		Question 임시질문 = Question.generate(1L, "title1", "contents1").writeBy(자바지기);
		자바지기_질문 = questionRepository.save(임시질문);
	}

	@DisplayName("Scenario: 사용자와 작성자가 동일하지 않은 질문을 삭제 시도한다.")
	@Test
	void 사용자_작성자_동일하지않은_질문_삭제() {
		//given
		Question 답변없는_자바지기_질문 = 자바지기_질문;

		//when

		//then
		assertThatThrownBy(() -> qnaService.deleteQuestion(산지기, 답변없는_자바지기_질문))
			.isInstanceOf(CannotDeleteException.class);
	}

	@DisplayName("Scenario: 사용자와 작성자가 동일한 질문을 삭제 시도한다. 질문의 답변은 존재하지 않는다.")
	@Test
	void 사용자_작성자_동일한_질문_삭제() {
		//given
		Question 답변없는_자바지기_질문 = 자바지기_질문;

		//when
		qnaService.deleteQuestion(자바지기, 답변없는_자바지기_질문);

		//then
		assertThat(질문_삭제_여부(자바지기_질문)).isTrue();
	}

	@DisplayName("Scenario: 사용자와 작성자가 동일한 질문을 삭제 시도한다. 질문의 답변은 존재하며 본인의 답변이다.")
	@Test
	void 사용자_작성자_동일한_질문_삭제_본인_답변만_존재() {
		//given
		Answer 자바지기_답변 = Answer.generate(1L, 자바지기, 자바지기_질문, "Answers Contents2");
		자바지기_답변 = answerRepository.save(자바지기_답변);
		Question 답변_존재_자바지기_질문 = 자바지기_질문;

		//when
		qnaService.deleteQuestion(자바지기, 답변_존재_자바지기_질문);

		//then
		assertThat(질문_삭제_여부(답변_존재_자바지기_질문)).isTrue();
		assertThat(답변_삭제_여부(자바지기_답변)).isTrue();
	}

	@DisplayName("Scenario: 사용자와 작성자가 동일한 질문을 삭제 시도한다. 질문의 답변은 존재하며 타인이 작성한 답변도 있다. ")
	@Test
	void 사용자_작성자_동일한_질문_삭제_본인_이외_답변_존재() {
		//given
		Answer 산지기_답변 = answerRepository.save(Answer.generate(2L, 산지기, 자바지기_질문, "Answers Contents3"));
		Question 답변_존재_자바지기_질문 = 자바지기_질문;

		//when

		//then
		assertThatThrownBy(() -> qnaService.deleteQuestion(산지기, 답변_존재_자바지기_질문))
			.isInstanceOf(CannotDeleteException.class);
	}

	private boolean 질문_삭제_여부(Question 질문) {
		Optional<Question> 조회한_질문 = questionRepository.findById(질문.id());
		if (조회한_질문.isPresent()) {
			return 조회한_질문.get().isDeleted();
		}
		return false;
	}

	private boolean 답변_삭제_여부(Answer 답변) {
		Optional<Answer> 조회한_답변 = answerRepository.findById(답변.id());
		if (조회한_답변.isPresent()) {
			return 조회한_답변.get().isDeleted();
		}
		return false;
	}
}
