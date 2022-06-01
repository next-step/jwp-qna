package qna.domain;

public class ChangeNameAndEmailDto {
    private final String password;
    private final String name;
    private final String email;

    public ChangeNameAndEmailDto(final String password, final String name, final String email) {
        this.password = password;
        this.name = name;
        this.email = email;
    }

    public User toEntity() {
        return new User(password, name, email);
    }
}
