package site.nomoreparties.stellarburgers.models.responses;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserSuccessResponse {

    private boolean success;

    private User user;

    private String accessToken;

    private String refreshToken;
}
