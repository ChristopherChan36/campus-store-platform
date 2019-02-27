/**
 * 修改密码的功能js块
 */
$(function() {
	// 修改平台密码的controller url
	var url = "/electronic-shop/local/changelocalpwd";
	// 从地址栏中的URL获取usertype
	var usertype = getQueryString("usertype");
	$("#submit").click(function() {
		// 获取帐号
		var username = $("#username").val();
		// 获取原密码
		var password = $("#password").val();
		// 获取新密码
		var newPassword = $("#newPassword").val();
		// 确认密码
		var confirmPassword = $("#confirmPassword").val();
		if(newPassword != confirmPassword) {
			$.toast("两次输入的新密码不一致！");
			return;
		}
		// 添加表单数据，新建FormData表单对象
		var formData = new FormData();
		formData.append("username", username);
		formData.append("password", password);
		formData.append("newPassword", newPassword);
		// 获取验证码
		var verifyCodeActual = $("#j_kaptcha").val();
		if(!verifyCodeActual) {
			$.toast("请输入验证码！");
			return;
		}
		formData.append("verifyCodeActual", verifyCodeActual);
		// 将参数以post请求方式访问后台，修改帐号密码
		$.ajax({
			url : url,
			type : 'POST',
			data : formData,
			contentType : false,
			processData : false,
			cache : false,
			success : function(data) {
				if (data.success) {
					$.toast('密码修改成功！');
					if(usertype == 1) {
						window.location.href = '/electronic-shop/frontend/index';
					}else {
						window.location.href = '/electronic-shop/shopadmin/shoplist';
					}
				} else {
					$.toast('密码修改失败: ' + data.errMsg);
					$('#kaptcha_img').click();
				}
			}
		});
	});
	
	$("#back").click(function() {
		window.location.href = "/electronic-shop/shopadmin/shoplist";
	});
});
