//School of Informatics Xiamen University, GPL-3.0 license

package cn.edu.xmu.oomall.goods.dao.bo;

import cn.edu.xmu.javaee.core.model.bo.OOMallObject;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
@ToString(callSuper = true)
public class ProductDraft extends OOMallObject {

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private Long originalPrice;

    @Getter
    @Setter
    private String originPlace;


    @Setter
    private Long shopId;

    @Setter
    private Long categoryId;

    @Setter
    private Long productId;
}
