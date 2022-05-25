package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.CannotDeleteException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class QuestionRepositoryTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserRepositoryTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserRepositoryTest.SANJIGI);

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void 질문_등록() {
        final User user = userRepository.save(createTestUser());
        final Question question = new Question("제목", "내용").writeBy(user);

        final Question saved = questionRepository.save(question);

        assertThat(saved.getId()).isNotNull();
    }

    @Test
    void 질문_조회() {
        final User user = userRepository.save(createTestUser());
        final Question question1 = questionRepository.save(new Question("제목", "내용").writeBy(user));

        final Question question2 = questionRepository.findById(question1.getId()).get();

        assertThat(question2).isNotNull();
        assertThat(question2.getTitle()).isEqualTo("제목");
        assertThat(question2.getContents()).isEqualTo("내용");
        assertThat(question2.getWriter()).isEqualTo(user);
    }

    @Test
    void 질문_수정() {
        final User user = userRepository.save(createTestUser());
        final Question question1 = questionRepository.save(new Question("제목", "내용").writeBy(user));

        question1.updateTitle("수정 제목");

        final Question question2 = questionRepository.findByTitle("수정 제목").get(0);
        assertThat(question2).isNotNull();
    }

    @Test
    void 질문_삭제() {
        final User user = userRepository.save(createTestUser());
        final Question question = questionRepository.save(new Question("제목", "내용").writeBy(user));

        questionRepository.delete(question);

        final Optional<Question> actual = questionRepository.findById(question.getId());
        assertThat(actual.isPresent()).isFalse();
    }

    @Test
    void 질문_삭제_DELETED() throws CannotDeleteException {
        final User user = userRepository.save(createTestUser());
        final Question question = questionRepository.save(new Question("제목", "내용").writeBy(user));

        question.delete(user);

        final Question actual = questionRepository.findById(question.getId()).get();
        assertThat(actual.isDeleted()).isTrue();
    }

    @Test
    void 질문_댓글_추가_테스트() {
        final User user = userRepository.save(createTestUser());
        final Question question = questionRepository.save(new Question("제목", "내용").writeBy(user));
        question.addAnswer(new Answer(user, question, "댓글 추가"));
        question.addAnswer(new Answer(user, question, "댓글 추가"));

        final Question actual = questionRepository.findByIdAndDeletedFalse(question.getId()).get();

        assertThat(actual.getAnswers().getList()).hasSize(2);
    }

    private User createTestUser() {
        return new User("donghee.han", "password", "donghee", "donghee.han@slipp.net");
    }
}
