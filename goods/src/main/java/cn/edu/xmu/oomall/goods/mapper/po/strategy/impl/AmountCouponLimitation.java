package cn.edu.xmu.oomall.goods.mapper.po.strategy.impl;


import cn.edu.xmu.oomall.goods.mapper.po.strategy.BaseCouponLimitation;
import cn.edu.xmu.oomall.goods.mapper.po.strategy.Item;

import java.util.List;

/**
 * @author xincong yao
 * @date 2020-11-19
 */
public class AmountCouponLimitation extends BaseCouponLimitation {

	public AmountCouponLimitation(){}

	public AmountCouponLimitation(long value) {
		super(value);
	}

	@Override
	public boolean pass(List<Item> orderItems) {
		long t = 0;
		for (Item oi : orderItems) {
			t += oi.getQuantity();
		}
		return t >= value;
	}
}
