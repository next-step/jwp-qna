package qna.domain;

import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Test
    void delete() throws CannotDeleteException {
        Question question = new Question(1L, "title1", "contents1", new ArrayList<>()).writeBy(UserTest.JAVAJIGI);
        List<DeleteHistory> deleteHistories = question.delete(UserTest.JAVAJIGI);
        List<DeleteHistory> expected = singletonList(DeleteHistory.ofQuestion(question.getId(), UserTest.JAVAJIGI));

        assertThat(deleteHistories).isEqualTo(expected);
    }
}
