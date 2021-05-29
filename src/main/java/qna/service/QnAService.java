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
        return questionRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(NotFoundException::new);
    }

    /**
     * // validate
     * 1. 질문id에 해당하는 질문 찾기
     * 2. 질문의 작성자 확인
     *
     * 3. 질문id에 해당하는 답변들 찾기
     * 4. 답변들마다 작성자 확인
     *
     * // delete
     * 5. 질문과, 답변들 삭제
     *
     * // save
     * 6. 삭제한 내용 history에 저장
     */
    @Transactional
    public void deleteQuestion(User loginUser, Long questionId) throws CannotDeleteException {
        Question question = findQuestionById(questionId);

        DeleteHistories deleteHistories = question.delete(loginUser);

        deleteHistoryService.saveAll(deleteHistories.histories());
    }
}