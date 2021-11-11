package qna.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import qna.CannotDeleteException;
import qna.NotFoundException;
import qna.domain.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class QnaService {

    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final DeleteHistoryService deleteHistoryService;

    public QnaService(QuestionRepository questionRepository, AnswerRepository answerRepository, DeleteHistoryService deleteHistoryService) {
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.deleteHistoryService = deleteHistoryService;
    }

    @Transactional
    public void deleteQuestion(User loginUser, Question deleteQuestion) throws CannotDeleteException {
        List<DeleteHistory> deleteHistories = deleteQuestionAndQuestionByAnswers(loginUser, deleteQuestion);

        deleteHistoryService.saveAll(deleteHistories);
    }

    private List<DeleteHistory> deleteQuestionAndQuestionByAnswers(User loginUser, Question deleteQuestion) {
        Question question = findQuestionById(deleteQuestion);

        cascadeDeleteQuestion(loginUser, question);

        return getDeleteHistories(question);
    }

    private void cascadeDeleteQuestion(User loginUser, Question question) {
        findAnswersByQuestionId(question);
        question.delete(loginUser);
        question.cascadeDeleteAnswers(loginUser);
    }

    private List<DeleteHistory> getDeleteHistories(Question question) {
        List<DeleteHistory> deleteHistories = new ArrayList<>();

        deleteHistories.add(new DeleteHistory(ContentType.QUESTION, question.getId(), question.getWriter()));
        deleteHistories.addAll(question.getAnswers().stream()
                .map(m -> new DeleteHistory(ContentType.ANSWER, m.getId(), m.getWriter()))
                .collect(Collectors.toList()));

        return deleteHistories;
    }

    private Question findQuestionById(Question question) {
        return questionRepository.findByIdAndDeletedFalse(question.getId())
                .orElseThrow(NotFoundException::new);
    }

    private List<Answer> findAnswersByQuestionId(Question deleteQuestion) {
        return answerRepository.findByQuestionIdAndDeletedFalse(deleteQuestion.getId());
    }

}
