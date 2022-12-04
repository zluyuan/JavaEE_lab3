package cn.edu.xmu.oomall.goods.mapper.po.strategy.impl;

import cn.edu.xmu.oomall.goods.mapper.po.strategy.BaseCouponLimitation;
import cn.edu.xmu.oomall.goods.mapper.po.strategy.Item;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author zhongyu wang 22920192204295
 * 复合优惠限制类
 */
@NoArgsConstructor
@Data
public class ComplexCouponLimitation extends BaseCouponLimitation {
    private List<BaseCouponLimitation> couponLimitationList;

    public ComplexCouponLimitation(BaseCouponLimitation... couponLimitations) {
        couponLimitationList = new ArrayList<>(couponLimitations.length);
        couponLimitationList.addAll(Arrays.asList(couponLimitations));
        this.className = this.getClass().getName();
    }

    @Override
    public boolean pass(List<Item> orderItems) {
        for(BaseCouponLimitation couponLimitation : couponLimitationList) {
            if (!couponLimitation.pass(orderItems)) {
                return false;
            }
        }
        return true;
    }
}
