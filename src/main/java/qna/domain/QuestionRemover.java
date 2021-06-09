package qna.domain;

public final class QuestionRemover {

    private QuestionRemover() {
    }

    public static DeleteHistories delete(Question question, User writer) {
        DeleteHistories deleteHistories = new DeleteHistories();
        deleteHistories.add(DeleteHistory.ofQuestion(question.getId(), writer));

        question
            .delete(writer)
            .deleteAnswers(writer)
            .stream()
            .map(answer -> DeleteHistory.ofAnswer(answer.getId(), writer))
            .forEach(deleteHistories::add);

        return deleteHistories;
    }

}
