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
class AnswerTest {
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
        user1 = userRepository.save(User.builder().userId("javajigi").password("password").name("name").email("javajigi@slipp.net").build());
        user2 = userRepository.save(User.builder().userId("sanjigi").password("password").name("name").email("sanjigi@slipp.net").build());

        question1 = questionRepository.save(Question.builder().title("title1").contents("contents1").build().writeBy(user1));
        question2 = questionRepository.save(Question.builder().title("title2").contents("contents2").build().writeBy(user2));

        answer1 = answerRepository.save(Answer.builder().writer(user1).question(question1).contents("Answers Contents1").build());
        answer2 = answerRepository.save(Answer.builder().writer(user2).question(question2).contents("Answers Contents2").build());
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
