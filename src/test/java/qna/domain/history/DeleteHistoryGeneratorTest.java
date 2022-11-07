package qna.domain.history;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.domain.UserTest;
import qna.domain.content.ContentType;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("삭제 이력을 생성하는 테스트")
class DeleteHistoryGeneratorTest {

    @Test
    @DisplayName("삭제 이력을 생성한다")
    void generate() {
        DeleteHistory deleteHistory =
                DeleteHistoryGenerator.generate(ContentType.ANSWER, 1L, UserTest.JAVAJIGI);

        assertThat(deleteHistory).satisfies((history) -> {
            assertEquals(ContentType.ANSWER, history.getContentType());
            assertEquals(1L, history.getContentId());
        });
    }

    @Test
    @DisplayName("삭제 이력을 병합한다")
    void combine() {
        DeleteHistory question =
                DeleteHistoryGenerator.generate(ContentType.QUESTION, 1L, UserTest.JAVAJIGI);

        List<DeleteHistory> answers = Arrays.asList(
            DeleteHistoryGenerator.generate(ContentType.ANSWER, 1L, UserTest.JAVAJIGI),
            DeleteHistoryGenerator.generate(ContentType.ANSWER, 2L, UserTest.JAVAJIGI));

        List<DeleteHistory> resultCombined = DeleteHistoryGenerator.combine(question, answers);

        assertAll(
                () -> assertThat(resultCombined).hasSize(3),
                () -> assertThat(resultCombined).contains(question, answers.get(0), answers.get(1))
        );
    }
}
