package qna.service;

import static java.util.stream.Stream.concat;
import static qna.domain.ContentType.ANSWER;
import static qna.domain.ContentType.QUESTION;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import qna.CannotDeleteException;
import qna.NotFoundException;
import qna.domain.Answer;
import qna.domain.DeleteHistory;
import qna.domain.Question;
import qna.domain.QuestionRepository;
import qna.domain.User;
import qna.domain.UserRepository;

@Service
public class QnaService {
    private static final Logger log = LoggerFactory.getLogger(QnaService.class);

    private UserRepository userRepository;
    private QuestionRepository questionRepository;
    private DeleteHistoryService deleteHistoryService;

    public QnaService(final UserRepository userRepository, QuestionRepository questionRepository,
                      DeleteHistoryService deleteHistoryService) {
        this.userRepository = userRepository;
        this.questionRepository = questionRepository;
        this.deleteHistoryService = deleteHistoryService;
    }

    @Transactional(readOnly = true)
    public Question findQuestionById(Long id) {
        return questionRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(NotFoundException::new);
    }

    @Transactional
    public void deleteQuestion(Long loginUserId, Long questionId) throws CannotDeleteException {
        Question question = findQuestionById(questionId);
        User loginUser = findUserById(loginUserId);
        validateQuestion(loginUser, question);
        question.delete();
        question.getAnswers().forEach(Answer::delete);
        deleteHistoryService.saveAll(mapToDeleteHistory(question));
    }

    private List<DeleteHistory> mapToDeleteHistory(final Question question) {
        return concat(Stream.of(new DeleteHistory(QUESTION, question.getId(), question.getWriter(),
                LocalDateTime.now())), question.getAnswers().stream()
                .map(answer -> new DeleteHistory(ANSWER, answer.getId(), answer.getWriter(), LocalDateTime.now())))
                .collect(Collectors.toList());
    }

    private User findUserById(final Long loginUserId) {
        return userRepository.findById(loginUserId).orElseThrow(NotFoundException::new);
    }

    private void validateQuestion(final User loginUser, final Question question) throws CannotDeleteException {
        if (!question.isOwner(loginUser)) {
            throw new CannotDeleteException("질문을 삭제할 권한이 없습니다.");
        }

        if (!question.isAnswersOwner(loginUser)) {
            throw new CannotDeleteException("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
        }
    }
}
