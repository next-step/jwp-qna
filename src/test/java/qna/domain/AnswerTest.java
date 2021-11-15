package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@DataJpaTest
public class AnswerTest {

    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    UserRepository userRepository;

    @Test
    public void 답변저장() {
        //given
        User write = userRepository.save(TestUserFactory.create("donkey"));
        Question question = questionRepository.save(TestQuestionFactory.create("title", "content", write));
        Answer actual = answerRepository.save(TestAnswerFactory.create(write, question, "content"));
        Long savedId = actual.getId();

        //when
        Answer expected = answerRepository.findById(savedId).get();

        //then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void 답변저장_후_답변불러오기() {
        //given
        User write = userRepository.save(TestUserFactory.create("donkey"));
        Question question = questionRepository.save(TestQuestionFactory.create("title", "content", write));
        Answer actual = answerRepository.save(TestAnswerFactory.create(write, question, "content"));

        //when
        List<Answer> answerList = answerRepository.findAll();
        Answer expected = answerList.get(0);

        //then
        assertAll(
                () -> assertThat(actual.getId()).isEqualTo(expected.getId()),
                () -> assertThat(actual.getWriter()).isEqualTo(expected.getWriter()),
                () -> assertThat(actual.getQuestion()).isEqualTo(expected.getQuestion()),
                () -> assertThat(actual.getContents()).isEqualTo(expected.getContents())
        );
    }

    @Test
    public void 답변저장_후_삭제() {
        //given
        User write = userRepository.save(TestUserFactory.create("donkey"));
        Question question = questionRepository.save(TestQuestionFactory.create("title", "content", write));
        Answer actual = answerRepository.save(TestAnswerFactory.create(write, question, "content"));

        //when
        actual.setDeleted(true);

        //then
        assertThat(actual.isDeleted()).isTrue();
    }

    @Test
    public void 같은_내용이_포함되는_답변목록_조회() {
        //given
        User write = userRepository.save(TestUserFactory.create("donkey"));
        Question question = questionRepository.save(TestQuestionFactory.create("title", "content", write));
        Answer answers1 = TestAnswerFactory.create(write, question, "Answers");
        Answer answers2 = TestAnswerFactory.create(write, question, "Answers");
        answerRepository.save(answers1);
        answerRepository.save(answers2);
        String contents = "Answers";

        //when
        List<Answer> expected = answerRepository.findByContentsContains(contents);

        //then
        assertThat(expected).hasSize(2);
    }

}
