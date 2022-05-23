package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");


    @Autowired
    private AnswerRepository answerRepository;


    @DisplayName("create_at, deleted 필드 값을 null 을 가질수 없다.")
    @Test
    void createTest() {
        Answer expected = answerRepository.save(A1);
        assertAll(
                () -> assertThat(expected.getCreatedAt()).isNotNull(),
                () -> assertThat(expected.getUpdatedAt()).isNotNull()
        );
    }

    @DisplayName("저장값 비교하기")
    @Test
    void identityTest() {
        Answer expected = answerRepository.save(A1);
        answerRepository.flush();
        Answer answer = answerRepository.findById(expected.getId()).get();
        assertThat(expected).isSameAs(answer);
    }


    @DisplayName("변경하기 ")
    @Test
    void updateTest(){
        Answer savedAnswer = answerRepository.save(A1);
        savedAnswer.setContents("Answers change");
        Optional<Answer> isSavedAnswer = answerRepository.findByIdAndDeletedFalse(savedAnswer.getId());
        assertThat(isSavedAnswer.isPresent()).isTrue();
        assertThat(isSavedAnswer.get().getContents()).isEqualTo(savedAnswer.getContents());
    }
}
