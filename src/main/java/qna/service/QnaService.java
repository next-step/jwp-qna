package qna.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import qna.CannotDeleteException;
import qna.NotFoundException;
import qna.domain.AnswerRepository;
import qna.domain.Answers;
import qna.domain.DeleteHistory;
import qna.domain.Question;
import qna.domain.QuestionRepository;
import qna.domain.User;
import qna.domain.dto.DeleteHistoryCombiner;

@Service
public class QnaService {
    private static final Logger log = LoggerFactory.getLogger(QnaService.class);

    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final DeleteHistoryService deleteHistoryService;

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
    public void deleteQuestion(User loginUser, Question question) throws CannotDeleteException {

        Question findQuestion = findQuestionById(question.getId());
        DeleteHistory deleteQuestion = findQuestion.delete(loginUser);

        List<DeleteHistory> deleteAnswers = findQuestion.getAnswers()
                                                        .excludeDeleteTrueAnswers()
                                                        .delete(loginUser);

        DeleteHistoryCombiner deleteHistoryCombiner = new DeleteHistoryCombiner();
        deleteHistoryCombiner.add(deleteQuestion);
        deleteHistoryCombiner.add(deleteAnswers);

        deleteHistoryService.saveAll(deleteHistoryCombiner.getCombiner());
    }
}
