package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

@DisplayName("answer 엔티티 테스트")
@DataJpaTest
class AnswerTest {
    public static final Answer A1 = new Answer(1L, UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(2L, UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @DisplayName("save 성공")
    @Test
    void save_answer_success() {
        assertThatNoException().isThrownBy(() -> answerRepository.save(A1));
    }

    @DisplayName("createdAt 자동 생성 여부 테스트")
    @Test
    void save_answer_createdAt() {
        //given, when:
        final Answer answer = answerRepository.save(A1);
        //then:
        assertThat(answer.getCreatedAt()).isNotNull();
    }

    @DisplayName("updatedAt 데이터 변경 여부 테스트")
    @Test
    void save_answer_changedUpdatedAt() {
        //given:
        final Answer answer = answerRepository.save(A1);
        final LocalDateTime before = answer.getUpdatedAt();
        //when:
        answer.setContents("modified");
        final Answer updatedAnswer = answerRepository.save(answer);
        testEntityManager.flush();
        //then:
        assertThat(updatedAnswer.getUpdatedAt()).isAfter(before);
    }

    @DisplayName("findByQuestionIdAndDeletedFalse 메서드 테스트")
    @Test
    void findByQuestionIdAndDeletedFalse_answer_success() {
        //given:
        Answer answer = answerRepository.save(A1);
        //when, then:
        assertThat(answerRepository.findByQuestionIdAndDeletedFalse(answer.getQuestionId())).contains(answer);
    }

    @DisplayName("findByIdAndDeletedFalse 메서드 테스트")
    @Test
    void findByIdAndDeletedFalse_answer_success() {
        //given:
        Answer answer = answerRepository.save(A1);
        //when, then:
        assertThat(answerRepository.findByIdAndDeletedFalse(answer.getId())).containsSame(answer);
    }
}
