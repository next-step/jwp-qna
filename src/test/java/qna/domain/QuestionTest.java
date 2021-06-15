package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import qna.CannotDeleteException;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
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

        q1 = questionRepository.save(new Question("title1", "contents1").writeBy(javajigi));
        q2 = questionRepository.save(new Question("title2", "contents2").writeBy(sanjigi));
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
    @DisplayName("삭제되지 않은 질문들 확인")
    public void findByDeletedFalse() {
        List<Question> question = questionRepository.findByDeletedFalse();
        assertThat(question.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("질문 삭제 - 작성자가 다른 경우")
    public void deleteDiffUser() {
        assertThatThrownBy(() -> q1.delete(sanjigi, new ArrayList<>()))
                .isInstanceOf(CannotDeleteException.class)
                .hasMessage("질문을 삭제할 권한이 없습니다.");
    }

    @Test
    @DisplayName("질문 삭제 - 작성자 + 삭제된 질문은 가져올 수 없다. ")
    @Transactional
    public void deleteSameUser() {
        q1.delete(javajigi, new ArrayList<>());
        Optional<Question> question = questionRepository.findByIdAndDeletedFalse(q1.getId());
        assertThatThrownBy(() -> question.orElseThrow(NoSuchElementException::new))
                .isInstanceOf(NoSuchElementException.class);
    }
}
