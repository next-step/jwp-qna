package qna.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Autowired
    private UserRepository users;
    @Autowired
    private QuestionRepository questions;

    @BeforeEach
    void setUp() {
        users.save(UserTest.SANJIGI);
        users.save(UserTest.JAVAJIGI);
    }

    @DisplayName("Question 저장 및 데이터 확인")
    @Test
    void saveQuestion() {
        final Question actual = questions.save(Q1);

        User writerId = actual.getWriter();

        assertThat(writerId).isEqualTo(Q1.getWriter());
    }

    @DisplayName("writer_id로 Question 정보 찾기")
    @Test
    void findByWriterId() {
        final Question standard = questions.save(Q1);
        final Question target = questions.findByWriterId(UserTest.JAVAJIGI.getId());

        User standardWriter = standard.getWriter();
        User targetWriter = target.getWriter();

        assertThat(standardWriter).isEqualTo(targetWriter);
    }

    @DisplayName("Question Title 정보 Like로 찾기")
    @Test
    void findByTitleLike() {
        questions.save(Q1);
        final Question target = questions.findByTitleLike("%1");

        String targetTitle = target.getTitle();

        assertThat(targetTitle).contains("1");
    }

    @DisplayName("Question 정보 중 deleted false 인 값 찾기")
    @Test
    void findByDeletedFalse() {
        Question firstQuestion = questions.save(Q1);
        Question secondQuestion = questions.save(Q2);

        List<Question> questionList = questions.findByDeletedFalse();

        assertThat(questionList).contains(firstQuestion, secondQuestion);
    }
}
