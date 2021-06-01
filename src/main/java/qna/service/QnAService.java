package qna.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import qna.CannotDeleteException;
import qna.NotFoundException;
import qna.domain.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class QnAService {
    private static final Logger log = LoggerFactory.getLogger(QnAService.class);

    private QuestionRepository questionRepository;
    private AnswerRepository answerRepository;
    private DeleteHistoryService deleteHistoryService;

    public QnAService(QuestionRepository questionRepository, AnswerRepository answerRepository, DeleteHistoryService deleteHistoryService) {
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.deleteHistoryService = deleteHistoryService;
    }

    @Transactional(readOnly = true)
    public Question findQuestionById(Long id) {
        return questionRepository.findById(id)
                .orElseThrow(NotFoundException::new);
    }

    @Transactional
    public void deleteQuestion(User loginUser, Long questionId) throws CannotDeleteException {
        Question question = findQuestionById(questionId);

        question.validateQuestionOwner(loginUser, question);

        List<Answer> answers = answerRepository.findByQuestion(question);

        validateSameOwnerInAnswer(loginUser, answers);

        List<DeleteHistory> deleteHistories = deleteHistories(question, answers);

        deleteHistoryService.saveAll(deleteHistories);
    }

    public void validateSameOwnerInAnswer(User loginUser, List<Answer> answers) throws CannotDeleteException {
        for (Answer answer : answers) {
            answer.validateSameOwnerInAnswer(loginUser);
        }
    }

    public List<DeleteHistory> deleteHistories(Question question, List<Answer> answers) {
        List<DeleteHistory> deleteHistories = new ArrayList<>();
        question.deleteQuestion();
        deleteHistories.add(new DeleteHistory(ContentType.QUESTION, question.getId(), question.getWriter(), LocalDateTime.now()));
        for (Answer answer : answers) {
            answer.deleteAnswer();
            deleteHistories.add(new DeleteHistory(ContentType.ANSWER, answer.getId(), answer.getWriter(), LocalDateTime.now()));
        }
        return deleteHistories;
    }
}
