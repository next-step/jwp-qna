package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class QuestionRepositoryTest {
    @Autowired
    private QuestionRepository questions;

    @DisplayName("Question 저장 테스트")
    @Test
    void save() {
        Question actual = questions.save(QuestionTest.Q1);
        assertAll(
                () -> assertThat(actual).isNotNull(),
                () -> assertThat(actual.isOwner(UserTest.JAVAJIGI)).isTrue()
        );
    }

    @DisplayName("Question에 Q1 를 저장 후 Title로 데이터 조회")
    @Test
    void findByTitle() {
        questions.save(QuestionTest.Q1);
        Question actual = questions.findByTitle("title1");
        assertThat(actual).isNotNull();
        assertThat(actual.isOwner(UserTest.JAVAJIGI)).isTrue();
    }

    @DisplayName("Question 에 Q1을 저장 후 writeBy 를 SANJIGI로 업데이트")
    @Test
    void update() {
        Question expected = questions.save(QuestionTest.Q1);
        expected.writeBy(UserTest.SANJIGI);
        Question actual = questions.findByWriterId(UserTest.SANJIGI.getId());

        assertThat(actual.isOwner(UserTest.SANJIGI)).isTrue();
        assertThat(actual.getTitle()).isNotEqualTo(QuestionTest.Q2.getTitle());
    }

    @DisplayName("Question 에 Q1를 저장 후 다시 delete")
    @Test
    void delete() {
        questions.save(QuestionTest.Q1);
        Question actual = questions.findByWriterId(UserTest.JAVAJIGI.getId());
        assertThat(actual).isNotNull();

        questions.delete(actual);
        Question expected = questions.findByWriterId(UserTest.JAVAJIGI.getId());
        assertThat(expected).isNull();
    }
}
