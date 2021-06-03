package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

@DataJpaTest
public class DeleteHistoryRepositoryTest {
    @Autowired
    private DeleteHistoryRepository deleteHistorys;

    @Autowired
    private UserRepository users;

    @Autowired
    private QuestionRepository questions;

    @Autowired
    private AnswerRepository answers;

    private DeleteHistory deleteHistory1;
    private DeleteHistory deleteHistory2;

    @BeforeEach
    void setUp() {
        User questionWriter = new User("qwriter", "password", "name", "sunju@slipp.net");
        Question question= new Question("title3", "contents2").writeBy(questionWriter);
        users.save(questionWriter);
        Question savedQuestion = questions.save(question);

        User answerWriter = new User("awriter", "password", "name", "sunju@slipp.net");
        Answer answer = new Answer(answerWriter, question, "Answers Contents");
        users.save(answerWriter);
        Answer savedAnswer = answers.save(answer);

        deleteHistory1 = new DeleteHistory(ContentType.ANSWER, savedAnswer, LocalDateTime.now());
        deleteHistory2 = new DeleteHistory(ContentType.QUESTION, savedQuestion, LocalDateTime.now());

    }

    @Test
    @DisplayName("save 테스트")
    void saveTest() {
        deleteHistorys.save(deleteHistory1);
        deleteHistorys.save(deleteHistory2);
        deleteHistorys.flush();
    }
}
