package qna.domain;

import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import qna.CannotDeleteException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@DataJpaTest
public class QuestionTest {

    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    UserRepository userRepository;

    @Test
    public void 질문_저장() {
        //given
        User writer = userRepository.save(TestUserFactory.create("donkey"));
        Question actual = questionRepository.save(TestQuestionFactory.create("title", "content", writer));
        Long savedId = actual.getId();

        //when
        Question expected = questionRepository.findById(savedId).get();

        //then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void 질문_저장_후_질문불러오기() {
        //given
        User writer = userRepository.save(TestUserFactory.create("donkey"));
        Question actual = questionRepository.save(TestQuestionFactory.create("title", "content", writer));

        //when
        List<Question> questionList = questionRepository.findAll();
        Question expected = questionList.get(0);

        //then
        assertAll(
                () -> assertThat(actual.getId()).isEqualTo(expected.getId()),
                () -> assertThat(actual.getWriter()).isEqualTo(expected.getWriter()),
                () -> assertThat(actual.getTitle()).isEqualTo(expected.getTitle()),
                () -> assertThat(actual.getContents()).isEqualTo(expected.getContents())
        );
    }

    @Test
    public void 질문_저장_후_삭제() throws CannotDeleteException {
        //given
        User writer = userRepository.save(TestUserFactory.create("donkey"));
        Question actual = questionRepository.save(TestQuestionFactory.create("title", "content", writer));

        //when
        actual.delete(writer);

        //then
        assertThat(actual.isDeleted()).isTrue();
    }

    @Test
    public void 질문한_사람이_로그인_사용자가_아닌경우엔_삭제할_수_없다() {
        //given
        User writer = userRepository.save(TestUserFactory.create("donkey"));
        Question actual = questionRepository.save(TestQuestionFactory.create("title", "content", writer));

        //when
        ThrowableAssert.ThrowingCallable throwingCallable = () -> actual.delete(TestUserFactory.create("donkey2"));

        //then
        assertThatExceptionOfType(CannotDeleteException.class)
                .isThrownBy(throwingCallable);
    }

    @Test
    public void 답변자이_없는_경우_삭제할_수_없다() throws CannotDeleteException {
        //given
        User writer = userRepository.save(TestUserFactory.create("donkey"));
        Question question = questionRepository.save(TestQuestionFactory.create("title", "content", writer));
        question.delete(writer);

        //when
        Question deletedQuestion = questionRepository.findByIdAndDeletedTrue(question.getId()).orElse(null);

        //then
        assertThat(deletedQuestion).isNotNull();
    }

}
