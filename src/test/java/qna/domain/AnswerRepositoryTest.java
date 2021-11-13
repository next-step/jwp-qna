package qna.domain;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import qna.CannotDeleteException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;


/**
 * packageName : qna.domain
 * fileName : AnswerRepositoryTest
 * author : haedoang
 * date : 2021-11-09
 * description :
 */
@DataJpaTest
@EnableJpaAuditing
@TestMethodOrder(MethodOrderer.MethodName.class)
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AnswerRepositoryTest {
    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private UserRepository userRepository;

    private User user1;
    private User user2;
    private Question question1;

    @BeforeEach
    void setUp() {
        this.user1 = userRepository.save(UserTest.JAVAJIGI);
        this.user2 = userRepository.save(UserTest.SANJIGI);
        this.question1 = questionRepository.save(new Question("title1", "contents1").writeBy(user1));
    }

    @Test
    @DisplayName("Answer 검증 테스트")
    public void T1_answerSaveTest() {
        //WHEN
        Answer answer = answerRepository.save(new Answer(user1, question1, "Answers Contents1"));
        Answer answer2 = answerRepository.save(new Answer(user2, question1, "Answers Contents2"));
        //THEN
        assertAll(
                () -> assertThat(answer.isOwner(user1)).isTrue(),
                () -> assertThat(answer2.isOwner(user2)).isTrue(),
                () -> assertThat(answer.getWriter()).isEqualTo(user1),
                () -> assertThat(answer2.getWriter()).isEqualTo(user2),
                () -> assertThat(answer.getQuestion()).isEqualTo(question1),
                () -> assertThat(answer2.getQuestion()).isEqualTo(question1)
        );
    }

    @Test
    @DisplayName("Answer 삭제 시 DeleteHistory 를 반환한다.")
    public void T2_answerDelete() throws Exception {
        //WHEN
        Answer answer = answerRepository.save(new Answer(user1, question1, "Answers Contents1"));
        Answer answer2 = answerRepository.save(new Answer(user2, question1, "Answers Contents2"));
        //THEN
        assertThat(answer.delete(user1)).isInstanceOf(DeleteHistory.class);
        assertThatThrownBy(()-> answer2.delete(user1)).isInstanceOf(CannotDeleteException.class)
                .hasMessageContaining("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
        //WHEN
        answerRepository.flush();
        Answer findAnswer = answerRepository.findById(answer.getId()).get();
        //THEN
        //assertThat(findAnswer.isDeleted()).isTrue();
    }
}
