package qna.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;


class DeletionTest {

    @Test
    void delete() {
        // given
        User questionUser = new User("user1", "user1Pass", "User1", "user1@gmail.com");
        Question question = new Question("Question1 title", "Question1 contents").writeBy(questionUser);

        //when
        Deletion deletion = new Deletion();

        assertAll(
                () -> assertThat(deletion.delete(question)).extracting("contentId").isEqualTo(question.getId()),
                () -> assertThat(deletion.delete(question)).extracting("deleter").isEqualTo(questionUser),
                () -> assertThat(deletion.isDeleted()).isTrue()
        );
    }
}