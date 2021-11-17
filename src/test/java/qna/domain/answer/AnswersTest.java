package qna.domain.answer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.QuestionTestFactory;
import qna.domain.UserTestFactory;
import qna.domain.deletehistory.DeleteHistory;
import qna.domain.question.Question;
import qna.domain.question.QuestionRepository;
import qna.domain.user.User;
import qna.domain.user.UserRepository;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AnswersTest {

    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private QuestionRepository questionRepository;

    @Test
    @DisplayName("자신이 작성한 답변을 삭제하여 삭제기록을 확인 할 수 있다.")
    void deleteAnswer() {
        final User writer = userRepository.save(UserTestFactory.create("testuser1", "testuser111@test.com"));
        final Question question = questionRepository.save(QuestionTestFactory.create("title", "content", writer));

        final List<Answer> answerList = Arrays.asList(
                AnswerTestFactory.create(writer, question, "Answer Content"),
                AnswerTestFactory.create(writer, question, "Answer Content1"),
                AnswerTestFactory.create(writer, question, "Answer Content2")
        );

        final List<Answer> savedAnswerList = answerRepository.saveAll(answerList);
        final Answers answers = Answers.of(savedAnswerList);

        final List<DeleteHistory> deleteHistories = answers.deleteAnswer(writer);

        assertThat(deleteHistories.size()).isEqualTo(3);
        assertThat(deleteHistories).contains(DeleteHistory.ofAnswer(savedAnswerList.get(0).getId(), writer));
        assertThat(deleteHistories).contains(DeleteHistory.ofAnswer(savedAnswerList.get(1).getId(), writer));
        assertThat(deleteHistories).contains(DeleteHistory.ofAnswer(savedAnswerList.get(2).getId(), writer));
    }
}