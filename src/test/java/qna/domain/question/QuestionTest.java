package qna.domain.question;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import qna.CannotDeleteException;
import qna.domain.answer.Answer;
import qna.domain.answer.AnswerRepository;
import qna.domain.history.DeleteHistory;
import qna.domain.history.DeleteHistoryRepository;
import qna.domain.user.User;
import qna.domain.user.UserRepository;
import qna.domain.user.UserTest;

@DirtiesContext
@DataJpaTest
public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    public static Question createQuestion(User writeUser) {
        return new Question("title", "contents").writeBy(writeUser);
    }

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;

    @Test
    @DisplayName("답변없는 질문 삭제 테스트")
    void delete_question() throws CannotDeleteException {
        User user = userRepository.save(UserTest.createUser("user"));
        Question question = createQuestion(user);
        question.delete(user);

        Question save = questionRepository.save(question);
        Question byId = questionRepository.findById(save.getId()).get();

        assertThat(byId.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("질문 등록자와 로그인한 사용자가 다른 경우 예외 발생")
    void delete_question_질문등록자_로그인사용자_다른경우_예외() {
        User writerUser = userRepository.save(UserTest.createUser("writerUser"));
        User loginUser = userRepository.save(UserTest.createUser("loginUser"));
        Question question = createQuestion(writerUser);

        assertThatThrownBy(() -> question.delete(loginUser))
                .isInstanceOf(CannotDeleteException.class);
    }

    @Test
    @DisplayName("질문 삭제 후 질문삭제 이력이 등록되는지 테스트")
    void save_delete_question_history() throws CannotDeleteException {
        User user = userRepository.save(UserTest.createUser("user"));
        Question question = createQuestion(user);
        List<DeleteHistory> deleteQuestionHistory = question.delete(user);
        List<DeleteHistory> deleteHistories = deleteHistoryRepository.saveAll(deleteQuestionHistory);

        for (DeleteHistory h: deleteHistories) { // TODO: 리팩토링 하기
            assertThat(h.getDeletedByUser()).isEqualTo(user);
        }

    }

    @Test
    @DisplayName("질문 삭제시 질문 사용자와 답변 글의 모든 답변 사용자가 일치하지 않는 경우 예외 발생 테스트")
    void delete_question_delete_answer_all_equals_user() {
        User loginUser = userRepository.save(UserTest.createUser("loginUser"));
        User user2 = userRepository.save(UserTest.createUser("user2"));
        User user3 = userRepository.save(UserTest.createUser("user3"));

        Question question = createQuestion(loginUser);
        question.addAnswer(new Answer(loginUser, question, "contents1"));
        question.addAnswer(new Answer(user2, question, "contents2"));
        question.addAnswer(new Answer(user3, question, "contents3"));
        questionRepository.saveAndFlush(question);

        assertThatThrownBy(() ->
                question.delete(loginUser)).isInstanceOf(CannotDeleteException.class);
    }

    @Test
    @DisplayName("질문 삭제 후 답변 삭제 테스트")
    void delete_question_delete_answer() {

    }

}
