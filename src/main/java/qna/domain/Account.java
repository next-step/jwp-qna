package qna.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Account {
    @Column(length = 20, nullable = false, unique = true)
    private String userId;

    @Column(length = 20, nullable = false)
    private String password;

    protected Account() {
    }

    Account(final String userId, final String password) {
        this.userId = userId;
        this.password = password;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final Account account = (Account)obj;
        return userId.equals(account.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }

    @Override
    public String toString() {
        return "Account{" +
            "userId='" + userId + '\'' +
            '}';
    }
}
