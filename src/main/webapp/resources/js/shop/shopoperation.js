/**
 * 
 */
$(function() {
	var shopId = getQueryString("shopId");
	var isEdit = shopId ? true:false;
	var initUrl = '/electronic-shop/shopadmin/getshopinitinfo';
	var registShopUrl = '/electronic-shop/shopadmin/registshop';
	var shopInfoUrl = '/electronic-shop/shopadmin/getshopbyid?shopId=' + shopId;
	var editShopUrl = '/electronic-shop/shopadmin/modifyshop';
	
	if(!isEdit) {
		getShopInitInfo();
	}else {
		getShopInfo(shopId);
	}
	
	//获取店铺信息
	function getShopInfo(shopId) {
		$.getJSON(shopInfoUrl, function(data) {
			if(data.success) {
				var shop = data.shop;
				$('#shop-name').val(shop.shopName);
				$('#shop-addr').val(shop.shopAddr);
				$('#shop-phone').val(shop.phone);
				$('#shop-desc').val(shop.shopDesc);
				var shopCategory = '<option data-id="'
						+ shop.shopCategory.shopCategoryId + '" selected>'
						+ shop.shopCategory.shopCategoryName + '</option>';
				var tempAreaHtml = '';
				data.areaList.map(function(item, index) {
					tempAreaHtml += '<option data-id="' + item.areaId + '">'
							+ item.areaName + '</option>';
				});
				$('#shop-category').html(shopCategory);
				$('#shop-category').attr('disabled','disabled');
				$('#area').html(tempAreaHtml);
				//对修改店铺所属区域默认选中
				$("#area option[data-id='"+shop.area.areaId+"']").attr("selected","selected");
			}
		});
	}
	
	//获取店铺的基本信息
	function getShopInitInfo() {
		//通过 HTTP GET 请求载入JSON数据
		$.getJSON(initUrl, function(data) {
			if(data.success) {
				//店铺分类
				var tempHtml = '';
				//区域
				var tempAreaHtml = '';
				data.shopCategoryList.map(function(item, index) {
					tempHtml += '<option data-id="' + item.shopCategoryId
						+ '">' + item.shopCategoryName + '</option>';
				});
				data.areaList.map(function(item, index) {
					tempAreaHtml += '<option data-id="' + item.areaId +
						'">' + item.areaName + '</option>';
				});
				$("#shop-category").html(tempHtml);
				$("#area").html(tempAreaHtml);
			}
		});
	}
	
	//表单提交初始化
	$("#submit").click(function() {
		//新建一个shop对象
		var shop = new Object();
		if(isEdit) {
			shop.shopId = shopId;
		}
		shop.shopName = $('#shop-name').val();
		shop.shopAddr = $('#shop-addr').val();
		shop.phone = $('#shop-phone').val();
		shop.shopDesc = $('#shop-desc').val();
		shop.shopCategory = {
			shopCategoryId:$('#shop-category').find('option').not(function() {
				return !this.selected;
			}).data('id')
		};
		shop.area = {
			areaId:$('#area').find('option').not(function() {
				return !this.selected;
			}).data('id')
		};
		var shopImg = $('#shop-img')[0].files[0];
		var formData = new FormData();
		formData.append('shopImg', shopImg);
		formData.append('shopStr', JSON.stringify(shop));
		var verifyCodeActual = $('#j_kaptcha').val();
		if(!verifyCodeActual) {
			$.toast("请输入验证码！");
			return;
		}
		formData.append("verifyCodeActual", verifyCodeActual);
		
		$.ajax({
			url:(isEdit?editShopUrl:registShopUrl),
			type:'POST',
			data:formData,
			contentType:false,
			processData:false,
			cache:false,
			success:function(data) {
				if(data.success) {
					$.toast("提交成功！");
				}else{
					$.toast("提交失败！" + data.errMsg);
				}
				$('#kaptcha_img').click();
			}
		});
	});
});