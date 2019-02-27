package com.christopher.o2o.utils;

public class PageCalculator {

	/**
	 * 页码转换成数据库的行码
	 * @param pageIndex
	 * @param pageSize
	 * @return rowIndex
	 * @date 2018年10月27日
	 */
	public static int calculateRowIndex(int pageIndex, int pageSize) {
		return (pageIndex > 0) ? (pageIndex - 1)*pageSize : 0;
	}
}
