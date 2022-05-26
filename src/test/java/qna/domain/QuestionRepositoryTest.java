package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class QuestionRepositoryTest extends BaseRepositoryTest {
    @Autowired
    QuestionRepository questionRepository;

    Question savedQ1;
    Question savedQ2;

    @BeforeEach
    void setUp() {
        // given
        saveUsers();
        savedQ1 = questionRepository.save(new Question("title1", "contents1").writeBy(savedJavajigi));
        savedQ2 = questionRepository.save(new Question("title2", "contents2").writeBy(savedSanjigi));
    }

    @Test
    @DisplayName("Question 저장 성공")
    void save() {
        // when
        final Question question = questionRepository.save(new Question("제목", "내용"));

        // then
        assertThat(question).isNotNull();
    }

    @ParameterizedTest
    @MethodSource("saveExceptionArgs")
    @DisplayName("Question 저장시 제약조건 으로 인한 저장 실패 케이스")
    void save_exception(Question expected) {
        // when & then
        assertThatThrownBy(() -> questionRepository.save(expected))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    static Stream<Arguments> saveExceptionArgs() {
        return Stream.of(
                Arguments.of(new Question(
                        "htvxfmpqhocuxcbnzcjvsuaiugucnfmmstrnaqvqbcipkvnrmfwzpyemyfgsxqqqsiinbmivuvrqpzvnwdilfnhnohuwcrewigxyzzhtjbrdcywdvsnuvmjspwycmogvdmbkfuamczyoncupyixmivoyefvpwwvgfzmdhvmrdydkvyeuttwqqkpkylniakpxuxnajfzuxawqhspwwpijcskfoynjabpcrjntdgigduasptxssjppojqdnuhtvxfmpqhocuxcbnzcjvsuaiugucnfmmstrnaqvqbcipkvnrmfwzpyemyfgsxqqqsiinbmivuvrqpzvnwdilfnhnohuwcrewigxyzzhtjbrdcywdvsnuvmjspwycmogvdmbkfuamczyoncupyixmivoyefvpwwvgfzmdhvmrdydkvyeuttwqqkpkylniakpxuxnajfzuxawqhspwwpijcskfoynjabpcrjntdgigduasptxssjppojqdnu",
                        "테스트"))
        );
    }

    @Test
    @DisplayName("deleted = false 인 question 리스트 조회")
    void findByDeletedFalse() {
        // when
        final List<Question> questionList = questionRepository.findByDeletedFalse();

        // then
        assertThat(questionList).hasSize(2);
    }

    @Test
    @DisplayName("id 와 deleted = false 인 question 조회")
    void findByIdAndDeletedFalse() {
        // when & then
        assertThat(questionRepository.findByIdAndDeletedFalse(savedQ1.getId())).isNotEmpty();
    }

    @Test
    @DisplayName("writer_id 변경")
    void update() {
        // when
        savedQ1.writeBy(savedJavajigi);

        // then
        assertThat(savedQ1.getWriter()).isEqualTo(savedJavajigi);
    }

    @Test
    @DisplayName("Question 삭제")
    void delete() {
        // when
        questionRepository.delete(savedQ1);

        // then
        assertThat(questionRepository.findById(savedQ1.getId())).isEmpty();
    }
}
