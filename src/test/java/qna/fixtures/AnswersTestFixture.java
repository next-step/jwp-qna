package qna.fixtures;

import qna.domain.Answer;
import qna.domain.Answers;
import qna.domain.Question;
import qna.domain.User;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AnswersTestFixture {

    public static Answers createAnswersBy(User writer, Question question, int answerCount) {
        AtomicLong answerId = new AtomicLong(1L);
        List<Answer> answers = IntStream.range(0, answerCount)
                .mapToObj(count -> AnswerTestFixture.createAnswerWithIdAndWriterAndQuestion(answerId.getAndIncrement(), writer, question))
                .collect(Collectors.toList());
        return new Answers(answers);
    }
}
