package qna.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class QuestionRepositoryTest {

    @Autowired
    AnswerRepository answers;

    @Autowired
    QuestionRepository questions;

    @Autowired
    UserRepository users;

    private Question question;
    private User user;

    @BeforeEach
    public void setUp() throws Exception {
        user = users.save(new User("answerJavajigi", "password", "javajigi", "javajigi@slipp.net"));
        question = new Question("title1", "contents1").writeBy(user);
    }

    @Test
    @DisplayName("QuestionRepository 저장 후 ID not null 체크")
    void save() {
        // when
        Question actual = questions.save(question);

        // then
        assertThat(actual.getId()).isNotNull();
    }

    @Test
    @DisplayName("QuestionRepository 저장 후 DB조회 객체 동일성 체크")
    void identity() {
        // when
        Question actual = questions.save(question);
        Question expect = questions.findById(actual.getId()).get();

        // then
        assertThat(actual).isEqualTo(expect);
    }

    @Test
    @DisplayName("삭제안된 Question 조회 검증, 조회결과있음")
    void findByIdAndDeletedFalse() {
        // given
        question.setDeleted(false);

        // when
        Question actual = questions.save(question);
        Question expect = questions.findByIdAndDeletedFalse(actual.getId()).get();

        // then
        assertThat(expect).isNotNull();
    }

    @Test
    @DisplayName("삭제안된 Question 조회 검증, 조회결과없음")
    void findByIdAndDeletedFalse_deleted_true() {
        // given
        // when
        Question actual = questions.save(question);
        actual.setDeleted(true);
        Optional<Question> expect = questions.findByIdAndDeletedFalse(question.getId());

        // then
        assertThat(expect.isPresent()).isFalse();
    }

    @Test
    @DisplayName("삭제안된 전체 질문 목록 불러오기 검증")
    void findByDeletedFalse() {
        // given
        QuestionTest.Q1.setDeleted(false);

        // when
        Question actual = questions.save(QuestionTest.Q1);

        List<Question> expect = questions.findByDeletedFalse();

        assertThat(expect).contains(actual);
    }
}
