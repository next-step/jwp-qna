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
public class AnswerTest {

    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Autowired
    AnswerRepository answers;
    @Autowired
    DeleteHistoryRepository deletes;
    @Autowired
    QuestionRepository questions;
    @Autowired
    UserRepository users;

    private static Stream<Answer> save_entity_and_find_test() {
        return Stream.of(A1,A2);
    }

    @ParameterizedTest
    @DisplayName("엔티티 저장 후 찾기 테스트")
    @MethodSource
    void save_entity_and_find_test(Answer input) {
        Answer answer = answers.save(input);
        assertThat(answers.findById(answer.getId()).get()).isEqualTo(answer);
    }

    @Test
    @DisplayName("엔티티 저장 후 수정 테스트")
    void save_entity_and_update_test() {
        //given
        User user = users.save(UserTest.JAVAJIGI);
        Question question = questions.save(QuestionTest.Q1);
        Answer answer = answers.save(new Answer(user, question, "답변테스트"));
        Long id = answer.getId();
        //when
        Answer searchResult = answers.findById(id).get();
        String updateContents = "수정된 컨텐츠";
        answers.save(new Answer(searchResult.getId(),
                    users.findById(searchResult.getWriterId()).get(),
                    questions.findById(searchResult.getQuestionId()).get(),
                updateContents));
        //then
        assertThat(answers.findById(id).get().getContents()).isEqualTo(updateContents);
    }


    private static Stream<Answer> save_entity_and_delete_test() {
        return Stream.of(A1,A2);
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
