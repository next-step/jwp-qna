package qna.domain;

import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import qna.CannotDeleteException;

@TestInstance(Lifecycle.PER_CLASS)
@DataJpaTest
public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);
    public static final Question Q3 = new Question("title3", "contents3").writeBy(UserTest.SANJIGI);

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @BeforeAll
    private void beforeAll() {
        userRepository.save(UserTest.JAVAJIGI);
        userRepository.save(UserTest.SANJIGI);
        
        questionRepository.save(Q1);
        questionRepository.save(Q2);

        answerRepository.save(AnswerTest.A1);
        answerRepository.save(AnswerTest.A2);
        answerRepository.save(AnswerTest.A3);
    }

    @DisplayName("삭제되지 않은 질문을 조회한다.")
    @Test
    public void find_notDelete() {
        // given

        // when
        List<Question> realQuestions = questionRepository.findByDeletedFalse();

        // then
        Assertions.assertThat(realQuestions.size()).isEqualTo(2);

        assertAll(
            () -> Assertions.assertThat(realQuestions.get(0).getId()).isEqualTo(Q1.getId()),
            () -> Assertions.assertThat(realQuestions.get(0).getTitle()).isEqualTo(Q1.getTitle()),
            () -> Assertions.assertThat(realQuestions.get(0).getContents()).isEqualTo(Q1.getContents()),
            () -> Assertions.assertThat(realQuestions.get(0).getWriter()).isEqualTo(Q1.getWriter())
        );

        assertAll(
            () -> Assertions.assertThat(realQuestions.get(1).getId()).isEqualTo(Q2.getId()),
            () -> Assertions.assertThat(realQuestions.get(1).getTitle()).isEqualTo(Q2.getTitle()),
            () -> Assertions.assertThat(realQuestions.get(1).getContents()).isEqualTo(Q2.getContents()),
            () -> Assertions.assertThat(realQuestions.get(1).getWriter()).isEqualTo(Q2.getWriter())
        );
    }

    @DisplayName("특정 유저의 질문을 조회한다.")
    @Test
    public void find_forWriter() {
        // given

        // when
        List<Question> realQuestions = questionRepository.findByWriterId(UserTest.SANJIGI.getId());

        // then
        Assertions.assertThat(realQuestions.size()).isEqualTo(1);
        
        assertAll(
            () -> Assertions.assertThat(realQuestions.get(0).getTitle()).isEqualTo(Q2.getTitle()),
            () -> Assertions.assertThat(realQuestions.get(0).getContents()).isEqualTo(Q2.getContents()),
            () -> Assertions.assertThat(realQuestions.get(0).getWriter()).isEqualTo(Q2.getWriter())
        );
    }

    @DisplayName("로그인된 사용자가 입력한 질문이 아닌 것을 지울경우 에러가 발생한다.")
    @Test
    public void check_deleteError_notOnwer() {
        // given
        User loginUser = UserTest.SANJIGI;

        // when
        // then
        Assertions.assertThatExceptionOfType(CannotDeleteException.class)
                    .isThrownBy(() -> Q1.delete(loginUser));
    }


    @DisplayName("로그인된 사용자가 입력한 질문이나 답변이 로그인된 사용자가 아닌 것이 있을경우 에러가 발생한다.")
    @Test
    public void check_deleteError_Onwer_hasNotAnswerOwner() {
        // given
        User loginUser = UserTest.SANJIGI;

        // when
        // then
        Assertions.assertThatExceptionOfType(CannotDeleteException.class)
                    .isThrownBy(() -> Q1.delete(loginUser));
    }

    @DisplayName("로그인된 사용자가 입력한 질문이고 답변이 로그인된 사용자가 입력한 것만 삭제한다.")
    @Test
    public void check_deleteError_Onwer_hasAnswerOwner() throws CannotDeleteException {
        // given
        User loginUser = UserTest.SANJIGI;

        // when
        DeleteHistories deleteHistory  = Q2.delete(loginUser);

        // then
        Assertions.assertThat(deleteHistory.toList().size()).isEqualTo(2);

        DeleteHistory realQuestionDeleteHistory = deleteHistory.toList().get(0);
        DeleteHistory expectedQuestionDeleteHistory = new DeleteHistory(ContentType.QUESTION, Q2.getId(), Q2.getWriter(), LocalDateTime.now());

        assertAll(
            () -> Assertions.assertThat(realQuestionDeleteHistory.getContentId()).isEqualTo(expectedQuestionDeleteHistory.getContentId()),
            () -> Assertions.assertThat(realQuestionDeleteHistory.getContentType()).isEqualTo(expectedQuestionDeleteHistory.getContentType()),
            () -> Assertions.assertThat(realQuestionDeleteHistory.getDeletedByUser()).isEqualTo(expectedQuestionDeleteHistory.getDeletedByUser())
        );
        Assertions.assertThat(Q2.isDeleted()).isTrue();

        DeleteHistory realAnswerDeleteHistory = deleteHistory.toList().get(1);
        DeleteHistory expectedAnswerDeleteHistory = new DeleteHistory(ContentType.ANSWER, AnswerTest.A3.getId(), AnswerTest.A3.getWriter(), LocalDateTime.now());

        assertAll(
            () -> Assertions.assertThat(realAnswerDeleteHistory.getContentId()).isEqualTo(expectedAnswerDeleteHistory.getContentId()),
            () -> Assertions.assertThat(realAnswerDeleteHistory.getContentType()).isEqualTo(expectedAnswerDeleteHistory.getContentType()),
            () -> Assertions.assertThat(realAnswerDeleteHistory.getDeletedByUser()).isEqualTo(expectedAnswerDeleteHistory.getDeletedByUser())
        );

        Assertions.assertThat(AnswerTest.A3.isDeleted()).isTrue();
    }

    @DisplayName("로그인된 사용자가 입력한 질문이고 답변이 없는경우에대해 삭제한다.")
    @Test
    public void check_deleteError_Onwer_noAnswer() throws CannotDeleteException {
        // given
        User loginUser = UserTest.SANJIGI;

        // when
        DeleteHistories deleteHistory  = Q3.delete(loginUser);

        // then
        Assertions.assertThat(deleteHistory.toList().size()).isEqualTo(1);

        DeleteHistory realQuestionDeleteHistory = deleteHistory.toList().get(0);
        DeleteHistory expectedQuestionDeleteHistory = new DeleteHistory(ContentType.QUESTION, Q3.getId(), Q3.getWriter(), LocalDateTime.now());

        assertAll(
            () -> Assertions.assertThat(realQuestionDeleteHistory.getContentId()).isEqualTo(expectedQuestionDeleteHistory.getContentId()),
            () -> Assertions.assertThat(realQuestionDeleteHistory.getContentType()).isEqualTo(expectedQuestionDeleteHistory.getContentType()),
            () -> Assertions.assertThat(realQuestionDeleteHistory.getDeletedByUser()).isEqualTo(expectedQuestionDeleteHistory.getDeletedByUser())
        );
        Assertions.assertThat(Q3.isDeleted()).isTrue();
    }
}
