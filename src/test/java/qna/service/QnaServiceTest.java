package qna.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import qna.domain.aggregate.DeleteHistoryGroup;
import qna.domain.entity.Answer;
import qna.domain.entity.Question;
import qna.domain.entity.User;
import qna.domain.repository.AnswerRepository;
import qna.domain.repository.QuestionRepository;
import qna.exception.CannotDeleteException;

@ExtendWith(MockitoExtension.class)
class QnaServiceTest {

	public static final User 자바지기 = User.generate(1L, "javajigi", "password1", "name1",
		"javajigi@slipp.net");
	public static final User 산지기 = User.generate(2L, "sanjigi", "password2", "name2",
		"sanjigi@slipp.net");

	@Mock
	private QuestionRepository questionRepository;

	@Mock
	private AnswerRepository answerRepository;

	@Mock
	private DeleteHistoryService deleteHistoryService;

	@InjectMocks
	private QnaService qnaService;

	private Question 자바지기_질문;
	private Answer 자바지기_답변;

	@BeforeEach
	public void setUp() throws Exception {
		자바지기_질문 = Question.generate(1L, "title1", "contents1").writeBy(자바지기);
		자바지기_답변 = Answer.generate(1L, 자바지기, 자바지기_질문, "Answers Contents1");
	}

	@Test
	public void delete_성공() throws Exception {
		//given

		//when
		assertThat(자바지기_질문.isDeleted()).isFalse();
		DeleteHistoryGroup deleteHistoryGroup = qnaService
			.deleteQuestion(자바지기, 자바지기_질문);

		//then
		assertThat(자바지기_질문.isDeleted()).isTrue();
		verifyDeleteHistories(deleteHistoryGroup);
	}

	@Test
	public void delete_다른_사람이_쓴_글() {
		//given

		//when

		//then
		assertThatThrownBy(() -> qnaService.deleteQuestion(산지기, 자바지기_질문))
			.isInstanceOf(CannotDeleteException.class);
	}

	@Test
	public void delete_성공_질문자_답변자_같음() throws Exception {
		//when
		DeleteHistoryGroup deleteHistoryGroup = qnaService
			.deleteQuestion(자바지기, 자바지기_질문);

		//then
		assertThat(자바지기_질문.isDeleted()).isTrue();
		assertThat(자바지기_답변.isDeleted()).isTrue();
		verifyDeleteHistories(deleteHistoryGroup);
	}

	@Test
	public void delete_답변_중_다른_사람이_쓴_글() {
		//given
		Answer 산지기_답변 = Answer.generate(2L, 산지기, 자바지기_질문, "Answers Contents1");
		자바지기_질문.addAnswer(산지기_답변);

		//when

		//then
		assertThatThrownBy(
			() -> qnaService.deleteQuestion(자바지기, 자바지기_질문)).isInstanceOf(CannotDeleteException.class);
	}

	private void verifyDeleteHistories(DeleteHistoryGroup 삭제이력그룹) {
		verify(deleteHistoryService).saveAll(삭제이력그룹.deleteHistories());
	}
}
