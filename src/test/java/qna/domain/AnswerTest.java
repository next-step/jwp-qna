package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Autowired
    private AnswerRepository answerRepository;

    private Answer answer1;
    private Answer answer2;

    @BeforeEach
    void saveDefaultAnswer() {
        answer1 = answerRepository.save(A1);
        answer2 = answerRepository.save(A2);
    }

    @Test
    @DisplayName("답변 등록")
    void save() {
        Answer actual = answer1;
        assertAll(
                () -> assertThat(actual).isNotNull(),
                () -> assertThat(actual.getId()).isEqualTo(A1.getId())
        );
    }
    
    /*
    1. Update : Contents 없데이터
    2. Question을 이용한 조회 및 조회 수 가져오기
    3. 상세 조회
    4. 삭제
        - Question 삭제 시 하위에 있는 Answer도 삭제 테스트
     */

    @Test
    @DisplayName("질문 수정")
    void update() {
        String contents = "질문 내용을 수정합니다.";
        answer1.setContents(contents);
        Answer actual = answerRepository.save(answer1);
        assertAll(
                () -> assertThat(actual).isNotNull(),
                () -> assertThat(actual.getContents()).isEqualTo(contents)
        );
    }
}
