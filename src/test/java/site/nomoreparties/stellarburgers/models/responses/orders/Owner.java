package site.nomoreparties.stellarburgers.models.responses.orders;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Owner {

    private String name;

    private String email;

    private String createdAt;

    private String updatedAt;

}
