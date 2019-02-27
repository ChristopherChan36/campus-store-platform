/**
 * 本地帐号登出系统的功能
 */
$(function() {
	$("#log-out").click(function() {
		// 清除session
		$.ajax({
			url:"/electronic-shop/local/logout",
			type:"post",
			async:false,
			cache:false,
			success:function(data) {
				if(data.success) {
					var usertype = $("#log-out").attr("usertype");
					// 清除成功后退出到登录界面
					window.location.href = "/electronic-shop/local/login?usertype=" + usertype;
					return false;
				}
			},
			error:function(data, error) {
				alert(error);
			}
		});
	});
});
