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

        Answers answers = new Answers(answerRepository.findByQuestion(question));

        answers.validateOwner(loginUser);

        DeleteHistories deleteHistories = new DeleteHistories();
        deleteHistories.addDeletedQuestion(question);
        deleteHistories.addDeletedAnswers(answers);

        deleteHistoryService.saveAll(deleteHistories.deleteHistories());
    }
}
