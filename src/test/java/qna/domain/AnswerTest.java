package qna.domain;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import java.time.LocalDateTime;
import java.util.List;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

public class AnswerTest extends BaseDomainTest<Answer> {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Autowired
    AnswerRepository answers;
    @Autowired
    UserRepository users;
    @Autowired
    QuestionRepository questions;

    @Autowired
    TestEntityManager entityManager;

    @BeforeEach
    void setUp() {
        answers.deleteAll();
        questions.deleteAll();
        users.deleteAll();
        flush();
    }

    @Test
    void 도메인을_생성할_수_있다() {
        List<Answer> 생성된_답변 = 작성자_생성();

        List<Answer> 답변목록 = answers.findAll();

        도메인_생성_검증(생성된_답변, 답변목록);
    }

    @Test
    void 도메인을_수정할_수_있다() {
        List<Answer> 수정할_도메인 = 작성자_생성();

        List<LocalDateTime> 최종_수정_일자 = 최종_수정_일자(수정할_도메인);

        도메인_수정(수정할_도메인);

        수정된_도메인_검증(수정할_도메인, 최종_수정_일자);
    }

    @Test
    void 생성날짜_수정날짜가_입력되어_있다() {
        List<Answer> 도메인 = 작성자_생성();
        도메인.forEach(this::생성날짜_수정날짜_검증);
    }

    @Test
    void 답변의_질문을_조회할_수_있다() {
        Answer 답변 = 답변_생성("답변");
        Question 질문 = 질문_생성("질문1");
        답변.toQuestion(질문);
        flush();

        assertThat(답변.getQuestion()).isEqualTo(질문);
        assertThat(질문.getAnswers()).containsOnlyOnce(답변);
    }

    @Test
    void 답변의_질문을_교체하면_이전_질문의_해당_답변은_조회할_수_없다() {
        Answer 답변 = 답변_생성("답변1");

        Question 질문1 = 질문_생성("질문1");
        Question 질문2 = 질문_생성("질문2");

        답변.toQuestion(질문1);
        답변.toQuestion(질문2);
        flush();

        assertThat(답변.getQuestion()).isEqualTo(질문2);
        assertThat(질문1.getAnswers()).doesNotContain(답변);
    }

    @Test
    void 답변의_작성자를_교체하면_이전_작성자는_해당_답변을_조회할_수_없다() {
        Answer 답변 = 답변_생성("답변1");
        User 이전_작성자 = 작성자_생성("작성자1");
        User 교체한_작성자 = 작성자_생성("작성자2");

        답변.setWriter(이전_작성자);
        답변.setWriter(교체한_작성자);

        assertThat(답변.getWriter()).isEqualTo(교체한_작성자);
        assertThat(이전_작성자.getAnswers()).doesNotContain(답변);
    }

    public static Answer 답변(String 제목, Question 질문, User 작성자) {
        return new Answer(작성자, 질문, 제목);
    }

    private Answer 답변_생성(String 내용) {
        return answers.save(답변(내용));
    }

    private Answer 답변(String 내용) {
        return new Answer(작성자_생성("유저1"), 질문_생성("질문1"), 내용);
    }

    List<Answer> 작성자_생성() {
        User 유저1 = 작성자_생성("유저1");
        Question 질문 = 질문_생성("질문1");
        return answers.saveAll(Lists.newArrayList(
            new Answer(유저1, 질문, "본문1"),
            new Answer(유저1, 질문, "본문2")
        ));
    }

    void 도메인_수정(List<Answer> 수정할_도메인) {
        String 수정_본문 = "수정된 본문";
        수정할_도메인.forEach(e -> e.modify(수정_본문));
        answers.saveAll(수정할_도메인);
        answers.flush();
    }

    private Question 질문_생성(String 제목) {
        return questions.save(QuestionTest.질문(제목));
    }

    private User 작성자_생성(String 이름) {
        return users.save(UserTest.사용자(이름));
    }

    private void flush() {
        entityManager.flush();
        entityManager.clear();
    }
}
