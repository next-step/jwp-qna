package qna.domain;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.CannotDeleteException;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
public class AnswersTest {

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    private User user1;

    private Question question1;

    private Answer answer1;
    private Answer answer2;

    @BeforeEach
    void setup() {
        user1 = new User("javajigi", "password", "name", "javajigi@slipp.net");
        userRepository.save(user1);

        question1 = questionRepository.save(new Question("title1", "contents1").writeBy(user1));

        answer1 = new Answer(user1, question1, "Answers contents1");
        answer2 = new Answer(user1, question1, "Answers contents2");

        answerRepository.save(answer1);
        answerRepository.save(answer2);
    }

    @DisplayName("질문자와 답변자가 다른 경우 답변을 삭제할 수 없다.")
    @Test
    void deleteAnswersFailTest() {
        User user2 = new User("sanjigi", "password", "name", "sanjigi@slipp.net");
        userRepository.save(user2);

        Answers answers = new Answers(Lists.newArrayList(answer1, answer2));

        assertThatThrownBy(() -> answers.deleteAnswers(user2))
                .isInstanceOf(CannotDeleteException.class)
                .hasMessageContaining("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
    }

    @DisplayName("질문자와 답변 글의 모든 답변자 같은 경우 삭제 하면 삭제 내역 목록을 반환한다.")
    @Test
    void deleteAnswersTest() {
        Answers answers = new Answers(Arrays.asList(answer1, answer2));

        answers.deleteAnswers(user1);

        assertThat(answers.getAnswers()).extracting("deleted").contains(true);
    }
}