package qna.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class AnswerTest {

    private Answer a1;
    private Answer a2;

    private Question q1;
    private Question q2;

    private User javajigi;
    private User sanjigi;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        javajigi = userRepository.save(UserTest.JAVAJIGI);
        sanjigi = userRepository.save(UserTest.SANJIGI);

        q1 = questionRepository.save(QuestionTest.Q1.writeBy(javajigi));
        q2 = questionRepository.save(QuestionTest.Q2.writeBy(sanjigi));

        a1 = answerRepository.save(new Answer(javajigi, q1, "Answers Contents1"));
        a2 = answerRepository.save(new Answer(sanjigi, q2, "Answers Contents2"));
    }

    @DisplayName("save 확인")
    @Test
    public void save() {
        assertAll(
                () -> assertThat(a1.getId()).isNotNull(),
                () -> assertThat(a1.getQuestion()).isSameAs(q1),
                () -> assertThat(a1.getWriter()).isSameAs(javajigi),
                () -> assertThat(a1.getContents()).isEqualTo("Answers Contents1"),
                () -> assertThat(a2.getId()).isNotNull(),
                () -> assertThat(a2.getQuestion()).isSameAs(q2),
                () -> assertThat(a2.getWriter()).isSameAs(sanjigi),
                () -> assertThat(a2.getContents()).isEqualTo("Answers Contents2")
        );
    }

    @DisplayName("findByIdAndDeletedFalse 확인")
    @Test
    void findByIdAndDeletedFalse() {
        a1.setDeleted(true);
        Optional<Answer> answer = answerRepository.findByIdAndDeletedFalse(a1.getId());
        assertThatThrownBy(() -> answer.orElseThrow(NoSuchElementException::new))
                .isInstanceOf(NoSuchElementException.class);
    }

    @DisplayName("삭제되지 않은 답변 목록을 질문 아이디로 가져오는지 확인 + answerFindByQuestionId")
    @Test
    void findByQuestionIdAndDeletedFalse() {
        List<Answer> answerList = answerRepository.findByQuestionIdAndDeletedFalse(QuestionTest.Q2.getId());
        assertThat(answerList.size()).isEqualTo(1);

        a2.setDeleted(true);

        answerList = answerRepository.findByQuestionIdAndDeletedFalse(QuestionTest.Q2.getId());
        assertThat(answerList).hasSize(0);
    }
}
