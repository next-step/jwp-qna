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
    private UserLogin userLogin;

    public UserInfo() {
    }

    public UserInfo(String name, UserLogin userLogin) {
        this.name = name;
        this.userLogin = userLogin;
    }

    public String getName() {
        return name;
    }

    public UserLogin getLoginInfo() {
        return userLogin;
    }

    public void update(UserInfo target) {
        if (!userLogin.equalsUserIdAndPassword(target.userLogin)) {
            throw new UnAuthorizedException("로그인 정보가 맞지 않아 개인 정보를 변경할 수 없습니다.");
        }
        this.name = target.name;
        this.userLogin = target.userLogin;
    }

    public boolean equalsNameAndEmail(UserInfo target) {
        return name.equals(target.name)
                && userLogin.getEmail().equals(target.userLogin.getEmail());
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
                ", loginInfo=" + userLogin +
                '}';
    }
}
