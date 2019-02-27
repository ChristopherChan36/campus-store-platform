package com.christopher.o2o.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.christopher.o2o.dao.ProductDao;
import com.christopher.o2o.dao.ProductImgDao;
import com.christopher.o2o.dto.ImageHolder;
import com.christopher.o2o.dto.ProductExecution;
import com.christopher.o2o.entity.Product;
import com.christopher.o2o.entity.ProductImg;
import com.christopher.o2o.enums.ProductStateEnum;
import com.christopher.o2o.exception.ProductCategoryOperationException;
import com.christopher.o2o.exception.ProductOperationException;
import com.christopher.o2o.service.ProductService;
import com.christopher.o2o.utils.ImageUtil;
import com.christopher.o2o.utils.PageCalculator;
import com.christopher.o2o.utils.PathUtil;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductDao productDao;
	@Autowired
	private ProductImgDao productImgDao;

	@Override
	@Transactional
	public ProductExecution addProduct(Product product, ImageHolder thumbnail, List<ImageHolder> productImgList)
			throws ProductOperationException {
		/*
		 * 执行步骤： 
		 * 1.处理缩略图上传，获取缩略图相对路径并赋值给 product商品信息 
		 * 2.往tb_product写入商品信息，获取写入商品的productId 
		 * 3.结合productId批量添加其商品详情图片信息 
		 * 4.将商品详情图片列表信息批量插入tb_product_img中
		 */
		// 空值判断 -> 商品信息以及商品所属店铺信息不为空
		if (product != null && product.getShop() != null && product.getShop().getShopId() != null) {
			// 给商品设置默认属性 -> 商品信息录入时间以及最新修改时间
			product.setCreateTime(new Date());
			product.setLastEditTime(new Date());
			// 设置商品状态为： 上架状态
			product.setEnableStatus(1);
			// 上传商品缩略图，若商品缩略图不为空则添加
			if (thumbnail != null) {
				addThumbnail(product, thumbnail);
			}
			try {
				// 添加商品信息
				int effectedNum = productDao.insertProduct(product);
				if (effectedNum <= 0) {
					throw new ProductCategoryOperationException("创建商品失败！");
				}
			} catch (ProductCategoryOperationException e) {
				throw new ProductCategoryOperationException("创建商品失败： " + e.getMessage());
			}
			// 若商品详情图片信息不为空则添加
			if (productImgList != null && productImgList.size() > 0) {
				addProductImgList(product, productImgList);
			}
			return new ProductExecution(ProductStateEnum.SUCCESS, product);
		} else {
			// 传参为空则返回空值错误信息状态
			return new ProductExecution(ProductStateEnum.EMPTY);
		}
	}
	
	@Override
	public Product getProductById(long productId) {

		return productDao.queryProductById(productId);
	}

	@Override
	@Transactional
	public ProductExecution modifyProduct(Product product, ImageHolder thumbnail, List<ImageHolder> productImgList)
			throws ProductOperationException {
		/*
		 * 1.若缩略图参数有值，则处理缩略图信息；若原先存在缩略图则先删除再添加新图，之后获取缩略图相对路径并且赋值给product
		 * 2.若商品详情图片列表参数有值，对商品详情图片列表进行同样的操作
		 * 3.将tb_product_img下面的该商品原先的商品详情图片记录全部清除
		 * 4.更新tb_product_img以及tb_product的信息
		 */
		// 空值判断
		if (product != null && product.getShop() != null && product.getShop().getShopId() != null) {
			// 给商品设置默认属性
			product.setLastEditTime(new Date());
			// 若商品缩略图不为空且原有缩略图不为空则删除原有缩略图信息并且添加需要修改的缩略图信息
			if (thumbnail != null) {
				// 首先获取商品的原有信息，因为原来的信息里有原图片的地址
				Product tempProduct = productDao.queryProductById(product.getProductId());
				// 如果该商品存在缩略图地址信息，则删除原有缩略图信息
				if (tempProduct.getImgAddr() != null) {
					ImageUtil.deleteFileOrPath(tempProduct.getImgAddr());
				}
				addThumbnail(product, thumbnail);
			}
			// 如果有新存入的商品详情图片，则将原先的删除，并添加新的图片
			if(productImgList != null && productImgList.size() > 0) {
				// 删除该商品下所有的详情图片信息
				deleteProductImgList(product.getProductId());
				// 添加商品详情图片信息
				addProductImgList(product, productImgList);
			}
			try {
				// 更新商品信息
				int effectedNum = productDao.updateProduct(product);
				if (effectedNum <= 0) {
					throw new RuntimeException("更新商品信息失败");
				}
				return new ProductExecution(ProductStateEnum.SUCCESS, product);
			} catch (ProductOperationException e) {
				throw new ProductOperationException("更新商品信息失败:" + e.toString());
			}
		} else {
			return new ProductExecution(ProductStateEnum.EMPTY);
		}
	}

	/**
	 * 根据商品id删除该商品下的所有详情图片信息
	 * @param productId 商品id
	 * @date 2018年11月9日
	 */
	private void deleteProductImgList(Long productId) {
		// 根据productId获取该商品下的详情图片信息
		List<ProductImg> productImgList = productImgDao.queryProductImgList(productId);
		// 根据详情图片的物理地址删除详情图片资源
		for (ProductImg productImg : productImgList) {
			ImageUtil.deleteFileOrPath(productImg.getImgAddr());
		}
		// 删除tb_product_img的相关信息
		productImgDao.deleteProductImgByProductId(productId);
	}

	/**
	 * 批量添加商品详细图片信息
	 * 
	 * @param product
	 *            商品信息
	 * @param productImgHolderList
	 *            商品详情图片信息
	 * @date 2018年11月3日
	 */
	private void addProductImgList(Product product, List<ImageHolder> productImgHolderList) {
		// 获取图片存储路径，这里直接存放到相应店铺的文件夹底下
		String dest = PathUtil.getShopImagePath(product.getShop().getShopId());
		List<ProductImg> productImgList = new ArrayList<>();
		// 遍历图片一次去处理，并且添加到productImg实体类中
		for (ImageHolder productImgHolder : productImgHolderList) {
			String imgAddr = ImageUtil.generateNormalImg(productImgHolder, dest);
			// 新建商品详情图片对象存储生成的图片信息
			ProductImg productImg = new ProductImg();
			productImg.setImgAddr(imgAddr);
			productImg.setProductId(product.getProductId());
			productImg.setCreateTime(new Date());
			productImgList.add(productImg);
		}
		// 如果确实是有详情图片信息需要添加，就执行商品详情图片的批量操作
		if (productImgList.size() > 0) {
			try {
				int effectedNum = productImgDao.batchInsertProductImg(productImgList);
				if (effectedNum <= 0) {
					throw new ProductOperationException("创建商品详情图片失败！");
				}
			} catch (ProductOperationException e) {
				throw new ProductOperationException("创建商品详情图片信息失败： " + e.toString());
			}
		}
	}

	/**
	 * 添加商品缩略图信息
	 * 
	 * @param product
	 *            商品信息
	 * @param thumbnail
	 *            商品缩略图信息
	 * @date 2018年11月3日
	 */
	private void addThumbnail(Product product, ImageHolder thumbnail) {
		// 获取图片保存物理地址的相对路径
		String dest = PathUtil.getShopImagePath(product.getShop().getShopId());
		// 上传商品缩略图信息
		String thumbnailAddr = ImageUtil.generateThumbnail(thumbnail, dest);
		// 在商品信息中插入商品缩略图的相对路径
		product.setImgAddr(thumbnailAddr);
	}

	@Override
	public ProductExecution getProductList(Product productCondition, int pageIndex, int pageSize) {
		// 页码转换成数据库的行码，并调用dao层取回指定页码的商品列表
		int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
		List<Product> productList = productDao.queryProductList(productCondition, rowIndex, pageSize);
		// 基于同样的查询条件返回该查询条件下的商品总数
		int count = productDao.queryProductCount(productCondition);
		ProductExecution productExecution = new ProductExecution();
		productExecution.setProductList(productList);
		productExecution.setCount(count);
		return productExecution;
	}

}
