package cn.edu.xmu.oomall.goods.service;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.util.RedisUtil;
import cn.edu.xmu.oomall.goods.dao.ProductDao;
import cn.edu.xmu.oomall.goods.dao.bo.Product;
import cn.edu.xmu.oomall.goods.dao.bo.SimpleProductDto;
import cn.edu.xmu.oomall.goods.service.dto.ProductDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

import static cn.edu.xmu.javaee.core.util.Common.cloneObj;

@Service
public class ProductService {

    private Logger logger = LoggerFactory.getLogger(ProductService.class);

    private ProductDao productDao;

    private RedisUtil redisUtil;

    @Autowired
    public ProductService(ProductDao productDao, RedisUtil redisUtil) {
        this.productDao = productDao;
        this.redisUtil = redisUtil;
    }

    /**
     * 获取某个商品信息，仅展示相关内容
     *
     * @param id 商品id
     * @return 商品对象
     */
    @Transactional(rollbackFor = {BusinessException.class})
    public ProductDto findProductById(Long id) throws BusinessException {
        logger.debug("findProductById: id = {}", id);
        /*
        String key = BloomFilter.PRETECT_FILTERS.get("ProductId");
        if (redisUtil.bfExist(key, id)){
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_OUTSCOPE.getMessage(),"产品", id));
        }
        */
        ProductDto dto = null;
        try {
            Product bo = productDao.findProductById(id);
            dto = cloneObj(bo, ProductDto.class);
            logger.debug("findProductById: dto = {}", dto);
            dto.setOtherProduct(bo.getOtherProduct().stream().map(product -> cloneObj(product, SimpleProductDto.class)).collect(Collectors.toList()));
        }catch (BusinessException e){
            if (ReturnNo.RESOURCE_ID_NOTEXIST == e.getErrno()){
                //redisUtil.bfAdd(key, id);
            }
            throw e;
        }
        return dto;
    }

}
