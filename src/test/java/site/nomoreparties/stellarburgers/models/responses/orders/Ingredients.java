package site.nomoreparties.stellarburgers.models.responses.orders;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Ingredients {

    private String _id;

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

    private Integer __v;
}
