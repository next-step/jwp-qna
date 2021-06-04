package qna.domain;

import org.junit.jupiter.api.*;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AnswerRepositoryTest extends JpaTest {
    private User user1;
    private User user2;
    private Question question1;
    private Answer answer1;
    private Answer answer2;

    @BeforeEach
    void setUp() {
        user1 = new User("userId1", "1234", "userName1", "userEmail1");
        user2 = new User("userId2", "1234", "userName2", "userEmail2");
        getUsers().saveAll(Arrays.asList(user1, user2));

        question1 = new Question("title1", "contents1").writeBy(user1);
        getQuestions().save(question1);

        answer1 = new Answer(user1, question1, "contents1");
        answer2 = new Answer(user2, question1, "contents2");
        getAnswers().saveAll(Arrays.asList(answer1, answer2));
    }

    @DisplayName("질문 아이디에 해당하는 답변 목록 중 삭제 상태가 아닌 것들만 리턴한다")
    @Test
    void findByQuestionIdAndDeletedFalse() {
        //given
        answer1.setDeleted(false);
        answer2.setDeleted(true);

        //when
        List<Answer> actual = getAnswers().findByQuestionIdAndDeletedFalse(question1.getId());

        //then
        assertThat(actual.size()).isEqualTo(1);
        assertThat(actual.get(0)).isSameAs(answer1);
    }

    @DisplayName("답변 아이디에 해당하는 답변이 삭제 상태가 아니라면 리턴한다.")
    @Test
    void findByIdAndDeletedFalse() {
        //given
        answer1.setDeleted(false);

        //when
        Answer actual = getAnswers().findByIdAndDeletedFalse(answer1.getId())
                .orElseThrow(EntityNotFoundException::new);

        //then
        assertThat(actual).isSameAs(answer1);
    }

    @DisplayName("답변 아이디에 해당하는 답변이 삭제 상태라면 EntityNotFoundException 을 발생시킨다.")
    @Test
    void findByIdAndDeletedFalseIfPresent() {
        //given
        answer1.setDeleted(true);

        //when
        assertThatThrownBy(() -> getAnswers().findByIdAndDeletedFalse(answer1.getId())
                .orElseThrow(EntityNotFoundException::new))
                .isInstanceOf(EntityNotFoundException.class); //then
    }
}