package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.CannotDeleteException;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class AnswerTest {

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    private User user1;
    private User user2;

    private Question question1;
    private Question question2;

    private Answer answer1;
    private Answer answer2;

    @BeforeEach
    void setup() {
        user1 = new User("javajigi", "password", "name", "javajigi@slipp.net");
        user2 = new User("sanjigi", "password", "name", "sanjigi@slipp.net");

        userRepository.save(user1);
        userRepository.save(user2);

        question1 = questionRepository.save(new Question("title1", "contents1").writeBy(user1));
        question2 = questionRepository.save(new Question("title2", "contents2").writeBy(user2));

        Answer A1 = new Answer(user1, question1, "contents1");
        Answer A2 = new Answer(user2, question2, "contents2");

        answer1 = answerRepository.save(new Answer(user1, question1, A1.getContents()));
        answer2 = answerRepository.save(new Answer(user2, question2, A2.getContents()));
    }

    @Test
    @DisplayName("Answer 저장 확인")
    void saveTest() {
        assertAll(
                () -> assertThat(answer1.getId()).isNotNull(),
                () -> assertThat(answer1.getWriter()).isEqualTo(question1.getWriter()),
                () -> assertThat(answer1.getQuestion()).isEqualTo(question1),
                () -> assertThat(answer1.getContents()).isEqualTo(question1.getContents())
        );
    }

    @Test
    @DisplayName("question id 값으로 deleted false 리스트 확인")
    void findByQuestionIdAndDeletedFalseTest() {
        List<Answer> actualList = answerRepository.findByQuestionAndDeletedFalse(question1);

        assertThat(actualList).contains(answer1);
    }

    @Test
    @DisplayName("Deleted값이 false인 answer 찾기")
    void findByIdAndDeletedFalseTest() {
        Answer result = answerRepository.findByIdAndDeletedFalse(answer1.getId()).get();
        assertThat(result.getId()).isEqualTo(answer1.getId());

        assertAll(
                () -> assertThat(result.getWriter()).isEqualTo(answer1.getWriter()),
                () -> assertThat(result.getQuestion()).isEqualTo(answer1.getQuestion()),
                () -> assertThat(result.getContents()).isEqualTo(answer1.getContents())
        ) ;
    }

    @DisplayName("작성자가 아닌 사용자는 답변을 삭제할 수 없다.")
    @Test
    void deleteFailTest() {
        //when & then
        assertThatThrownBy(() -> answer1.delete(user2)).isInstanceOf(CannotDeleteException.class)
                .hasMessageContaining("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
    }

    @DisplayName("삭제하면 답변이 삭제 상태가 되고, 삭제 내역을 반환한다.")
    @Test
    void deleteSuccessTest() {
        //when
        DeleteHistory delete = answer2.delete(user2);
        //then
        assertAll(
                () -> assertThat(answer2.isDeleted()).isTrue(),
                () -> assertThat(delete).isEqualTo(new DeleteHistory(ContentType.ANSWER, answer2.getId(), answer2.getWriter(), LocalDateTime.now()))
        );
    }
}
