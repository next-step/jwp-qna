package qna.domain;

import org.hibernate.Hibernate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import qna.config.QnaDataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static qna.domain.FixtureUser.HEOWC;

@QnaDataJpaTest
class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository repository;

    private User user;

    @BeforeEach
    void setUp(@Autowired UserRepository userRepository) {
        userRepository.deleteAll();
        user = userRepository.save(new User("question", "1234", "question", "question@example.com"));
    }

    @DisplayName("save하면 id 자동 생성")
    @Test
    void save() {
        final Question question = repository.save(new Question("question", "question").writeBy(user));
        assertThat(question.getId()).isNotNull();
    }

    @DisplayName("저장한 엔티티와 동일한 id로 조회한 엔티티는 동일성 보장")
    @Test
    void sameEntity() {
        final Question saved = repository.save(new Question("question", "question").writeBy(user));
        final Question question = repository.findById(saved.getId()).get();
        assertAll(
                () -> assertThat(saved.getId()).isEqualTo(question.getId()),
                () -> assertThat(saved).isEqualTo(question),
                () -> assertThat(saved.getWriter()).isEqualTo(question.getWriter())
        );
    }

    @DisplayName("Question에 대한 User 변경")
    @Test
    void writeBy() {
        final Question question = new Question("question", "question").writeBy(user);
        final Question saved = repository.save(question.writeBy(HEOWC));
        assertThat(saved.getWriter()).isEqualTo(HEOWC);
    }

    @DisplayName("지연 로딩으로 인한 초기화 여부 확인")
    @Test
    void lazyLoading(@Autowired UserRepository userRepository,
                     @Autowired PlatformTransactionManager transactionManager) {
        final TransactionTemplate template = new TransactionTemplate(transactionManager);
        template.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        final Long id = template.execute(status -> {
            final User savedUser = userRepository.save(new User("lazyloading", "1234", "lazyloading",
                                                         "lazyloading@example.com"));
            final Question question = new Question("question", "question").writeBy(savedUser);
            return repository.save(question).getId();
        });
        final List<Answer> answers = repository.getOne(id).getAnswers();
        assertThat(Hibernate.isInitialized(answers)).isFalse();
    }
}
