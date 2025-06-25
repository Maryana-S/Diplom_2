package site.nomoreparties.stellarburgers.models.responses.orders;


import com.google.gson.annotations.SerializedName;
import lombok.NoArgsConstructor;
import lombok.Data;

@Data
@NoArgsConstructor
public class Order {

    private Integer number;

    private Ingredients[] ingredients;

    @SerializedName(value = "_id")
    private String id;

    private Owner owner;

    private String status;

    private String name;

    private String createdAt;

    private String updatedAt;

    private Integer price;

}
