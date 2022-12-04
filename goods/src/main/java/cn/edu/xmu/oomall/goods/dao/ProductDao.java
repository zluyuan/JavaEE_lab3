//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.goods.dao;


import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.dto.PageDto;
import cn.edu.xmu.javaee.core.util.RedisUtil;
import cn.edu.xmu.oomall.goods.dao.bo.Product;
import cn.edu.xmu.oomall.goods.dao.bo.SimpleProductDto;
import cn.edu.xmu.oomall.goods.mapper.ProductPoMapper;
import cn.edu.xmu.oomall.goods.mapper.po.ProductPo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static cn.edu.xmu.javaee.core.model.Constants.MAX_RETURN;
import static cn.edu.xmu.javaee.core.util.Common.cloneObj;

/**
 * @author Ming Qiu
 **/
@Repository
public class ProductDao {

    private Logger logger = LoggerFactory.getLogger(ProductDao.class);

    private final static String KEY = "P%d";

    private final static String OTHER_KEY = "PO%d";

    @Value("${oomall.product.timeout}")
    private int timeout;


    private ProductPoMapper productPoMapper;

    private OnsaleDao onsaleDao;
    private CategoryDao categoryDao;

    private RedisUtil redisUtil;


    @Autowired
    public ProductDao(ProductPoMapper productPoMapper, OnsaleDao onSaleDao, CategoryDao categoryDao, RedisUtil redisUtil) {
        this.productPoMapper = productPoMapper;
        this.onsaleDao = onSaleDao;
        this.redisUtil = redisUtil;
        this.categoryDao = categoryDao;
    }

    private Product getBo(ProductPo po, Optional<String> redisKey) {
        Product bo = cloneObj(po, Product.class);
        this.setBo(bo);
        redisKey.ifPresent(key -> redisUtil.set(key, bo, timeout));
        return bo;
    }

    private void setBo(Product bo) {
        bo.setCategoryDao(this.categoryDao);
        bo.setOnsaleDao(this.onsaleDao);
        bo.setProductDao(this);
    }


    /**
     * 用GoodsPo对象找Goods对象
     *
     * @param name
     * @return Goods对象列表，带关联的Product返回
     */
    public PageDto<Product> retrieveProductByName(String name, int page, int pageSize) throws RuntimeException {
        List<Product> productList = null;
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<ProductPo> pageObj = productPoMapper.findByNameEqualsAndStatusNot(name, Product.BANNED, pageable);
        if (!pageObj.isEmpty()) {
            productList = pageObj.stream().map(po -> this.getBo(po, Optional.ofNullable(null))).collect(Collectors.toList());
        } else {
            productList = new ArrayList<>();
        }
        logger.debug("retrieveProductByName: productList = {}", productList);
        return new PageDto<>(productList, page, pageSize);
    }

    /**
     * 用id查找对象
     *
     * @param id
     * @return product对象
     */
    public Product findProductById(Long id) throws RuntimeException {
        logger.debug("findProductById: id = {}", id);
        if (null == id){
            return null;
        }

        String key = String.format(KEY, id);
        if (redisUtil.hasKey(key)){
            Product bo = (Product) redisUtil.get(key);
            setBo(bo);
            return bo;
        }
        Optional<ProductPo> ret = productPoMapper.findById(id);
        if (ret.isPresent()) {
            return this.getBo(ret.get(), Optional.of(key));
        } else {
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "产品", id));
        }
    }

    /**
     * 用id查找对象
     *
     * @param goodsId goodsId
     * @return product对象
     */
    public List<Product> retrieveOtherProductById(Long goodsId) throws RuntimeException {
        if (null == goodsId){
            return null;
        }

        String key = String.format(OTHER_KEY, goodsId);
        if (redisUtil.hasKey(key)){
            List<Long> otherIds = (List<Long>) redisUtil.get(key);
            List<Product> retList = otherIds.stream().map(productId -> this.findProductById(productId)).filter(obj -> null != obj).collect(Collectors.toList());
            return retList;
        }

        Pageable pageable = PageRequest.of(0, MAX_RETURN);
        Page<ProductPo> ret = productPoMapper.findByGoodsIdEquals(goodsId, pageable);
        if (ret.isEmpty()){
            return new ArrayList<>();
        }else{
            List<Product> retList = ret.stream().map(productPo -> this.findProductById(productPo.getId())).collect(Collectors.toList());
            redisUtil.set(key, (ArrayList<Long>) retList.stream().map(obj -> obj.getId()).collect(Collectors.toList()), timeout);
            return retList;
        }
    }

}
