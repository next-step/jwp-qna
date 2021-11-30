package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;
import qna.domain.field.Title;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class QuestionTest {

    private static final Logger _log = LoggerFactory.getLogger(QuestionTest.class);

    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    private Question question1;
    private Question question2;

    @BeforeEach
    public void saveDefaultQuestions() {
        question1 = questionRepository.save(Q1);
        question2 = questionRepository.save(Q2);
        answerRepository.save(new Answer(1L, UserTest.JAVAJIGI, question1, "Answers Contents1"));
        answerRepository.save(new Answer(2L, UserTest.SANJIGI, question1, "Answers Contents2"));
        answerRepository.save(new Answer(3L, UserTest.SANJIGI, question2, "Answers Contents3"));
        answerRepository.save(new Answer(4L, UserTest.SANJIGI, question2, "Answers Contents4"));
    }

    @Test
    @DisplayName("질문 등록")
    void addQuestion() {
        Question newQuestion = new Question("First Question", "This is first Question.");
        User user = UserTest.JAVAJIGI;
        newQuestion.writeBy(user);
        assertAll(
                () -> assertThat(newQuestion).isNotNull(),
                () -> assertThat(newQuestion.getTitle()).isEqualTo("First Question"),
                () -> assertThat(newQuestion.getContents()).isEqualTo("This is first Question."),
                () -> assertThat(newQuestion.getWriterId()).isEqualTo(user.getId())
        );
    }

    @Test
    @DisplayName("질문 수정 : 질문 제목, 질문 내용")
    void update() {
        System.out.println("question1 : " + question1.toString());
        question1.registerTitle("첫번째 질문");
        question1.registerContents("첫번째 질문 내용입니다.");
        Question actual = questionRepository.save(question1);
        assertAll(
                () -> assertThat(actual).isNotNull(),
                () -> assertThat(actual.getTitle()).isEqualTo("첫번째 질문"),
                () -> assertThat(actual.getContents()).isEqualTo("첫번째 질문 내용입니다.")
        );
    }

    @Test
    @DisplayName("전체 질문 목록 수 조회")
    void countQuestions() {
        Long count = questionRepository.count();
        assertThat(count).isEqualTo(2L);
    }

    @ParameterizedTest
    @CsvSource(value = {"0:title1", "1:title2"}, delimiter = ':')
    @DisplayName("전체 질문 목록 조회")
    void findQuestions(int index, String title) {
        String orderByColumn = "id";
        List<Question> questions = questionRepository.findAll(Sort.by(Sort.Direction.ASC, orderByColumn));
        assertAll(
                () -> assertThat(questions.size()).isEqualTo(2L),
                () -> assertThat(questions.get(index).getTitle()).isEqualTo(title)
        );
    }

    @Test
    @DisplayName("작성자 ID를 통해 질문 목록 수 가져오기")
    void countByWriter() {
        assertThat(questionRepository.countByWriter(UserTest.JAVAJIGI)).isEqualTo(1L);
    }

    @Test
    @DisplayName("작성자 ID를 통해 질문 목록 가져오기")
    void findQuestionsByWriter() {
        List<Question> questions = questionRepository.findByWriter(UserTest.JAVAJIGI);
        assertAll(
                () -> assertThat(questions.size()).isEqualTo(1L),
                () -> assertThat(questions.get(0).getTitle()).isEqualTo("title1")
        );
    }

    @Test
    @DisplayName("질문 Title을 통해 질문 목록 수 가져오기")
    void countByTitleContains() {
        // Like 검색 공부하기.
        assertThat(questionRepository.countByTitle(new Title("title1"))).isEqualTo(1L);
    }

    @Test
    @DisplayName("질문 Title을 통해 질문 목록 가져오기")
    void findByTitleContains() {
        // Like 검색 공부하기.
        List<Question> questions = questionRepository.findByTitle(new Title("title1"));
        assertAll(
                () -> assertThat(questions.size()).isEqualTo(1L),
                () -> assertThat(questions.contains(question1)).isTrue()
        );
    }

    @Test
    @DisplayName("질문 삭제")
    void deleteQuestion() {
        Long answerCount = answerRepository.countByQuestionIdAndDeletedFalse(question2.getId());
        boolean isOwner = question2.isOwner(UserTest.SANJIGI);
        if(isOwner && answerCount == 0) {
            changeDeleted(question2);
        }
        if(isOwner && answerCount != 0) {
            deleteQuestionWithAnswers(question2);
        }
        Question actual = questionRepository.save(question2);
        assertThat(actual.isDeleted()).isTrue();
    }

    private void deleteQuestionWithAnswers(Question question) {
        List<Answer> answers = answerRepository.findByQuestionIdAndDeletedFalse(question.getId());
        if(answers.stream().allMatch(answer -> answer.isOwner(question.getWriter()))) {
            for(Answer answer : answers) {
                answer.changeDeleted(true);
            }
            changeDeleted(question);
        }
    }

    private void changeDeleted(Question question) {
        question.changeDeleted(true);
    }
}
