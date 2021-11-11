package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("DeleteHistories 테스트")
class DeleteHistoriesTest {

    @Test
    @DisplayName("삭제 이력 리스트를 생성한다.")
    void create() {
        // given
        Question question = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
        Answer firstAnswer = new Answer(UserTest.JAVAJIGI, question, "Answers Contents1");
        Answer secondAnswer = new Answer(UserTest.SANJIGI, question, "Answers Contents2");

        // when
        DeleteHistories deleteHistories = DeleteHistories.fromQuestion(question);

        // then
        assertThat(deleteHistories.getDeleteHistories()).containsExactly(
                new DeleteHistory(ContentType.QUESTION, question.getId(), question.getWriter()),
                new DeleteHistory(ContentType.ANSWER, secondAnswer.getId(), secondAnswer.getWriter()),
                new DeleteHistory(ContentType.ANSWER, firstAnswer.getId(), firstAnswer.getWriter())
        );
    }
}
