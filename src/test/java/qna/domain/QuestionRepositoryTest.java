package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    private User writer;

    @BeforeEach
    void setUp() {
        writer = userRepository.save(UserTest.JAVAJIGI);
    }

    @Test
    void Question_저장() {
        Question question = new Question("title1", "contents1").writeBy(writer);
        Question result = questionRepository.save(question);

        assertAll(
                () -> assertThat(result.getId()).isNotNull(),
                () -> assertThat(result.getTitle()).isEqualTo(question.getTitle())
        );
    }

    @Test
    void Question_단건_조회() {
        Question question = questionRepository.save(new Question("title1", "contents1").writeBy(writer));

        assertThat(questionRepository.findById(question.getId()).get())
                .usingRecursiveComparison()
                .isEqualTo(question);
    }

    @Test
    void Question_전체_조회() {
        questionRepository.save(new Question("title1", "contents1").writeBy(writer));
        questionRepository.save(new Question("title2", "contents2").writeBy(writer));

        assertThat(questionRepository.findAll()).hasSize(2);
    }

    @Test
    void Question_삭제여부_컬럼이_false인_단건_조회() {
        Question question = questionRepository.save(new Question("title1", "contents1").writeBy(writer));

        assertThat(questionRepository.findByIdAndDeletedFalse(question.getId()).get()).isEqualTo(question);

        Question findQuestion = questionRepository.findById(question.getId()).get();
        findQuestion.setDeleted(true);

        assertThat(questionRepository.findByIdAndDeletedFalse(question.getId()).isPresent()).isFalse();
    }

    @Test
    void Question_삭제여부_컬럼이_false인_전체_조회() {
        Question question1 = questionRepository.save(new Question("title1", "contents1").writeBy(writer));
        questionRepository.save(new Question("title2", "contents2").writeBy(writer));

        assertThat(questionRepository.findByDeletedFalse()).hasSize(2);

        Question findQuestion = questionRepository.findById(question1.getId()).get();
        findQuestion.setDeleted(true);

        assertThat(questionRepository.findByDeletedFalse()).hasSize(1);
    }

    @Test
    void writer_연관관계_맵핑_검증() {
        Question question = questionRepository.save(new Question("title1", "contents1").writeBy(writer));
        Question findQuestion = questionRepository.findById(question.getId()).get();

        assertThat(findQuestion.getWriter()).isEqualTo(writer);
    }

}
