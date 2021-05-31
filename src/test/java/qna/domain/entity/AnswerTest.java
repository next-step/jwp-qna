package qna.domain.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.repository.AnswerRepository;
import qna.domain.repository.QuestionRepository;
import qna.domain.repository.UserRepository;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

//TODO : 피드백 후 삭제하기
/* 개별 TEST는 성공하지만, 전체 TEST는 실패하는 이유를 모르겠습니다.. */

@DataJpaTest
public class AnswerTest {
    public static final Answer ANSWER_OF_JAVAJIGI = new Answer(UserTest.USER_JAVAJIGI, QuestionTest.QUESTION_OF_JAVAJIGI, "Answers Contents1");
    public static final Answer ANSWER_OF_SANJIGI = new Answer(UserTest.USER_SANJIGI, QuestionTest.QUESTION_OF_JAVAJIGI, "Answers Contents2");

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    private User user1;
    private User user2;

    private Question question1;
    private Question question2;

    private Answer answer1;
    private Answer answer2;

    @BeforeEach
    public void setUp() {
        user1 = userRepository.save(UserTest.USER_JAVAJIGI);
        user2 = userRepository.save(UserTest.USER_SANJIGI);

        question1 = questionRepository.save(QuestionTest.QUESTION_OF_JAVAJIGI);
        question2 = questionRepository.save(QuestionTest.QUESTION_OF_SANJIGI);

        answer1 = answerRepository.save(ANSWER_OF_JAVAJIGI);
        answer2 = answerRepository.save(ANSWER_OF_SANJIGI);
    }

    @Test
    public void exists() {
        assertAll(
            () -> assertThat(answer1.getId()).isNotNull(),
            () -> assertThat(answer2.getId()).isNotNull(),
            () -> assertThat(answer1.getQuestion()).isEqualTo(question1),
            () -> assertThat(answer2.getQuestion()).isEqualTo(question1),
            () -> assertThat(answer1.getWriter()).isEqualTo(user1),
            () -> assertThat(answer2.getWriter()).isEqualTo(user2)
        );
    }

    @Test
    @DisplayName("동등성 비교")
    public void isEqualTo() {
        assertAll(
            () -> assertThat(answer1).isEqualTo(ANSWER_OF_JAVAJIGI),
            () -> assertThat(answer2).isEqualTo(ANSWER_OF_SANJIGI)
        );
    }

    @Test
    public void findByQuestionIdAndDeletedFalse() {
        List<Answer> answers = answerRepository.findByQuestionIdAndDeletedFalse(QuestionTest.QUESTION_OF_JAVAJIGI.getId());

        assertAll(
            () -> assertThat(answers.size()).isEqualTo(2),
            () -> assertThat(answers).contains(answer1, answer2)
        );
    }

    @Test
    public void findByQuestionIdAndDeletedFalse2() {
        answer2.deleted();

        List<Answer> answers = answerRepository.findByQuestionIdAndDeletedFalse(QuestionTest.QUESTION_OF_JAVAJIGI.getId());

        assertAll(
            () -> assertThat(answers.size()).isEqualTo(1),
            () -> assertThat(answers).contains(answer1),
            () -> assertThat(answers).doesNotContain(answer2)
        );
    }

    @Test
    public void findByIdAndDeletedFalse() {
        Optional<Answer> answerOptional = answerRepository.findByIdAndDeletedFalse(answer1.getId());

        assertAll(
            () -> assertThat(answerOptional).isNotEmpty(),
            () -> assertThat(answerOptional.get()).isEqualTo(answer1)
        );
    }


}