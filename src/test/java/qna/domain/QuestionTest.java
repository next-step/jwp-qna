package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@EnableJpaAuditing
@DataJpaTest
public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void 질문_등록() {
        Question question = new Question("제목", "내용").writeBy(UserTest.JAVAJIGI);
        Question saved = questionRepository.save(question);
        assertThat(saved.getId()).isNotNull();
    }

    @Test
    void 질문_조회() {
        userRepository.save(UserTest.JAVAJIGI);
        Question actualQuestion = questionRepository.save(new Question("제목", "내용").writeBy(UserTest.JAVAJIGI));

        Question expected = questionRepository.findById(actualQuestion.getId()).get();
        assertThat(expected).isNotNull();
        assertThat(expected.getTitle()).isEqualTo("제목");
        assertThat(expected.getContents()).isEqualTo("내용");

        User actual = userRepository.findById(expected.getWriterId()).get();
        assertThat(actual).isEqualTo(UserTest.JAVAJIGI);
    }

    @Test
    void 질문_수정() {
        Question question1 = questionRepository.save(new Question("제목", "내용").writeBy(UserTest.SANJIGI));
        question1.updateTitle("수정 제목");

        Question question2 = questionRepository.findByTitle("수정 제목").get(0);
        assertThat(question2).isNotNull();
    }

    @Test
    void 질문_삭제_영속성_컨텍스트_DB() {
        Question question = questionRepository.save(new Question("제목", "내용").writeBy(UserTest.SANJIGI));
        List<Question> list = questionRepository.findByTitle("제목");
        assertThat(list).hasSize(1);

        questionRepository.delete(question);

        // 영속성 컨텍스트에서 '쓰기 지연 저장소'에서 DB로 SQL 쿼리를 실행하도록 키가 아닌 Title로 한번 조회한다.
        Question deleted = questionRepository.findByTitle("제목").get(0);

        // Soft Delete가 정상 동작하여 deleted 컬럼이 true로 업데이트된다.
        assertThat(deleted.isDeleted()).isTrue();
    }

    @Test
    void 질문_삭제_영속성_컨텍스트() {
        Question question1 = questionRepository.save(new Question("제목", "내용").writeBy(UserTest.SANJIGI));

        // Soft Delete로 deleted가 true가 되는 것을 기대한다.
        questionRepository.delete(question1);

        // 하지만 영속성 컨텍스트에서 가져온 데이터가 존재하지 않는다.
        Optional<Question> expected = questionRepository.findById(question1.getId());
        assertThat(expected).isEmpty();
    }
}
