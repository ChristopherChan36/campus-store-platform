/**
 * 微信用户绑定系统本地帐号功能
 */
$(function() {
	// 绑定帐号的controller url
	var bindUrl = '/electronic-shop/local/bindlocalauth';
	// 从地址栏的URL中获取 usertype字段信息
	// usertype=1，则为前端展示系统，2为店家管理系统
	var usertype = getQueryString("usertype");
	$('#submit').click(function() {
		// 获取输入的帐号
		var username = $('#username').val();
		// 获取输入的密码
		var password = $('#psw').val();
		// 获取输入的验证码信息
		var verifyCodeActual = $('#j_kaptcha').val();
		// 是否需要进行验证码校验的 标识符
		var needVerify = false;
		if (!verifyCodeActual) {
			$.toast('请输入验证码！');
			return;
		}
		// 访问后台，绑定本地帐号信息
		$.ajax({
			url : bindUrl,
			async : false,
			cache : false,
			type : "post",
			dataType : 'json',
			data : {
				"username" : username,
				"password" : password,
				"verifyCodeActual" : verifyCodeActual
			},
			success : function(data) {
				if (data.success) {
					$.toast('帐号绑定成功！');
					if(usertype == 1) {
						window.location.href = '/electronic-shop/frontend/index';
					}else {
						window.location.href = '/electronic-shop/shopadmin/shoplist';
					}
				} else {
					$.toast('帐号绑定失败:' + data.errMsg);
					$('#kaptcha_img').click();
				}
			}
		});
	});
});