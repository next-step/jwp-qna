package qna.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import qna.CannotDeleteException;
import qna.domain.Answer;
import qna.domain.AnswerRepository;
import qna.domain.Question;
import qna.domain.QuestionRepository;
import qna.domain.User;
import qna.domain.UserRepository;

@SpringBootTest
class QnaServiceWithoutMockitoTest {

	public static final User JAVAJIGI = User.generate(1L, "javajigi", "password1", "name1",
		"javajigi@slipp.net");
	public static final User SANJIGI = User.generate(2L, "sanjigi", "password2", "name2",
		"sanjigi@slipp.net");

	@Autowired
	private QuestionRepository questionRepository;
	@Autowired
	private AnswerRepository answerRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private QnaService qnaService;

	private User savedJavajigi;
	private User savedSanjigi;
	private Question questionWithAnswersWrittenByUserA;
	private Question questionWithAnswersWrittenByUserB;
	private Answer answerWrittenByUserA;
	private Answer answerWrittenByUserB;

	@BeforeEach
	public void setUp() {
		사용자_인스턴스_초기화();
	}

	private void 사용자_인스턴스_초기화() {
		savedJavajigi = userRepository.save(JAVAJIGI);
		savedSanjigi = userRepository.save(SANJIGI);
	}

	@DisplayName("Scenario: 사용자와 작성자가 동일하지 않은 질문을 삭제 시도한다.")
	@Test
	void 사용자_작성자_동일하지않은_질문_삭제() {
		//given
		Question questionWithoutAnswers = questionRepository
			.save(Question.generate(1L, "title1", "contents1").writeBy(savedJavajigi));

		//when

		//then
		삭제_시도_중_예외_발생(questionWithoutAnswers);
		assertThat(질문_삭제_여부(questionWithoutAnswers)).isFalse();
	}

	private boolean 질문_삭제_여부(Question question) {
		Optional<Question> foundQuestion = questionRepository.findById(question.id());
		if (foundQuestion.isPresent()) {
			return foundQuestion.get().isDeleted();
		}
		return false;
	}

	private void 삭제_시도_중_예외_발생(Question question) {
		assertThatThrownBy(() -> qnaService.deleteQuestion(savedSanjigi, question))
			.isInstanceOf(CannotDeleteException.class);
	}

	@DisplayName("Scenario: 사용자와 작성자가 동일한 질문을 삭제 시도한다. 질문의 답변은 존재하지 않는다.")
	@Test
	void 사용자_작성자_동일한_질문_삭제() {
		//given
		Question questionWithoutAnswers = questionRepository
			.save(Question.generate(1L, "title1", "contents1").writeBy(savedJavajigi));

		//when
		qnaService.deleteQuestion(savedJavajigi, questionWithoutAnswers);

		//then
		assertThat(질문_삭제_여부(questionWithoutAnswers)).isTrue();
	}

	@DisplayName("Scenario: 사용자와 작성자가 동일한 질문을 삭제 시도한다. 질문의 답변은 존재하며 본인의 답변이다.")
	@Test
	void 사용자_작성자_동일한_질문_삭제_본인_답변만_존재() {
		//given
		questionWithAnswersWrittenByUserA = questionRepository
			.save(Question.generate(2L, "title2", "contents2").writeBy(savedJavajigi));
		answerWrittenByUserA = answerRepository
			.save(Answer.generate(1L, savedJavajigi, questionWithAnswersWrittenByUserA,
				"Answers Contents2"));

		//when
		qnaService.deleteQuestion(savedJavajigi, questionWithAnswersWrittenByUserA);

		//then
		assertThat(질문_삭제_여부(questionWithAnswersWrittenByUserA)).isTrue();
		assertThat(답변_삭제_여부(answerWrittenByUserA)).isTrue();
	}

	private boolean 답변_삭제_여부(Answer answer) {
		Optional<Answer> foundAnswer = answerRepository.findById(answer.id());
		if (foundAnswer.isPresent()) {
			return foundAnswer.get().isDeleted();
		}
		return false;
	}

	@DisplayName("사용자와 작성자가 동일한 질문을 삭제 시도한다. 질문의 답변은 존재하며 타인이 작성한 답변도 있다. ")
	@Test
	void 사용자_작성자_동일한_질문_삭제_본인_이외_답변_존재() {
		//given
		questionWithAnswersWrittenByUserB = questionRepository
			.save(Question.generate(3L, "title3", "contents3").writeBy(savedJavajigi));
		answerWrittenByUserB = answerRepository
			.save(Answer.generate(2L, savedSanjigi, questionWithAnswersWrittenByUserB,
				"Answers Contents3"));

		//when

		//then
		삭제_시도_중_예외_발생(questionWithAnswersWrittenByUserB);
		assertThat(질문_삭제_여부(questionWithAnswersWrittenByUserB)).isFalse();
	}
}
