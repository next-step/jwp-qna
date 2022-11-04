package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
@DisplayName("답변 저장소 테스트")
public class AnswerRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private AnswerRepository answerRepository;

    private Question question;
    private User answerWriter;
    private Answer answer;

    @BeforeEach
    void setUp() {
        User questionWriter = userRepository.save(new User(1L, "chaeeun", "password", "name", "chaeeun@gmail.com"));
        question = questionRepository.save(new Question(1L, questionWriter, "title", "contents"));
        answerWriter = userRepository.save(UserTestFixture.SANJIGI);
        answer = new Answer(answerWriter, question, "contents");
    }

    @Test
    @DisplayName("답변 저장")
    void 저장() {
        Answer savedAnswer = answerRepository.save(answer);

        assertAll(
                () -> assertThat(savedAnswer.getId()).isNotNull(),
                () -> assertThat(answer.getQuestion()).isEqualTo(savedAnswer.getQuestion()),
                () -> assertThat(answer.getContents()).isEqualTo(savedAnswer.getContents()),
                () -> assertThat(answer.getWriteBy()).isEqualTo(savedAnswer.getWriteBy()),
                () -> assertThat(answer.isDeleted()).isEqualTo(savedAnswer.isDeleted())
        );

    }

    @Test
    @DisplayName("답변 수정")
    void 수정() {
        answerRepository.save(answer);
        answer.updateContents("답변 수정 테스트");
        Answer updatedAnswer = answerRepository.findById(answer.getId()).get();

        assertThat(updatedAnswer.getContents()).isEqualTo(answer.getContents());
    }

    @Test
    @DisplayName("답변 삭제")
    void 삭제() {
        answerRepository.save(answer);
        answerRepository.delete(answer);

        assertThat(answerRepository.findById(answer.getId())).isEmpty();
    }

    @Test
    @DisplayName("답변 ID로 삭제되지 않은 답변 조회")
    void 답변_ID로_삭제되지_않은_답변_조회() {
        Answer savedAnswer = answerRepository.save(answer);
        Answer expected = answerRepository.findByIdAndDeletedFalse(savedAnswer.getId()).get();

        assertAll(
                () -> assertThat(savedAnswer.getId()).isEqualTo(expected.getId()),
                () -> assertThat(savedAnswer.getQuestion()).isEqualTo(expected.getQuestion()),
                () -> assertThat(savedAnswer.getContents()).isEqualTo(expected.getContents()),
                () -> assertThat(savedAnswer.getWriteBy()).isEqualTo(expected.getWriteBy()),
                () -> assertThat(savedAnswer.isDeleted()).isEqualTo(expected.isDeleted())
        );
    }

    @Test
    @DisplayName("질문 ID로 삭제되지 않은 답변 목록 조회")
    void 질문_ID로_삭제되지_않은_답변목록_조회() {
        Answer answer1 = answerRepository.save(new Answer(answerWriter, question, "contents"));
        Answer answer2 = answerRepository.save(new Answer(answerWriter, question, "contents"));
        List<Answer> answerList = answerRepository.findByQuestionIdAndDeletedFalse(question.getId());

        assertThat(answerList).hasSize(2);
        assertThat(answerList).containsAll(Arrays.asList(answer1, answer2));
    }
}
