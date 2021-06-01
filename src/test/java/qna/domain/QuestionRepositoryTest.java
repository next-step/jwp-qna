package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static qna.domain.AnswerTest.A1;
import static qna.domain.AnswerTest.A2;
import static qna.domain.QuestionTest.Q1;

@DataJpaTest
public class QuestionRepositoryTest {
    @Autowired
    private AnswerRepository answers;

    @Autowired
    private UserRepository users;

    @Autowired
    private QuestionRepository questions;

    @BeforeEach
    void setUp() {
        users.save(UserTest.JAVAJIGI);
        users.save(UserTest.SANJIGI);
        users.flush();
    }

    @DisplayName("Question 저장 테스트")
    @Test
    void save() {
        Question actual = questions.save(Q1);
        assertAll(
                () -> assertThat(actual).isNotNull(),
                () -> assertThat(actual.isOwner(UserTest.JAVAJIGI)).isTrue()
        );
    }

    @DisplayName("Question에 Q1 를 저장 후 Title로 데이터 조회")
    @Test
    void findByTitle() {
        questions.save(Q1);
        Question actual = questions.findByTitle("title1");
        assertThat(actual).isNotNull();
        assertThat(actual.isOwner(UserTest.JAVAJIGI)).isTrue();
    }

    @DisplayName("Question 에 Q1을 저장 후 writeBy 를 SANJIGI로 업데이트")
    @Test
    void update() {
        Question expected = questions.save(Q1);
        expected.writeBy(UserTest.SANJIGI);
        Question actual = questions.findByWriter(UserTest.SANJIGI);

        assertThat(actual.isOwner(UserTest.SANJIGI)).isTrue();
        assertThat(actual.getTitle()).isNotEqualTo(QuestionTest.Q2.getTitle());
    }

    @DisplayName("Question 에 Q1를 저장 후 다시 delete")
    @Test
    void delete() {
        questions.save(Q1);
        Question actual = questions.findByWriter(UserTest.JAVAJIGI);
        assertThat(actual).isNotNull();

        questions.delete(actual);
        Question expected = questions.findByWriter(UserTest.JAVAJIGI);
        assertThat(expected).isNull();
    }

    @DisplayName("Question Q1에 등록된 Answer List 조회 테스트")
    @Test
    void findByQuestionId() {
        questions.save(Q1);
        answers.save(A1);
        answers.save(A2);

        Question actual = questions.findByWriter(UserTest.JAVAJIGI);
        List<Answer> answers = actual.getAnswers();
        answers.stream()
                .forEach(
                        answer -> assertThat(answer.getQuestionId()).isEqualTo(Q1.getId())
                );
    }
}
