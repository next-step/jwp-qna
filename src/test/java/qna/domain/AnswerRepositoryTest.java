package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    private User writer;
    private Question question;

    @BeforeEach
    void setUp() {
        writer = userRepository.save(UserTest.JAVAJIGI);
        question = questionRepository.save(new Question("title1", "contents1").writeBy(writer));
    }

    @Test
    void Answer_저장() {
        Answer answer = new Answer(writer, question, "Answers Contents1");
        Answer result = answerRepository.save(answer);

        assertAll(
                () -> assertThat(result.getId()).isNotNull(),
                () -> assertThat(result.getContents()).isEqualTo(answer.getContents())
        );
    }

    @Test
    void Answer_단건_조회() {
        Answer result = answerRepository.save(new Answer(writer, question, "Answers Contents1"));

        assertThat(answerRepository.findById(result.getId()).get())
                .usingRecursiveComparison()
                .isEqualTo(result);
    }

    @Test
    void Answer_전체_조회() {
        answerRepository.save(new Answer(writer, question, "Answers Contents1"));
        answerRepository.save(new Answer(writer, question, "Answers Contents2"));

        assertThat(answerRepository.findAll()).hasSize(2);
    }

    @Test
    void Answer_삭제여부_컬럼이_false인_단건_조회() {
        Answer answer = answerRepository.save(new Answer(writer, question, "Answers Contents1"));

        assertThat(answerRepository.findByIdAndDeletedFalse(answer.getId()).get()).isEqualTo(answer);

        Answer findAnswer = answerRepository.findById(answer.getId()).get();
        findAnswer.setDeleted(true);

        assertThat(answerRepository.findByIdAndDeletedFalse(answer.getId()).isPresent()).isFalse();

    }

    @Test
    void Answer_findByQuestionId_삭제여부_컬럼이_false인_전체_조회() {
        Answer result = answerRepository.save(new Answer(writer, question, "Answers Contents1"));

        assertThat(answerRepository.findByQuestionIdAndDeletedFalse(result.getQuestion().getId())).hasSize(1);

        Answer findAnswer = answerRepository.findById(result.getId()).get();
        findAnswer.setDeleted(true);

        assertThat(answerRepository.findByQuestionIdAndDeletedFalse(findAnswer.getQuestion().getId()).size()).isZero();
    }

    @Test
    void question_연관관계_맵핑_검증() {
        Answer answer = answerRepository.save(new Answer(writer, question, "Answers Contents1"));
        Answer findAnswer = answerRepository.findById(answer.getId()).get();

        assertThat(findAnswer.getQuestion()).isEqualTo(question);
    }

    @Test
    void writer_연관관계_맵핑_검증() {
        Answer answer = answerRepository.save(new Answer(writer, question, "Answers Contents1"));
        Answer findAnswer = answerRepository.findById(answer.getId()).get();

        assertThat(findAnswer.getWriter()).isEqualTo(writer);
    }

}
