$(function() {
	// 获取指定商铺下的商品列表的URL
	var listUrl = '/electronic-shop/shopadmin/getproductlistbyshop?pageIndex=1&pageSize=999';
	// 商品下架的URL
	var statusUrl = '/electronic-shop/shopadmin/modifyproduct';
	getList();
	
	/**
	 * 获取指定店铺下的商品列表信息
	 * @returns
	 */
	function getList() {
		//通过ajax操作从后台获取此店铺的商品列表
		$.getJSON(listUrl, function(data) {
			if(data.success) {
				var productList = data.productList;
				var tempHtml = '';
				// 遍历每条商品信息，拼接成一行显示，列信息包括：
				// 商品名称，优先级，上架/下架（包含productId），编辑按钮（包含productId）
				// 预览（包含productId）
				productList.map(function(item, index) {
					// 商品默认为下架状态
					var textOp = '下架';
					var contraryStatus = 0;
					if(item.enableStatus == 0) {
						//若商品状态值为0，表明是已下架的商品，操作变更为上架（即点击上架按钮上架相关商品）
						textOp = '上架';
						contraryStatus = 1;
					}else {
						contraryStatus = 0;
					}
					// 拼接每件商品的行信息
					tempHtml += '' + '<div class="row row-product" align="center">'
					+ '<div class="col-33">'
					+ item.productName
					+ '</div>'
					+ '<div class="col-20">'
					+ item.point
					+ '</div>'
					+ '<div class="col-40">'
					+ '<a href="#" class="edit" data-id="'
					+ item.productId
					+ '" data-status="'
					+ item.enableStatus
					+ '">编辑</a>'
					+ '<a href="#" class="status" data-id="'
					+ item.productId
					+ '" data-status="'
					+ contraryStatus
					+ '">'
					+ textOp
					+ '</a>'
					+ '<a href="#" class="preview" data-id="'
					+ item.productId
					+ '" data-status="'
					+ item.enableStatus
					+ '">预览</a>'
					+ '</div>'
					+ '</div>';
				});
				$('.product-wrap').html(tempHtml);
			}
		});
	}
	
	// 将class为product-wrap里面的a标签绑定上点击的事件
	$(".product-wrap").on("click", "a", function(event) {
		var target = $(event.currentTarget);
		if (target.hasClass('edit')) {
			// 如果class edit 则点击就进入店铺信息编辑页面，并带有productId参数
			window.location.href = '/electronic-shop/shopadmin/productoperation?productId='
					+ event.currentTarget.dataset.id;
		} else if (target.hasClass('status')) {
			// 如果class status 则调用后台功能上/下架相关商品，并带有productId参数
			changeItemStatus(event.currentTarget.dataset.id,
					event.currentTarget.dataset.status);
		} else if (target.hasClass('preview')) {
			// 如果class preview 则去前台展示系统该商品详情页预览商品情况，并带有productId参数
			window.location.href = '/electronic-shop/frontend/productdetail?productId='
					+ event.currentTarget.dataset.id;
		}
	});
	//商品的上/下架功能
	function changeItemStatus(productId, enableStatus) {
		var product = {};
		product.productId = productId;
		product.enableStatus = enableStatus;
		$.confirm("确定么？", function() {
			$.ajax({
				url : statusUrl,
				type : 'POST',
				data : {
					productStr : JSON.stringify(product),
					statusChange : true
				},
				dataType : 'json',
				success : function(data) {
					if (data.success) {
						$.toast('操作成功！');
						getList();
					} else {
						$.toast('操作失败！');
					}
				}
			});
		});
	}
});