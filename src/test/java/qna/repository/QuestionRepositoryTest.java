package qna.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Example;
import qna.domain.Answer;
import qna.domain.Question;
import qna.domain.User;

@DataJpaTest
public class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository questionRepository;

    @Test
    @DisplayName("저장")
    void save() {
        //Given
        User questioner = new User("test1", "1234", "홍길동", "hong@test.com");

        //When
        Question question = new Question("질문있어요", "내용입니다.", questioner);
        Question savedQuestion = questionRepository.save(question);

        //Then
        assertAll(
            () -> assertThat(savedQuestion).isNotNull(),
            () -> assertThat(savedQuestion).isEqualTo(question)
        );
    }

    @Test
    @DisplayName("단건조회 (getOne)")
    void getOne() {
        //Given
        User questioner = new User("test1", "1234", "홍길동", "hong@test.com");
        Question question = new Question("질문있어요", "내용입니다.", questioner);
        questionRepository.save(question);

        //When
        Question savedQuestion = questionRepository.getOne(question.getId());

        //Then
        assertAll(
            () -> assertThat(savedQuestion).isNotNull(),
            () -> assertThat(savedQuestion).isEqualTo(question)
        );
    }

    @Test
    @DisplayName("단건조회시 (getOne) 데이터 없을경우 참조(lazyLoad)하는 시점에 예외발생")
    void getOne_null_lazy() {
        //Given
        User questioner = new User("test1", "1234", "홍길동", "hong@test.com");
        Question question = new Question("질문있어요", "내용입니다.", questioner);
        questionRepository.save(question);

        //Then
        Question savedQuestion = questionRepository.getOne(12345L);

        //Then
        assertThatThrownBy(() -> System.out.println(savedQuestion))
            .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("단건조회 (findOne)")
    void findOne() {
        //Given
        User questioner = new User("test1", "1234", "홍길동", "hong@test.com");
        Question question = new Question("질문있어요", "내용입니다.", questioner);
        questionRepository.save(question);

        //When
        Question findQuestion = new Question(question.getTitle(), question.getContents());
        Question savedQuestion = questionRepository.findOne(Example.of(findQuestion)).get();

        //Then
        assertAll(
            () -> assertThat(savedQuestion).isNotNull(),
            () -> assertThat(savedQuestion).isEqualTo(question)
        );
    }

    @Test
    @DisplayName("단건조회 (findById)")
    void findById() {
        //Given
        User questioner = new User("test1", "1234", "홍길동", "hong@test.com");
        Question question = new Question("질문있어요", "내용입니다.", questioner);
        questionRepository.save(question);

        //When
        Question savedQuestion = questionRepository.findById(question.getId()).get();

        //Then
        assertAll(
            () -> assertThat(savedQuestion).isNotNull(),
            () -> assertThat(savedQuestion).isEqualTo(question)
        );
    }

    @Test
    @DisplayName("전체 리스트 조회 (findAll)")
    void findAll() {
        //Given
        User questioner = new User("test1", "1234", "홍길동", "hong@test.com");
        List<Question> questions = new ArrayList<>();
        questions.add(new Question("질문있어요1", "내용입니다1", questioner));
        questions.add(new Question("질문있어요2", "내용입니다2", questioner));
        questions.add(new Question("질문있어요3", "내용입니다3", questioner));
        questionRepository.saveAll(questions);

        //When
        List<Question> savedQuestions = questionRepository.findAll();

        //Then
        assertThat(savedQuestions).hasSize(3);
    }

    @Test
    @DisplayName("전체 리스트 조회 (삭제여부가 false인것)")
    void findByDeletedFalse() {
        //Given
        User questioner = new User("test1", "1234", "홍길동", "hong@test.com");
        List<Question> questions = new ArrayList<>();
        questions.add(new Question("질문있어요1", "내용입니다1", questioner));
        questions.add(new Question("질문있어요2", "내용입니다2", questioner));
        questions.add(new Question("질문있어요3", "내용입니다3", questioner).setDeleted(true));
        questionRepository.saveAll(questions);

        //When
        List<Question> savedQuestions = questionRepository.findByDeletedFalse();

        //Then
        assertThat(savedQuestions).hasSize(2);
    }

    @Test
    @DisplayName("리스트 조회 (findBy)")
    void findBy() {
        //Given
        User questioner = new User("test1", "1234", "홍길동", "hong@test.com");
        List<Question> questions = new ArrayList<>();
        questions.add(new Question("질문있어요1", "내용입니다1", questioner));
        questions.add(new Question("질문있어요2", "내용입니다2", questioner));
        questions.add(new Question("질문있어요3", "내용입니다3", questioner));
        questionRepository.saveAll(questions);

        //When
        List<Question> savedQuestions = questionRepository.findByTitleAndContents("질문있어요1", "내용입니다1");

        //Then
        assertThat(savedQuestions).hasSize(1);
    }

    @Test
    @DisplayName("카운트")
    void count() {
        //Given
        User questioner = new User("test1", "1234", "홍길동", "hong@test.com");
        Question question = new Question("질문있어요1", "내용입니다1", questioner);
        questionRepository.save(question);

        //When
        long count = questionRepository.count(Example.of(new Question(question.getTitle(), question.getContents())));

        //Then
        assertThat(count).isEqualTo(1);
    }

    @Test
    @DisplayName("카운트 (countBy)")
    void countBy() {
        //Given
        User questioner = new User("test1", "1234", "홍길동", "hong@test.com");
        Question question = new Question("질문있어요1", "내용입니다1", questioner);
        questionRepository.save(question);

        //When
        long count = questionRepository.countById(question.getId());

        //Then
        assertThat(count).isEqualTo(1);
    }

    @Test
    @DisplayName("수정")
    void update() {
        //Given
        User questioner = new User("test1", "1234", "홍길동", "hong@test.com");
        Question question = new Question("질문있어요", "내용입니다.", questioner);
        questionRepository.save(question);

        //When
        Question savedQuestion = questionRepository.findById(question.getId()).get();
        savedQuestion.setTitle("제목 변경");

        //Then
        Question actual = questionRepository.findById(savedQuestion.getId()).get();
        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getTitle()).isEqualTo(savedQuestion.getTitle())
        );
    }

    @Test
    @DisplayName("삭제")
    void delete() {
        //Given
        User questioner = new User("test1", "1234", "홍길동", "hong@test.com");
        Question question = new Question("질문있어요", "내용입니다.", questioner);
        questionRepository.save(question);

        //When
        questionRepository.delete(question);

        //Then
        assertThat(questionRepository.findById(question.getId()).isPresent()).isFalse();
    }

    @Test
    @DisplayName("writer 연관관계")
    void relation_writer() {
        //Given
        User questioner = new User("test1", "1234", "홍길동", "hong@test.com");
        Question question = new Question("질문있어요", "내용입니다.", questioner);
        questionRepository.save(question);

        //When
        Question savedQuestion = questionRepository.getOne(question.getId());
        User writer = savedQuestion.getWriter();

        //Then
        assertAll(
            () -> assertThat(writer).isNotNull(),
            () -> assertThat(writer).isEqualTo(questioner)
        );
    }

    @Test
    @DisplayName("answer 연관관계")
    void relation_answer() {
        //Given
        User questioner = new User("test1", "1234", "홍길동", "hong@test.com");
        User answerer = new User("test2", "1234", "아무개", "anyone@test.com");
        Question question = new Question("질문있어요", "내용입니다.", questioner);
        Answer answer = new Answer(answerer, question, "답변내용입니다2");
        question.addAnswer(answer);

        questionRepository.save(question);

        //When
        Question savedQuestion = questionRepository.findById(question.getId()).get();

        List<Answer> answers = savedQuestion.getAnswers().undeletedAnswers();

        //Then
        assertAll(
            () -> assertThat(answers).isNotEmpty(),
            () -> assertThat(answers).hasSize(1)
        );
    }

}
