package qna.domain;

import org.junit.jupiter.api.BeforeEach;
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

    @BeforeEach
    void setUp() {
        userRepository.save(UserTest.JAVAJIGI);
        userRepository.save(UserTest.SANJIGI);
        questionRepository.save(QuestionTest.Q1);
    }

    @Test
    public void 답변저장() {
        //given
        Answer actual = answerRepository.save(A1);
        Long savedId = actual.getId();

        //when
        Answer expected = answerRepository.findById(savedId).get();

        //then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void 답변저장_후_답변불러오기() {
        //given
        Answer actual = answerRepository.save(A1);

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
        Answer actual = answerRepository.save(A1);

        //when
        actual.setDeleted(true);

        //then
        assertThat(actual.isDeleted()).isTrue();
    }

    @Test
    public void 같은_내용이_포함되는_답변목록_조회() {
        //given
        answerRepository.save(A1);
        answerRepository.save(A2);

        String contents = "Answers";

        //when
        List<Answer> expected = answerRepository.findByContentsContains(contents);

        //then
        assertThat(2).isEqualTo(expected.size());
    }

}
