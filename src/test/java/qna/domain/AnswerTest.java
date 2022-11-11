package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import qna.CannotDeleteException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;
    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    void setUp() {
        userRepository.save(UserTest.JAVAJIGI);
        userRepository.save(UserTest.SANJIGI);
        questionRepository.save(QuestionTest.Q1);
        questionRepository.save(QuestionTest.Q2);
    }

    @Test
    void 답변저장_성공() {
        answerRepository.save(A1);
        answerRepository.save(A2);
        flushAndClear();

        Answer answer1 = answerRepository.findById(1L).get();
        Answer answer2 = answerRepository.findById(2L).get();

        assertAll(
                () -> assertThat(answer1.getId()).isEqualTo(1L),
                () -> assertThat(answer2.getId()).isEqualTo(2L)
        );
    }

    @DisplayName("답변 삭제 성공 시 delete flag가 true로 변경")
    @Test
    void 답변삭제_성공() throws Exception {
        // given
        answerRepository.save(A1);

        // when
        A1.delete(UserTest.JAVAJIGI);
        flushAndClear();

        // then
        Answer answer = answerRepository.findById(A1.getId()).get();
        assertThat(answer.isDeleted()).isTrue();
    }

    @DisplayName("답변 삭제 시 답변 작성자가 로그인 사용자와 다른 경우 예외")
    @Test
    void 답변삭제_예외_작성자_다른경우() throws Exception {
        // given
        answerRepository.save(A1);

        // when / then
        assertThatThrownBy(() -> A1.delete(UserTest.SANJIGI))
                .isInstanceOf(CannotDeleteException.class);
    }

    @Test
    void 답변삭제후_DeleteHistory저장_성공() throws Exception {
        // given
        answerRepository.save(A1);

        // when
        DeleteHistory delete = A1.delete(UserTest.JAVAJIGI);
        deleteHistoryRepository.save(delete);

        // then
        DeleteHistory deleteHistory = deleteHistoryRepository.findById(1L).get();
        assertThat(deleteHistory.equals(delete)).isTrue();
    }

    private void flushAndClear() {
        entityManager.flush();
        entityManager.clear();
    }
}
