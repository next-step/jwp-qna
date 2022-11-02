package qna.domain;

import java.time.LocalDateTime;
import java.util.List;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class AnswerTest extends BaseDomainTest<Answer> {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Autowired
    AnswerRepository answers;
    @Autowired
    UserRepository users;
    @Autowired
    QuestionRepository questions;

    @BeforeEach
    void setUp() {
        answers.deleteAll();
        questions.deleteAll();
        users.deleteAll();
    }

    @Test
    void 도메인을_생성할_수_있다() {
        List<Answer> 생성된_답변 = 유저_생성();

        List<Answer> 답변목록 = answers.findAll();

        도메인_생성_검증(생성된_답변, 답변목록);
    }

    @Test
    void 도메인을_수정할_수_있다() {
        List<Answer> 수정할_도메인 = 유저_생성();

        List<LocalDateTime> 최종_수정_일자 = 최종_수정_일자(수정할_도메인);

        도메인_수정(수정할_도메인);

        수정된_도메인_검증(수정할_도메인, 최종_수정_일자);
    }

    @Test
    void 생성날짜_수정날짜가_입력되어_있다() {
        List<Answer> 도메인 = 유저_생성();
        도메인.forEach(this::생성날짜_수정날짜_검증);
    }

    List<Answer> 유저_생성() {
        User 유저1 = 유저_생성("유저1");
        Question 질문 = 질문_생성();
        return answers.saveAll(Lists.newArrayList(
            new Answer(유저1, 질문, "본문1"),
            new Answer(유저1, 질문, "본문2")
        ));
    }

    private Question 질문_생성() {
        return questions.save(QuestionTest.질문_생성("질문1"));
    }

    private User 유저_생성(String 유저_아이디) {
        return users.save(UserTest.유저_생성(유저_아이디));
    }

    void 도메인_수정(List<Answer> 수정할_도메인) {
        String 수정_본문 = "수정된 본문";
        수정할_도메인.forEach(e -> e.modify(수정_본문));
        answers.saveAll(수정할_도메인);
        answers.flush();
    }
}
