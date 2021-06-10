package qna.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import qna.CannotDeleteException;
import qna.NotFoundException;
import qna.domain.*;


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

    public List<DeleteHistory> deleteQuestion(User loginUser, Long questionId) throws CannotDeleteException {
        Question question = findQuestionById(questionId);
        return question.delete(loginUser);
    }

    public List<DeleteHistory> deleteAnswer(User loginUser, Long questionId) throws CannotDeleteException {
        List<DeleteHistory> deleteHistories = new ArrayList<>();
        for (Answer answer : answerRepository.findByQuestionIdAndDeletedFalse(questionId)) {
            deleteHistories.add(answer.delete(loginUser));
        }
        return deleteHistories;
    }

    @Transactional
    public void deleteQna(User loginUser, Long questionId) throws CannotDeleteException {
        List<DeleteHistory> deleteHistories = deleteQuestion(loginUser, questionId);
        deleteHistories.addAll(deleteAnswer(loginUser, questionId));
        deleteHistoryService.saveAll(deleteHistories);
    }
}
