package com.christopher.o2o.enums;

/**
 * @ClassName: ShopStateEnum
 * @Description: 店铺类型的Enum
 * @author christopher chan
 * @date 2018年10月18日
 *
 */
public enum ShopStateEnum {

	CHECK(0, "审核中"), OFFLINE(-1, "非法店铺"), SUCCESS(1, "操作成功"), PASS(2, "通过认证"),
	INNER_ERROR(-1001, "内部系统错误"), NULL_SHOPID(-1002, "shopId为空"),NULL_SHOP(-1003, "shop信息为空"),
	NULL_AREA(-1004, "区域信息为空"),NULL_SHOPCATEGORY(-1005, "店铺类别信息为空")
	;

	// 私有成员变量
	private int state;
	private String stateInfo;

	private ShopStateEnum(int state, String stateInfo) {
		this.state = state;
		this.stateInfo = stateInfo;
	}

	/**
	 * 根据传入的state返回相对应的enum值
	 * @param state 返回结果状态
	 * @return stateEnum
	 * @date 2018年10月18日
	 */
	public static ShopStateEnum stateOf(int state) {
		for (ShopStateEnum stateEnum : ShopStateEnum.values()) {
			if (stateEnum.getState() == state) {
				return stateEnum;
			}
		}
		return null;
	}

	public int getState() {
		return state;
	}

	public String getStateInfo() {
		return stateInfo;
	}

}
