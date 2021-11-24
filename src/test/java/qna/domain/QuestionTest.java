package qna.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.NotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@DisplayName("질문 테스트")
public class QuestionTest {

    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    UserRepository userRepository;

    private User javajigi;
    private User sanjigi;

    @BeforeEach
    void setUp() {
        javajigi = userRepository.save(UserTest.JAVAJIGI);
        sanjigi = userRepository.save(UserTest.SANJIGI);
    }

    @Test
    @DisplayName("질문 저장")
    void save() {
        final String title = questionRepository.save(QuestionFixture.질문().writeBy(javajigi)).getTitle();
        questionRepository.flush();
        assertThat(title).isEqualTo("title1");

    }

    @Test
    @DisplayName("질문 ID로 질문 조회")
    void findById() {
        final Question question = questionRepository.save(QuestionFixture.질문().writeBy(javajigi));
        questionRepository.flush();
        final Question expected = questionRepository.findById(question.getId())
                .orElseThrow(NotFoundException::new);
        assertThat(question.getId()).isEqualTo(expected.getId());
    }

    @Test
    @DisplayName("삭제되지 않은 질문 조회")
    void findByDeletedFalse() {
        final Question question = questionRepository.save(QuestionFixture.질문().writeBy(javajigi));
        question.setDeleted(true);
        questionRepository.flush();
        assertThat(questionRepository.findByDeletedFalse()).hasSize(0);
    }

    @Test
    @DisplayName("ID로 삭제되지 않은 질문 조회")
    void findByIdAndDeletedFalse() {
        final Question question = questionRepository.save(QuestionFixture.질문().writeBy(javajigi));
        question.setDeleted(true);
        questionRepository.flush();
        assertThatThrownBy(() -> questionRepository.findByIdAndDeletedFalse(question.getId())
                .orElseThrow(NotFoundException::new))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("isOwner 메소드 테스트")
    void isOwnerTest() {
        final Question question = questionRepository.save(QuestionFixture.질문().writeBy(javajigi));
        questionRepository.flush();
        assertThat(question.isOwner(javajigi)).isTrue();
    }

    @AfterEach
    public void tearDown() {
        userRepository.deleteAll();
        questionRepository.deleteAll();
    }
}
