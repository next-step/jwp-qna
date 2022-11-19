package qna.domain;

public class DomainTestFactory {

    public static Answer createAnswer(User user, Question question) {
        return new Answer(user, question, new Contents("contents"));
    }

    public static User createUser(String id) {
        return new User(new UserId(id), new Password("password"), new Name("name"), new Email("test@test.com"));
    }

    public static Question createQuestion() {
        return new Question(new Title("title"), new Contents("contents"));
    }

    public static DeleteHistory createDeleteHistory(ContentType contentType, Long contentId, User user) {
        return new DeleteHistory(contentType, contentId, user);
    }
}
