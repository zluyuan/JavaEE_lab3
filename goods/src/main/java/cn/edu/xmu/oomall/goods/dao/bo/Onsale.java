//School of Informatics Xiamen University, GPL-3.0 license

package cn.edu.xmu.oomall.goods.dao.bo;

import cn.edu.xmu.javaee.core.model.bo.OOMallObject;
import cn.edu.xmu.oomall.goods.dao.activity.ActivityDao;
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
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@ToString(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Onsale extends OOMallObject implements Serializable {
    @ToString.Exclude
    @JsonIgnore
    private Logger logger = LoggerFactory.getLogger(Onsale.class);

    /**
     * 正常
     */
    public static final Byte NORMAL = 0;
    /**
     * 秒杀
     */
    public static final Byte SECONDKILL = 1;
    /**
     * 团购
     */
    public static final Byte GROUPON = 2;
    /**
     * 预售
     */
    public static final Byte ADVSALE = 3;



    @Setter
    @Getter
    private Long price;

    @Setter
    @Getter
    private LocalDateTime beginTime;

    @Setter
    @Getter
    private LocalDateTime endTime;

    @Setter
    @Getter
    private Integer quantity;

    @Setter
    @Getter
    private Byte invalid;

    @Setter
    @Getter
    private Integer maxQuantity;

    @Setter
    private Long shopId;

    @Setter
    private Long productId;

    @JsonIgnore
    private List<Activity> actList;

    @Setter
    @JsonIgnore
    private ActivityDao activityDao;

    public List<Activity> getActList(){
        if (null == this.actList && null != this.activityDao){
            this.actList = this.activityDao.retrieveByOnsaleId(this.id);
            logger.debug("getActList: actList = {}", actList);
        }
        return this.actList;

    }

    @Setter
    private Byte type;

    public Byte getType(){
        if (null  != this.getActList()) {
            List<Activity> acts = this.getActList().stream().filter(act -> act instanceof AdvanceSaleAct || act instanceof GrouponAct).limit(1).collect(Collectors.toList());
            if (acts.size() > 0) {
                if (acts.get(0) instanceof AdvanceSaleAct) {
                    return ADVSALE;
                } else {
                    return GROUPON;
                }
            }
        }
        return this.type;
    }
}
