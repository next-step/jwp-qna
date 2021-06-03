package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class DeleteHistoryRepositoryTest {
    @Autowired
    private DeleteHistoryRepository deleteHistories;

    @Autowired
    private AnswerRepository answers;

    @Autowired
    private QuestionRepository questions;

    @Autowired
    private UserRepository users;

    @Test
    public void save_테스트() {
        User writer_saved = users.save(new User("javajigi", "password", "name", "javajigi@slipp.net"));
        Question question_saved = questions.save(new Question("title1", "contents1").writeBy(writer_saved));
        Answer answer = new Answer(writer_saved, question_saved, "Answers Contents1");
        answer.toQuestion(question_saved);
        Answer answer_saved = answers.save(answer);

        DeleteHistory deleteHistory = new DeleteHistory(ContentType.ANSWER, answer_saved.getId(), writer_saved, LocalDateTime.now());
        DeleteHistory actual = deleteHistories.save(deleteHistory);

        assertThat(actual.getContentId()).isEqualTo(answer_saved.getId());
    }
}