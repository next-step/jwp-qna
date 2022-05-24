package qnamission.domain;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class DeleteHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long contentId;

    private String contentType;

    private LocalDateTime createDate;

    private Long deletedById;

    protected DeleteHistory() {

    }

    public Long getId() {
        return this.id;
    }
}
