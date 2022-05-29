package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import qna.CannotDeleteException;
import qna.NotFoundException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class QuestionTest {
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private TestEntityManager testEntityManager;

    private User JAVAJIGI;
    private User SANJIGI;
    private Question Q1;
    private Question Q2;
    private Answer A1;
    private Answer A2;

    @BeforeEach
    void setUp() {
        JAVAJIGI = new User(null, "javajigi", "password", "name", "javajigi@slipp.net");
        SANJIGI = new User(null, "sanjigi", "password", "name", "sanjigi@slipp.net");

        Q1 = new Question("title1", "contents1").writeBy(JAVAJIGI);
        Q2 = new Question("title2", "contents2").writeBy(SANJIGI);

        A1 = new Answer(JAVAJIGI, Q1, "Answers Contents1");
        A1.setDeleted(true);

        A2 = new Answer(SANJIGI, Q1, "Answers Contents2");

        testEntityManager.persistAndFlush(JAVAJIGI);
        testEntityManager.persistAndFlush(SANJIGI);
        testEntityManager.persistAndFlush(Q1);
        testEntityManager.persistAndFlush(Q2);
        testEntityManager.persistAndFlush(A1);
        testEntityManager.persistAndFlush(A2);

        testEntityManager.clear();
    }

    @Test
    void findByDeletedFalse() {
        //when
        List<Question> actual = questionRepository.findByDeletedFalse();
        //then
        assertAll(
                () -> assertThat(actual.size()).isEqualTo(2),
                () -> assertThat(actual).containsExactlyInAnyOrder(Q1, Q2)
        );
    }

    @Test
    @DisplayName("삭제되지 않은 질문을 하나 가져올 때, 이 질문에 대한 삭제되지 않은 답변을 같이 가져올 수 있다")
    void findWithAnswersByIdAndDeletedFalse() {
        //when
        Question actual = questionRepository.findWithAnswersByIdAndDeletedFalse(Q1.getId()).orElseThrow(NotFoundException::new);

        //then
        assertAll(
                () -> assertThat(actual).isNotNull(),
                () -> assertThat(actual.getAnswers()).containsExactly(A2),
                () -> assertThat(A2.isDeleted()).isFalse()
        );
    }

    @Test
    @DisplayName("로그인 유저와 질문 작성자가 같으면 질문을 삭제할 수 있다")
    void delete_if_same_writer_and_login_user() throws CannotDeleteException {
        //when
        Q1.delete(JAVAJIGI);

        //then
        assertThat(Q1.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("로그인 유저와 질문 작성자가 다르면 질문을 삭제할 수 없다")
    void delete_if_different_writer_and_login_user() {
        assertThrows(CannotDeleteException.class, () -> Q1.delete(SANJIGI));
    }
}
