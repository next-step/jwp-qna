package qna.domain;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.CannotDeleteException;

@DataJpaTest
public class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void save() {
        User savedUser = userRepository.save(TestDummy.USER_SANJIGI);
        TestDummy.QUESTION1.setWriter(savedUser);
        Question savedQuestion = questionRepository.save(TestDummy.QUESTION1);

        assertAll(
            () -> assertThat(savedQuestion.getTitle()).isEqualTo(TestDummy.QUESTION1.getTitle()),
            () -> assertThat(savedQuestion.getContents()).isEqualTo(TestDummy.QUESTION1.getContents()),
            () -> assertThat(savedQuestion.getWriter()).isEqualTo(TestDummy.QUESTION1.getWriter()),
            () -> assertThat(savedQuestion.isDeleted()).isEqualTo(TestDummy.QUESTION1.isDeleted()),
            () -> assertThat(savedQuestion.getCreatedDateTime()).isBefore(LocalDateTime.now())
        );
    }

    @Test
    void read() {
        User savedUser = userRepository.save(TestDummy.USER_SANJIGI);
        TestDummy.QUESTION1.setWriter(savedUser);
        Long savedId = questionRepository.save(TestDummy.QUESTION1).getId();
        Question savedQuestion = questionRepository.findByIdAndDeletedFalse(savedId).get();

        assertAll(
            () -> assertThat(savedQuestion.getId()).isEqualTo(savedId),
            () -> assertThat(savedQuestion.getTitle()).isEqualTo(TestDummy.QUESTION1.getTitle()),
            () -> assertThat(savedQuestion.getContents()).isEqualTo(TestDummy.QUESTION1.getContents()),
            () -> assertThat(savedQuestion.getWriter()).isEqualTo(TestDummy.QUESTION1.getWriter()),
            () -> assertThat(savedQuestion.isDeleted()).isEqualTo(TestDummy.QUESTION1.isDeleted()),
            () -> assertThat(savedQuestion.getCreatedDateTime()).isBefore(LocalDateTime.now())
        );
    }

    @Test
    @DisplayName("질문을 저장하는 경우, 답변도 같이 저장된다.")
    void saveAfterAddAnswer() {
        userRepository.save(TestDummy.USER_JAVAJIGI);
        userRepository.save(TestDummy.USER_SANJIGI);
        TestDummy.QUESTION1.addAnswer(TestDummy.ANSWER2);
        TestDummy.QUESTION1.addAnswer(TestDummy.ANSWER1);

        Long savedId = questionRepository.save(TestDummy.QUESTION1).getId();

        Question savedQuestion = questionRepository.findByIdAndDeletedFalseWithAnswers(savedId).get();

        assertThat(savedQuestion.getAnswers().getValues().size()).isEqualTo(2);
    }

    @Test
    @DisplayName("답변 삭제후, 질문 저장")
    void 답변_삭제후_질문_저장() throws CannotDeleteException {
        userRepository.save(TestDummy.USER_JAVAJIGI);
        userRepository.save(TestDummy.USER_SANJIGI);
        TestDummy.QUESTION1.addAnswer(TestDummy.ANSWER2);
        TestDummy.QUESTION1.addAnswer(TestDummy.ANSWER1);
        TestDummy.ANSWER1.delete(TestDummy.USER_JAVAJIGI);

        Long savedId = questionRepository.save(TestDummy.QUESTION1).getId();

        Question savedQuestion = questionRepository.findByIdAndDeletedFalseWithAnswers(savedId).get();

        assertThat(savedQuestion.getAnswers().getValues()
            .stream()
            .filter(answer -> !answer.isDeleted())
            .collect(toList()).size()).isEqualTo(1);
    }
}
