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

    public QnaService(QuestionRepository questionRepository, DeleteHistoryService deleteHistoryService) {
        this.questionRepository = questionRepository;
        this.deleteHistoryService = deleteHistoryService;
    }

    @Transactional
    public void deleteQuestion(User loginUser, Long questionId) throws CannotDeleteException {
        Question question = findQuestionBy(questionId);
        question.deleteBy(loginUser);

        deleteHistoryService.saveAll(deleteHistoriesOf(question));
    }

    private List<DeleteHistory> deleteHistoriesOf(Question question) {
        List<DeleteHistory> deleteHistories = new ArrayList<>();

        deleteHistories.add(new DeleteHistory(question));
        question.getAnswers().stream().map(DeleteHistory::new).forEach(deleteHistories::add);

        return deleteHistories;
    }

    @Transactional(readOnly = true)
    public Question findQuestionBy(Long id) {
        return questionRepository.findByIdAndDeletedFalse(id).orElseThrow(NotFoundException::new);
    }
}
