package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import qna.CannotDeleteException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;
    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    void setUp() {
        userRepository.save(UserTest.JAVAJIGI);
        userRepository.save(UserTest.SANJIGI);

        answerRepository.save(AnswerTest.A1);
    }

    @Test
    void 질문저장_성공() {
        questionRepository.save(Q1);
        questionRepository.save(Q2);
        flushAndClear();

        Question question1 = questionRepository.findById(1L).get();
        Question question2 = questionRepository.findById(2L).get();

        assertAll(
                () -> assertThat(question1.getId()).isEqualTo(1L),
                () -> assertThat(question2.getId()).isEqualTo(2L)
        );
    }

    @DisplayName("연관관계 편의 메서드 테스트")
    @Test
    void 답글추가_성공_연관관계_편의_메서드_테스트() {
        Q1.addAnswer(AnswerTest.A1);
        questionRepository.save(Q1);
        flushAndClear();

        Question question = questionRepository.findById(1L).get();

        assertThat(question.answers()).contains(AnswerTest.A1);
    }

    @DisplayName("삭제 시 deleted 값을 true로 변경 테스트")
    @Test
    void 질문_삭제_성공_테스트() throws CannotDeleteException {
        // given
        questionRepository.save(Q1);

        // when
        DeleteHistories delete = Q1.delete(UserTest.JAVAJIGI);
        deleteHistoryRepository.saveAll(delete.deleteHistories());
        flushAndClear();

        // then
        Question question = questionRepository.findById(1L).get();
        assertThat(question.isDeleted()).isTrue();
    }

    @DisplayName("삭제 시 질문 작성자가 로그인 사용자와 다른 경우 예외")
    @Test
    void 질문_삭제_예외_작성자_다른경우() {
        // given
        questionRepository.save(Q1);

        // when / then
        assertThatThrownBy(() -> Q1.delete(UserTest.SANJIGI))
                .isInstanceOf(CannotDeleteException.class);
    }


    private void flushAndClear() {
        entityManager.flush();
        entityManager.clear();
    }
}
