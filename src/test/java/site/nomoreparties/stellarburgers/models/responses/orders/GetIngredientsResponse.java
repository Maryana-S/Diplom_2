package site.nomoreparties.stellarburgers.models.responses.orders;

import lombok.NoArgsConstructor;
import lombok.Data;

@Data
@NoArgsConstructor
public class GetIngredientsResponse {

    private boolean success;

    private Ingredients[] data;

}
