package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

public class QuestionTest extends BaseDomainTest<Question> {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Autowired
    QuestionRepository questions;

    @Autowired
    UserRepository users;

    @Autowired
    TestEntityManager entityManager;

    @BeforeEach
    void setUp() {
        questions.saveAll(질문_생성());
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
        User 작성자 = 작성자_생성();
        질문.setWriter(작성자);
        flush();

        작성자 = 질문.getWriter();
        assertThat(작성자).isNotNull();
        assertThat(작성자.getQuestions()).contains(질문);
    }

    List<Question> 질문_생성() {
        return questions.saveAll(Lists.newArrayList(
            new Question("제목1", "본문1"),
            new Question("제목2", "본문2")
        ));
    }

    void 도메인_수정(List<Question> 수정할_도메인) {
        String 수정_제목 = "수정된 제목";
        String 수정_본문 = "수정된 본문";
        수정할_도메인.forEach(e -> e.modify(수정_제목, 수정_본문));
        questions.saveAll(수정할_도메인);
        questions.flush();
    }

    private User 작성자_생성() {
        return users.save(UserTest.사용자("작성자1"));
    }

    void flush() {
        entityManager.flush();
        entityManager.clear();
    }

    public static Question 질문(String 제목) {
        return new Question(제목, "내용");
    }
}
