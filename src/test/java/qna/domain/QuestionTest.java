package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class QuestionTest {

    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Autowired
    QuestionRepository questions;
    @Autowired
    DeleteHistoryRepository deletes;

    private static Stream<Question> question_save_equal_test() {
        return Stream.of(Q1,Q2);
    }

    @ParameterizedTest
    @DisplayName("question 엔티티 저장 후 찾기 테스트")
    @MethodSource
    void question_save_equal_test(Question input) {
        Question question = questions.save(input);
        assertThat(questions.findById(question.getId()).get()).isEqualTo(question);
    }

    @Test
    @DisplayName("question 엔티티 저장 후 수정 테스트")
    void question_save_update_test() {
        //given
        Question question = questions.save(Q1);
        Long id = question.getId();
        //when
        Question searchResult = questions.findById(id).get();
        String updateContents = "수정된 컨텐츠";
        questions.save(new Question(id, searchResult.getTitle(), updateContents));
        //then
        assertThat(questions.findById(id).get().getContents()).isEqualTo(updateContents);
    }

    private static Stream<Question> question_save_delete_test() {
        return Stream.of(Q1,Q2);
    }

    @ParameterizedTest
    @DisplayName("question 엔티티 저장 후 삭제 테스트")
    @MethodSource
    void question_save_delete_test(Question input) {
        //given
        Question question = questions.save(input);
        Long id = question.getId();
        DeleteHistory deleteHistory = deletes
                .save(new DeleteHistory(ContentType.QUESTION, id, id, LocalDateTime.now()));
        //when
        questions.deleteById(id);
        DeleteHistory expected = deletes.save(deleteHistory);
        //then
        assertThat(questions.findById(id).orElse(null)).isNull();
        assertThat(deleteHistory).isEqualTo(expected);
    }
}
