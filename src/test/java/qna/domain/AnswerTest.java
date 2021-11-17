package qna.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AnswerTest {

    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        User javajigi = userRepository.save(UserTest.JAVAJIGI);
        User sanjigi = userRepository.save(UserTest.SANJIGI);

        Question question = QuestionTest.Q1.writeBy(javajigi);
        Question savedQuestion = questionRepository.save(question);

        A1.setWriter(javajigi);
        A2.setWriter(sanjigi);
        A1.setQuestion(savedQuestion);
        A2.setQuestion(savedQuestion);

        answerRepository.save(A1);
        answerRepository.save(A2);
    }

    @AfterEach
    void clean() {
        answerRepository.deleteAll();
        questionRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("주어진 ID에 해당하는 답변을 리턴한다.")
    void 주어진_ID에_해당하는_답변을_리턴한다() {
        // given
        Answer answer = answerRepository.findAll().get(0);

        // when
        Answer result = answerRepository.findById(answer.getId()).get();

        // then
        assertThat(result).isEqualTo(answer);
    }

    @Test
    @DisplayName("답변 내용을 수정한다.")
    void 답변_내용을_수정한다() {
        // given
        Answer answer = answerRepository.findAll().get(0);
        String newContents = "Update Contents";

        // when
        answer.setContents(newContents);

        // then
        List<Answer> result = answerRepository.findByContents(newContents);
        assertThat(result).containsExactly(answer);
    }

    @Test
    @DisplayName("모든 답변을 삭제한다.")
    void 모든_답변을_삭제한다() {
        // given
        List<Answer> prevResult = answerRepository.findAll();
        assertThat(prevResult.size()).isGreaterThan(0);

        // when
        answerRepository.deleteAll();

        // then
        List<Answer> result = answerRepository.findAll();
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("주어진 질문에 달린 삭제되지 않은 답변 목록을 리턴한다.")
    void 주어진_질문에_달린_삭제되지_않은_답변_목록을_리턴한다() {
        // given
        Question question = questionRepository.findAll().get(0);

        // when
        List<Answer> result = answerRepository.findByQuestionIdAndDeletedFalse(question.getId());

        // then
        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("주어진 ID에 해당하는 삭제되지 않은 답변을 리턴한다.")
    void 주어진_ID에_해당하는_삭제되지_않은_답변을_리턴한다() {
        // given
        Answer answer = answerRepository.findAll().get(0);

        // when
        Answer result = answerRepository.findByIdAndDeletedFalse(answer.getId()).get();

        // then
        assertThat(result).isEqualTo(answer);
    }
}
