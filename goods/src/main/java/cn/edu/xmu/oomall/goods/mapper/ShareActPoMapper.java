//School of Informatics Xiamen University, GPL-3.0 license

package cn.edu.xmu.oomall.goods.mapper;

import cn.edu.xmu.oomall.goods.mapper.po.ShareActPo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShareActPoMapper extends MongoRepository<ShareActPo, String> {

}
