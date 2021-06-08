package qna.domain;

public final class QuestionRemover {

    private QuestionRemover() {
    }

    public static DeleteHistories delete(Question question, User writer) {
        DeleteHistories deleteHistories = new DeleteHistories();
        deleteHistories.add(new DeleteHistory(question, writer));

        question
            .delete(writer)
            .deleteAnswers(writer)
            .stream()
            .map(answer -> new DeleteHistory(answer, writer))
            .forEach(deleteHistories::add);

        return deleteHistories;
    }

}
