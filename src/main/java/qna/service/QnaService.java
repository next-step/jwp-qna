package qna.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import qna.CannotDeleteException;
import qna.NotFoundException;
import qna.domain.*;

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
        DeletedHistories deletedHistories = deleteQuestionWithAnswer(loginUser, question);
        deleteHistoryService.saveAll(deletedHistories.getDeleteHistories());
    }

    /**
     * 질문 삭제 시 답변까지 삭제
     * @param user
     * @return
     */
    public DeletedHistories deleteQuestionWithAnswer(User user, Question question) throws CannotDeleteException {
        if(!question.isOwner(user)) {
            throw new CannotDeleteException("질문을 삭제할 권한이 없습니다.");
        }

        DeletedHistories deletedHistories = new DeletedHistories();
        deletedHistories = deleteAnswers(deletedHistories, question, user);

        question.changeDeleted(true);
        deletedHistories.addDeleteHistory(ContentType.QUESTION, question.getId(), question.getWriter());

        return deletedHistories;
    }

    private DeletedHistories deleteAnswers(DeletedHistories deletedHistories, Question question, User user) throws CannotDeleteException {
        Answers answers = new Answers(answerRepository.findByQuestionIdAndDeletedFalse(question.getId()));
        if(!answers.isAnswersOwner(user)) {
            throw new CannotDeleteException("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
        }

        if(answers.count() != 0) {
            answers.deleteAnswers(user);
            setDeletedHistories(deletedHistories, answers);
        }

        return deletedHistories;
    }

    private void setDeletedHistories(DeletedHistories deletedHistories, Answers answers) {
        for(Answer answer : answers.getAnswers()) {
            deletedHistories.addDeleteHistory(ContentType.ANSWER, answer.getId(), answer.getWriter());
        }
    }
}
