package qna.domain.commons;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Title {
  @Column(length = 100, nullable = false)
  private String title;

  protected Title() {}

  public Title(String title) {
    this.title = title;
  }

  public static Title of(String title) {
    return new Title(title);
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

}