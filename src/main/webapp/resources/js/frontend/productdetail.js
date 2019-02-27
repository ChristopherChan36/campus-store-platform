$(function() {
	var productId = getQueryString('productId');
	var productUrl = '/electronic-shop/frontend/listproductdetailpageinfo?productId='
			+ productId;

	$
			.getJSON(
					productUrl,
					function(data) {
						if (data.success) {
							// 获取商品信息
							var product = data.product;
							// 给商品信息相关HTML空间赋值
							
							// 商品缩略图
							$('#product-img').attr('src', product.imgAddr);
							// 商品更新时间
							$('#product-time').text(
									new Date(product.lastEditTime)
											.Format("yyyy-MM-dd"));
							if (product.point != undefined) {
								$('#product-point').text(
										'购买可得' + product.point + '积分');
							}
							// 商品名称
							$('#product-name').text(product.productName);
							// 商品简介
							$('#product-desc').text(product.productDesc);
							// 商品价格展示逻辑，主要判断原价现价是否为空，所有都为空则不显示该价格栏
							if (product.normalPrice != undefined
									&& product.promotionPirce != undefined) {
								$('#price').show();
								$('#normalPrice').text(
										'<del>' + '￥' + product.normalPrice
												+ '</del>');
								$('#promotionPrice').text(
										'￥' + product.promotionPirce);
							} else if (product.normalPrice != undefined
									&& product.promotionPirce == undefined) {
								$('#price').show();
								$('#promotionPrice').text(
										'￥' + product.normalPrice);
							} else if (product.normalPrice == undefined
									&& product.promotionPirce != undefined) {
								$('#promotionPrice').text(
										'￥' + product.promotionPrice);
							}
							var imgListHtml = '';
							product.productImgList.map(function(item, index) {
								imgListHtml += '<div> <img src="'
										+ item.imgAddr
										+ '" width="100%" /></div>';
							});
							/*if (data.needQRCode) {
								// 生成购买商品的二维码供商家扫描
								imgListHtml += '<div> <img src="/myo2o/frontend/generateqrcode4product?productId='
										+ product.productId
										+ '" width="100%"/></div>';
							}*/
							$('#imgList').html(imgListHtml);
						}
					});
	$('#me').click(function() {
		$.openPanel('#panel-left-demo');
	});
	$.init();
});
