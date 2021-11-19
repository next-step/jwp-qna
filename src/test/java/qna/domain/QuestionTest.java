package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import javax.persistence.EntityManager;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Autowired
    EntityManager entityManager;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    UserRepository userRepository;

    @Test
    void save() {
        //given
        final User javajigi = 유저등록(UserTest.JAVAJIGI);
        final User sanjigi = 유저등록(UserTest.SANJIGI);

        //when
        final Question javajigiQuestion = questionRepository.save(Q1.writeBy(javajigi));
        final Question sanjigiQuestion = questionRepository.save(Q2.writeBy(sanjigi));

        System.out.println(javajigiQuestion);
        System.out.println(sanjigiQuestion);

        //then
        assertAll(
            () -> assertThat(javajigiQuestion).isNotNull(),
            () -> assertThat(javajigiQuestion.isOwner(javajigi)).isTrue(),
            () -> assertThat(sanjigiQuestion).isNotNull(),
            () -> assertThat(sanjigiQuestion.isOwner(sanjigi)).isTrue()
        );
    }

    @Test
    void findByDeletedFalse() {
        //given
        final User javajigi = 유저등록(UserTest.JAVAJIGI);
        final User sanjigi = 유저등록(UserTest.SANJIGI);

        final Question javajigiQuestion = questionRepository.save(Q1.writeBy(javajigi));
        final Question sanjigiQuestion = questionRepository.save(Q2.writeBy(sanjigi));

        questionRepository.delete(sanjigiQuestion);

        //when
        final List<Question> actualQuestions = questionRepository.findByDeletedFalse();

        assertAll(
            () -> assertThat(actualQuestions).isNotEmpty(),
            () -> assertThat(actualQuestions).hasSize(1),
            () -> assertThat(actualQuestions)
                .extracting(question -> question.isOwner(javajigi), Question::isDeleted)
                .containsExactly(new Tuple(true, false))
        );
    }

    @Test
    void jpaAuditing() {
        //given
        final User user = 유저등록(UserTest.JAVAJIGI);

        assertDoesNotThrow(() -> {
            //when then - @CreatedDate 테스트
            final Question question = questionRepository.save(Q1.writeBy(user));
            entityManager.detach(question);

            //when
            final Question changeQuestion =
                questionRepository.findById(question.getId()).orElseThrow(RuntimeException::new);
            changeQuestion.change("내용변경", "제목변경");
            entityManager.flush();

            //then - @LastModifiedDate 테스트
            assertThat(changeQuestion).isNotEqualTo(question);
        });
    }

    @Test
    void findByIdAndDeletedFalse() {
        //given
        final User javajigi = 유저등록(UserTest.JAVAJIGI);
        final User sanjigi = 유저등록(UserTest.SANJIGI);

        final Question javajigiQuestion = questionRepository.save(Q1.writeBy(javajigi));
        final Question sanjigiQuestion = questionRepository.save(Q2.writeBy(sanjigi));

        questionRepository.delete(sanjigiQuestion);

        //when
        Question actualQuestion =
            questionRepository.findByIdAndDeletedFalse(javajigiQuestion.getId()).orElseThrow(RuntimeException::new);

        assertAll(
            () -> assertThat(actualQuestion.isOwner(javajigi)).isTrue(),
            () -> assertThat(actualQuestion.isDeleted()).isFalse()
        );
    }

    private User 유저등록(final User user) {
        return userRepository.save(user);
    }
}
