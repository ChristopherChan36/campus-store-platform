package com.christopher.o2o;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @ClassName: BaseTest
 * @Description: 配置Spring和junit整合，junit启动时加载Spring IOC容器
 * @author christopher chan
 * @date 2018年10月13日
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
// 告诉junit 加载spring配置文件的位置
@ContextConfiguration({
	"classpath:spring/spring-dao.xml", 
	"classpath:spring/spring-service.xml",
	"classpath:spring/spring-redis.xml"
})
public class BaseTest {

}
