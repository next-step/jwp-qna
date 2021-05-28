package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.util.ReflectionTestUtils;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
@Transactional
public class AnswerTest {
    public static Answer A1;
    public static Answer A2;
    public static Answer A3;

    @Autowired
    private AnswerRepository answerRepository;

    private Answer a1;
    private Answer a2;
    private Answer a3;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(QuestionTest.Q1, "id", 1L);
        ReflectionTestUtils.setField(QuestionTest.Q1, "id", 2L);

        A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
        A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");
        A3 = new Answer(UserTest.INSUP, QuestionTest.Q2, "Answers Contents3");

        a1 = answerRepository.save(A1);
        a2 = answerRepository.save(A2);
        a3 = answerRepository.save(A3);
    }

    @DisplayName("Answer 저장")
    @Test
    void save() {
        assertAll(
                () -> assertThat(a1.getId()).isNotNull(),
                () -> assertThat(a1.getContents()).isEqualTo("Answers Contents1")
        );
    }

    @DisplayName("Answer id로 찾기")
    @Test
    void findById() {
        Answer findAnswer = answerRepository.findById(
                a1.getId()).orElseThrow(NoSuchElementException::new);

        assertAll(
                () -> assertThat(findAnswer.getContents()).isEqualTo("Answers Contents1"),
                () -> assertThat(findAnswer.getQuestionId()).isEqualTo(QuestionTest.Q1.getId()),
                () -> assertThat(findAnswer.getWriterId()).isEqualTo(UserTest.JAVAJIGI.getId()),
                () -> assertThat(findAnswer).isSameAs(a1)
        );

    }

    @DisplayName("업데이트 확인")
    @Test
    void update() {
        a2.setContents("Answers updatedContents2");

        assertAll(
                () -> assertThat(a2.getContents()).isEqualTo("Answers updatedContents2")
        );
    }

    @DisplayName("NonNull인 항목 Not Null인지 확인")
    @Test
    void checkNonNull() {
        assertThat(a1.isDeleted()).isNotNull();
    }

    @DisplayName("Answer의 deleted가 true인 경우 find 안되도록")
    @Test
    void findByIdAndDeletedFalse() {
        a3.setDeleted(true);
        Optional<Answer> optAnswer = answerRepository.findByIdAndDeletedFalse(a3.getId());
        assertThatThrownBy(() -> optAnswer.orElseThrow(NoSuchElementException::new))
                .isInstanceOf(NoSuchElementException.class);
    }

    @DisplayName("동일한 QuestionId를 가지고 있는 항목 확인")
    @Test
    void findByQuestionIdAndDeletedFalse() {
        System.out.println(QuestionTest.Q1.getId());
        List<Answer> answerList = answerRepository.findByQuestionIdAndDeletedFalse(QuestionTest.Q1.getId());
        assertThat(answerList).hasSize(2);
    }
}
