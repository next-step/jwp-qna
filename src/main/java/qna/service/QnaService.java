package qna.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import qna.CannotDeleteException;
import qna.domain.AnswerRepository;
import qna.domain.DeleteHistoryGroup;
import qna.domain.Question;
import qna.domain.QuestionRepository;
import qna.domain.User;

@Service
public class QnaService {

	private static final Logger log = LoggerFactory.getLogger(QnaService.class);

	private QuestionRepository questionRepository;
	private DeleteHistoryService deleteHistoryService;

	public QnaService(QuestionRepository questionRepository, AnswerRepository answerRepository,
		DeleteHistoryService deleteHistoryService) {
		this.questionRepository = questionRepository;
		this.deleteHistoryService = deleteHistoryService;
	}

	@Transactional
	public void deleteQuestion(User loginUser, Question question) throws CannotDeleteException {
		question.delete(loginUser);
		questionRepository.save(question);
		DeleteHistoryGroup deleteHistoryGroup = DeleteHistoryGroup.generateByQuestion(question);
		deleteHistoryService.saveAll(deleteHistoryGroup.deleteHistories());
	}
}
