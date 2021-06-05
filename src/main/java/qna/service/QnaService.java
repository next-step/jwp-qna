package qna.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import qna.CannotDeleteException;
import qna.NotFoundException;
import qna.domain.*;

import java.util.ArrayList;
import java.util.List;

@Service
public class QnaService {
	private static final Logger log = LoggerFactory.getLogger(QnaService.class);

	private final QuestionRepository questionRepository;
	private final DeleteHistoryService deleteHistoryService;

	public QnaService(QuestionRepository questionRepository, AnswerRepository answerRepository, DeleteHistoryService deleteHistoryService) {
		this.questionRepository = questionRepository;
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
		List<Answer> answers = question.getAnswers();
		for (Answer answer : answers) {
			if (!answer.isOwner(loginUser)) {
				throw new CannotDeleteException("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
			}
		}

		question.delete(loginUser);
		List<DeleteHistory> deleteHistories = new ArrayList<>();
		deleteHistories.add(new DeleteHistory(ContentType.QUESTION, questionId, question.getWriter()));
		for (Answer answer : answers) {
			answer.delete();
			deleteHistories.add(new DeleteHistory(ContentType.ANSWER, answer.getId(), answer.getWriter()));
		}
		deleteHistoryService.saveAll(deleteHistories);
	}
}
