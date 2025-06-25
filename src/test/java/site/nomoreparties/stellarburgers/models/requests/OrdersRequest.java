package site.nomoreparties.stellarburgers.models.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrdersRequest {

    private String[] ingredients;

}
