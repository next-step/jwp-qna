package qna.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import qna.CannotDeleteException;
import qna.NotFoundException;
import qna.domain.DeleteHistoryRepository;
import qna.domain.Question;
import qna.domain.QuestionRepository;
import qna.domain.User;

@Service
public class QnaService {
	private static final Logger log = LoggerFactory.getLogger(QnaService.class);

	private QuestionRepository questionRepository;
	private DeleteHistoryRepository deleteHistoryRepository;

	public QnaService(final QuestionRepository questionRepository,
		final DeleteHistoryRepository deleteHistoryRepository) {
		this.questionRepository = questionRepository;
		this.deleteHistoryRepository = deleteHistoryRepository;
	}

	@Transactional(readOnly = true)
	public Question findQuestionById(Long id) {
		return questionRepository.findByIdAndDeletedFalse(id)
			.orElseThrow(NotFoundException::new);
	}

	@Transactional
	public void deleteQuestion(User loginUser, Long questionId) throws CannotDeleteException {
		Question question = findQuestionById(questionId);
		deleteHistoryRepository.saveAll(question.delete(loginUser));
	}
}
