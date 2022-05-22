package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class DeleteHistoryRepositoryTest {

    @Autowired
    private DeleteHistoryRepository repository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void 삭제_이력_저장() {
        final User user = userRepository.save(createTestUser());
        final DeleteHistory deleteHistory = new DeleteHistory(ContentType.QUESTION, 1L, LocalDateTime.now()).deleteBy(user);
        final DeleteHistory expected = repository.save(deleteHistory);

        assertThat(expected).isNotNull();
    }

    @Test
    void 삭제_이력_조회() {
        final User user = userRepository.save(createTestUser());
        final DeleteHistory deleteHistory = new DeleteHistory(ContentType.QUESTION, 1L, LocalDateTime.now()).deleteBy(user);
        final DeleteHistory expected = repository.save(deleteHistory);

        final Optional<DeleteHistory> actual = repository.findById(expected.getId());

        assertThat(actual).isNotEmpty();
    }

    @Test
    void 질문_삭제_이력() {
        final User user = userRepository.save(createTestUser());
        final Question question = new Question(1L, "질문", "내용");
        final DeleteHistory deleteHistory = new DeleteHistory(ContentType.QUESTION, question.getId(), LocalDateTime.now()).deleteBy(user);

        repository.save(deleteHistory);

        Optional<DeleteHistory> actual = repository.findByContentId(question.getId());
        assertThat(actual).isNotEmpty();
    }

    @Test
    void 댓글_삭제_이력() {
        final User user = userRepository.save(new User("donghee", "password", "donghee", "donghee.han@slipp.net"));
        final Question question = questionRepository.save(new Question("제목", "내용")).writeBy(user);
        final Answer answer = answerRepository.save(new Answer("댓글 내용").toQuestion(question).writeBy(user));

        repository.save(new DeleteHistory(ContentType.ANSWER, answer.getId(), LocalDateTime.now())).deleteBy(user);

        Optional<DeleteHistory> actual = repository.findByContentId(answer.getId());
        assertThat(actual).isNotEmpty();
    }

    private User createTestUser() {
        return new User("donghee.han", "password", "donghee", "donghee.han@slipp.net");
    }
}

