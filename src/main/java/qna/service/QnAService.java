package qna.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import qna.CannotDeleteException;
import qna.NotFoundException;
import qna.domain.Answer;
import qna.domain.ContentType;
import qna.domain.DeleteHistory;
import qna.domain.Question;
import qna.domain.QuestionRepository;
import qna.domain.User;
import qna.domain.UserRepository;

@Service
public class QnAService {
    private static final Logger log = LoggerFactory.getLogger(QnAService.class);

    private final QuestionRepository questionRepository;
    private final DeleteHistoryService deleteHistoryService;
    private final UserRepository userRepository;

    public QnAService(QuestionRepository questionRepository,
                      DeleteHistoryService deleteHistoryService,
                      UserRepository userRepository) {
        this.questionRepository = questionRepository;
        this.deleteHistoryService = deleteHistoryService;
        this.userRepository = userRepository;
    }

    @Transactional
    public void deleteQuestion(long loginUserId, long questionId) throws CannotDeleteException {

        Question question =
            questionRepository.findById(questionId)
                              .orElseThrow(() -> new CannotDeleteException("질문을 찾을 수 없습니다."));

        if (question.isDeleted()) {
            throw new CannotDeleteException("이미 삭제된 질문입니다.");
        }

        User loginUser =
            userRepository.findById(loginUserId)
                          .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));

        if (!question.isOwner(loginUser)) {
            throw new CannotDeleteException("질문을 삭제할 권한이 없습니다.");
        }

        List<Answer> answers = question.getAnswers();
        for (Answer answer : answers) {
            if (!answer.isOwner(loginUser)) {
                throw new CannotDeleteException("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
            }
        }

        List<DeleteHistory> deleteHistories = new ArrayList<>();
        question.setDeleted(true);
        deleteHistories.add(new DeleteHistory(ContentType.QUESTION, question.getId(), question.getWriter(), LocalDateTime.now()));
        for (Answer answer : answers) {
            answer.setDeleted(true);
            deleteHistories.add(new DeleteHistory(ContentType.ANSWER, answer.getId(), answer.getWriter(), LocalDateTime.now()));
        }
        deleteHistoryService.saveAll(deleteHistories);
    }
}
