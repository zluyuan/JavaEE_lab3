package cn.edu.xmu.oomall.goods.mapper.po.strategy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * @author xincong yao
 * @date 2020-11-18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class BaseCouponDiscount implements Computable{

	public BaseCouponDiscount(BaseCouponLimitation limitation, long value) {
		this.couponLimitation = limitation;
		this.value = value;
		this.className = this.getClass().getName();
	}

	protected long value;

	protected String className;

	protected BaseCouponLimitation couponLimitation;

	@Override
	public List<Item> compute(List<Item> orderItems) {
		if (!couponLimitation.pass(orderItems)) {
			for (Item oi : orderItems) {
				oi.setCouponActivityId(null);
			}
			return orderItems;
		}

		calcAndSetDiscount(orderItems);

		return orderItems;
	}

	public abstract void calcAndSetDiscount(List<Item> orderItems);


}
