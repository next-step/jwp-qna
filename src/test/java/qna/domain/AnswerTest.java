package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("answer 엔티티 테스트")
@DataJpaTest
class AnswerTest {
    public static final Answer A1 = new Answer(1L, UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(2L, UserTest.SANJIGI, QuestionTest.Q2, "Answers Contents2");
    public static final Answer DELETE_SOON_ANSWER = new Answer(3L, UserTest.MINGVEL, QuestionTest.Q3,
            "Answers Contents3");

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @DisplayName("save 성공")
    @Test
    void save_answer_success() {
        //given:
        Answer answer = answerRepository.save(A2);
        assertThat(answer).isEqualTo(A2);
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

    @DisplayName("save - Update 동작 테스트")
    @Test
    void saveUpdate_answer_success() {
        //given:
        Answer answer = answerRepository.save(A1);
        String modifiedContent = "updated Content";
        //when:
        answer.setContents(modifiedContent);
        Answer modifiedAnswer = answerRepository.findByIdAndDeletedFalse(answer.getId()).orElse(new Answer());
        //then:
        assertThat(modifiedAnswer.getContents()).isEqualTo(modifiedContent);
    }

    @DisplayName("delete 메서드 테스트")
    @Test
    void delete_answer_success() {
        //given:
        Answer answer = answerRepository.save(DELETE_SOON_ANSWER);
        //when:
        answerRepository.delete(answer);
        Answer resultAnswer = answerRepository.findById(answer.getId()).orElse(null);
        assertThat(resultAnswer).isNull();
    }
}
