package qna.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import qna.NotFoundException;
import qna.domain.Answer;
import qna.domain.AnswerRepository;
import qna.domain.DeleteHistory;
import qna.domain.Question;
import qna.domain.QuestionRepository;
import qna.domain.User;

@Service
public class QnaService {
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final DeleteHistoryService deleteHistoryService;

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
    public void deleteQuestion(User loginUser, Long questionId) {
        List<DeleteHistory> deleteHistories = new ArrayList<>();

        Question question = findQuestionById(questionId);
        deleteHistories.add(question.delete(loginUser));

        List<Answer> answers = answerRepository.findByQuestionIdAndDeletedFalse(questionId);
        for (Answer answer : answers) {
            deleteHistories.add(answer.delete(loginUser));
        }

        deleteHistoryService.saveAll(deleteHistories);
    }
}
