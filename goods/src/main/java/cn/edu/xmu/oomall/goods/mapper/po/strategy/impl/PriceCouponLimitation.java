package cn.edu.xmu.oomall.goods.mapper.po.strategy.impl;

import cn.edu.xmu.oomall.goods.mapper.po.strategy.BaseCouponLimitation;
import cn.edu.xmu.oomall.goods.mapper.po.strategy.Item;

import java.util.List;

/**
 * @author xincong yao
 * @date 2020-11-18
 * modified by zhongyu wang
 * date 2021-11-12
 */
public class PriceCouponLimitation extends BaseCouponLimitation {

	public PriceCouponLimitation() {

	}

	public PriceCouponLimitation(long value) {
		super(value);
	}

	@Override
	public boolean pass(List<Item> orderItems) {
		long t = 0;
		for (Item oi : orderItems) {
			t += oi.getQuantity() * oi.getPrice();
		}
		return t >= value;
	}

}
