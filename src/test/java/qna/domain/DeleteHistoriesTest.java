package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.CannotDeleteException;
import qna.NotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static qna.domain.AnswerTest.answer;
import static qna.domain.QuestionTest.question;
import static qna.domain.UserTest.userB;

@DataJpaTest
public class DeleteHistoriesTest {
    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    private static Question question;
    private static Answers answers;

    @BeforeEach
    void setUp() throws CannotDeleteException {
        Answer answer = answer(5);
        answer.mappingToWriter(userRepository.save(userB()));
        Question questionEntity = questionRepository.save(question(5));
        answer.mappingToQuestion(questionEntity);
        Answer save = answerRepository.save(answer);

        question = questionRepository.findByIdAndDeletedFalse(questionEntity.getId()).orElseThrow(NotFoundException::new);
        answers = Answers.createAnswers(answerRepository.findByQuestionIdAndDeletedFalse(save.getQuestion().getId()), save.getWriter());
    }

    @DisplayName("question 과 answers 를 delete 하는 정적 팩토리 메소드")
    @Test
    void createDeletedHistories() {
        DeleteHistories deleteHistories = DeleteHistories.createDeletedHistories(question, answers);
        assertThat(deleteHistories).isEqualTo(DeleteHistories.createDeletedHistories(question, answers));
    }

    @DisplayName("deleteQuestion 를 하면 question 의 deleted 컬럼 값이 true 변경되었는지 확인")
    @Test
    void deleteQuestion() {
        DeleteHistories.createDeletedHistories(question, answers);
        assertThat(question.isDeleted()).isTrue();
    }

    @DisplayName("deleteAnswers 를 하면 Answer deleted 컬럼 값이 true 변경되었는지 확인")
    @Test
    void deleteAnswers() {
        DeleteHistories.createDeletedHistories(question, answers);
        assertThat(answers.getAnswers().get(0).isDeleted()).isTrue();
    }
}
