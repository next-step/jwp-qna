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
class QuestionRepositoryTest {
    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    UserRepository userRepository;

    @Test
    void 질문_저장_및_찾기() {
        User savedUser1 = userRepository.save(newUser("1"));
        Question question = newQuestion("1").writeBy(savedUser1);
        Question actual = questionRepository.save(question);
        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getContents()).isEqualTo(question.getContents()),
            () -> assertThat(actual.getTitle()).isEqualTo(question.getTitle()),
            () -> assertThat(actual.isDeleted()).isFalse(),
            () -> assertThat(actual.getWriter()).isEqualTo(question.getWriter()),
            () -> assertThat(actual.getCreatedAt()).isNotNull(),
            () -> assertThat(actual.getUpdatedAt()).isNotNull(),
            () -> assertThat(actual.getUpdatedAt()).isEqualTo(actual.getCreatedAt())
        );
        assertThat(questionRepository.findByIdAndDeletedFalse(actual.getId())).contains(actual);
    }

    @Test
    void 질문_복수개_저장_후_찾기() {
        User savedUser1 = userRepository.save(newUser("1"));
        Question question1 = newQuestion("1").writeBy(savedUser1);
        Question savedQuestion1 = questionRepository.save(question1);

        User savedUser2 = userRepository.save(newUser("2"));
        Question question2 = newQuestion("2").writeBy(savedUser2);
        Question savedQuestion2 = questionRepository.save(question2);

        List<Question> findQuestions = questionRepository.findByDeletedFalse();
        assertThat(findQuestions).hasSize(2);
        assertThat(findQuestions).containsExactly(savedQuestion1, savedQuestion2);
    }

    @Test
    void 질문_삭제여부_변경() {
        User savedUser = userRepository.save(newUser("1"));
        Question question = newQuestion("1").writeBy(savedUser);
        Question savedQuestion = questionRepository.save(question);
        assertThat(questionRepository.findByIdAndDeletedFalse(savedQuestion.getId())).contains(savedQuestion);
        savedQuestion.changeDeleted(true);
        assertThat(questionRepository.findByIdAndDeletedFalse(savedQuestion.getId())).isEmpty();
    }

    @Test
    void 연관관계_편의메서드_예외처리_확() {
        User savedUser = userRepository.save(newUser("1"));
        Question question = newQuestion("1");
        question.writeBy(savedUser);
        question.writeBy(savedUser);
        question.writeBy(savedUser);
        question.writeBy(savedUser);
        question.writeBy(savedUser);

        assertThat(savedUser.getQuestions()).hasSize(1);
    }
}