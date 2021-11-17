package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.CannotDeleteException;

import javax.persistence.EntityManager;

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
    Answer savedAnswer;
    @Autowired
    EntityManager em;
    Answer answer1;
    User user1;
    User user2;
    Question question1;

    @BeforeEach
    void init() {
        // given
        user1 = new User("javajigi", "password", "name", "javajigi@slipp.net");
        user2 = new User("sanjigi", "password", "name", "sanjigi@slipp.net");
        question1 = new Question("title1", "contents1").writeBy(user1);
        answer1 = new Answer(user1, question1, "Answers Contents1");
        userRepository.save(user1);
        savedAnswer = answerRepository.save(answer1);
    }

    @Test
    void 저장() {
        assertAll(
                () -> assertThat(savedAnswer.getId()).isNotNull(),
                () -> assertThat(savedAnswer.getContents()).isEqualTo(answer1.getContents())
        );
    }

    @Test
    void 검색() {
        assertThat(answerRepository.findById(savedAnswer.getId()).get().getId())
                .isEqualTo(savedAnswer.getId());
    }

    @Test
    void 연관관계_유저_조회() {
        user1.addAnswer(answer1);
        assertAll(
                () -> assertThat(savedAnswer.getWriter()).isEqualTo(user1),
                () -> assertThat(user1.getAnswers().get(0)).isEqualTo(savedAnswer)
        );
    }

    @Test
    void 연관관계_질문_조회() {
        Question savedQuestion = questionRepository.save(question1);
        savedQuestion.addAnswer(answer1);
        assertAll(
                () -> assertThat(savedAnswer.getQuestion()).isEqualTo(savedQuestion),
                () -> assertThat(savedQuestion.getAnswers().get(0)).isEqualTo(savedAnswer)
        );
    }

    @Test
    void 검색_없을경우() {
        answer1.setDeleted(true);
        questionRepository.save(question1);
        assertThat(answerRepository.findByIdAndDeletedFalse(answer1.getId()).isPresent()).isFalse();
    }

    @Test
    void 수정() {
        User savedUser2 = userRepository.save(user2);
        questionRepository.save(question1);
        savedAnswer.setWriter(user2);
        answerRepository.flush();
        em.clear();
        savedUser2 = userRepository.findById(savedUser2.getId()).get();
        assertThat(savedUser2.getAnswers().get(0).getId()).isEqualTo(savedAnswer.getId());
    }

    @Test
    void 삭제() {
        answerRepository.delete(savedAnswer);
        assertThat(answerRepository.findById(savedAnswer.getId())).isEmpty();
    }

    @Test
    void 답변삭제() {
        // when
        DeleteHistory deleteHistory = answer1.deleteRelatedAnswerAndCreateDeleteHistory(user1);

        // then
        assertAll(
                () -> assertThat(answer1.isDeleted()).isTrue(),
                () -> assertThat(deleteHistory.getContentId()).isEqualTo(answer1.getId())
        );
    }

    @Test
    void 답변삭제_작성자_다를경우() {
        //given
        userRepository.save(user2);

        // when, then
        assertThatThrownBy(() ->
                answer1.deleteRelatedAnswerAndCreateDeleteHistory(user2))
                .isInstanceOf(CannotDeleteException.class);
    }
}
