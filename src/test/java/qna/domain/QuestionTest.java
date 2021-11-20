package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.CannotDeleteException;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class QuestionTest {

    public static final Question Q1 = Question.of("title1", "contents1");
    public static final Question Q2 = Question.of("title2", "contents2");

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @DisplayName("save 테스트")
    @Test
    void save() {
        // given
        User newUser = userRepository.save(UserTest.JAVAJIGI);

        // when
        Question newQuestion = questionRepository.save(Q1.writeBy(newUser));

        // then
        assertAll(
                () -> assertThat(newQuestion.getId()).isNotNull()
                , () -> assertThat(newQuestion.getTitle()).isEqualTo(Q1.getTitle())
                , () -> assertThat(newQuestion.getContents()).isEqualTo(Q1.getContents())
                , () -> assertThat(newQuestion.getWriter().getId()).isNotNull()
        );
    }

    @DisplayName("findByDeletedFalse 테스트")
    @Test
    void findByDeletedFalse() {
        // given
        User newUser = userRepository.save(UserTest.JAVAJIGI);
        Question newQuestion = questionRepository.save(Q1.writeBy(newUser));

        // when
        List<Question> questions = questionRepository.findByDeletedFalse();

        // then
        assertAll(
                () -> assertThat(questions).hasSize(1)
                , () -> assertThat(questions.get(0).getId()).isNotNull()
                , () -> assertThat(questions).containsExactly(newQuestion)
        );
    }

    @DisplayName("findByIdAndDeletedFalse 테스트")
    @Test
    void findByIdAndDeletedFalse() {
        // given
        User newUser = userRepository.save(UserTest.JAVAJIGI);
        Question newQuestion = questionRepository.save(Q1.writeBy(newUser));

        // when
        Question findQuestion = questionRepository.findByIdAndDeletedFalse(newQuestion.getWriter().getId()).get();

        // then
        assertAll(
                () -> assertThat(findQuestion.getId()).isNotNull()
                , () -> assertThat(findQuestion.getWriter().getId()).isNotNull()
                , () -> assertThat(findQuestion).isEqualTo(newQuestion)
        );
    }

    @DisplayName("delete 성공 테스트 - 답변 포함 X")
    @Test
    void delete_success() throws CannotDeleteException {
        // given
        User user = UserTest.JAVAJIGI;
        Question question = Question.of("title1", "contents1").writeBy(user);

        // when
        question.delete(user);

        // then
        assertThat(question.isDeleted()).isTrue();
    }

    @DisplayName("delete 성공 테스트 - 답변 포함 O, 다른 사용자 답변 없음")
    @Test
    void deleteWithAnswer_otherAnswer_empty_success() throws CannotDeleteException {
        // given
        User user = UserTest.JAVAJIGI;
        Question question = Question.of("title1", "contents1").writeBy(user);
        Answer answer = Answer.of(user, question, "Answers Contents1");
        question.addAnswer(answer);

        // when
        question.delete(user);

        // then
        assertAll(
                () -> assertThat(question.isDeleted()).isTrue()
                , () -> assertThat(question.getAnswers().get(0).isDeleted()).isTrue()
        );
    }

    @DisplayName("delete 성공 테스트 - 답변 포함 O, 다른 사용자 답변 삭제됨")
    @Test
    void deleteWithAnswer_otherAnswer_deletedTrue_success() throws CannotDeleteException {
        // given
        User writer = UserTest.JAVAJIGI;
        User otherUser = UserTest.SANJIGI;

        Question question = Question.of("title1", "contents1").writeBy(writer);
        Answer writerAnswer = Answer.of(writer, question, "Answers Contents1");
        Answer otherUserAnswer = Answer.of(otherUser, question, "Answers Contents1");

        question.addAnswer(writerAnswer);
        question.addAnswer(otherUserAnswer);

        // when
        otherUserAnswer.delete(otherUser);
        question.delete(writer);

        // then
        assertAll(
                () -> assertThat(question.isDeleted()).isTrue()
                , () -> assertThat(question.getAnswers().get(0).isDeleted()).isTrue()
        );
    }

    @DisplayName("delete 성공 테스트 - 답변 포함 O, 다른 사용자 답변 삭제안됨")
    @Test
    void deleteWithAnswer_otherAnswer_deletedFalse_success() throws CannotDeleteException {
        // given
        User writer = UserTest.JAVAJIGI;
        User otherUser = UserTest.SANJIGI;

        Question question = Question.of("title1", "contents1").writeBy(writer);
        Answer writerAnswer = Answer.of(writer, question, "Answers Contents1");
        Answer otherUserAnswer = Answer.of(otherUser, question, "Answers Contents1");

        question.addAnswer(writerAnswer);
        question.addAnswer(otherUserAnswer);

        // when & then
        assertThatExceptionOfType(CannotDeleteException.class)
                .isThrownBy(() -> assertThat(question.delete(writer)))
                .withMessage(Answer.EXIST_OTHER_ANSWERS);
    }

    @DisplayName("delete 실패 테스트")
    @Test
    void delete_failure() {
        // given
        User user = UserTest.JAVAJIGI;
        Question question = Question.of("title1", "contents1").writeBy(user);

        // when & then
        assertThatExceptionOfType(CannotDeleteException.class)
                .isThrownBy(() -> question.delete(UserTest.SANJIGI))
                .withMessage(Question.NO_DELETE_PERMISSION);
    }

    @DisplayName("답변 추가 성공 테스트")
    @Test
    void addAnswer_success() {
        // given
        User user = UserTest.JAVAJIGI;
        Question question = Question.of("title1", "contents1").writeBy(user);
        Answer answer = Answer.of(user, question, "Answers Contents1");

        // when
        question.addAnswer(answer);

        // then
        assertThat(question.getAnswers()).hasSize(1);
    }

    @DisplayName("답변 추가 null 테스트")
    @Test
    void addAnswer_null() {
        // given
        User user = UserTest.JAVAJIGI;
        Question question = Question.of("title1", "contents1").writeBy(user);
        Answer answer = null;

        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> question.addAnswer(answer))
                .withMessage(Question.ANSWER_CAN_NOT_BE_NULL);
    }

    @DisplayName("답변 추가 중복 테스트")
    @Test
    void addAnswer_duplicate() {
        // given
        User user = UserTest.JAVAJIGI;
        Question question = Question.of("title1", "contents1").writeBy(user);
        Answer answer = Answer.of(user, question, "Answers Contents1");

        // when
        question.addAnswer(answer);

        // then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> question.addAnswer(answer))
                .withMessage(Question.DUPLICATE_ANSWER);
    }
}
