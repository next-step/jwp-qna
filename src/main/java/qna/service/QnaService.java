package qna.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import qna.CannotDeleteException;
import qna.NotFoundException;
import qna.domain.User;
import qna.domain.exception.question.AnswerOwnerNotMatchedException;
import qna.domain.exception.question.QuestionOwnerNotMatchedException;
import qna.domain.history.DeleteHistoryList;
import qna.domain.question.AnswerList;
import qna.domain.question.Question;
import qna.repository.AnswerRepository;
import qna.repository.QuestionRepository;

@Service
public class QnaService {
    private static final Logger log = LoggerFactory.getLogger(QnaService.class);

    private QuestionRepository questionRepository;
    private AnswerRepository answerRepository;
    private DeleteHistoryService deleteHistoryService;

    public QnaService(QuestionRepository questionRepository, AnswerRepository answerRepository, DeleteHistoryService deleteHistoryService) {
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
        AnswerList deletedAnswers;
        try {
            deletedAnswers = question.deleteBy(loginUser);
        } catch (QuestionOwnerNotMatchedException e) {
            throw new CannotDeleteException("질문을 삭제할 권한이 없습니다.", e);
        } catch (AnswerOwnerNotMatchedException e) {
            throw new CannotDeleteException("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.", e);
        }

        DeleteHistoryList deleteHistories = new DeleteHistoryList();
        deleteHistories.addQuestionHistory(question, deletedAnswers);

        questionRepository.delete(question);
        deleteHistoryService.saveAll(deleteHistories.toList());
    }
}
