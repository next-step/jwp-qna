package qna.service;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import qna.common.exception.NotFoundException;
import qna.domain.Answer;
import qna.domain.DeleteHistory;
import qna.domain.Question;
import qna.domain.User;
import qna.enumType.ContentType;
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
        Question question = findQuestionById(questionId, loginUser);
        List<Answer> answers = findByQuestionIdAndDeletedFalse(questionId, loginUser);

        List<DeleteHistory> deleteHistories = new ArrayList<>();
        question.setDeleted();
        deleteHistories.add(new DeleteHistory(ContentType.QUESTION, questionId, loginUser));
        for (Answer answer : answers) {
            answer.setDeleted();
            deleteHistories.add(new DeleteHistory(ContentType.ANSWER, answer.getId(), loginUser));
        }
        deleteHistoryService.saveAll(deleteHistories);
    }

    @Transactional(readOnly = true)
    public Question findQuestionById(Long id, User loginUser) {
        Question question = questionRepository.findByIdAndDeletedFalse(id).orElseThrow(NotFoundException::new);
        question.isOwner(loginUser);
        return question;
    }

    @Transactional(readOnly = true)
    public List<Answer> findByQuestionIdAndDeletedFalse(Long id, User loginUser){
        List<Answer> answers = answerRepository.findByQuestion_IdAndDeletedFalse(id);
        answers.forEach(answer -> answer.isOwner(loginUser));
        return answers;
    }
}
