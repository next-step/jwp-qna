package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("답안 기능 테스트")
@DataJpaTest
public class AnswerTest {

    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Autowired
    private AnswerRepository answerRepository;

    @DisplayName("답안정보 단일 저장을 테스트합니다.")
    @Test
    public void 단일_저장() {
        Answer savedAnswer = answerRepository.save(A1);

        assertAll(
                () -> assertThat(savedAnswer).isInstanceOf(Answer.class).isNotNull(),
                () -> assertThat(savedAnswer.getId()).isNotNull().isGreaterThan(0L)
        );
    }

    @DisplayName("답안정보 목록 저장을 테스트합니다.")
    @Test
    public void 목록_저장() {
        List<Answer> answers = new ArrayList<>();
        answers.add(A1);
        answers.add(A2);

        List<Answer> savedAnswers = answerRepository.saveAll(answers);

        assertAll(
                () -> assertThat(savedAnswers).isNotEmpty().hasSize(answers.size()),
                () -> savedAnswers.forEach(savedAnswer -> {
                    assertThat(savedAnswer).isInstanceOf(Answer.class).isNotNull();
                    assertThat(savedAnswer.getId()).isNotNull().isGreaterThan(0L);

                    // answer 목록의 내용이 저장된게 맞는지 더블체크
                    Optional<Answer> hasAnswer = answers.stream().filter(user -> user.getId() == savedAnswer.getId()).findAny();
                    assertThat(hasAnswer.isPresent()).isTrue();
                })
        );
    }

    @DisplayName("답안정보 조회 성공을 테스트합니다.")
    @Test
    public void 조회_성공() {
        Answer savedAnswer = answerRepository.save(A1);
        Optional<Answer> answer = answerRepository.findById(savedAnswer.getId());

        assertAll(
                () -> assertThat(answer).isPresent(),
                () -> assertThat(answer.get().getContents()).isEqualTo(A1.getContents())
        );
    }

    @DisplayName("삭제되지 않은 답안정보 조회 성공을 테스트합니다.")
    @Test
    public void 삭제되지_않은_답안_조회_성공() {
        Answer savedAnswer = answerRepository.save(A1);
        Optional<Answer> answer = answerRepository.findByIdAndDeletedFalse(savedAnswer.getId());

        assertAll(
                () -> assertThat(answer).isPresent(),
                () -> assertThat(answer.get().getContents()).isEqualTo(A1.getContents())
        );
    }

    @DisplayName("삭제되지 않은 답안목록 조회 성공을 테스트합니다.")
    @Test
    public void 삭제되지_않은_답안목록_조회_성공() {
        List<Answer> answers = new ArrayList<>();
        answers.add(A1);
        answers.add(A2);

        answerRepository.saveAll(answers);

        List<Answer> notDeletedAnswers = answerRepository.findByQuestionIdAndDeletedFalse(A1.getQuestionId());
        assertThat(notDeletedAnswers).hasSize(answers.size());
    }

    @DisplayName("답안정보(id) 조회 실패를 테스트합니다.")
    @Test
    public void id_조회_실패() {
        answerRepository.save(A1);

        Optional<Answer> answer = answerRepository.findById(0L);
        assertThat(answer.isPresent()).isFalse();
    }

}
