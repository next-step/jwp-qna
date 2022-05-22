package qna.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import qna.domain.Question;
import qna.domain.User;
import qna.domain.UserTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class QuestionRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    private Question question;
    private User writer;

    @BeforeEach
    void setUp() {
        writer = userRepository.save(UserTest.JAVAJIGI);
        question = questionRepository.save(new Question("title", "contents").writeBy(writer));
    }

    @Test
    @DisplayName("User(writer) 연관관계 테스트")
    void Question_to_Writer() {
        em.clear();
        Optional<Question> optionalQuestion = questionRepository.findById(this.question.getId());
        assertThat(optionalQuestion.get().getWriter().getId()).isEqualTo(writer.getId());
    }

    @Test
    @DisplayName("삭제되지 않은 상태의 question를 id 기준으로 검색")
    void Question_findByIdAndDeletedFalse() {
        em.clear();
        Optional<Question> optionalQuestion = questionRepository.findByIdAndDeletedFalse(question.getId());
        assertThat(optionalQuestion.isPresent()).isTrue();

        optionalQuestion.get().setDeleted(true);
        questionRepository.flush();
        em.clear();

        optionalQuestion = questionRepository.findByIdAndDeletedFalse(question.getId());
        assertThat(optionalQuestion.isPresent()).isFalse();
    }

    @Test
    @DisplayName("삭제되지 않았은 상태의 question를 id 기준으로 검색")
    void Question_findByQuestionIdAndDeletedFalse() {
        Question question = questionRepository.save(new Question("title", "contents").writeBy(writer));
        question.setDeleted(true);
        questionRepository.flush();
        em.clear();

        List<Question> questionList = questionRepository.findByDeletedFalse();

        assertAll(
            () -> assertThat(questionList).hasSize(1),
            () -> assertThat(questionList.get(0).getId()).isEqualTo(this.question.getId())
        );
    }

    @Test
    @DisplayName("findById가 정상적으로 이루어지는지 확인")
    void Question_select() {
        em.clear();
        Optional<Question> question = questionRepository.findById(this.question.getId());
        assertThat(question.isPresent()).isTrue();
    }

    @Test
    @DisplayName("save가 정상적으로 이루어지는지 확인")
    void Question_save() {
        assertThat(question.getId()).isNotNull();
    }

    @Test
    @DisplayName("update가 정상적으로 이루어지는지 확인")
    void Question_update() {
        question.setContents("update test");
        questionRepository.flush();
        Optional<Question> question = questionRepository.findById(this.question.getId());
        assertThat(question.get().getContents()).isEqualTo("update test");
    }

    @Test
    @DisplayName("delete가 정상적으로 이루어지는지 확인")
    void Question_delete() {
        questionRepository.delete(question);
        questionRepository.flush();
        Optional<Question> question = questionRepository.findById(this.question.getId());
        assertThat(question.isPresent()).isFalse();
    }

}
