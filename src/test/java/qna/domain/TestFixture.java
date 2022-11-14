package qna.domain;

public class TestFixture {
    public static Answer createAnswer(User writer, Question question) {
        return new Answer(writer, question, "contents");
    }

    public static Answer createAnswer() {
        return new Answer(createUser(), createQuestion(), "contents");
    }

    public static Question createQuestion(User writer) {
        return new Question("title", "contents").writeBy(writer);
    }

    public static Question createQuestion() {
        return new Question("title", "contents").writeBy(createUser());
    }


    public static User createUser(String userId) {
        return new User(userId, "password", "tester", userId + "@domain.com");
    }

    public static User createUser() {
        return new User("tester", "password", "tester", "tester@domain.com");
    }
}
