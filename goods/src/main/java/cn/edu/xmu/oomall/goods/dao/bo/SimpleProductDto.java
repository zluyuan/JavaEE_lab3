//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.goods.dao.bo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class SimpleProductDto {
    private Long id;
    private String name;
    private Long price;
    private Integer quantity;
    private Byte status;
}
