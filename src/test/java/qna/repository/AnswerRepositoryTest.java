package qna.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static qna.domain.AnswerTest.ANSWERS_CONTENTS_1;
import static qna.domain.AnswerTest.ANSWERS_CONTENTS_2;
import static qna.domain.QuestionTest.QUESTION_1;
import static qna.domain.UserTest.JAVAJIGI;

@DataJpaTest
@DisplayName("답변 Repository")
class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @DisplayName("저장_성공")
    @Test
    void save() {

        User javajigi = createUser(JAVAJIGI);
        Answer answer = answerRepository.save(new Answer(javajigi, createQuestion(createUser(javajigi), QUESTION_1), ANSWERS_CONTENTS_1));

        assertAll(
                () -> assertThat(answer.getId()).isNotNull(),
                () -> assertThat(answer.getWriter()).isNotNull(),
                () -> assertThat(answer.getQuestion().getId()).isNotNull(),
                () -> assertThat(answer.getCreatedAt()).isNotNull(),
                () -> assertThat(answer.getUpdatedAt()).isNotNull(),
                () -> assertThat(answer.getContents()).isEqualTo(ANSWERS_CONTENTS_1)
        );
    }

    @DisplayName("findByQuestionIdAndDeletedFalse_조회_성공")
    @Test
    void findByQuestionIdAndDeletedFalse() {

        User javajigi = createUser(JAVAJIGI);
        Answer answer = createAnswer(javajigi, createQuestion(createUser(javajigi), QUESTION_1));

        List<Answer> answers = answerRepository.findByQuestionIdAndDeletedFalse(answer.getQuestion().getId());

        assertAll(
                () -> assertThat(answers).hasSize(1),
                () -> assertThat(answers).containsExactly(answer)
        );
    }

    @DisplayName("findByIdAndDeletedFalse_조회_성공")
    @Test
    void findByIdAndDeletedFalse() {

        User javajigi = createUser(JAVAJIGI);
        Answer answer = createAnswer(javajigi, createQuestion(createUser(javajigi), QUESTION_1));
        Answer findAnswer = answerRepository.findByIdAndDeletedFalse(answer.getId()).orElse(null);

        assertAll(
                () -> assertThat(findAnswer).isNotNull(),
                () -> assertThat(findAnswer).isEqualTo(answer)
        );
    }

    @DisplayName("삭제_성공")
    @Test
    void delete() {

        User javajigi = createUser(JAVAJIGI);
        Answer answer = createAnswer(javajigi, createQuestion(createUser(javajigi), QUESTION_1));
        assertThat(answer).isNotNull();

        answerRepository.delete(answer);

        assertThat(answerRepository.findByIdAndDeletedFalse(answer.getId())).isNotPresent();
    }

    private User createUser(User user) {
        return userRepository.save(user);
    }

    private Question createQuestion(User user, Question question) {
        return questionRepository.save(question.writeBy(user));
    }

    private Answer createAnswer(User user, Question question) {
        return answerRepository.save(new Answer(user, question, ANSWERS_CONTENTS_2));
    }
}
