package qna.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.*;

import static org.assertj.core.api.Assertions.assertThat;
import static qna.domain.AnswerTest.ANSWERS_CONTENTS_2;

@DataJpaTest
@DisplayName("삭제 내역 Repository")
class DeleteHistoriesRepositoryTest {

    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;

    @DisplayName("삭제 내역을 추가한다.")
    @Test
    void add() {

        User javajigi = createUser(UserTest.JAVAJIGI);
        Question question = createQuestion(javajigi, QuestionTest.QUESTION_1);

        DeleteHistory deleteHistory = deleteHistoryRepository.save(DeleteHistory.ofQuestion(question.getId(), question.getWriter()));

        DeleteHistories deleteHistories = new DeleteHistories();
        deleteHistories.add(deleteHistory);

        assertThat(deleteHistories.size()).isEqualTo(1);
    }

    private User createUser(User user) {
        return userRepository.save(user);
    }

    private Question createQuestion(User user, Question question) {
        return questionRepository.save(question.writeBy(user));
    }

    private Answer createAnswer(User user, Question question) {
        User writer = createUser(user);
        return answerRepository.save(new Answer(writer, question, ANSWERS_CONTENTS_2));
    }
}
