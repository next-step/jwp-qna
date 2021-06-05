package qna.domain;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.NotFoundException;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository answers;

    private Question question1;
    private Answer answer1;
    private Answer answer2;

    @BeforeEach
    void setUp() {
        User user1 = new User("userId1", "1234", "userName1", "userEmail1");
        User user2 = new User("userId2", "1234", "userName2", "userEmail2");
        question1 = new Question("title1", "contents1").writeBy(user1);
        answer1 = new Answer(user1, question1, "contents1");
        answer2 = new Answer(user2, question1, "contents2");
        answers.saveAll(Arrays.asList(answer1, answer2));
    }

    @DisplayName("질문 아이디에 해당하는 답변 목록 중 삭제 상태가 아닌 것들만 리턴한다")
    @Test
    void findByQuestionIdAndDeletedFalse() {
        //given
        answer2.delete();

        //when
        List<Answer> actual = answers.findByQuestionIdAndDeletedFalse(question1.getId());

        //then
        assertThat(actual.size()).isEqualTo(1);
        assertThat(actual.get(0)).isSameAs(answer1);
    }

    @DisplayName("답변 아이디에 해당하는 답변이 삭제 상태가 아니라면 리턴한다.")
    @Test
    void findByIdAndDeletedFalse() {
        //given
        //Answer 삭제하지 않음

        //when
        Answer actual = answers.findByIdAndDeletedFalse(answer1.getId())
                .orElseThrow(NotFoundException::new);

        //then
        assertThat(actual).isSameAs(answer1);
    }

    @DisplayName("답변 아이디에 해당하는 답변이 삭제 상태라면 NotFoundException 을 발생시킨다.")
    @Test
    void findByIdAndDeletedFalseIfPresent() {
        //given
        answer1.delete();

        //when
        assertThatThrownBy(() -> answers.findByIdAndDeletedFalse(answer1.getId())
                .orElseThrow(NotFoundException::new))
                .isInstanceOf(NotFoundException.class); //then
    }
}