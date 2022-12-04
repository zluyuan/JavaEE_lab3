//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.goods.dao;


import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.util.RedisUtil;
import cn.edu.xmu.oomall.goods.dao.activity.ActivityDao;
import cn.edu.xmu.oomall.goods.dao.bo.Onsale;
import cn.edu.xmu.oomall.goods.mapper.OnsalePoMapper;
import cn.edu.xmu.oomall.goods.mapper.po.OnsalePo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static cn.edu.xmu.javaee.core.model.Constants.MAX_RETURN;
import static cn.edu.xmu.javaee.core.util.Common.cloneObj;

@Repository
public class OnsaleDao {

    private static final String KEY = "O%d";
    private static final String VALID_KEY = "OV%d";

    private final static Logger logger = LoggerFactory.getLogger(OnsaleDao.class);

    public  final static Onsale NOTEXIST = new Onsale(){{
        setId(-1L);
    }};

    @Value("${oomall.onsale.timeout}")
    private int timeout;

    private OnsalePoMapper onsalePoMapper;

    private ActivityDao activityDao;

    private RedisUtil redisUtil;

    @Autowired
    public OnsaleDao(OnsalePoMapper onsalePoMapper, ActivityDao activityDao, RedisUtil redisUtil) {
        this.onsalePoMapper = onsalePoMapper;
        this.activityDao = activityDao;
        this.redisUtil = redisUtil;
    }

    private Onsale getBo(OnsalePo po, Optional<String> redisKey){
        Onsale bo = cloneObj(po, Onsale.class);
        Long newTimeout = getNewTimeout(bo.getEndTime());
        this.setBo(bo);
        redisKey.ifPresent(key -> redisUtil.set(key, bo, newTimeout));
        return bo;
    }

    private Long getNewTimeout(LocalDateTime endTime) {
        Long diff = Duration.between(LocalDateTime.now(), endTime).toSeconds();
        Long newTimeout = Math.min(this.timeout,  diff);
        return newTimeout;
    }

    private void setBo(Onsale bo){
        bo.setActivityDao(this.activityDao);
    }
    /**
     * 获得货品的最近的价格和库存
     *
     * @param productId 货品对象
     * @return 规格对象
     */
    public Onsale findLatestValidOnsale(Long productId) throws RuntimeException{
        logger.debug("findLatestValidOnsale: id ={}",productId);
        String key = String.format(VALID_KEY, productId);
        if (redisUtil.hasKey(key)){
            Integer onsaleId = (Integer) redisUtil.get(key);
            if (!onsaleId.equals(NOTEXIST.getId().intValue())) {
                try {
                    Onsale bo = this.findById(Long.valueOf(onsaleId));
                    setBo(bo);
                    return bo;
                } catch (BusinessException e) {
                    if (ReturnNo.RESOURCE_ID_NOTEXIST != e.getErrno()) {
                        throw e;
                    }
                }
            }
            return NOTEXIST;
        }

        Pageable pageable = PageRequest.of(0, MAX_RETURN, Sort.by("beginTime").ascending());
        LocalDateTime now = LocalDateTime.now();
        Page<OnsalePo> retObj = this.onsalePoMapper.findByProductIdEqualsAndEndTimeAfter(productId, now, pageable);
        if (retObj.isEmpty() ){
            redisUtil.set(key, NOTEXIST.getId(), timeout);
            return NOTEXIST;
        }else{
            OnsalePo po = retObj.stream().limit(1).collect(Collectors.toList()).get(0);
            Onsale bo =  this.getBo(po, Optional.ofNullable(null));
            redisUtil.set(key, bo.getId(), getNewTimeout(bo.getEndTime()) );
            return bo;
        }
    }

    /**
     * 用id找对象
     * @author Ming Qiu
     * <p>
     * date: 2022-12-04 8:34
     * @param id
     * @return
     * @throws RuntimeException
     */
    public Onsale findById(Long id) throws RuntimeException{
        logger.debug("findById: id ={}",id);
        if (null == id){
            return null;
        }

        String key = String.format(KEY, id);
        if (redisUtil.hasKey(key)){
            Onsale bo = (Onsale) redisUtil.get(key);
            setBo(bo);
            return bo;
        }

        Optional<OnsalePo> retObj = this.onsalePoMapper.findById(id);
        if (retObj.isEmpty() ){
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "销售", id));
        }else{
            OnsalePo po = retObj.get();
            return this.getBo(po, Optional.of(key));
        }
    }

}
