package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class AnswerTest {

    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    EntityManager em;

    @Test
    public void 답변저장() {
        //given
        Answer actual = answerRepository.save(A1);
        Long savedId = actual.getId();
        em.clear();

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
                () -> assertThat(actual.getWriterId()).isEqualTo(expected.getWriterId()),
                () -> assertThat(actual.getQuestionId()).isEqualTo(expected.getQuestionId()),
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
