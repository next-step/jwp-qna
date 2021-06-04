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

@Service
public class QnaService {
    private static final Logger log = LoggerFactory.getLogger(QnaService.class);

    private QuestionRepository questionRepository;
    private DeleteHistoryService deleteHistoryService;
    private AnswerRepository answerRepository;

    public QnaService(QuestionRepository questionRepository, DeleteHistoryService deleteHistoryService, AnswerRepository answerRepository) {
        this.questionRepository = questionRepository;
        this.deleteHistoryService = deleteHistoryService;
        this.answerRepository = answerRepository;
    }

    @Transactional(readOnly = true)
    public Question findQuestionById(Long id) {
        return questionRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(NotFoundException::new);
    }

    @Transactional
    public void deleteQuestion(User loginUser, Long questionId) throws CannotDeleteException {
        Question question = findQuestionById(questionId);

        List<Answer> answers = answerRepository.findByQuestionIdAndDeletedFalse(question.getId());

        deleteHistoryService.saveAll(deleteHistoriesCreate(question, new Answers(answers), loginUser));
    }

    public DeleteHistories deleteHistoriesCreate(Question question, Answers answers, User loginUser){
        List<DeleteHistory> deleteHistories = new ArrayList<>();
        question.deletedByUser(loginUser);
        deleteHistoryCreate(question, loginUser);

        answers.deleteAnswers(loginUser);
        new DeleteHistory(ContentType.ANSWER, loginUser.getId(), question.getWriter(), LocalDateTime.now());

        return new DeleteHistories(deleteHistories);
    }

    private void deleteHistoryCreate(Question question, User loginUser) {

        new DeleteHistory(ContentType.QUESTION, question.getId(), question.getWriter(), LocalDateTime.now());

        DeleteHistories deleteHistories = new DeleteHistories();
        deleteHistories.addDeleteHistory(new DeleteHistory(ContentType.QUESTION, question.getId(), question.getWriter(), LocalDateTime.now()));

        question.getAnswers().deleteAnswers(loginUser);
    }
}
