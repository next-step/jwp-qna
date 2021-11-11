package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
class AnswerRepositoryTest {
    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    private Answer answer1;
    private Answer answer2;

    private User user1;
    private User user2;

    private Question question1;
    private Question question2;

    @BeforeEach
    void setUp() {
        user1 = userRepository.save(User.create("javajigi", "password", "name", "javajigi@slipp.net"));
        user2 = userRepository.save(User.create("sanjigi", "password", "name", "sanjigi@slipp.net"));

        question1 = questionRepository.save(Question.create("title1", "contents1")).writeBy(user1);
        question2 = questionRepository.save(Question.create("title2", "contents2")).writeBy(user2);

        answer1 = answerRepository.save(Answer.create(question1, user1, "Answers Contents1"));
        answer2 = answerRepository.save(Answer.create(question2, user2, "Answers Contents2"));
    }

    @Test
    @DisplayName("답변을 저장한다.")
    void save() {
        //given //when //then
        assertAll(
                () -> assertThat(answer1.getId()).isNotNull(),
                () -> assertThat(answer1.getWriter()).isEqualTo(user1),
                () -> assertThat(answer1.getQuestion()).isEqualTo(question1),
                () -> assertThat(answer1.getContents()).isEqualTo("Answers Contents1")
        );
    }

    @Test
    @DisplayName("질문에 달린 답변 중 삭제되지 않은 답변 목록을 조회한다.")
    void findByQuestionIdAndDeletedFalse() {
        //given
        answer2.setDeleted(true);
        Long questionId = question2.getId();

        //when
        List<Answer> answers = answerRepository.findByQuestionIdAndDeletedFalse(questionId);

        //then
        assertThat(answers).isEmpty();
    }

    @Test
    @DisplayName("삭제되지 않은 답변 한 건을 조회한다.")
    void findByIdAndDeletedFalse() {
        //given
        Long answerId = answer2.getId();

        //when
        Optional<Answer> findAnswer = answerRepository.findByIdAndDeletedFalse(answerId);

        //then
        assertThat(findAnswer).hasValueSatisfying(answer -> assertAll(
                () -> assertThat(answer.getWriter()).isEqualTo(user2),
                () -> assertThat(answer.getQuestion()).isEqualTo(question2),
                () -> assertThat(answer.getContents()).isEqualTo("Answers Contents2")
        ));
    }
}
