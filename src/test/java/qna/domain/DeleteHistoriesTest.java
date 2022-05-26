package qna.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.repository.QuestionRepository;
import qna.repository.UserRepository;

import static qna.domain.QuestionTest.Q1;
import static qna.domain.UserTest.JAVAJIGI;

@DataJpaTest
class DeleteHistoriesTest {

    private Question question;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @BeforeEach
    void setUp() {
        User user = userRepository.save(JAVAJIGI);
        question = questionRepository.save(Q1.writeBy(user));
    }

    @Test
    @DisplayName("삭제 기록을 추가하는 테스트")
    void add() {
        DeleteHistories deleteHistories = new DeleteHistories();
        deleteHistories.add(DeleteHistory.ofQuestion(question.getId(), UserTest.JAVAJIGI));

        Assertions.assertThat(deleteHistories.getDeleteHistories()).size().isEqualTo(1);
    }

    @Test
    @DisplayName("삭제 기록을 합치는 테스트")
    void merge() {
        DeleteHistories deleteHistories1 = new DeleteHistories();
        deleteHistories1.add(DeleteHistory.ofQuestion(question.getId(), UserTest.JAVAJIGI));

        DeleteHistories deleteHistories2 = new DeleteHistories();
        deleteHistories2.add(DeleteHistory.ofQuestion(question.getId(), UserTest.JAVAJIGI));

        deleteHistories1.merge(deleteHistories2);
        Assertions.assertThat(deleteHistories1.getDeleteHistories()).size().isEqualTo(2);
    }

}
