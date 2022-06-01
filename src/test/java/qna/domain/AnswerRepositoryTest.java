package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class AnswerRepositoryTest extends BaseRepositoryTest {
    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    QuestionRepository questionRepository;

    Question savedQ1;
    Question savedQ2;

    Answer savedA1;
    Answer savedA2;


    @BeforeEach
    void setUp() {
        saveUsers();
        savedQ1 = questionRepository.save(new Question("title1", "contents1").writeBy(savedJavajigi));
        savedQ2 = questionRepository.save(new Question("title2", "contents2").writeBy(savedSanjigi));
        savedA1 = new Answer(savedJavajigi, savedQ1, "Answers Contents1").writeBy(savedJavajigi);
        savedQ1.addAnswer(savedA1);
        savedA2 = new Answer(savedSanjigi, savedQ2, "Answers Contents2").writeBy(savedSanjigi);
        savedQ2.addAnswer(savedA2);
        answerRepository.flush();
    }

    @Test
    @DisplayName("Answer 저장")
    void save() {
        // when
        final Answer answer = answerRepository.save(new Answer(savedSanjigi, savedQ1, "Answers Contents1"));

        // then
        assertThat(answer).isNotNull();
    }

    @Test
    @DisplayName("id 와 delete = false 인 Answer 들 조회")
    void findByIdAndDeletedFalse() {
        // when
        final Optional<Answer> actual = answerRepository.findByIdAndDeletedFalse(savedA1.getId());

        // then
        assertThat(actual).isNotEmpty();
    }

    @Test
    @DisplayName("answers 정보 조회")
    void getAnswers() {
        // when & then
        assertThat(savedQ1.getAnswers().contains(savedA1)).isTrue();
    }

    @Test
    @DisplayName("Answer contents 내용 변경")
    void update() {
        // when
        savedA1.changeContents("contents 변경");

        // then
        assertThat(savedA1.getContents()).isEqualTo(answerRepository.findById(savedA1.getId()).get().getContents());
    }

    @Test
    @DisplayName("Answer 삭제")
    void delete() {
        // when
        savedA1.delete(savedJavajigi);

        // then
        assertThat(savedA1.isDeleted()).isTrue();
    }
}
