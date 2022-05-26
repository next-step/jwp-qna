package qna.service;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    private static final Logger log = LoggerFactory.getLogger(QnaService.class);

    private QuestionRepository questionRepository;
    private AnswerRepository answerRepository;
    private DeleteHistoryService deleteHistoryService;

    @Autowired
    public QnaService(QuestionRepository questionRepository, AnswerRepository answerRepository,
        DeleteHistoryService deleteHistoryService) {
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.deleteHistoryService = deleteHistoryService;
    }

    @Transactional(readOnly = true)
    public Question findQuestionById(Long id) {
        return questionRepository.findByIdAndDeletedIsFalse(id)
            .orElseThrow(NotFoundException::new);
    }

    @Transactional
    public void deleteQuestion(User loginUser, Long questionId) {
        Question question = questionRepository.findByIdAndDeletedIsFalse(
            questionId).orElseThrow(NotFoundException::new);

        question.delete(loginUser);
        List<DeleteHistory> deleteHistories = getDeleteHistories(question);
        deleteHistoryService.saveAll(deleteHistories);

        answerRepository.updateDeleteOfAnswers(
            question.getAnswers().stream()
                .map(Answer::getId)
                .collect(Collectors.toList())
        );

    }

    private List<DeleteHistory> getDeleteHistories(Question question) {
        List<DeleteHistory> deleteHistories = new LinkedList<>();
        deleteHistories.add(question.makeDeleteHistory());
        deleteHistories.addAll(
            question.getAnswers().stream()
                .map(Answer::makeDeleteHistory)
                .collect(Collectors.toList())
        );
        return deleteHistories;
    }
}
