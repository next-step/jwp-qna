package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.CannotDeleteException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static qna.domain.QuestionTest.Q1;
import static qna.domain.UserTest.JAVAJIGI;
import static qna.domain.UserTest.SANJIGI;

@DataJpaTest
public class AnswerTest {
    public static final Answer A1 = new Answer(JAVAJIGI, Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(SANJIGI, Q1, "Answers Contents2");

    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private QuestionRepository questionRepository;

    @BeforeEach
    void setUp() {
        userRepository.save(JAVAJIGI);
        userRepository.save(SANJIGI);
        questionRepository.save(Q1);
        answerRepository.save(A1);
        answerRepository.save(A2);
    }

    @Test
    void 답변_작성자와_로그인_유저가_같아야_삭제처리_할_수_있다() {
        assertThatThrownBy(() -> A1.delete(User.GUEST_USER)).isInstanceOf(CannotDeleteException.class);
        assertThatThrownBy(() -> A1.delete(SANJIGI)).isInstanceOf(CannotDeleteException.class);
        assertDoesNotThrow(() -> A1.delete(JAVAJIGI));

        assertThatThrownBy(() -> A2.delete(User.GUEST_USER)).isInstanceOf(CannotDeleteException.class);
        assertThatThrownBy(() -> A2.delete(JAVAJIGI)).isInstanceOf(CannotDeleteException.class);
        assertDoesNotThrow(() -> A2.delete(SANJIGI));
    }
}
