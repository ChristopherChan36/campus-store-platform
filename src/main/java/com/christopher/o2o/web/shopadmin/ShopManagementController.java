package com.christopher.o2o.web.shopadmin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.christopher.o2o.dto.ImageHolder;
import com.christopher.o2o.dto.ShopExecution;
import com.christopher.o2o.entity.Area;
import com.christopher.o2o.entity.PersonInfo;
import com.christopher.o2o.entity.Shop;
import com.christopher.o2o.entity.ShopCategory;
import com.christopher.o2o.enums.ShopStateEnum;
import com.christopher.o2o.exception.ShopOperationException;
import com.christopher.o2o.service.AreaService;
import com.christopher.o2o.service.ShopCategoryService;
import com.christopher.o2o.service.ShopService;
import com.christopher.o2o.utils.CodeUtil;
import com.christopher.o2o.utils.HttpServletRequestUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @ClassName: ShopManagementController 
 * @Description: 店铺管理的Controller
 * @author christopher chan
 * @date 2018年10月19日  
 *
 */
@Controller
@RequestMapping("/shopadmin")
public class ShopManagementController {

	@Autowired
	private ShopService shopService;
	@Autowired
	private ShopCategoryService shopCategoryService;
	@Autowired
	private AreaService areaService;
	
	/**
	 * 进入店铺管理页面的controller
	 * @param request
	 * @return
	 * @date 2018年10月27日
	 */
	@RequestMapping(value="/getshopmanagementinfo", method=RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getShopManagementInfo(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<>();
		long shopId = HttpServletRequestUtil.getLong(request, "shopId");
		if(shopId <= 0) {
			Object currentShopObj = request.getSession().getAttribute("currentShop");
			if(currentShopObj == null) {
				modelMap.put("redirect", true);
				modelMap.put("url", "/electronic-shop/shopadmin/shoplist");
			}else {
				Shop currentShop = (Shop) currentShopObj;
				modelMap.put("redirect", false);
				modelMap.put("shopId", currentShop.getShopId());
			}
		}else {
			Shop currentShop = new Shop();
			currentShop.setShopId(shopId);
			request.getSession().setAttribute("currentShop", currentShop);
			modelMap.put("redirect", false);
		}
		return modelMap;
	}
	
	/**
	 * 获取店铺信息的controller 包含分页功能
	 * @param request
	 * @return 
	 * @date 2018年10月27日
	 */
	@RequestMapping(value="/getshoplist", method=RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getShopList(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<>();
		PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
		try {
			Shop shopCondition = new Shop();
			shopCondition.setOwner(user);
			ShopExecution shopExecution = shopService.getShopList(shopCondition, 1, 100);
			// 将该用户可操作的店铺列表信息放入session中作为权限验证的依据，即该帐号只能操作它所拥有的店铺
			request.getSession().setAttribute("shopList", shopExecution.getShopList());
			modelMap.put("shopList", shopExecution.getShopList());
			modelMap.put("user", user);
			modelMap.put("success", true);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
		}
		return modelMap;
	}
	
	@RequestMapping(value="/getshopbyid", method=RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getShopById(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
 		Long shopId = HttpServletRequestUtil.getLong(request, "shopId");
		if(shopId > -1) {
			try {
				Shop shop = shopService.getByShopId(shopId);
				List<Area> areaList = areaService.getAreaList();
				modelMap.put("shop", shop);
				modelMap.put("areaList", areaList);
				modelMap.put("success", true);
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
			}
		}else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty shopId");
		}
		return modelMap;
	}
	
	/**
	 * 店铺修改功能
	 * @param request 请求对象
	 * @return modelMap 返回结果
	 * @date 2018年10月23日
	 */
	@RequestMapping(value = "/modifyshop", method = {RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	private Map<String, Object> modifyShop(HttpServletRequest request) {
		//请求返回结果
		Map<String, Object> modelMap = new HashMap<>();
		//校验验证码的正确性
		if(!CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "您输入的验证码有误！");
			return modelMap;
		}
		//1. 接收并转化相应的请求参数，包括店铺信息以及图片信息
		String shopStr = HttpServletRequestUtil.getString(request, "shopStr");
		//将Json格式的字符串转换为 相对应的实体类对象
		ObjectMapper mapper = new ObjectMapper();
		Shop shop = null;
		try {
			shop = mapper.readValue(shopStr, Shop.class);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
			return modelMap;
		}
		//接收图片，使用CommonsMultipartResolver类处理 - session level
		CommonsMultipartFile shopImg = null;
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		if(commonsMultipartResolver.isMultipart(request)) {
			MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
			shopImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
		}
		//2. 修改店铺
		if(shop != null && shop.getShopId() != null) {
			ShopExecution shopExecution;
			try {
				if(shopImg == null) {
					shopExecution = shopService.modifyShop(shop, null);
				}else {
					ImageHolder shopImgHolder = new ImageHolder(shopImg.getOriginalFilename(),
							shopImg.getInputStream());
					shopExecution = shopService.modifyShop(shop, shopImgHolder);
				}
				if(shopExecution.getState() == ShopStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				}else {
					modelMap.put("success", false);
					modelMap.put("errMsg", shopExecution.getStateInfo());
				}
			} catch (ShopOperationException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
			} catch (IOException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
			}
			return modelMap;
		}else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入店铺信息");
			return modelMap;
		}
	}
	
	/**
	 * 获取店铺页面的 店铺类别和区域信息
	 * @return  modelMap
	 * @date 2018年10月25日
	 */
	@RequestMapping(value="/getshopinitinfo", method=RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getShopInitInfo() {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		List<ShopCategory> shopCategoryList = new ArrayList<>();
		List<Area> areaList = new ArrayList<>();
		try {
			shopCategoryList = shopCategoryService.getShopCategoryList(new ShopCategory());
			areaList = areaService.getAreaList();
			modelMap.put("shopCategoryList", shopCategoryList);
			modelMap.put("areaList", areaList);
			modelMap.put("success", true);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
		}
		return modelMap;
	}
	
	/**
	 * 店铺注册功能
	 * @param request 请求对象
	 * @return modelMap 返回结果
	 * @date 2018年10月23日
	 */
	@RequestMapping(value = "/registshop", method = {RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	private Map<String, Object> registShop(HttpServletRequest request) {
		//请求返回结果
		Map<String, Object> modelMap = new HashMap<>();
		//校验验证码的正确性
		if(!CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "您输入的验证码有误！");
			return modelMap;
		}
		//1. 接收并转化相应的请求参数，包括店铺信息以及图片信息
		String shopStr = HttpServletRequestUtil.getString(request, "shopStr");
		//将Json格式的字符串转换为 相对应的实体类对象
		ObjectMapper mapper = new ObjectMapper();
		Shop shop = null;
		try {
			shop = mapper.readValue(shopStr, Shop.class);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
			return modelMap;
		}
		//接收图片，使用CommonsMultipartResolver类处理 - session level
		CommonsMultipartFile shopImg = null;
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		if(commonsMultipartResolver.isMultipart(request)) {
			MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
			shopImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
		}else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "上传图片不能为空");
			return modelMap;
		}
		//2. 注册店铺
		if(shop != null && shopImg != null) {
			PersonInfo owner = (PersonInfo) request.getSession().getAttribute("user");
			shop.setOwner(owner);
			
			/*File shopImgFile = new File(PathUtil.getImgBasePath() + ImageUtil.getRandomFileName());
			try {
				shopImgFile.createNewFile();
			} catch (IOException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
				return modelMap;
			}*/
			/*try {
				inputStreamToFile(shopImg.getInputStream(), shopImgFile);
			} catch (IOException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
				return modelMap;
			}*/
			
			//店铺注册
			ShopExecution shopExecution;
			try {
				ImageHolder shopImgHolder = new ImageHolder(shopImg.getOriginalFilename(),
						shopImg.getInputStream());
				shopExecution = shopService.addShop(shop, shopImgHolder);
				if(shopExecution.getState() == ShopStateEnum.CHECK.getState()) {
					modelMap.put("success", true);
					//将该店铺 保存到session中的相对应的用户中
					@SuppressWarnings("unchecked")
					List<Shop> shopList = (List<Shop>) request.getSession().
												getAttribute("shopList");
					if(shopList == null || shopList.size() == 0) {
						shopList = new ArrayList<>();
					}
					shopList.add(shopExecution.getShop());
					// 即该用户拥有操作当前新建店铺的权限
					request.getSession().setAttribute("shopList", shopList);
				}else {
					modelMap.put("success", false);
					modelMap.put("errMsg", shopExecution.getStateInfo());
				}
			} catch (ShopOperationException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
			} catch (IOException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
			}
			return modelMap;
		}else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入店铺信息");
			return modelMap;
		}
	}
	
	/**
	 * 将InputStream类型的数据流转换成File类型
	 * @param inputStream
	 * @param file
	 * @date 2018年10月23日
	 */
	/*private static void inputStreamToFile(InputStream inputStream, File file) {
		OutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream(file);
			int bytesRead = 0;
			byte[] buffer = new byte[1024];
			while((bytesRead = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, bytesRead);
			}
		} catch (Exception e) {
			throw new RuntimeException("调用inputStreamToFile产生异常：" + e.getMessage());
		} finally {
			try{
				if(outputStream != null) {
					outputStream.close();
				}
				if(inputStream != null) {
					inputStream.close();
				}
			} catch (IOException e) {
				throw new RuntimeException("inputStreamToFile关闭io产生异常：" + e.getMessage());
			}
		}
	}*/
}
