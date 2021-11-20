package qna.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class QuestionRepositoryTest {
    @Autowired
    UserRepository users;
    @Autowired
    QuestionRepository questions;

    private User javaJigi;
    private User sanJigi;
    private Question javaJigiQuestion;
    private Question sanJigiQuestion;

    @BeforeEach
    void setUp() {
        javaJigi = users.save(UserTest.JAVAJIGI);
        sanJigi = users.save(UserTest.SANJIGI);
        javaJigiQuestion = QuestionTest.Q1;
        sanJigiQuestion = QuestionTest.Q1;
    }

    @DisplayName("Question 저장 및 데이터 확인")
    @Test
    void saveQuestion() {
        final Question actual = questions.save(javaJigiQuestion);

        User writerId = actual.getWriter();

        assertThat(writerId).isEqualTo(javaJigiQuestion.getWriter());
    }

    @DisplayName("writer_id로 Question 정보 찾기")
    @Test
    void findByWriterId() {
        final Question standard = questions.save(javaJigiQuestion);
        final Question target = questions.findByWriterId(javaJigi.getId());

        User standardWriter = standard.getWriter();
        User targetWriter = target.getWriter();

        assertThat(standardWriter).isEqualTo(targetWriter);
    }

    @DisplayName("Question Title 정보 Like로 찾기")
    @Test
    void findByTitleLike() {
        questions.save(javaJigiQuestion);
        final Question target = questions.findByTitleLike(new Title("%1"));

        String targetTitle = target.getTitle();

        assertThat(targetTitle).contains("1");
    }

    @DisplayName("Question 정보 중 deleted false 인 값 찾기")
    @Test
    void findByDeletedFalse() {
        Question firstQuestion = questions.save(javaJigiQuestion);
        Question secondQuestion = questions.save(sanJigiQuestion);

        List<Question> questionList = questions.findByDeletedFalse();

        assertThat(questionList).contains(firstQuestion, secondQuestion);
    }
}
