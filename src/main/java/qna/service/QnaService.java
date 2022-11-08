package qna.service;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import qna.CannotDeleteException;
import qna.NotFoundException;
import qna.domain.Answer;
import qna.domain.ContentType;
import qna.domain.DeleteHistories;
import qna.domain.DeleteHistory;
import qna.domain.Question;
import qna.domain.User;
import qna.repository.AnswerRepository;
import qna.repository.QuestionRepository;

@Service
public class QnaService {
    private static final Logger log = LoggerFactory.getLogger(QnaService.class);

    private QuestionRepository questionRepository;
    private AnswerRepository answerRepository;
    private DeleteHistoryService deleteHistoryService;

    public QnaService(QuestionRepository questionRepository, AnswerRepository answerRepository,
                      DeleteHistoryService deleteHistoryService) {
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

        DeleteHistories deleteHistories = new DeleteHistories();

        Question question = findQuestionById(questionId);

        deleteHistories.add(question.delete(loginUser));

        List<Answer> answers = answerRepository.findByQuestionIdAndDeletedFalse(questionId);

        answers.stream()
                .map(answer -> answer.delete(loginUser))
                .forEach(deleteHistories::add);

        deleteHistoryService.saveAll(deleteHistories);
    }
}
