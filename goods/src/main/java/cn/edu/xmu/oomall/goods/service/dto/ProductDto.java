//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.goods.service.dto;

import cn.edu.xmu.oomall.goods.dao.bo.Activity;
import cn.edu.xmu.oomall.goods.dao.bo.SimpleProductDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 商品视图对象
 * @author Ming Qiu
 **/
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class ProductDto {

    private Long id;
    private String name;
    private Byte status;
    private String skuSn;
    private Long originalPrice;
    private Long weight;
    private String barcode;
    private String unit;
    private String originPlace;
    private List<SimpleProductDto> otherProduct;
    private Long price;
    private Integer quantity;
    private Integer maxQuantity;
    private LocalDateTime beginTime;
    private LocalDateTime endTime;
}
