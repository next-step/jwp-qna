package qna.domain;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import qna.config.QnaDataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static qna.domain.FixtureAnswer.A1;
import static qna.domain.FixtureQuestion.Q1;
import static qna.domain.FixtureQuestion.Q2;
import static qna.domain.FixtureUser.*;

@QnaDataJpaTest
class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository repository;

    @BeforeAll
    static void setUp(@Autowired UserRepository userRepository,
                      @Autowired QuestionRepository questionRepository) {
        userRepository.deleteAll();
        userRepository.save(JAVAJIGI);
        userRepository.save(SANJIGI);
        userRepository.save(HEOWC);
        questionRepository.deleteAll();
        questionRepository.save(Q1);
        questionRepository.save(Q2);
    }

    @AfterAll
    static void destroy(@Autowired UserRepository userRepository,
                        @Autowired QuestionRepository questionRepository) {
        questionRepository.deleteAll();
        userRepository.deleteAll();
    }

    @DisplayName("save하면 id 자동 생성")
    @Test
    void save() {
        final Answer answer = repository.save(A1);
        assertThat(answer.getId()).isNotNull();
    }

    @DisplayName("저장한 엔티티와 동일한 id로 조회한 엔티티는 동일성 보장")
    @Test
    void sameEntity() {
        final Answer saved = repository.save(A1);
        final Answer answer = repository.findById(saved.getId()).get();
        assertAll(
                () -> assertThat(answer.getId()).isEqualTo(saved.getId()),
                () -> assertThat(answer).isEqualTo(saved),
                () -> assertThat(answer.getQuestion()).isEqualTo(A1.getQuestion()),
                () -> assertThat(answer.getWriter()).isEqualTo(A1.getWriter())
        );
    }

    @DisplayName("Answer에 대한 Question을 변경")
    @Test
    void toQuestion(@Autowired QuestionRepository questionRepository) {
        final Question savedQuestion = questionRepository.save(new Question("3", "3"));
        final Answer answer = new Answer(HEOWC, Q1, "dummy");
        answer.toQuestion(savedQuestion);

        final Answer saved = repository.save(answer);
        final Question question = saved.getQuestion();
        assertAll(
                () -> assertThat(question).isNotEqualTo(Q1),
                () -> assertThat(question.getTitle()).isEqualTo("3"),
                () -> assertThat(question.getContents()).isEqualTo("3")
        );
    }
}
