package site.nomoreparties.stellarburgers.models.responses.orders;

import io.qameta.allure.internal.shadowed.jackson.annotation.JsonSetter;
import io.qameta.allure.internal.shadowed.jackson.annotation.Nulls;
import lombok.NoArgsConstructor;
import lombok.Data;

@Data
@NoArgsConstructor
public class Order {

    private Integer number;

    private Ingredients[] ingredients;

    private String _id;

    private Owner owner;

    private String status;

    private String name;

    private String createdAt;

    private String updatedAt;

    private Integer price;

}
