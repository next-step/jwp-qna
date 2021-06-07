package qna.domain;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository questions;

    private Question q1 = QuestionTest.Q1;

    private Question q2 = QuestionTest.Q2;

    @BeforeEach
    public void setUp() {
        q1.setDeleted(false);
        q2.setDeleted(false);
        questions.save(q1);
        questions.save(q2);
    }

    @Test
    @DisplayName("질문 등록하기")
    public void save() {
        assertAll(
                () -> assertThat(q1.getId()).isNotNull(),
                () -> assertThat(q1.getId()).isEqualTo(QuestionTest.Q1.getId()),
                () -> assertThat(q2.getId()).isNotNull(),
                () -> assertThat(q2.getId()).isEqualTo(QuestionTest.Q2.getId())
        );
    }

    @Test
    @DisplayName("삭제된 질문을 가져오지 않는지 확인")
    public void findByIdAndDeletedFalse() {
        q1.setDeleted(true);
        Optional<Question> question = questions.findByIdAndDeletedFalse(QuestionTest.Q1.getId());
        assertThat(question).isEmpty();
    }

    @Test
    @DisplayName("삭제되지 않은 질문 모두 가져오기")
    public void findByDeletedFalse() {
        List<Question> questionList = questions.findByDeletedFalse();
        assertThat(questionList.size()).isEqualTo(2);
    }

}