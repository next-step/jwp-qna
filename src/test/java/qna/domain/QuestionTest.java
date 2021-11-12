package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import java.util.List;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@DataJpaTest
public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Autowired
    QuestionRepository questionRepository;

    @MockBean
    UserRepository userRepository;

    @BeforeEach
    void setUp() {
        목업_자바지기와_상지기_유저등록(UserTest.JAVAJIGI);
        목업_자바지기와_상지기_유저등록(UserTest.SANJIGI);
    }

    private void 목업_자바지기와_상지기_유저등록(final User user) {
        //given - save user
        given(userRepository.save(user)).willReturn(user);

        //when - save user
        userRepository.save(user);

        //then - save user
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void save() {
        //when
        final Question javajigiQuestion = questionRepository.save(Q1);
        final Question sanjigiQuestion = questionRepository.save(Q2);

        //then
        assertAll(
            () -> assertThat(javajigiQuestion).isNotNull(),
            () -> assertThat(javajigiQuestion.isOwner(UserTest.JAVAJIGI)).isTrue(),
            () -> assertThat(sanjigiQuestion).isNotNull(),
            () -> assertThat(sanjigiQuestion.isOwner(UserTest.SANJIGI)).isTrue()
        );
    }

    @Test
    void findByDeletedFalse() {
        //given
        final Question javajigiQuestion = questionRepository.save(Q1);
        final Question sanjigiQuestion = questionRepository.save(Q2);

        questionRepository.delete(sanjigiQuestion);

        //when
        final List<Question> actualQuestions = questionRepository.findByDeletedFalse();

        assertAll(
            () -> assertThat(actualQuestions).isNotEmpty(),
            () -> assertThat(actualQuestions).hasSize(1),
            () -> assertThat(actualQuestions)
                .extracting(question -> question.isOwner(UserTest.JAVAJIGI), Question::isDeleted)
                .containsExactly(new Tuple(true, false))
        );
    }

    @Test
    void findByIdAndDeletedFalse() {
        //given
        final Question javajigiQuestion = questionRepository.save(Q1);
        final Question sanjigiQuestion = questionRepository.save(Q2);

        questionRepository.delete(sanjigiQuestion);

        //when
        Question actualQuestion =
            questionRepository.findByIdAndDeletedFalse(javajigiQuestion.getId()).orElseThrow(RuntimeException::new);

        assertAll(
            () -> assertThat(actualQuestion.isOwner(UserTest.JAVAJIGI)).isTrue(),
            () -> assertThat(actualQuestion.isDeleted()).isFalse()
        );
    }
}
