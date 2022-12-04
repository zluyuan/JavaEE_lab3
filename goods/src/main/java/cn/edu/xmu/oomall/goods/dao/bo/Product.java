//School of Informatics Xiamen University, GPL-3.0 license

package cn.edu.xmu.oomall.goods.dao.bo;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.bo.OOMallObject;
import cn.edu.xmu.oomall.goods.dao.CategoryDao;
import cn.edu.xmu.oomall.goods.dao.OnsaleDao;
import cn.edu.xmu.oomall.goods.dao.ProductDao;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

@NoArgsConstructor
@ToString(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Product extends OOMallObject implements Serializable {

    @ToString.Exclude
    @JsonIgnore
    private  final static Logger logger = LoggerFactory.getLogger(Product.class);

    /**
     * 共三种状态
     */
    //禁售中
    public static final  Byte BANNED = 0;
    //下架
    public static final  Byte OFFSHELF  = 1;
    //上架
    public static final  Byte ONSHELF  = 2;

    /**
     * 状态和名称的对应
     */
    @JsonIgnore
    @ToString.Exclude
    public static final Map<Byte, String> STATUSNAMES = new HashMap(){
        {
            put(OFFSHELF, "下架");
            put(ONSHELF, "上架");
            put(BANNED, "禁售");
        }
    };
    /**
     * 获得当前状态名称
     * @author Ming Qiu
     * <p>
     * date: 2022-11-13 0:43
     * @return
     */
    @JsonIgnore
    public String getStatusName(){
        return STATUSNAMES.get(this.status);
    }

    @Getter
    @Setter
    private String skuSn;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private Long originalPrice;

    @Getter
    @Setter
    private Long weight;

    @Getter
    @Setter
    private String barcode;

    @Getter
    @Setter
    private String unit;

    @Getter
    @Setter
    private String originPlace;

    @Getter
    @Setter
    private Long shopLogisticId;

    @Setter
    private Integer commissionRatio;

    public Integer getCommissionRatio(){
        if (null == this.commissionRatio && null != this.getCategory()){
            this.commissionRatio = this.getCategory().getCommissionRatio();
        }
        return this.commissionRatio;
    }

    @Setter
    private Byte status;

    /**
     * 获得商品状态
     * @return
     */
    public Byte getStatus() {
        logger.debug("getStatus: id ={}",this.id);
        if ((Product.BANNED == this.status)) {
            return Product.BANNED;
        }else{
            if (null == this.getValidOnsale()){
                return Product.OFFSHELF;
            }else{
                if (this.getValidOnsale().getBeginTime().isBefore(LocalDateTime.now())) {
                    return Product.ONSHELF;
                }else{
                    return Product.OFFSHELF;
                }
            }
        }
    }

    @Setter
    private Long goodsId;
    /**
     * 相关商品
     */
    @JsonIgnore
    private List<Product> otherProduct;

    @Setter
    @JsonIgnore
    private ProductDao productDao;


    public List<Product> getOtherProduct(){
        if (null == this.otherProduct && null != this.productDao){
            this.otherProduct = this.productDao.retrieveOtherProductById(this.goodsId);
            logger.debug("getOtherProduct: otherProduct = {}",this.otherProduct);
        }
        return this.otherProduct;
    }
    /**
     * 有效上架， 包括即将上架
     */
    @JsonIgnore
    private Onsale validOnsale;

    @Setter
    @JsonIgnore
    private OnsaleDao onsaleDao;

    public Onsale getValidOnsale(){
        if (null == this.validOnsale && null != this.onsaleDao){
            this.validOnsale = this.onsaleDao.findLatestValidOnsale(id);
            logger.debug("getValidOnsale: validOnsale = {}", this.validOnsale);
        }

        if (this.validOnsale.getId().equals(OnsaleDao.NOTEXIST.getId())){
            return null;
        }
        return this.validOnsale;
    }

    @JsonIgnore
    public Long getPrice() {
        if (null != this.getValidOnsale()) {
            return this.validOnsale.getPrice();
        } else {
            return null;
        }
    }

    @JsonIgnore
    public Integer getQuantity() {
        if (null != this.getValidOnsale()) {
            return this.validOnsale.getQuantity();
        } else {
            return null;
        }
    }

    @JsonIgnore
    public LocalDateTime getBeginTime() {
        if (null != this.getValidOnsale()) {
            return this.validOnsale.getBeginTime();
        } else {
            return null;
        }
    }

    @JsonIgnore
    public LocalDateTime getEndTime() {
        if (null != this.getValidOnsale()) {
            return this.validOnsale.getEndTime();
        } else {
            return null;
        }
    }

    @JsonIgnore
    public Integer getMaxQuantity() {
        if (null != this.getValidOnsale()) {
            return this.validOnsale.getMaxQuantity();
        } else {
            return null;
        }
    }

    @Setter
    private Long categoryId;
    /**
     * 所属分类
     */
    @JsonIgnore
    private Category category;

    @Setter
    @JsonIgnore
    private CategoryDao categoryDao;


    public Category getCategory(){
        if (null == this.category && null != this.categoryDao){
            try {
                this.category = this.categoryDao.findById(this.categoryId);
            }catch (BusinessException e){
                if (ReturnNo.RESOURCE_ID_NOTEXIST == e.getErrno()){
                    this.categoryId = null;
                    logger.error("getCategory: product(id = {})'s categoryId is invalid.", id);
                }
            }
        }
        return this.category;
    }

    @JsonIgnore
    public List<Activity> getActList(){
        return this.getValidOnsale().getActList();
    }

    @Setter
    private Long shopId;

    @Setter
    private Optional<Long> templateId;

}
