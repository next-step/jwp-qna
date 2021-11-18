package qna.repos;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.*;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository questions;

    @Autowired
    private AnswerRepository answers;

    @Autowired
    private UserRepository users;

    private User user;

    @BeforeEach
    void setUp() {
        user = users.save(UserTest.JAVAJIGI);
        users.save(user);
    }

    @DisplayName("Question 저장 테스트")
    @Test
    void save() {
        Question actual = questions.save(new Question("title1", "contents1").writeBy(user));

        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getContents()).isEqualTo("contents1")
        );
    }

    @DisplayName("Question deleted=false 경우 테스트")
    @Test
    void findByIdAndDeletedFalse() {
        //given
        Question q1 = questions.save(new Question("title1", "contents1").writeBy(user));
        Question q2 = questions.save(new Question("title2", "contents2").writeBy(user));

        //when
        q2.setDeleted(true);
        questions.save(q2);
        Optional<Question> a1Result = questions.findByIdAndDeletedFalse(q1.getId());
        Optional<Question> a2Result = questions.findByIdAndDeletedFalse(q2.getId());
        List<Question> result = questions.findByDeletedFalse();

        //then
        assertAll(
                () -> assertThat(a1Result).isNotEmpty(),
                () -> assertThat(a2Result).isEmpty(),
                () -> assertThat(result.size()).isEqualTo(1)
        );
    }

    @DisplayName("Question Answer 연관관계 테스트")
    @Test
    void saveQuestionAndAnswer() {
        //given
        Question q1 = questions.save(new Question("title1", "contents1").writeBy(user));

        //when
        answers.save(new Answer(user, q1, "Answers Contents1"));
        Question actual = questions.save(q1);
        Answers answers = actual.getAnswers();

        //then
        assertThat(answers.getSize()).isEqualTo(1);
    }
}
