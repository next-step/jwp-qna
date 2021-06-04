package qna.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import qna.CannotDeleteException;
import qna.NotFoundException;
import qna.domain.DeleteHistory;
import qna.domain.Question;
import qna.domain.QuestionRepository;
import qna.domain.User;

@Service
public class QnAService {
	private static final Logger log = LoggerFactory.getLogger(QnAService.class);

	private QuestionRepository questionRepository;
	// private AnswerRepository answerRepository;
	private DeleteHistoryService deleteHistoryService;

	public QnAService(QuestionRepository questionRepository,
		DeleteHistoryService deleteHistoryService) {
		this.questionRepository = questionRepository;
		// this.answerRepository = answerRepository;
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
		List<DeleteHistory> deleteHistories = question.delete(loginUser);
		deleteHistoryService.saveAll(deleteHistories);
	}
}
