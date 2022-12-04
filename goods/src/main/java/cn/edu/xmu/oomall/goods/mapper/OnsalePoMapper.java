//School of Informatics Xiamen University, GPL-3.0 license

package cn.edu.xmu.oomall.goods.mapper;

import cn.edu.xmu.oomall.goods.mapper.po.OnsalePo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OnsalePoMapper extends JpaRepository<OnsalePo, Long> {
    Page<OnsalePo> findByProductIdEqualsAndEndTimeAfter(Long productId, LocalDateTime time, Pageable pageable);
}
