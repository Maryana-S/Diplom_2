package site.nomoreparties.stellarburgers.models.responses.orders;

import lombok.NoArgsConstructor;
import lombok.Data;

@Data
@NoArgsConstructor
public class PostOrderSuccessResponse {

    private String name;

    private Order order;

    private boolean success;

}
