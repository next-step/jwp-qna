package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.CannotDeleteException;
import qna.NotFoundException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
public class QuestionTest {
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private UserRepository userRepository;

    private User JAVAJIGI;
    private User SANJIGI;
    private Question Q1;
    private Question Q2;

    @BeforeEach
    void setUp() {
        JAVAJIGI = new User(null, "javajigi", "password", "name", "javajigi@slipp.net");
        SANJIGI = new User(null, "sanjigi", "password", "name", "sanjigi@slipp.net");

        Q1 = new Question("title1", "contents1").writeBy(JAVAJIGI);
        Q2 = new Question("title2", "contents2").writeBy(SANJIGI);

        userRepository.save(JAVAJIGI);
        userRepository.save(SANJIGI);
        questionRepository.save(Q1);
        questionRepository.save(Q2);
    }

    @Test
    void findByDeletedFalse() {
        //when
        List<Question> actual = questionRepository.findByDeletedFalse();

        //then
        assertAll(
                () -> assertThat(actual.size()).isEqualTo(2),
                () -> assertThat(actual).contains(Q1, Q2)
        );
    }

    @Test
    @DisplayName("질문을 하나 가져올 때, 이 질문에 대한 답변을 같이 가져올 수 있는지 확인한다")
    void findByIdDeletedFalse() {
        //given
        final Answer A1 = new Answer(JAVAJIGI, Q1, "Answers Contents1");
        final Answer A2 = new Answer(SANJIGI, Q1, "Answers Contents2");
        Q1.addAnswer(A1);
        Q1.addAnswer(A2);
        answerRepository.save(A1);
        answerRepository.save(A2);

        //when
        Question actual = questionRepository.findByIdAndDeletedFalse(Q1.getId()).orElseThrow(NotFoundException::new);

        //then
        assertAll(
                () -> assertThat(actual).isNotNull(),
                () -> assertThat(actual.getAnswers()).contains(A1, A2)
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
