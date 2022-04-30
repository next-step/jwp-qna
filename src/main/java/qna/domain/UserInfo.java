package qna.domain;

import qna.exception.UnAuthorizedException;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import java.util.Objects;

@Embeddable
public class UserInfo {

    @Column(length = 20, nullable = false)
    private String name;

    @Embedded
    private LoginInfo loginInfo;

    public UserInfo() {
    }

    public UserInfo(String name, LoginInfo loginInfo) {
        this.name = name;
        this.loginInfo = loginInfo;
    }

    public String getName() {
        return name;
    }

    public LoginInfo getLoginInfo() {
        return loginInfo;
    }

    public void update(UserInfo target) {
        if (!loginInfo.equalsUserIdAndPassword(target.loginInfo)) {
            throw new UnAuthorizedException();
        }
        this.name = target.name;
        this.loginInfo = target.loginInfo;
    }

    public boolean equalsNameAndEmail(UserInfo target) {
        return name.equals(target.name)
                && loginInfo.getEmail().equals(target.loginInfo.getEmail());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserInfo)) return false;
        UserInfo userInfo = (UserInfo) o;
        return getName().equals(userInfo.getName()) && getLoginInfo().equals(userInfo.getLoginInfo());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getLoginInfo());
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "name='" + name + '\'' +
                ", loginInfo=" + loginInfo +
                '}';
    }
}
