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
import qna.CannotDeleteException;
import qna.domain.Answer;
import qna.domain.AnswerRepository;
import qna.domain.DeleteHistoryGroup;
import qna.domain.Question;
import qna.domain.QuestionRepository;
import qna.domain.User;

@ExtendWith(MockitoExtension.class)
class QnaServiceTest {

	public static final User JAVAJIGI = User.generate(1L, "javajigi", "password1", "name1",
		"javajigi@slipp.net");
	public static final User SANJIGI = User.generate(2L, "sanjigi", "password2", "name2",
		"sanjigi@slipp.net");

	@Mock
	private QuestionRepository questionRepository;

	@Mock
	private AnswerRepository answerRepository;

	@Mock
	private DeleteHistoryService deleteHistoryService;

	@InjectMocks
	private QnaService qnaService;

	private Question questionWrittenByJavajigi;
	private Answer answerWrittenByJavajigi;

	@BeforeEach
	public void setUp() throws Exception {
		questionWrittenByJavajigi = Question.generate(1L, "title1", "contents1").writeBy(JAVAJIGI);
		answerWrittenByJavajigi = Answer.generate(1L, JAVAJIGI, questionWrittenByJavajigi,
			"Answers Contents1");
		questionWrittenByJavajigi.addAnswer(answerWrittenByJavajigi);
	}

	@DisplayName("delete_성공")
	@Test
	public void deleteSuccess() throws Exception {
		//given

		//when
		assertThat(questionWrittenByJavajigi.isDeleted()).isFalse();
		DeleteHistoryGroup deleteHistoryGroup = qnaService.deleteQuestion(JAVAJIGI, questionWrittenByJavajigi);

		//then
		assertThat(questionWrittenByJavajigi.isDeleted()).isTrue();
		verifyDeleteHistories(deleteHistoryGroup);
	}

	@DisplayName("delete_다른_사람이_쓴_글")
	@Test
	public void deletePostWrittenByTheOthers() {
		//given

		//when

		//then
		assertThatThrownBy(() -> qnaService.deleteQuestion(SANJIGI, questionWrittenByJavajigi))
			.isInstanceOf(CannotDeleteException.class);
	}

	@DisplayName("delete_성공_질문자_답변자_같음")
	@Test
	public void deleteSamePostByQuestionerAndAnswerer() throws Exception {
		//when
		DeleteHistoryGroup deleteHistoryGroup = qnaService.deleteQuestion(JAVAJIGI, questionWrittenByJavajigi);

		//then
		assertThat(questionWrittenByJavajigi.isDeleted()).isTrue();
		assertThat(answerWrittenByJavajigi.isDeleted()).isTrue();
		verifyDeleteHistories(deleteHistoryGroup);
	}

	@DisplayName("delete_답변_중_다른_사람이_쓴_글")
	@Test
	public void deletePostingWrittenByTheOthers() {
		//given
		Answer answer2 = Answer.generate(2L, SANJIGI, questionWrittenByJavajigi, "Answers Contents1");
		questionWrittenByJavajigi.addAnswer(answer2);

		//when

		//then
		assertThatThrownBy(
			() -> qnaService.deleteQuestion(JAVAJIGI, questionWrittenByJavajigi))
			.isInstanceOf(CannotDeleteException.class);
	}

	private void verifyDeleteHistories(DeleteHistoryGroup deleteHistoryGroup) {
		verify(deleteHistoryService).saveAll(deleteHistoryGroup.deleteHistories());
	}
}
