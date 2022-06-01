package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.CannotDeleteException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class QuestionRepositoryTest {
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private UserRepository userRepository;

    private Question question1, question2;
    private User writer;

    @BeforeEach
    void init() {
        //given
        writer = userRepository.save(new User("javajigi", "password", "name", "javajigi@slipp.net"));
        question1 = questionRepository.save(new Question("title1", "contents1").writeBy(writer));
        question2 = questionRepository.save(new Question("title2", "contents2").writeBy(writer));
    }

    @Test
    @DisplayName("질문 저장 및 값 비교 테스트")
    void save() {
        //when
        final Question actual = questionRepository.save(question1);

        //then
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getTitle()).isEqualTo(question1.getTitle()),
                () -> assertThat(actual.getContents()).isEqualTo(question1.getContents()),
                () -> assertThat(actual.getWriter()).isEqualTo(question1.getWriter())
        );
    }

    @Test
    @DisplayName("삭제되지 않은 질문 목록 조회")
    void findByDeletedFalse() throws CannotDeleteException {
        //when
        question1.delete(writer);
        List<Question> founds = questionRepository.findByDeletedFalse();

        //then
        assertAll(
                () -> assertThat(founds).hasSize(1),
                () -> assertThat(founds).doesNotContain(question1),
                () -> assertThat(founds).containsExactly(question2)
        );
    }

    @Test
    @DisplayName("id로 삭제되지 않은 질문 목록 조회")
    void findByIdAndDeletedFalse() throws CannotDeleteException {
        //when
        question1.delete(writer);
        Optional<Question> foundsQuestion1 = questionRepository.findByIdAndDeletedFalse(question1.getId());
        Optional<Question> foundsQuestion2 = questionRepository.findByIdAndDeletedFalse(question2.getId());

        //then
        assertAll(
                () -> assertThat(foundsQuestion1.isPresent()).isFalse(),
                () -> assertThat(foundsQuestion2.isPresent()).isTrue()
        );
    }

    @Test
    @DisplayName("질문에 답변 등록 후 답변 정보 확인")
    void addAnswer() {
        //when
        final Answer answer = answerRepository.save(new Answer(writer, question1, "Answers Contents"));
        question1.addAnswer(answer);

        //then
        assertAll(
                () -> assertThat(question1.getAnswers()).hasSize(1),
                () -> assertThat(question1.getAnswers()).containsExactly(answer)
        );
    }
}