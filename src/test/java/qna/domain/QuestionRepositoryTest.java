package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository questions;

    private Question question1;
    private Question question2;

    @BeforeEach
    void setUp() {
        User user1 = new User("userId1", "1234", "userName1", "userEmail1");
        User user2 = new User("userId2", "1234", "userName2", "userEmail2");
        question1 = new Question("title1", "contents1").writeBy(user1);
        question2 = new Question("title2", "contents2").writeBy(user2);
        questions.saveAll(Arrays.asList(question1, question2));
    }

    @DisplayName("삭제 상태가 아닌 질문들을 조회한다.")
    @Test
    void findByDeletedFalse() {
        //given
        question1.delete();

        //when
        List<Question> actual = questions.findByDeletedFalse();

        //then
        assertThat(actual.size()).isEqualTo(1);
        assertThat(actual.get(0)).isSameAs(question2);
    }

    @DisplayName("질문 아이디에 해당하는 질문이 삭제 상태가 아닌 것을 리턴한다.")
    @Test
    void findByIdAndDeletedFalse() {
        //given
        //Question1 삭제하지 않음

        //when
        Question actual = questions.findByIdAndDeletedFalse(question1.getId())
                .orElseThrow(NotFoundException::new);

        //then
        assertThat(actual).isSameAs(question1);
    }

    @DisplayName("질문 아이디에 해당하는 질문이 삭제 상태가 아닌 것이 없다면 예외를 발생시킨다.")
    @Test
    void findByIdAndDeletedFalseException() {
        //given
        question1.delete();
        question2.delete();

        //when
        assertThatThrownBy(() -> questions.findByIdAndDeletedFalse(question1.getId())
                .orElseThrow(NotFoundException::new))
                .isInstanceOf(NotFoundException.class); //then
    }
}