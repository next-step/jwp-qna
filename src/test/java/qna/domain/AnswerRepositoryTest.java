package qna.domain;

import org.hibernate.Hibernate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;
import qna.config.QnaDataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@QnaDataJpaTest
class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository repository;

    private Question question;
    private User user;

    @BeforeEach
    void setUp(@Autowired QuestionRepository questionRepository,
               @Autowired UserRepository userRepository) {
        repository.deleteAll();
        userRepository.deleteAll();
        user = userRepository.save(new User("answer", "1234", "answer", "answer@example.com"));
        questionRepository.deleteAll();
        question = questionRepository.save(new Question("answer", "answer").writeBy(user));
    }

    @DisplayName("save하면 id 자동 생성")
    @Test
    void save() {
        final Answer answer = repository.save(new Answer(user, question, "answer"));
        assertThat(answer.getId()).isNotNull();
    }

    @DisplayName("저장한 엔티티와 동일한 id로 조회한 엔티티는 동일성 보장")
    @Test
    void sameEntity() {
        final Answer saved = repository.save(new Answer(user, question, "answer"));
        final Answer answer = repository.findById(saved.getId()).get();
        assertAll(
                () -> assertThat(answer.getId()).isEqualTo(saved.getId()),
                () -> assertThat(answer).isEqualTo(saved),
                () -> assertThat(answer.getQuestion()).isEqualTo(saved.getQuestion()),
                () -> assertThat(answer.getWriter()).isEqualTo(saved.getWriter())
        );
    }

    @DisplayName("Answer에 대한 Question을 변경")
    @Test
    void setQuestion(@Autowired QuestionRepository questionRepository) {
        final Question savedQuestion = questionRepository.save(new Question("3", "3").writeBy(user));
        final Answer answer = new Answer(user, question, "answer");
        answer.setQuestion(savedQuestion);

        final Answer saved = repository.save(answer);
        final Question question = saved.getQuestion();
        assertAll(
                () -> assertThat(question).isEqualTo(savedQuestion),
                () -> assertThat(question.getTitle()).isEqualTo(new Title("3")),
                () -> assertThat(question.getContents()).isEqualTo(new Contents("3"))
        );
    }

    @DisplayName("지연 로딩으로 인한 초기화 여부 확인")
    @Test
    void lazyLoading(@Autowired QuestionRepository questionRepository,
                     @Autowired UserRepository userRepository,
                     @Autowired PlatformTransactionManager transactionManager) {
        final TransactionTemplate template = new TransactionTemplate(transactionManager);
        template.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        final Long id = template.execute(status -> {
            final User savedUser = userRepository.save(new User("lazyloading", "1234", "lazyloading",
                    "lazyloading@example.com"));
            final Question savedQuestion = questionRepository.save(new Question("question", "question")
                    .writeBy(savedUser));
            final Answer answer = new Answer(savedUser, savedQuestion, "answer");
            return repository.save(answer).getId();
        });
        final Question question = repository.getOne(id).getQuestion();
        assertThat(Hibernate.isInitialized(question)).isFalse();
    }
}
