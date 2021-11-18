package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.CannotDeleteException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class AnswerTest {
    @Autowired
    AnswerRepository answerRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    QuestionRepository questionRepository;

    Answer answer;

    @BeforeEach
    void init() {
        answer = TestAnswerFactory.create();
    }

    @Test
    void 저장() {
        // when
        save(answer);

        // then
        assertThat(answer.getId()).isNotNull();
    }

    @Test
    void 검색() {
        // given
        save(answer);

        // then
        Answer foundAnswer = answerRepository.findById(this.answer.getId()).get();

        // then
        assertThat(foundAnswer).isEqualTo(answer);
    }

    @Test
    void 연관관계_유저_조회() {
        //given
        Answer savedAnswer = save(answer);

        // when
        User user = savedAnswer.getWriter();

        // then
        assertThat(answer.getWriter()).isEqualTo(user);
    }

    @Test
    void 연관관계_질문_조회() {
        //given
        Answer savedAnswer = save(answer);

        // when
        Question question = savedAnswer.getQuestion();

        // then
        assertThat(answer.getQuestion()).isEqualTo(question);
    }

    @Test
    void 수정() {
        // given
        User user2 = new User("sanjigi", "password", "name", "sanjigi@slipp.net");
        userRepository.save(user2);
        save(answer);

        // when
        answer.setWriter(user2);

        // then
        assertThat(answer.getWriter()).isEqualTo(user2);
    }

    @Test
    void 삭제() {
        // given
        save(answer);

        //when
        answerRepository.delete(answer);

        //then
        assertThat(answerRepository.findById(answer.getId())).isEmpty();
    }

    @Test
    void 답변삭제() {
        // given
        User user = answer.getWriter();
        userRepository.save(user);
        save(answer);

        // when
        DeleteHistory deleteHistory = answer.delete(user);

        // then
        assertAll(
                () -> assertThat(answer.isDeleted()).isTrue(),
                () -> assertThat(deleteHistory.getContentId()).isEqualTo(answer.getId())
        );
    }

    @Test
    void 답변삭제_작성자_다를경우() {
        //given
        User user = answer.getWriter();
        User user2 = new User("sanjigi", "password", "name", "sanjigi@slipp.net");
        userRepository.save(user);
        userRepository.save(user2);

        // when, then
        assertThatThrownBy(() ->
                answer.delete(user2)).isInstanceOf(CannotDeleteException.class);
    }

    private Answer save(Answer answer) {
        return answerRepository.save(answer);
    }

}
