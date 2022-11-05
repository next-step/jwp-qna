package qna.domain.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import qna.domain.Question;
import qna.domain.QuestionTest;

@DataJpaTest
class QuestionRepositoryTest {

    @Autowired
    QuestionRepository questions;

    @Autowired
    TestEntityManager em;

    @Test
    void 엔티티_저장() {
        Question expected = new Question("코드 리뷰 요청드립니다.", "리뷰 잘 부탁드립니다.");
        Question actual = questions.save(expected);
        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual == expected).isTrue();
    }

    @Test
    void Id로_삭제() {
        Question question = questions.save(new Question("코드 리뷰 요청드립니다.", "리뷰 잘 부탁드립니다."));
        questions.delete(question);
        questions.flush();
    }

    @Test
    @DisplayName("delete 컬럼이 false 인 질문목록 조회")
    void findByDeletedFalse() {
        questions.save(QuestionTest.Q1);
        questions.save(QuestionTest.Q2);
        List<Question> questionList = questions.findByDeletedFalse();
        assertThat(questionList).hasSize(2);
    }

    @Test
    @DisplayName("메소드명에 And 를 포함하여 And 조건문을 수행할 수 있다")
    void findByIdAndDeletedFalse() {
        Question expected = questions.save(QuestionTest.Q1);
        questions.save(QuestionTest.Q2);
        Question actual = questions.findByIdAndDeletedFalse(expected.getId()).get();
        assertThat(actual.getId()).isEqualTo(expected.getId());
    }
}
