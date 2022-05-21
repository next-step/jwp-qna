package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");


    @Autowired
    AnswerRepository answers;

    @DisplayName("create_at, deleted 필드 값을 null 을 가질수 없다.")
    @Test
    void createTest() {
        Answer expected = answers.save(A1);
        assertAll(
                () -> assertThat(expected.getCreateAt()).isNotNull(),
                () -> assertThat(expected.getDeleted()).isNotNull()
        );
    }

    @DisplayName("저장값 비교하기")
    @Test
    void identityTest() {
        Answer expected = answers.save(A1);
        Answer answer = answers.findById(A1.getId()).get();
        assertThat(expected).isSameAs(answer);
    }


    @DisplayName("변경하기 ")
    @Test
    void updateTest(){
        Answer savedAnswer = answers.save(A1);
        savedAnswer.setContents("Answers change");
        Optional<Answer> isAnswer = answers.findByContents("Answers change");
        assertThat(isAnswer.isPresent()).isTrue();
        assertThat(isAnswer.get().getContents()).isEqualTo(savedAnswer.getContents());
    }

    @DisplayName("동일 질문 이고 삭제가 안된 정보 가져오기 테스트")
    @Test
    void getSameQuestionListTest() {
        answers.saveAll(Arrays.asList(A1, A2));
        List<Answer> byQuestionIdAndDeletedFalse = answers.findByQuestionIdAndDeletedFalse(QuestionTest.Q1.getId());
        assertThat(byQuestionIdAndDeletedFalse).contains(A1, A2);
    }
}
