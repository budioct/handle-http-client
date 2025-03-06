package budhioct.dev.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class TodoDTO {
    private int userId;
    private int id;
    private String title;
    private boolean completed;

    @Override
    public String toString() {
        return "TodoDTO{" +
                "userId=" + userId +
                ", id=" + id +
                ", title='" + title + '\'' +
                ", completed=" + completed +
                '}';
    }

}
