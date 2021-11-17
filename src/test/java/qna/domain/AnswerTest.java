package qna.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import qna.CannotDeleteException;

@DataJpaTest
public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setup() {
        A1.getWriter().setId(null);
        final User user = userRepository.save(A1.getWriter());
        A1.setWriter(user);
        QuestionTest.Q1.setWriter(user);
        QuestionTest.Q1.setId(null);
        final Question question = questionRepository.save(QuestionTest.Q1);
        A1.setQuestion(question);
    }

    @Test
    @DisplayName("Answer Entity Create 및 ID 생성 테스트")
    void save() {
        final Answer answer = answerRepository.save(A1);
        assertThat(answer.getId()).isNotNull();
    }

    @Test
    @DisplayName("Answer Entity Read 테스트")
    void findById() {
        final Answer saved = answerRepository.save(A1);
        final Answer found = answerRepository.findById(saved.getId()).orElseGet(()->null);
        assertThat(found).isEqualTo(saved);
    }

    @Test
    @DisplayName("Answer Entity Update 테스트")
    void update() {
        final Answer saved = answerRepository.save(A1);
        saved.setContents("updated!");
        final Answer found = answerRepository.findById(saved.getId()).orElseThrow(() -> new RuntimeException("테스트실패"));
        assertThat(found.getContents()).isEqualTo("updated!");
    }

    @Test
    @DisplayName("Answer Entity Delete 테스트")
    void delete() {
        final Answer saved = answerRepository.save(A1);
        answerRepository.delete(saved);
        answerRepository.flush();
        final Answer found = answerRepository.findById(saved.getId()).orElseGet(() -> null);
        assertThat(found).isNull();
    }

    @Test
    @DisplayName("Question Entity를 가지고 있는 Answer Entity Save 테스트")
    void saveWithQuestion() {
        final Answer saved = answerRepository.save(A1);
        final Answer found = answerRepository.findById(saved.getId()).orElseThrow(() -> new RuntimeException("테스트실패"));
        assertThat(found.getQuestion()).isEqualTo(saved.getQuestion());
    }

    @Test
    @DisplayName("답변 삭제하기 로직(성공)")
    void deleteAnswer() {
        Question question1 = new Question(1L,"t1","c1").writeBy(UserTest.JAVAJIGI);
        Answer answer1 = new Answer(1L, UserTest.JAVAJIGI, question1, "answer c1");
        assertThatCode(()->{
            DeleteHistory deleted = answer1.delete(answer1.getWriter());
            assertThat(answer1).extracting("deleted").isEqualTo(true);
            assertThat(deleted).extracting("contentId").isEqualTo(answer1.getId());
            assertThat(deleted.getContentType()).isEqualTo(ContentType.ANSWER);
        }).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("답변 삭제하기 로직(실패-작성자가 다를경우")
    void deleteAnswerFail() {
        Question question1 = new Question(1L,"t1","c1").writeBy(UserTest.JAVAJIGI);
        Answer answer1 = new Answer(1L, UserTest.JAVAJIGI, question1, "answer c1");
        assertThatThrownBy(()->{
            answer1.delete(UserTest.SANJIGI);
        }).isInstanceOf(CannotDeleteException.class);
    }
}
