package qna.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class AnswerRepositoryTest extends BaseDataJpaTest {

    @Autowired
    private AnswerRepository repository;

    Answer answer;
    Answer savedAnswer;

    @BeforeEach
    void setUp() {
        answer = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
        savedAnswer = repository.save(answer);
    }

    @Test
    @DisplayName("서로 같은 데이터를 가진 엔티티는 동일해야 한다")
    void entitySameAsTest() {
        assertThat(savedAnswer).isSameAs(answer);
    }

    @Test
    @DisplayName("조회한 데이터와 같은 id 값을 가진 엔티티는 동일해야 한다")
    void entityRetrieveTest() {
        List<Answer> findAnswers = repository.findByQuestionIdAndDeletedFalse(answer.getQuestionId());
        Answer findAnswer = repository.findById(savedAnswer.getId()).get();

        assertAll(
                () -> assertThat(findAnswers.get(0)).isSameAs(savedAnswer),
                () -> assertThat(findAnswer).isSameAs(savedAnswer),
                () -> assertThat(findAnswer).isSameAs(findAnswers.get(0))
        );
    }

    @Test
    @DisplayName("저장 전 후의 데이터가 같아야 한다")
    void entitySameValueTest() {
        assertAll(
                () -> assertThat(savedAnswer.getId()).isNotNull(),
                () -> assertThat(savedAnswer.getContents()).isEqualTo(answer.getContents()),
                () -> assertThat(savedAnswer.getQuestionId()).isEqualTo(answer.getQuestionId()),
                () -> assertThat(savedAnswer.getWriterId()).isEqualTo(answer.getWriterId()),
                () -> assertThat(savedAnswer.isDeleted()).isEqualTo(answer.isDeleted())
        );
    }

    @Test
    @DisplayName("inert 시 createAt 이 자동으로 입력된다.")
    void dateAutoCreateTest() {

        assertAll(
                () -> assertThat(savedAnswer.getCreateAt()).isNotNull(),
                () -> assertThat(savedAnswer.getUpdateAt()).isNull()
        );
    }

    @Test
    @DisplayName("update 시 updateAt 이 자동으로 변경된다.")
    void dateAutoModifyTest() {
        savedAnswer.setContents("update date?");
        repository.flush();

        assertThat(savedAnswer.getUpdateAt()).isNotNull();
    }

    @AfterEach
    void endUp() {
        repository.deleteAll();
    }
}