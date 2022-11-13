package qna.domain;

public class DomainTestFactory {

    public static Answer createAnswer(User user, Question question) {
        return new Answer(user, question, "contents");
    }

    public static User createUser(String id) {
        return new User(id, "password", "name", "test@test.com");
    }

    public static Question createQuestion() {
        return new Question("title", "contents");
    }

    public static DeleteHistory createDeleteHistory(ContentType contentType, Long contentId, User user) {
        return new DeleteHistory(contentType, contentId, user);
    }
}
