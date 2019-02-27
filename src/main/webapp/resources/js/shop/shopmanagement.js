$(function() {
	// 获取URL，根据参数名称 获取参数值
	var shopId = getQueryString("shopId");
	//进入店铺管理页面
	var shopInfoUrl = "/electronic-shop/shopadmin/getshopmanagementinfo?shopId="
			+ shopId;
	$.getJSON(shopInfoUrl, function(data) {
		if (data.redirect) {
			window.location.href = data.url;
		} else {
			if (data.shopId != undefined && data.shopId != null) {
				shopId = data.shopId;
			}
			$("#shopInfo")
					.attr(
							"href",
							"/electronic-shop/shopadmin/shopoperation?shopId="
									+ shopId);
		}
	});
});