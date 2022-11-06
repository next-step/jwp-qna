package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static qna.domain.QuestionTest.*;
import static qna.domain.UserTest.*;

import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class AnswerRepositoryTest {
    @Autowired
    AnswerRepository answerRepository;
    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    UserRepository userRepository;

    private User savedQuestionWriter;
    private User savedAnswerWriter;
    private Question savedQuestion;
    private Answer savedAnswer;

    @BeforeEach
    void setUp() {
        userRepository.deleteAllInBatch();
        questionRepository.deleteAllInBatch();
        answerRepository.deleteAllInBatch();
        this.savedQuestionWriter = userRepository.save(newUser("1"));
        this.savedAnswerWriter = userRepository.save(newUser("2"));
        this.savedQuestion = questionRepository.save(newQuestion("1").writeBy(this.savedQuestionWriter));
        savedAnswer = answerRepository.save(new Answer(savedAnswerWriter, savedQuestion, "answerContents"));
    }

    @Test
    void 답변_저장_및_찾기() {
        assertAll(
            () -> assertThat(savedAnswer.getId()).isNotNull(),
            () -> assertThat(savedAnswer.getCreatedAt()).isNotNull(),
            () -> assertThat(savedAnswer.getUpdatedAt()).isNotNull(),
            () -> assertThat(savedAnswer.getUpdatedAt()).isEqualTo(savedAnswer.getCreatedAt()),
            () -> assertThat(savedAnswer.isDeleted()).isFalse(),
            () -> assertThat(savedAnswer.getQuestion()).isEqualTo(savedQuestion),
            () -> assertThat(savedAnswer.getContents()).isEqualTo("answerContents"),
            () -> assertThat(savedAnswer.getWriter()).isEqualTo(savedAnswerWriter)
        );
        assertThat(answerRepository.findByIdAndDeletedFalse(savedAnswer.getId())).contains(savedAnswer);
    }

    @Test
    void 답변_삭제여부_변경() {
        Answer findAnswer = answerRepository.findByIdAndDeletedFalse(savedAnswer.getId())
            .orElseThrow(RuntimeException::new);
        assertThat(findAnswer).isEqualTo(savedAnswer);
        findAnswer.changeDeleted(true);
        assertThat(answerRepository.findByIdAndDeletedFalse(findAnswer.getId())).isEmpty();
    }

    @Test
    void 삭제되지않은_질문에대한_답변들_찾기() {
        User savedAnswerWriter2 = userRepository.save(newUser("3"));
        Answer savedAnswer2 = answerRepository.save(new Answer(savedAnswerWriter2, savedQuestion, "answerContents2"));

        assertThat(questionRepository.findByIdAndDeletedFalse(savedQuestion.getId()))
            .get()
            .extracting(Question::getAnswers, InstanceOfAssertFactories.LIST)
            .hasSize(2)
            .containsExactly(savedAnswer, savedAnswer2);
    }
}