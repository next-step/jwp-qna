package qna.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import qna.CannotDeleteException;
import qna.NotFoundException;
import qna.domain.Answer;
import qna.domain.AnswerRepository;
import qna.domain.DeleteHistory;
import qna.domain.Question;
import qna.domain.QuestionRepository;
import qna.domain.User;
import qna.domain.message.ExceptionMessage;

@Service
public class QnaService {

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
        if (!question.isOwner(loginUser)) {
            throw new CannotDeleteException(ExceptionMessage.NO_PERMISSION_DELETE_QUESTION);
        }

        List<Answer> answers = answerRepository.findByQuestionAndDeletedFalse(question);
        for (Answer answer : answers) {
            if (!answer.isOwner(loginUser)) {
                throw new CannotDeleteException(ExceptionMessage.NO_PERMISSION_DELETE_ANSWER);
            }
        }

        List<DeleteHistory> deleteHistories = new ArrayList<>();
        question.deleted();
        deleteHistories.add(question.toDeletedHistory());
        for (Answer answer : answers) {
            answer.deleted();
            deleteHistories.add(answer.toDeletedHistory());
        }
        deleteHistoryService.saveAll(deleteHistories);
    }
}
