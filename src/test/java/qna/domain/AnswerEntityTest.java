package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.NotFoundException;
import qna.UnAuthorizedException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static qna.domain.QuestionEntityTest.provideQuestion;
import static qna.domain.UserRepositoryTest.provideUser;

@DisplayName("답변 테스트")
public class AnswerEntityTest {

    @DisplayName("생성 성공")
    @Test
    void create_answer_success() {
        //given:
        final long id = 1;
        final User writer = provideUser();
        final Question question = provideQuestion();
        final Contents contents = Contents.from("content");
        //when:
        Answer answer = new Answer(id, writer, question, contents);
        //then:
        assertThat(answer.getId()).isEqualTo(id);
    }

    @DisplayName("생성 실패 - writer가 없는 경우")
    @Test
    void createWithoutWriter_answer_success() {
        //given:
        final long id = 1;
        final User writer = null;
        final Question question = provideQuestion();
        final Contents contents = Contents.from("content");
        //when, then:
        assertThatThrownBy(() -> new Answer(id, writer, question, contents))
                .isInstanceOf(UnAuthorizedException.class);
    }

    @DisplayName("생성 실패 - question이 없는 경우")
    @Test
    void createWithoutQuestion_answer_success() {
        //given:
        final long id = 1;
        final User writer = provideUser();
        final Question question = null;
        final Contents contents = Contents.from("content");
        //when, then:
        assertThatThrownBy(() -> new Answer(id, writer, question, contents))
                .isInstanceOf(NotFoundException.class);
    }

    public static Answer provideAnswer(long id, User user, Question question) {
        return provideAnswer(id, user, question, Contents.from("ABC"));
    }

    public static Answer provideAnswer(long id, User user, Question question, Contents contents) {
        return new Answer(id, user, question, contents);
    }
}
