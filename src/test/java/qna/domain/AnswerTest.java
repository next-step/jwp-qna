package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
public class AnswerTest {
    public static final Answer ANSWER1 = new Answer(UserTest.JAVAJIGI, QuestionTest.QUESTION1, "Answers Contents1");
    public static final Answer ANSWER2 = new Answer(UserTest.SANJIGI, QuestionTest.QUESTION1, "Answers Contents2");

    @Autowired
    AnswerRepository answerRepository;

    @Test
    @DisplayName("save 테스트")
    void saveTest() {
        // 데이터 테스트
        assertThat(ANSWER1.getId()).isNull();
        Answer actualAnswer1 = answerRepository.save(ANSWER1);
        assertThat(actualAnswer1.getId()).isNotNull(); // id 생성 테스트
        assertThat(actualAnswer1.getWriterId()).isEqualTo(ANSWER1.getWriterId());
        assertThat(actualAnswer1.getQuestionId()).isEqualTo(ANSWER1.getQuestionId());
        assertThat(actualAnswer1.getContents()).isEqualTo(ANSWER1.getContents());

        // 리스트 테스트
        Answer actualAnswer2 = answerRepository.save(ANSWER2);
        List<Answer> answerList = answerRepository.findAll();
        assertThat(answerList.size()).isEqualTo(2);
        assertThat(answerList).containsExactly(actualAnswer1, actualAnswer2);
    }

    @Test
    @DisplayName("question id 기준 검색 테스트")
    void findByQuestionIdAndDeletedFalseTest() {
        answerRepository.save(ANSWER1);
        answerRepository.save(ANSWER2);
        List<Answer> expectedList = answerRepository.findAll();
        List<Answer> actualList = answerRepository.findByQuestionIdAndDeletedFalse(QuestionTest.QUESTION1.getId());

        //findByQuestionId test
        assertThat(actualList.size()).isEqualTo(2);
        assertThat(actualList).isEqualTo(expectedList);

        //deleted false test
        for(Answer answer : actualList) {
            assertThat(answer.isDeleted()).isEqualTo(false);
        }
    }

    @Test
    @DisplayName("id 기준 검색 테스트")
    void findByIdAndDeletedFalseTest() {
        Answer expectedAnswer = answerRepository.save(ANSWER1);
        answerRepository.save(ANSWER2);

        Answer answer = answerRepository.findByIdAndDeletedFalse(expectedAnswer.getId())
                .orElseThrow(IllegalArgumentException::new);
        assertThat(answer).isEqualTo(expectedAnswer); // findById test
        assertThat(answer.isDeleted()).isEqualTo(false); //deleted false test

    }
}
