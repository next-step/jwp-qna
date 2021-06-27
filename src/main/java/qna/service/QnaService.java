package qna.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import qna.CannotDeleteException;
import qna.NotFoundException;
import qna.domain.AnswerRepository;
import qna.domain.ContentType;
import qna.domain.DeleteHistory;
import qna.domain.Question;
import qna.domain.QuestionRepository;
import qna.domain.User;

@Service
public class QnaService {
	private static final Logger log = LoggerFactory.getLogger(QnaService.class);

	private QuestionRepository questionRepository;
	private AnswerRepository answerRepository;
	private DeleteHistoryService deleteHistoryService;

	public QnaService(QuestionRepository questionRepository, AnswerRepository answerRepository,
		DeleteHistoryService deleteHistoryService) {
		this.questionRepository = questionRepository;
		this.answerRepository = answerRepository;
		this.deleteHistoryService = deleteHistoryService;
	}

	@Transactional(readOnly = true)
	public Question findQuestionById(Long id) {
		return questionRepository.findByIdAndDeletedFalse(id)
			.orElseThrow(NotFoundException::new);
	}

	@Transactional
	public void deleteQuestion(User loginUser, Long questionId) throws CannotDeleteException {
		Question question = findQuestionById(questionId);
		if (!question.canDeleteQuestion(loginUser)) {
			throw new CannotDeleteException("질문을 삭제할 권한이 없습니다.");
		}

		question.delete(true);
		makeDeleteHistory(question, loginUser);
	}

	private void makeDeleteHistory(Question question, User loginUser) {
		List<DeleteHistory> deleteHistories = new ArrayList<>();
		deleteHistories.add(new DeleteHistory(ContentType.QUESTION, question.getId(), loginUser));

		question.getAnswers().stream()
			.map(answer -> new DeleteHistory(ContentType.ANSWER, answer.getId(), loginUser))
			.forEach(deleteHistory -> deleteHistories.add(deleteHistory));
		deleteHistoryService.saveAll(deleteHistories);
	}

}
