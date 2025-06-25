package site.nomoreparties.stellarburgers.models.responses;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FailureResponse {

    private boolean success;

    private String message;
}
