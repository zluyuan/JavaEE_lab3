//School of Informatics Xiamen University, GPL-3.0 license

package cn.edu.xmu.oomall.goods.dao;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.util.RedisUtil;
import cn.edu.xmu.oomall.goods.dao.bo.Category;
import cn.edu.xmu.oomall.goods.dao.bo.Product;
import cn.edu.xmu.oomall.goods.mapper.CategoryPoMapper;
import cn.edu.xmu.oomall.goods.mapper.po.CategoryPo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static cn.edu.xmu.javaee.core.util.Common.cloneObj;

@Repository
public class CategoryDao {
    private Logger logger = LoggerFactory.getLogger(CategoryDao.class);

    private final static String KEY = "C%d";

    @Value("${oomall.category.timeout}")
    private int timeout;

    private CategoryPoMapper categoryPoMapper;

    private RedisUtil redisUtil;

    @Autowired
    public CategoryDao(CategoryPoMapper categoryPoMapper, RedisUtil redisUtil) {
        this.categoryPoMapper = categoryPoMapper;
        this.redisUtil = redisUtil;
    }

    private Category getBo(CategoryPo po, Optional<String> redisKey){
        Category bo = cloneObj(po, Category.class);
        this.setBo(bo);
        redisKey.ifPresent(key -> redisUtil.set(key, bo, timeout));
        return bo;
    }

    private void setBo(Category bo){
        bo.setCategoryDao(this);
    }

    public Category findById(Long id) throws RuntimeException{
        if (null == id){
            return null;
        }

        String key = String.format(KEY, id);

        if (redisUtil.hasKey(key)){
            Category bo = (Category) redisUtil.get(key);
            setBo(bo);
            return bo;
        }

        Optional<CategoryPo> ret = this.categoryPoMapper.findById(id);
        if (ret.isPresent()){
            return this.getBo(ret.get(), Optional.of(key));
        } else{
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(),"分类", id));
        }
    }

}
