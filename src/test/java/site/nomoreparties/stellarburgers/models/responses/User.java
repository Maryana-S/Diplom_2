package site.nomoreparties.stellarburgers.models.responses;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class User {

    private String email;

    private String name;
}
