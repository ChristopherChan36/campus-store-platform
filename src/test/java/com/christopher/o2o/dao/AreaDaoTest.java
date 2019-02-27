package com.christopher.o2o.dao;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.christopher.o2o.BaseTest;
import com.christopher.o2o.entity.Area;

/**
 * @ClassName: AreaDaoTest 
 * @Description: 测试dao  
 * @author christopher chan
 * @date 2018年10月13日  
 *
 */
public class AreaDaoTest extends BaseTest {

	//引入AreaDao
	@Autowired
	private AreaDao areaDao;
	
	@Test
	public void testQueryArea() {
		
		List<Area> areaList = areaDao.queryArea();
		assertEquals(2,areaList.size());
//		System.out.println(areaList);
		
	}
	
}
