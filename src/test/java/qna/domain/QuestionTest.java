package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);
    
    @Autowired
    private QuestionRepository questions;

    @Autowired
    private UserRepository users;

    @Autowired
    private AnswerRepository answers;

    @Autowired
    private EntityManager entityManager;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User("mwkwon", "password", "권민욱", "mwkwon0110@gmail.com");
        users.save(user);
    }

    @Test
    @DisplayName("질문 테이블 정상 저장 테스트")
    void save() {
        Question expected = new Question("title1", "contents").writeBy(user);
        Question actual = questions.save(expected);
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getWriter()).isEqualTo(expected.getWriter()),
                () -> assertThat(user.getId()).isNotNull(),
                () -> assertThat(actual.getId()).isEqualTo(user.getId())
        );
    }

    @Test
    @DisplayName("질문 테이블 아이디 정상 조회 테스트")
    void findById() {
        Question expected = questions.save(new Question("title1", "contents").writeBy(user));
        Optional<Question> actual = questions.findById(expected.getId());
        assertThat(actual.isPresent()).isTrue();
        assertThat(actual.get().getId()).isEqualTo(expected.getId());
        assertThat(actual.get() == expected).isTrue();
    }

    @Test
    @DisplayName("question entity를 이용하여 답변 리스트 정상 조회 테스트")
    void findAnswersByQuestion() {
        Question expected = new Question("title1", "contents").writeBy(user);
        questions.save(expected);
        entityManager.flush();
        entityManager.clear();

        List<Answer> answers = Arrays.asList(
                new Answer(user, expected, "Answers Contents1"),
                new Answer(user, expected, "Answers Contents1"));
        List<Answer> exceptedAnswers = this.answers.saveAll(answers);

        Optional<Question> actual = questions.findById(expected.getId());

        assertThat(actual.isPresent()).isTrue();
        assertThat(actual.get().isContainAnswer(answers.get(0))).isTrue();
        assertThat(actual.get().isContainAnswer(answers.get(1))).isTrue();
        assertThat(actual.get().getAnswers().size()).isEqualTo(answers.size());

    }

    @Test
    @DisplayName("질문 테이블 정상 수정 테스트")
    void update() {
        Question expected = questions.save(new Question("title1", "contents").writeBy(user));
        expected.addDeleted(true);
        entityManager.flush();
        entityManager.clear();
        Optional<Question> actual = questions.findById(expected.getId());
        assertThat(actual.isPresent()).isTrue();
        assertThat(actual.get().isDeleted()).isTrue();
    }

    @Test
    @DisplayName("질문 테이블 정상 삭제 테스트")
    void delete() {
        Question expected = questions.save(new Question("title1", "contents").writeBy(user));
        questions.delete(expected);
        entityManager.flush();
        entityManager.clear();
        Optional<Question> actual = questions.findById(expected.getId());
        assertThat(actual.isPresent()).isFalse();
    }
}
