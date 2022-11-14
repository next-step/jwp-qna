package qna.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.CannotDeleteException;
import qna.domain.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static qna.domain.AnswerTest.ANSWERS_CONTENTS_2;

@DataJpaTest
@DisplayName("삭제 내역 Repository")
class DeleteHistoryRepositoryTest {

    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;

    @DisplayName("저장_성공")
    @Test
    void save() {

        User javajigi = createUser(UserTest.JAVAJIGI);
        Question question = createQuestion(javajigi, QuestionTest.QUESTION_1);
        Answer answer = createAnswer(javajigi, question);

        DeleteHistory deleteHistory = deleteHistoryRepository.save(DeleteHistory.ofAnswer(answer.getId(), javajigi));

        assertAll(
                () -> assertThat(deleteHistory.getId()).isNotNull(),
                () -> assertThat(deleteHistory.getContentType()).isEqualTo(ContentType.ANSWER),
                () -> assertThat(deleteHistory.getContentId()).isNotNull(),
                () -> assertThat(deleteHistory.getCreateDate()).isNotNull(),
                () -> assertThat(deleteHistory.getDeletedById()).isNotNull());
    }

    @DisplayName("조회_성공")
    @Test
    void find() throws CannotDeleteException {

        User javajigi = createUser(UserTest.JAVAJIGI);
        List<DeleteHistory> deleteHistories = deleteHistoryRepository.saveAll(createQuestion(javajigi, QuestionTest.QUESTION_1).delete(javajigi).getDeleteHistories());
        Long deleteHistoryId = deleteHistories.get(0).getId();

        DeleteHistory findDeleteHistory = deleteHistoryRepository.findById(deleteHistoryId).orElse(null);

        assertAll(
                () -> assertThat(findDeleteHistory.getId()).isEqualTo(deleteHistoryId),
                () -> assertThat(findDeleteHistory.getContentType()).isEqualTo(ContentType.QUESTION),
                () -> assertThat(findDeleteHistory.getContentId()).isNotNull(),
                () -> assertThat(findDeleteHistory.getCreateDate()).isNotNull(),
                () -> assertThat(findDeleteHistory.getDeletedById()).isNotNull());
    }

    @DisplayName("삭제 내역 리스트 저장.")
    @Test
    void add() throws CannotDeleteException {

        User javajigi = createUser(UserTest.JAVAJIGI);
        Question question = createQuestion(javajigi, QuestionTest.QUESTION_1);

        List<DeleteHistory> deleteHistories = deleteHistoryRepository.saveAll(question.delete(javajigi).getDeleteHistories());

        assertThat(deleteHistories).hasSize(1);
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
