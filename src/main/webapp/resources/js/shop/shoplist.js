$(function() {
	//调用渲染 商品列表信息的方法
	getlist();
	
	// 获取商品列表
	function getlist() {
		$.ajax({
			url : "/electronic-shop/shopadmin/getshoplist",
			type : "get",
			dataType : "json",
			success : function(data) {
				if (data.success) {
					handleList(data.shopList);
					handleUser(data.user);
				}
			}
		});
	}
	// 填充用户名称
	function handleUser(data) {
		$('#user-name').text(data.name);
	}
	// 渲染商品列表信息
	function handleList(data) {
		var html = '';
		//遍历填充商铺信息
		data.map(function(item, index) {
			html += '<div class="row row-shop"><div class="col-40">'
					+ item.shopName + '</div><div class="col-40">'
					+ shopStatus(item.enableStatus)
					+ '</div><div class="col-20">'
					+ goShop(item.enableStatus, item.shopId) + '</div></div>';
		});
		$('.shop-wrap').html(html);
	}
	//根据店铺状态 获取状态名称
	function shopStatus(status) {
		if (status == 0) {
			return '审核中';
		} else if (status == -1) {
			return '店铺非法';
		} else {
			return '审核通过';
		}
	}
	//生成进入 编辑店铺页面的 链接
	function goShop(status, id) {
		if (status != 0 && status != -1) {
			return '<a href="/electronic-shop/shopadmin/shopmanagement?shopId='+ id +'">进入</a>';
		} else {
			return '';
		}
	}
});