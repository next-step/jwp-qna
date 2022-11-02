package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class QuestionTest {

    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Autowired
    private QuestionRepository questionRepository;
    private List<Question> expectList;

    @BeforeEach
    void 저장() {
        questionRepository.save(Q1);
        questionRepository.save(Q2);
    }

    @Test
    void 조회() {
        expectList = questionRepository.findAll();
        assertAll(
            () -> assertThat(expectList).isNotNull(),
            () -> assertThat(expectList.size()).isEqualTo(2),
            () -> assertThat(questionRepository.findByContents("contents1").getWriterId()).isEqualTo(UserTest.JAVAJIGI.getId()),
            () -> assertThat(questionRepository.findByContents("contents2").getWriterId()).isEqualTo(UserTest.SANJIGI.getId())
        );
    }

    @Test
    void 수정() {
        Question expect = questionRepository.findByContents("contents1");
        expect.setWriterId(UserTest.SANJIGI.getId());

        assertThat(questionRepository.save(expect).getWriterId()).isEqualTo(UserTest.SANJIGI.getId());
    }

    @Test
    void 삭제() {
        questionRepository.deleteAll();

        assertThat(questionRepository.findAll().size()).isEqualTo(0);
    }
}
