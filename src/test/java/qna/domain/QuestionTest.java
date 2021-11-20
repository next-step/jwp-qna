package qna.domain;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import qna.NotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
public class QuestionTest {

    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    UserRepository userRepository;

    public User javajigi;
    public User sanjigi;

    @BeforeEach
    void setUp() {
        javajigi = userRepository.save(UserTest.JAVAJIGI);
        sanjigi = userRepository.save(UserTest.SANJIGI);
        userRepository.flush();
    }

    @Test
    void save() {
        final String title = questionRepository.save(QuestionFixture.질문().writeBy(javajigi)).getTitle();
        questionRepository.flush();
        assertThat(title).isEqualTo("title1");

    }

    @Test
    void findById() {
        final Question question = questionRepository.save(QuestionFixture.질문().writeBy(javajigi));
        questionRepository.flush();
        final Question expected = questionRepository.findById(question.getId())
                .orElseThrow(NotFoundException::new);
        assertThat(question.getId()).isEqualTo(expected.getId());
    }

    @Test
    @DisplayName("삭제되지 않은 질문 찾기")
    void findByDeletedFalse() {
        final Question question = questionRepository.save(QuestionFixture.질문().writeBy(javajigi));
        question.setDeleted(true);
        questionRepository.flush();
        assertThat(questionRepository.findByDeletedFalse()).hasSize(0);
    }

    @Test
    @DisplayName("id로 삭제되지 않은 질문 찾기")
    void findByIdAndDeletedFalse() {
        final Question question = questionRepository.save(QuestionFixture.질문().writeBy(javajigi));
        question.setDeleted(true);
        questionRepository.flush();
        assertThatThrownBy(() -> questionRepository.findByIdAndDeletedFalse(question.getId())
                .orElseThrow(NotFoundException::new))
                .isInstanceOf(NotFoundException.class);
    }

    @AfterEach
    public void tearDown() {
        userRepository.deleteAll();
        questionRepository.deleteAll();
        userRepository.flush();
        questionRepository.flush();
    }
}
