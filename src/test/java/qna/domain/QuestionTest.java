package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.List;

import org.assertj.core.api.AssertionsForInterfaceTypes;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import qna.CannotDeleteException;

public class QuestionTest extends BaseDomainTest<Question> {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Autowired
    QuestionRepository questions;

    @Autowired
    UserRepository users;

    @Autowired
    AnswerRepository answers;

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
        List<Question> 생성된_질문 = 질문_생성();

        List<Question> 질문목록 = questions.findAll();

        도메인_생성_검증(질문목록, 생성된_질문);
    }

    @Test
    void 도메인을_수정할_수_있다() {
        List<Question> 수정할_도메인 = 질문_생성();

        List<LocalDateTime> 최종_수정_일자 = 최종_수정_일자(수정할_도메인);

        도메인_수정(수정할_도메인);

        수정된_도메인_검증(수정할_도메인, 최종_수정_일자);
    }

    @Test
    void 생성날짜_수정날짜가_입력되어_있다() {
        List<Question> 도메인 = 질문_생성();
        도메인.forEach(this::생성날짜_수정날짜_검증);
    }

    @Test
    void 질문을_등록한_사용자를_조회할_수_있다() {
        Question 질문 = 질문_생성().get(0);
        User 작성자 = 작성자_생성("작성자1");
        질문.setWriter(작성자);
        flush();

        작성자 = 질문.getWriter();
        assertThat(작성자).isNotNull();
        assertThat(작성자.getQuestions()).contains(질문);
    }

    @Test
    void 사용자의_질문들을_조회할_수_있다() {
        User 작성자 = 작성자_생성("작성자");
        Question 질문1 = 질문_생성("질문1");
        Question 질문2 = 질문_생성("질문2");
        작성자.addQuestion(질문1);
        작성자.addQuestion(질문2);
        flush();

        List<Question> 사용자의_질문 = 작성자.getQuestions();

        AssertionsForInterfaceTypes.assertThat(사용자의_질문).isNotEmpty();
        AssertionsForInterfaceTypes.assertThat(사용자의_질문)
            .flatExtracting(Question::getWriter)
            .hasSize(2)
            .containsExactlyInAnyOrder(작성자, 작성자);
    }

    @Test
    void 질문의_작성자를_교체하면_이전_작성자는_해당_질문을_조회할_수_없다() {
        User 작성자1 = 작성자_생성("작성자1");
        Question 질문 = 질문_생성().get(0);
        질문.setWriter(작성자1);

        User 작성자2 = 작성자_생성("작성자2");
        질문.setWriter(작성자2);
        flush();
        assertThat(작성자2.getQuestions()).containsOnlyOnce(질문);
        assertThat(작성자1.getQuestions()).doesNotContain(질문);
    }

    @Test
    void 질문에_등록된_답변들을_조회할_수_있다() {
        User 작성자 = 작성자_생성("질문_작성자1");
        Question 질문 = 질문_생성().get(0);
        질문.setWriter(작성자);

        Answer 답변1 = 답변_생성("답변1", 질문);
        Answer 답변2 = 답변_생성("답변2", 질문);
        질문.addAnswer(답변1);
        질문.addAnswer(답변2);
        flush();

        assertThat(질문.getAnswers()).hasSize(2);
        assertThat(질문.getAnswers()).containsOnlyOnce(답변1, 답변2);
    }

    @Test
    void 답변을_등록한_사용자를_조회할_수_있다() {
        User 작성자 = 작성자_생성("작성자1");
        Answer 답변 = 답변_생성("답변1", 질문_생성("질문1"));

        답변.setWriter(작성자);
        flush();

        assertThat(답변.getWriter()).isEqualTo(작성자);
        assertThat(작성자.getAnswers()).contains(답변);
    }

    @Test
    void 사용자가_등록한_답변들을_조회할_수_있다() {
        User 사용자 = 작성자_생성("사용자1");
        Answer 답변1 = 답변_생성("답변1", 질문_생성("질문1"));
        Answer 답변2 = 답변_생성("답변2", 질문_생성("질문1"));

        사용자.addAnswer(답변1);
        사용자.addAnswer(답변2);
        flush();

        assertThat(답변1.getWriter()).isEqualTo(사용자);
        assertThat(답변2.getWriter()).isEqualTo(사용자);
        assertThat(사용자.getAnswers()).containsOnlyOnce(답변1, 답변2);
    }

    @Test
    void 질문은_로그인_사용자와_작성자가_같은_경우_삭제할_수_있다() {
        Question 질문1 = 질문_생성("질문1", "작성자1");
        User 작성자 = 질문1.getWriter();

        질문1.delete(작성자);

        질문_삭제됨(질문1);
    }

    @Test
    void 질문_삭제시_로그인_사용자와_작성자가_다른_경우_예외가_발생한다() {
        Question 질문 = 질문_생성("질문1", "작성자1");
        User 작성자2 = 작성자_생성("작성자2");

        assertThatThrownBy(() -> 질문.delete(작성자2))
            .isInstanceOf(CannotDeleteException.class);
    }

    @Test
    void 작성자가_질문_삭제시_답변이_존재할_경우_예외가_발생한다() {
        Question 질문 = 질문_생성("질문1", "작성자1");
        User 작성자 = 질문.getWriter();
        답변_생성("답변1", 질문, 작성자_생성("답변자1"));
        flush();

        assertThatThrownBy(() -> 질문.delete(작성자))
            .isInstanceOf(CannotDeleteException.class);
    }

    @Test
    void 질문과_답변의_모든_작성자가_동일할_경우_삭제가_가능하다() {
        Question 질문 = 질문_생성("질문1", "작성자1");
        User 작성자 = 질문.getWriter();
        답변_생성("답변1", 질문, 작성자);
        flush();

        질문.delete(작성자);

        질문_삭제됨(질문);
    }

    @Test
    void 질문자와_답변자가_다른_경우_답변을_삭제할_수_없다() {
        Question 질문 = 질문_생성("질문1", "작성자1");
        User 작성자 = 질문.getWriter();
        User 다른_작성자 = 작성자_생성("작성자2");
        답변_생성("답변1", 질문, 다른_작성자);
        flush();

        assertThatThrownBy(() -> 질문.delete(작성자))
            .isInstanceOf(CannotDeleteException.class);
    }

    private Question 질문_생성(String 질문_제목, String 작성자_이름) {
        Question 질문 = 질문_생성(질문_제목);
        User 작성자 = 작성자_생성(작성자_이름);
        작성자.addQuestion(질문);
        flush();
        return 질문;
    }

    private void 질문_삭제됨(Question 질문) {
        List<Question> 삭제되지_않은_질문 = questions.findByDeletedFalse();
        assertThat(삭제되지_않은_질문).doesNotContain(질문);
    }

    private Answer 답변_생성(String 내용, Question 질문) {
        return answers.save(new Answer(작성자_생성("답변_작성자1"), 질문, 내용));
    }

    private Answer 답변_생성(String 내용, Question 질문, User 답변자) {
        return answers.save(new Answer(답변자, 질문, 내용));
    }

    List<Question> 질문_생성() {
        return Lists.newArrayList(질문_생성("질문1"), 질문_생성("질문2"));
    }

    Question 질문_생성(String 제목) {
        return questions.save(질문(제목));
    }

    void 도메인_수정(List<Question> 수정할_도메인) {
        String 수정_제목 = "수정된 제목";
        String 수정_본문 = "수정된 본문";
        수정할_도메인.forEach(e -> e.modify(수정_제목, 수정_본문));
        questions.saveAll(수정할_도메인);
        questions.flush();
    }

    private User 작성자_생성(String 이름) {
        return users.save(UserTest.사용자(이름));
    }

    public static Question 질문(String 제목) {
        return new Question(제목, "내용");
    }

    void flush() {
        entityManager.flush();
        entityManager.clear();
    }
}
