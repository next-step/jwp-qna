package qna.service;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import qna.common.exception.NotFoundException;
import qna.domain.Answer;
import qna.domain.Answers;
import qna.domain.DeleteHistories;
import qna.domain.Question;
import qna.domain.User;
import qna.repository.AnswerRepository;
import qna.repository.QuestionRepository;

@Service
public class QnaService {
    private static final Logger log = LoggerFactory.getLogger(QnaService.class);

    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final DeleteHistoryService deleteHistoryService;

    public QnaService(QuestionRepository questionRepository, AnswerRepository answerRepository, DeleteHistoryService deleteHistoryService) {
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.deleteHistoryService = deleteHistoryService;
    }

    @Transactional
    public void deleteQuestion(User loginUser, Long questionId) {
        Question question = findQuestionById(questionId);
        question.isOwner(loginUser);
        Answers answers = new Answers(findByQuestionIdAndDeletedFalse(questionId));
        answers.isOwner(loginUser);

        DeleteHistories deleteHistories = new DeleteHistories();
        question.setDeleted();
        deleteHistories.addQuestionDeleteHistory(question, loginUser);
        deleteHistories.addAnswersDeleteHistory(answers, loginUser);

        deleteHistoryService.saveAll(deleteHistories);
    }

    @Transactional(readOnly = true)
    public Question findQuestionById(Long questionId) {
        return questionRepository.findByIdAndDeletedFalse(questionId).orElseThrow(NotFoundException::new);
    }

    @Transactional(readOnly = true)
    public List<Answer> findByQuestionIdAndDeletedFalse(Long questionId){
        return answerRepository.findByQuestion_IdAndDeletedFalse(questionId);
    }
}
