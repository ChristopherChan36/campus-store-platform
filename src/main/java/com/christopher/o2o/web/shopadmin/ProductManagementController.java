package com.christopher.o2o.web.shopadmin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.christopher.o2o.dto.ImageHolder;
import com.christopher.o2o.dto.ProductExecution;
import com.christopher.o2o.entity.Product;
import com.christopher.o2o.entity.ProductCategory;
import com.christopher.o2o.entity.Shop;
import com.christopher.o2o.enums.ProductStateEnum;
import com.christopher.o2o.exception.ProductOperationException;
import com.christopher.o2o.service.ProductCategoryService;
import com.christopher.o2o.service.ProductService;
import com.christopher.o2o.utils.CodeUtil;
import com.christopher.o2o.utils.HttpServletRequestUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @ClassName: ProductManagementController
 * @Description: 商品管理控制层
 * @author christopher chan
 * @date 2018年11月6日
 *
 */
@RestController
@RequestMapping("/shopadmin")
public class ProductManagementController {

	@Autowired
	private ProductService productService;
	@Autowired
	private ProductCategoryService productCategoryService;

	// 定义支持上传商品详情图片的最大数量
	private static final int IMAGE_MAX_COUNT = 6;

	/**
	 * 新增商品信息
	 * 
	 * @param request
	 * @param productStr
	 *            商品信息
	 * @return
	 * @date 2018年11月6日
	 */
	@RequestMapping(value = "/addproduct", method = RequestMethod.POST)
	private Map<String, Object> addProduct(HttpServletRequest request, @RequestParam("productStr") String productStr) {
		Map<String, Object> modelMap = new HashMap<>();
		// 1.验证码的校验
		if (!CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "您输入的验证码有误！");
			return modelMap;
		}
		// 接收前端参数的变量的初始化，包括商品、缩略图、详情图片列表实体类
		// 将Json字符串转成实体类对象
		ObjectMapper mapper = new ObjectMapper();
		Product product = null;
		ImageHolder thumbnail = null;
		List<ImageHolder> productImgList = new ArrayList<>();
		// 获取文件流对象
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		try {
			// 2.若请求对象中存在文件流，则取出相关的文件（包括缩略图和详情图片）
			if (multipartResolver.isMultipart(request)) {
				thumbnail = handleImage(request, thumbnail, productImgList);
			} else {
				modelMap.put("success", false);
				modelMap.put("errMsg", "上传商品图片不能为空！");
				return modelMap;
			}
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
			return modelMap;
		}
		try {
			// 3.尝试获取前台传过来的表单String流并将其转成Product实体类
			product = mapper.readValue(productStr, Product.class);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
			return modelMap;
		}
		// 4.若Product信息、缩略图以及详情图片列表为非空，则开始进行商品添加操作
		if (product != null && thumbnail != null && productImgList.size() > 0) {
			try {
				// 从session中获取当前店铺的id并且赋值给product，减少对前端数据的依赖
				Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
				product.setShop(currentShop);
				// 执行添加操作
				ProductExecution productExecution = productService.addProduct(product, thumbnail, productImgList);
				if (productExecution.getState() == ProductStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", productExecution.getStateInfo());
				}
			} catch (ProductOperationException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
				return modelMap;
			}
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入商品信息！");
		}
		return modelMap;
	}

	/**
	 * 根据商品id查询商品详细信息
	 * 
	 * @param productId
	 * @return
	 * @date 2018年11月12日
	 */
	@RequestMapping(value = "/getproductbyid", method = RequestMethod.GET)
	private Map<String, Object> getProductById(@RequestParam(value = "productId") Long productId) {
		Map<String, Object> modelMap = new HashMap<>();
		// 非空判断
		if (productId > -1) {
			// 根据商品id获取商品信息
			Product product = productService.getProductById(productId);
			// 并且获取该商品所属店铺下的所有商品类别列表信息
			List<ProductCategory> productCategoryList = productCategoryService
					.getProductCategoryList(product.getShop().getShopId());
			// 存入结果集中
			modelMap.put("product", product);
			modelMap.put("productCategoryList", productCategoryList);
			modelMap.put("success", true);
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty productId !");
		}
		return modelMap;
	}

	@RequestMapping(value = "/modifyproduct", method = RequestMethod.POST)
	private Map<String, Object> modifyProduct(HttpServletRequest request,
			@RequestParam("productStr") String productStr) {
		Map<String, Object> modelMap = new HashMap<>();
		// 是商品编辑时候调用 还是 上下架操作的时候调用
		// 若为前者则进行验证码判断，后者直接跳过验证码的判断
		boolean statusChange = HttpServletRequestUtil.getBoolean(request, "statusChange");
		// 验证码的判断
		if (!statusChange && !CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "输入的验证码有误！");
			return modelMap;
		}
		// 接收前端参数的变量初始化，包括商品，缩略图，详情图片列表实体类
		ObjectMapper mapper = new ObjectMapper();
		Product product = null;
		ImageHolder thumbnail = null;
		List<ImageHolder> productImgList = new ArrayList<>();
		// 新建文件处理对象
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		// 若请求对象域中存在文件流，则取出相关的文件输入流（包括商品缩略图和详情图片）
		try {
			if (multipartResolver.isMultipart(request)) {
				thumbnail = handleImage(request, thumbnail, productImgList);
			}
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
			return modelMap;
		}
		try {
			// 尝试获取前台传过来的表单String流并将其转成Product实体类
			product = mapper.readValue(productStr, Product.class);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
			return modelMap;
		}
		// 非空判断
		if (product != null) {
			try {
				// 从session中获取当前店铺的id并且赋值给product，减少对前端数据的依赖
				Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
				product.setShop(currentShop);
				// 执行添加操作
				ProductExecution productExecution = productService.modifyProduct(product, thumbnail, productImgList);
				if (productExecution.getState() == ProductStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", productExecution.getStateInfo());
				}
			} catch (ProductOperationException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
				return modelMap;
			}
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入商品信息！");
		}
		return modelMap;
	}

	@RequestMapping(value = "/getproductlistbyshop", method = RequestMethod.GET)
	private Map<String, Object> getProductListByShop(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<>();
		// 获取前台传过来的页码
		int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
		// 获取前台传过来的每页最大显示数
		int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
		// 从当前session中获取店铺信息，主要是获取shopId
		Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
		// 空值判断
		if ((pageIndex > -1) && (pageSize > -1) && (currentShop != null) && (currentShop.getShopId() != null)) {
			// 获取传入的需要检索的条件，包括是否需要从某个商品类别以及模糊查询商品名称去筛选某个店铺下的商品列表
			// 筛选的条件可以进行排列组合
			long productCategoryId = HttpServletRequestUtil.getLong(request, "productCategoryId");
			String productName = HttpServletRequestUtil.getString(request, "productName");
			// 对前台传过来的筛选条件进行整合处理
			Product productCondition = compactProductCondition(currentShop.getShopId(), productCategoryId, productName);
			// 传入查询条件以及分页信息进行查询，返回响应的商品列表以及总数
			ProductExecution pe = productService.getProductList(productCondition, pageIndex, pageSize);
			modelMap.put("productList", pe.getProductList());
			modelMap.put("count", pe.getCount());
			modelMap.put("success", true);
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty pageSize or pageIndex or shopId");
		}
		return modelMap;
	}

	/**
	 * 整合处理前台穿过来的筛选条件
	 * @param shopId 商品id
	 * @param productCategoryId 商品列表id
	 * @param productName 商品名称
	 * @return
	 * @date 2018年11月12日
	 */
	private Product compactProductCondition(long shopId, long productCategoryId, String productName) {
		Product productCondition = new Product();
		Shop shop = new Shop();
		shop.setShopId(shopId);
		productCondition.setShop(shop);
		//若有指定类别的要求则添加进筛选条件对象中
		if (productCategoryId != -1L) {
			ProductCategory productCategory = new ProductCategory();
			productCategory.setProductCategoryId(productCategoryId);
			productCondition.setProductCategory(productCategory);
		}
		// 若有商品名称模糊查询的要求则添加
		if (productName != null) {
			productCondition.setProductName(productName);
		}
		return productCondition;
	}

	/**
	 * 处理request域中图片文件流的封装方法
	 * 
	 * @param request
	 *            请求域
	 * @param thumbnail
	 *            缩略图文件流
	 * @param productImgList
	 *            详情图片文件流
	 * @return ImageHolder 缩略图
	 * @throws IOException
	 * @date 2018年11月12日
	 */
	private ImageHolder handleImage(HttpServletRequest request, ImageHolder thumbnail, List<ImageHolder> productImgList)
			throws IOException {
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		// 取出缩略图并且构建ImageHolder对象
		CommonsMultipartFile thumbnailFile = (CommonsMultipartFile) multipartRequest.getFile("thumbnail");
		if (thumbnailFile != null) {
			thumbnail = new ImageHolder(thumbnailFile.getOriginalFilename(), thumbnailFile.getInputStream());
		}
		// 取出详情图片列表并且构建List<ImageHolder>列表对象，最多支持六张图片的上传
		for (int i = 0; i < IMAGE_MAX_COUNT; i++) {
			CommonsMultipartFile productImgFile = (CommonsMultipartFile) multipartRequest.getFile("productImg" + i);
			if (productImgFile != null) {
				// 若取出的第 i个详情图片文件流不为空，则将其加入详情图片列表中
				ImageHolder productImg = new ImageHolder(productImgFile.getOriginalFilename(),
						productImgFile.getInputStream());
				productImgList.add(productImg);
			} else {
				// 若取出的第i个详情图片文件流为空，则终止循环
				break;
			}
		}
		return thumbnail;
	}
}
