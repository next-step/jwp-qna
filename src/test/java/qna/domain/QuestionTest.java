package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    private Question q1;
    private Question q2;

    private User javajigi;
    private User sanjigi;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        javajigi = userRepository.save(UserTest.JAVAJIGI);
        sanjigi = userRepository.save(UserTest.SANJIGI);
        Q1.writeBy(javajigi);
        Q2.writeBy(sanjigi);
        Q2.delete(true);
        q1 = questionRepository.save(Q1);
        q2 = questionRepository.save(Q2);
    }

    @Test
    @DisplayName("save 확인")
    public void save() {
        assertAll(
                () -> assertThat(q1.getId()).isNotNull(),
                () -> assertThat(q1.getContents()).isEqualTo(Q1.getContents()),
                () -> assertThat(q1.getWriter()).isSameAs(javajigi),
                () -> assertThat(q2.getId()).isNotNull(),
                () -> assertThat(q2.getContents()).isEqualTo(Q2.getContents()),
                () -> assertThat(q2.getWriter()).isSameAs(sanjigi)
        );
    }

    @Test
    @DisplayName("삭제된 질문을 가져올수 없는지 확인")
    public void findByIdAndDeletedFalse() {
        Optional<Question> question = questionRepository.findByIdAndDeletedFalse(q2.getId());
        assertThatThrownBy(() -> question.orElseThrow(NoSuchElementException::new))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @DisplayName("삭제되지 않은 질문들 확인")
    public void findByDeletedFalse() {
        List<Question> question = questionRepository.findByDeletedFalse();
        assertThat(question.size()).isEqualTo(1);
    }
}
