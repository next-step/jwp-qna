package qna.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static qna.domain.AnswerTest.A1;

@DataJpaTest
class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository repository;

    Answer savedAnswer;

    @BeforeEach
    void setUp() {
        savedAnswer = repository.save(A1);
    }

    @Test
    @DisplayName("서로 같은 데이터를 가진 엔티티는 동일해야 한다")
    void answerSameAsTest() {
        assertThat(savedAnswer).isSameAs(A1);
    }

    @Test
    @DisplayName("조회한 데이터와 같은 id 값을 가진 엔티티는 동일해야 한다")
    void answerRetrieveTest() {
        List<Answer> findAnswers = repository.findByQuestionIdAndDeletedFalse(A1.getQuestionId());
        Answer findAnswer = repository.findByIdAndDeletedFalse(savedAnswer.getId()).get();

        System.out.println("size: " + findAnswers.size());

        assertAll(
                () -> assertThat(findAnswers.get(0)).isSameAs(savedAnswer),
                () -> assertThat(findAnswer).isSameAs(savedAnswer),
                () -> assertThat(findAnswer).isSameAs(findAnswers.get(0))
        );
    }

    @Test
    @DisplayName("저장 전 후의 데이터가 같아야 한다")
    void answerSameValueTest() {
        assertAll(
                () -> assertThat(savedAnswer.getId()).isNotNull(),
                () -> assertThat(savedAnswer.getContents()).isEqualTo(A1.getContents()),
                () -> assertThat(savedAnswer.getQuestionId()).isEqualTo(A1.getQuestionId()),
                () -> assertThat(savedAnswer.getWriterId()).isEqualTo(A1.getWriterId()),
                () -> assertThat(savedAnswer.isDeleted()).isEqualTo(A1.isDeleted())
        );
    }

    @AfterEach
    void endUp() {
        repository.deleteAll();
    }
}