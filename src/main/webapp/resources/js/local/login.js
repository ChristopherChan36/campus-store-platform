/**
 * 本地帐号登录功能
 */
$(function() {
	// 登录验证的controller url
	var loginCheckUrl = '/electronic-shop/local/logincheck';
	// 从地址栏里的URL里获取usertype
	// usertype=1,则为客户，其余为店家
	var usertype = getQueryString("usertype");
	// 登录次数，累计登录三次失败之后自动弹出验证码要求输入
	var loginCount = 0;

	$('#submit').click(function() {
		// 获取输入的帐号
		var username = $('#username').val();
		// 获取输入的密码
		var password = $('#psw').val();
		// 获取用户输入的验证码信息
		var verifyCodeActual = $('#j_kaptcha').val();
		// 是否需要验证码验证的标识，默认为false，即不需要
		var needVerify = false;
		// 如果登录次数超过三次
		if (loginCount >= 3) {
			// 那么就需要验证码校验登录信息
			if (!verifyCodeActual) {
				$.toast('请输入验证码！');
				return;
			} else {
				needVerify = true;
			}
		}
		// 访问后台进行登录验证
		$.ajax({
			url : loginCheckUrl,
			async : false,
			cache : false,
			type : "post",
			dataType : 'json',
			data : {
				"username" : username,
				"password" : password,
				"verifyCodeActual" : verifyCodeActual,
				"needVerify" : needVerify
			},
			success : function(data) {
				if (data.success) {
					$.toast('登录成功！');
					if(usertype == 1) {
						// 若用户在前端展示系统页面则自动链接到前端展示系统首页
						window.location.href = '/electronic-shop/frontend/index';
					}else if(usertype == 2) {
						// 店家身份返回值店家管理系统首页
						window.location.href = '/electronic-shop/shopadmin/shoplist';
					}else {
						$.toast("系统未知身份！");
					}
				} else {
					$.toast('登录失败:' + data.errMsg);
					// 登录失败次数累计+1
					loginCount++;
					// 登录失败次数超过三次，则显示验证码校验功能
					if (loginCount >= 3) {
						$('#verifyPart').show();
					}
				}
			}
		});
	});

	$('#register').click(function() {
		window.location.href = '/myo2o/shop/register';
	});
});