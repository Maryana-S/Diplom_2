package site.nomoreparties.stellarburgers.models.responses.orders;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Ingredients {

    @SerializedName(value = "_id")
    private String id;

    private String name;

    private String type;

    private Integer proteins;

    private Integer fat;

    private Integer carbohydrates;

    private Integer calories;

    private Integer price;

    private String image;

    private String image_mobile;

    private String image_large;

    @SerializedName(value = "__v")
    private Integer v;
}
