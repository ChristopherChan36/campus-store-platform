$(function() {
	// 定义加载标识符
	var loading = false;
	// 分页允许返回的最大条数，超过此数则禁止访问后台
	var maxItems = 999;
	// 默认一页返回的商品数
	var pageSize = 3;
	// 列出查询商品列表的URL
	var listUrl = '/electronic-shop/frontend/listproductsbyshop';
	// 默认页码从第一页开始
	var pageNum = 1;
	// 从地址栏中获取shopId
	var shopId = getQueryString('shopId');
	// 商品类别id
	var productCategoryId = '';
	// 商品名称
	var productName = '';
	// 获取本店铺信息以及商品类别信息列表的URL
	var searchDivUrl = '/electronic-shop/frontend/listshopdetailpageinfo?shopId='
			+ shopId;
	// 渲染出店铺基本信息以及商品类别信息以供搜索
	getSearchDivData();
	// 预先加载商品信息
	addItems(pageSize, pageNum);
	
	// 给兑换礼品的a标签赋值兑换礼品的URL， 2.0讲解
	//$("#exchangelist").attr("href", "/electronic-shop/frontend/awardlist?shopId=" + shopId);
	
	/**
	 * 获取本店铺信息以及该店铺下的商品类别信息列表
	 * @returns
	 */
	function getSearchDivData() {
		var url = searchDivUrl;
		$.getJSON(url,function(data) {
			if (data.success) {
				var shop = data.shop;
				$('#shop-cover-pic').attr('src', shop.shopImg);
				$('#shop-update-time').html(
						new Date(shop.lastEditTime)
								.Format("yyyy-MM-dd"));
				$('#shop-name').html(shop.shopName);
				$('#shop-desc').html(shop.shopDesc);
				$('#shop-addr').html(shop.shopAddr);
				$('#shop-phone').html(shop.phone);
				// 获取后台返回的该店铺下的商品列表
				var productCategoryList = data.productCategoryList;
				var html = '';
				// 遍历商品列表，生成可以点击搜索相应商品类别下的商品的a标签
				productCategoryList
						.map(function(item, index) {
							html += '<a href="#" class="button" data-product-search-id='
									+ item.productCategoryId
									+ '>'
									+ item.productCategoryName
									+ '</a>';
						});
				// 将商品类别a标签绑定到响应的HTML控件中
				$('#shopdetail-button-div').html(html);
			}
		});
	}
	
	/**
	 * 获取分页展示的商品列表信息
	 * @param pageSize
	 * @param pageIndex
	 * @returns
	 */
	function addItems(pageSize, pageIndex) {
		// 拼接出查询的URL,赋空值就表示去掉这个查询条件的限制，有值就代表按照当前条件进行筛选
		var url = listUrl + '?' + 'pageIndex=' + pageIndex + '&pageSize='
				+ pageSize + '&productCategoryId=' + productCategoryId
				+ '&productName=' + productName + '&shopId=' + shopId;
		// 设定加载符，若还在后台取数据则不能再次访问后台，避免多次重复加载
		loading = true;
		// 访问后台获取相应查询条件下的商品列表信息
		$.getJSON(url, function(data) {
			if (data.success) {
				// 获取当前查询条件下返回的商品总数
				maxItems = data.count;
				var html = '';
				// 遍历商品列表，拼接出卡片集合
				data.productList.map(function(item, index) {
					html += '' + '<div class="card" data-product-id='
							+ item.productId + '>'
							+ '<div class="card-header">' + item.productName
							+ '</div>' + '<div class="card-content">'
							+ '<div class="list-block media-list">' + '<ul>'
							+ '<li class="item-content">'
							+ '<div class="item-media">' + '<img src="'
							+ item.imgAddr + '" width="44">' + '</div>'
							+ '<div class="item-inner">'
							+ '<div class="item-subtitle">' + item.productDesc
							+ '</div>' + '</div>' + '</li>' + '</ul>'
							+ '</div>' + '</div>' + '<div class="card-footer">'
							+ '<p class="color-gray">'
							+ new Date(item.lastEditTime).Format("yyyy-MM-dd")
							+ '更新</p>' + '<span>点击查看</span>' + '</div>'
							+ '</div>';
				});
				// 将卡片集合添加到目标HTML控件中
				$('.list-div').append(html);
				// 获取目前为止已显示的卡片总数，包含之前已经加载的
				var total = $('.list-div .card').length;
				// 若总数达到跟按照此查询条件列出来的结果总数一致，则停止后台的加载
				if (total >= maxItems) {
					// 隐藏加载提示符
    				$('.infinite-scroll-preloader').hide();
				}else {
					// 显示加载提示符
    				$(".infinite-scroll-preloader").show();
				}
				// 否则页码数加1，继续加载出新的商品卡片信息
				pageNum += 1;
				// 加载结束，可以再次进行加载
				loading = false;
				//容器发生改变,如果是js滚动，需要刷新滚动
				$.refreshScroller();
			}
		});
	}

	 // 下滑屏幕自动进行分页搜索- 无限滚动事件
	$(document).on('infinite', '.infinite-scroll-bottom', function() {
		if (loading)
			return;
		// 模拟js的加载过程
    	loading = true;
    	setTimeout(function() {
    		// 重置加载flag
    		loading = false;
    		addItems(pageSize, pageNum);
    	}, 1000);
	});

	// 点击商品卡片进入该商品的详情页面
	$('.list-div').on('click', '.card',function(e) {
		var productId = e.currentTarget.dataset.productId;
		window.location.href = '/electronic-shop/frontend/productdetail?productId='
				+ productId;
	});
	
	// 选择新的商品类别之后，重置分页页码，清空原先的商品列表信息，按照新的类别去查询
	$('#shopdetail-button-div').on('click', '.button', function(e) {
		// 获取当前选中的商品类别的id(使用的是 h5的 dataset API)
		productCategoryId = e.target.dataset.productSearchId;
		if (productCategoryId) {
			//若之前已选定了别的category，则移除其选定效果，改成选定新的
			if ($(e.target).hasClass('button-fill')) {//当前选中的商品类别已被选中
				// 则移除当前类别按钮的选中效果
				$(e.target).removeClass('button-fill');
				// 并且将商品类别id置为空
				productCategoryId = '';
			} else {// 若当前商品类别未选中
				// 则将当前选中的类别按钮置为选定状态，其他类别按钮置为非选定状态
				$(e.target).addClass('button-fill').siblings()
						.removeClass('button-fill');
			}
			// 由于查询条件改变，那么清空商品列表再重新进行查询
			$('.list-div').empty();
			// 重置页码
			pageNum = 1;
			addItems(pageSize, pageNum);
		}
	});

	// 需要查询的商品名称发生改变，重置页码，清空原先的商品列表，按照新的商品名称进行模糊查询
	$('#search').on('change', function(event) {
		productName = event.target.value;
		// 清空商品列表
		$('.list-div').empty();
		// 重置页码
		pageNum = 1;
		addItems(pageSize, pageNum);
	});

	// 点击后打开右侧栏信息
	$('#me').click(function() {
		$.openPanel('#panel-right-demo');
	});
	
	// 初始化页面
	$.init();
});
