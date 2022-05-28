package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private QuestionRepository questionRepository;

    @DisplayName("Answer 정보 id로 조회 테스트")
    @Test
    void findById() {
        Optional<Answer> optionalAnswer = answerRepository.findById(3001L);
        assertThat(optionalAnswer).isPresent();

        Answer answer = optionalAnswer.get();
        assertAll(
                () -> assertThat(answer.getId()).isEqualTo(3001L),
                () -> assertThat(answer.getWriter().getId()).isEqualTo(1001L),
                () -> assertThat(answer.getQuestion().getId()).isEqualTo(2001L),
                () -> assertThat(answer.getContents()).isEqualTo("수원"),
                () -> assertThat(answer.isDeleted()).isTrue(),
                () -> assertThat(answer.getCreatedAt()).isNotNull()
        );
    }

    @DisplayName("Answer 정보 저장 테스트")
    @Test
    void save() {
        User writer = userRepository.findById(1001L).get();
        Question question = questionRepository.findById(2001L).get();
        Answer answer = answerRepository.save(new Answer(writer, question, "abc"));
        answerRepository.flush();

        Optional<Answer> optionalAnswer = answerRepository.findById(answer.getId());
        assertThat(optionalAnswer).isPresent();

        Answer savedAnswer = optionalAnswer.get();
        assertAll(
                () -> assertThat(savedAnswer.getWriter()).isEqualTo(writer),
                () -> assertThat(savedAnswer.getQuestion().getId()).isEqualTo(question.getId()),
                () -> assertThat(savedAnswer.getContents()).isEqualTo("abc"),
                () -> assertThat(savedAnswer.isDeleted()).isFalse(),
                () -> assertThat(savedAnswer.getCreatedAt()).isNotNull(),
                () -> assertThat(savedAnswer.getUpdatedAt()).isNotNull()
        );
    }

    @DisplayName("id와 삭제여부로 정보 조회")
    @Test
    void findByIdAndDeletedFalse() {
        Optional<Answer> optionalAnswer = answerRepository.findByIdAndDeletedFalse(3002L);
        assertThat(optionalAnswer).isPresent();

        Answer answer = optionalAnswer.get();
        assertAll(
                () -> assertThat(answer.getId()).isEqualTo(3002L),
                () -> assertThat(answer.getWriter().getId()).isEqualTo(1002L),
                () -> assertThat(answer.getQuestion().getId()).isEqualTo(2002L),
                () -> assertThat(answer.getContents()).isEqualTo("한국대"),
                () -> assertThat(answer.isDeleted()).isFalse(),
                () -> assertThat(answer.getCreatedAt()).isNotNull()
        );
    }

    @DisplayName("id와 삭제여부로 삭제된 id 조회")
    @Test
    void findByIdAndDeletedFalseDeleted() {
        Optional<Answer> optionalAnswer = answerRepository.findByIdAndDeletedFalse(3001L);
        assertThat(optionalAnswer).isNotPresent();
    }

    @DisplayName("질문 id와 삭제여부로 정보 조회")
    @Test
    void findByQuestionIdAndDeletedFalse() {
        List<Answer> list = answerRepository.findByQuestionIdAndDeletedFalse(2003L);
        assertThat(list).hasSize(2);
    }

    @DisplayName("질문 id와 삭제여부로 삭제된 질문 관련 정보 조회")
    @Test
    void findByQuestionIdAndDeletedFalseDeleted() {
        List<Answer> list = answerRepository.findByQuestionIdAndDeletedFalse(2001L);
        assertThat(list).isEmpty();
    }
}
