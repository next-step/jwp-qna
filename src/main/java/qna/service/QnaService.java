package qna.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import qna.CannotDeleteException;
import qna.NotFoundException;
import qna.domain.*;

import java.util.ArrayList;
import java.util.List;

@Service
public class QnaService {

    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final DeleteHistoryService deleteHistoryService;

    public QnaService(QuestionRepository questionRepository, AnswerRepository answerRepository, DeleteHistoryService deleteHistoryService) {
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.deleteHistoryService = deleteHistoryService;
    }

    @Transactional(readOnly = true)
    public Question findQuestionById(Question question) {
        return questionRepository.findByIdAndDeletedFalse(question.getId())
                .orElseThrow(NotFoundException::new);
    }

    @Transactional(readOnly = true)
    public List<Answer> findAnswersByQuestionId(Question deleteQuestion) {
        return answerRepository.findByQuestionIdAndDeletedFalse(deleteQuestion.getId());
    }

    @Transactional
    public void deleteQuestion(User loginUser, Question deleteQuestion) throws CannotDeleteException {
        List<DeleteHistory> deleteHistories = new ArrayList<>();

        Question question = findQuestionById(deleteQuestion);
        deleteHistories.add(question.delete(loginUser));

        List<Answer> deleteAnswers = findAnswersByQuestionId(deleteQuestion);
        deleteHistories.addAll(question.deleteQuestionByAnswers(loginUser, deleteAnswers));

        deleteHistoryService.saveAll(deleteHistories);
    }

}
