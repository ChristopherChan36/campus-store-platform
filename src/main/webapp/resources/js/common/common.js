/**
 * 更换验证码图片
 */
function changeVerifyCode(img) {
	img.src = "../kaptcha?" + Math.floor(Math.random()*100);
}

/**
 * 获取URL，根据参数名称 获取参数值
 * @param name 参数名称
 * @return 参数值
 */
function getQueryString(name) {
	//正则匹配
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
	//获取地址栏的url并且匹配正则表达式获取参数
	var r = window.location.search.substr(1).match(reg);
	if (r != null) {
		return decodeURIComponent(r[2]);
	}
	return '';
}

/**
 * 格式化日期字符串
 */
Date.prototype.Format = function(fmt) {
	var o = {
		"M+" : this.getMonth() + 1, // 月份
		"d+" : this.getDate(), // 日
		"h+" : this.getHours(), // 小时
		"m+" : this.getMinutes(), // 分
		"s+" : this.getSeconds(), // 秒
		"q+" : Math.floor((this.getMonth() + 3) / 3), // 季度
		"S" : this.getMilliseconds()
	// 毫秒
	};
	if (/(y+)/.test(fmt))
		fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "")
				.substr(4 - RegExp.$1.length));
	for ( var k in o)
		if (new RegExp("(" + k + ")").test(fmt))
			fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k])
					: (("00" + o[k]).substr(("" + o[k]).length)));
	return fmt;
}