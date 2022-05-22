package qna.question.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import qna.question.domain.Answer;
import qna.question.domain.Question;
import qna.question.exception.CannotDeleteException;
import qna.question.exception.NotFoundException;
import qna.question.repository.AnswerRepository;
import qna.question.repository.QuestionRepository;
import qna.user.domain.User;

import java.util.List;

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

    @Transactional
    public void deleteQuestion(User loginUser, Long questionId) throws CannotDeleteException {
        Question question = findQuestionById(questionId);
        List<Answer> answers = answerRepository.findByQuestionIdAndDeletedFalse(questionId);

        deleteHistoryService.saveAll(question.deleteQuestionWithRelatedAnswer(loginUser, answers));
    }
}
