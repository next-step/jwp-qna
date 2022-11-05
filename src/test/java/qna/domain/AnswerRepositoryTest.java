package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static qna.domain.QuestionTest.*;
import static qna.domain.UserTest.*;

import java.util.List;

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

    @Test
    void 답변_저장_및_찾기() {
        User questionWriter = userRepository.save(newUser("1"));
        Question question = questionRepository.save(newQuestion("1"));
        question.writeBy(questionWriter);
        User answerWriter = userRepository.save(newUser("2"));
        Answer answer = new Answer(answerWriter, question, "answerContents");
        Answer actual = answerRepository.save(answer);
        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getCreatedAt()).isNotNull(),
            () -> assertThat(actual.getUpdatedAt()).isNotNull(),
            () -> assertThat(actual.getUpdatedAt()).isEqualTo(actual.getCreatedAt()),
            () -> assertThat(actual.isDeleted()).isFalse(),
            () -> assertThat(actual.getQuestion()).isEqualTo(answer.getQuestion()),
            () -> assertThat(actual.getContents()).isEqualTo(answer.getContents()),
            () -> assertThat(actual.getWriter()).isEqualTo(answer.getWriter())
        );
        assertThat(answerRepository.findByIdAndDeletedFalse(actual.getId())).contains(answer);
    }

    @Test
    void 답변_삭제여부_변경() {
        User questionWriter = userRepository.save(newUser("1"));
        Question question = questionRepository.save(newQuestion("1"));
        question.writeBy(questionWriter);
        User answerWriter = userRepository.save(newUser("2"));
        Answer answer = new Answer(answerWriter, question, "answerContents");
        Answer savedAnswer = answerRepository.save(answer);

        Answer findAnswer = answerRepository.findByIdAndDeletedFalse(savedAnswer.getId())
            .orElseThrow(RuntimeException::new);
        assertThat(findAnswer).isEqualTo(savedAnswer);
        findAnswer.changeDeleted(true);
        assertThat(answerRepository.findByIdAndDeletedFalse(findAnswer.getId())).isEmpty();
    }

    @Test
    void 삭제되지않은_질문에대한_답변들_찾기() {
        User questionWriter = userRepository.save(newUser("1"));
        Question question = newQuestion("1");
        Question savedQuestion = questionRepository.save(question);
        savedQuestion.writeBy(questionWriter);

        User answerWriter = userRepository.save(newUser("2"));
        Answer savedAnswer1 = answerRepository.save(new Answer(answerWriter, savedQuestion, "answerContents1"));
        Answer savedAnswer2 = answerRepository.save(new Answer(answerWriter, savedQuestion, "answerContents2"));

        Question findQuestion = questionRepository.findByIdAndDeletedFalse(savedQuestion.getId())
            .orElseThrow(RuntimeException::new);

        List<Answer> findAnswers = findQuestion.getAnswers();
        assertThat(findAnswers).hasSize(2);
        assertThat(findAnswers).containsExactly(savedAnswer1, savedAnswer2);
    }
}