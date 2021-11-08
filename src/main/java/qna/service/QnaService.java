package qna.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import qna.CannotDeleteException;
import qna.NotFoundException;
import qna.domain.*;

import java.util.ArrayList;
import java.util.List;

@Service
public class QnaService {

    private QuestionRepository questionRepository;
    private DeleteHistoryService deleteHistoryService;

    public QnaService(QuestionRepository questionRepository, DeleteHistoryService deleteHistoryService) {
        this.questionRepository = questionRepository;
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
        if (!question.isOwner(loginUser)) {
            throw new CannotDeleteException("질문을 삭제할 권한이 없습니다.");
        }

        for (Answer answer : question.getAnswers()) {
            if (!answer.isOwner(loginUser)) {
                throw new CannotDeleteException("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
            }
        }

        List<DeleteHistory> deleteHistories = new ArrayList<>();
        question.delete();
        deleteHistories.add(new DeleteHistory(ContentType.QUESTION, question.getId(), question.getWriter()));
        for (Answer answer : question.getAnswers()) {
            answer.delete();
            deleteHistories.add(new DeleteHistory(ContentType.ANSWER, answer.getId(), answer.getWriter()));
        }
        deleteHistoryService.saveAll(deleteHistories);
    }
}
