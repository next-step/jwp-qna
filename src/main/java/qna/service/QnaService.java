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
    private final DeleteHistoryService deleteHistoryService;

    public QnaService(QuestionRepository questionRepository, DeleteHistoryService deleteHistoryService) {
        this.questionRepository = questionRepository;
        this.deleteHistoryService = deleteHistoryService;
    }

    @Transactional(readOnly = true)
    public Question findQuestionById(Long questionId) {
        return questionRepository.findByIdAndDeletedFalse(questionId)
                .orElseThrow(NotFoundException::new);
    }

    @Transactional
    public void deleteQuestion(User loginUser, Long questionId) throws CannotDeleteException {
        List<DeleteHistory> deleteHistories = new ArrayList<>();
        Question question = findQuestionById(questionId);
        deleteHistories.add(question.delete(loginUser));

        List<Answer> answers = question.getAnswers();
        for (Answer answer : answers) {
            deleteHistories.add(answer.delete(loginUser));
        }
        deleteHistoryService.saveAll(deleteHistories);
    }
}
