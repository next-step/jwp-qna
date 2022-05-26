package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import qna.CannotDeleteException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest(includeFilters = { @ComponentScan.Filter(value = { EnableJpaAuditing.class }) })
public class AnswerTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private AnswerRepository answerRepository;

    private User JAVAJIGI;
    private Question Q1;
    private Answer A1;

    @BeforeEach
    void setUp() {
        JAVAJIGI = new User(null, "javajigi", "password", "name", "javajigi@slipp.net");
        Q1 = new Question("title1", "contents1").writeBy(JAVAJIGI);
        A1 = new Answer(JAVAJIGI, Q1, "Answers Contents1");

        userRepository.save(JAVAJIGI);
        questionRepository.save(Q1);
        answerRepository.save(A1);

        Q1.addAnswer(A1);
    }

    @Test
    @DisplayName("Answer를_새로_저장하게_되면_발급된_Id_를_확인할_수_있다")
    void save() {
        //given
        final Answer A2 = new Answer(JAVAJIGI, Q1, "Answers Contents2");
        assertThat(A2.getId()).isNull();

        //when
        answerRepository.save(A2);

        //then
        assertThat(A2.getId()).isNotNull();
    }

    @Test
    @DisplayName("로그인 유저와 답변 작성자가 같으면 질문을 삭제할 수 있다")
    void delete_if_same_writer_and_login_user() throws CannotDeleteException {
        //when
        A1.delete(JAVAJIGI);

        //then
        assertThat(A1.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("로그인 유저와 답변 작성자가 다르면 질문을 삭제할 수 없다")
    void delete_if_different_writer_and_login_user() {
        assertThrows(CannotDeleteException.class, () -> A1.delete(User.GUEST_USER));
    }
}
