//School of Informatics Xiamen University, GPL-3.0 license

package cn.edu.xmu.oomall.goods.dao.bo;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.bo.OOMallObject;
import cn.edu.xmu.oomall.goods.dao.CategoryDao;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

@NoArgsConstructor
@ToString(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Category extends OOMallObject implements Serializable {
    @ToString.Exclude
    @JsonIgnore
    private Logger logger = LoggerFactory.getLogger(Category.class);

    @Getter
    @Setter
    private String name;

    @Setter
    private Long pid;

    private Category parent;

    @Setter
    @JsonIgnore
    private CategoryDao categoryDao;

    public Category getParent(){
        if (null == this.parent && null != this.categoryDao){
            try {
                this.parent = this.categoryDao.findById(this.pid);
            }catch (BusinessException e){
                if (ReturnNo.RESOURCE_ID_NOTEXIST == e.getErrno()){
                    logger.error("getParent: category(id = {})'s pid is invalid...");
                }
            }
        }
        return this.parent;
    }

    @Setter
    private Integer commissionRatio;

    public Integer getCommissionRatio(){
        if (null == this.commissionRatio && null != this.getParent()){
            this.commissionRatio = this.parent.getCommissionRatio();
        }
        return this.commissionRatio;
    }
}
