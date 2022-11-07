package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class AnswerTest {

    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");
    private static final Stream<Answer> answerList = Stream.of(A1,A2);

    @Autowired
    AnswerRepository answers;
    @Autowired
    DeleteHistoryRepository deletes;

    private static Stream<Answer> save_entity_equal_test() {
        return answerList;
    }

    @ParameterizedTest
    @DisplayName("저장한 엔티티가 동일한지 테스트")
    @MethodSource
    void save_entity_equal_test(Answer input) {
        Answer answer = answers.save(input);
        assertThat(input).isEqualTo(answer);
    }

    private static Stream<Answer> save_entity_and_find_test() {
        return answerList;
    }

    @ParameterizedTest
    @DisplayName("엔티티 저장 후 찾기 테스트")
    @MethodSource
    void save_entity_and_find_test(Answer input) {
        Answer answer = answers.save(input);
        assertThat(answer).isEqualTo(answers.findById(answer.getId()).get());
    }

    private static Stream<Answer> save_entity_and_delete_test() {
        return answerList;
    }

    @ParameterizedTest
    @DisplayName("엔티티 저장 후 삭제 테스트")
    @MethodSource
    void save_entity_and_delete_test(Answer input) {
        //given
        Answer answer = answers.save(input);
        DeleteHistory deleteHistory = new DeleteHistory(ContentType.ANSWER,
                answer.getId(), answer.getId(), LocalDateTime.now());
        //when
        answers.deleteById(answer.getId());
        DeleteHistory expected = deletes.save(deleteHistory);
        //then
        assertThat(answers.findById(answer.getId()).orElse(null)).isNull();
        assertThat(deleteHistory).isEqualTo(expected);
    }

}
