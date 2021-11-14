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
        User write = userRepository.save(TestUserFactory.create("donkey"));
        Question actual = questionRepository.save(TestQuestionFactory.create("title", "content", write));
        Long savedId = actual.getId();

        //when
        Question expected = questionRepository.findById(savedId).get();

        //then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void 질문_저장_후_질문불러오기() {
        //given
        User write = userRepository.save(TestUserFactory.create("donkey"));
        Question actual = questionRepository.save(TestQuestionFactory.create("title", "content", write));

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
    public void 질문_저장_후_삭제() {
        //given
        User write = userRepository.save(TestUserFactory.create("donkey"));
        Question actual = questionRepository.save(TestQuestionFactory.create("title", "content", write));

        //when
        actual.setDeleted(true);

        //then
        assertThat(actual.isDeleted()).isTrue();
    }

    @Test
    public void 제목에_같은_단어가_포함되는_질문_목록_조회() {
        //given
        User write = userRepository.save(TestUserFactory.create("donkey"));
        questionRepository.save(TestQuestionFactory.create("title", "content", write));
        questionRepository.save(TestQuestionFactory.create("title", "content", write));

        String title = "title";

        //when
        List<Question> expected = questionRepository.findByTitleContains(title);

        //then
        assertThat(2).isEqualTo(expected.size());
    }

}
