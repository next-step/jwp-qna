package qna.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class DeletionTest {

    @Test
    void delete() {
        // given
        User questionUser = new User("user1", "user1Pass", "User1", "user1@gmail.com");
        Question question = new Question("Question1 title", "Question1 contents").writeBy(questionUser);

        //when
        Deletion deletion = new Deletion();
        deletion.delete();

        //then
        assertThat(deletion.isDeleted()).isTrue();
    }
}