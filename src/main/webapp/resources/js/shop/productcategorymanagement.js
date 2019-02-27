$(function() {
	var listUrl = '/electronic-shop/shopadmin/getproductcategorylist';
	var addUrl = "/electronic-shop/shopadmin/addproductcategories";
	var deleteUrl = "/electronic-shop/shopadmin/removeproductcategory";
	
	getProductCategoryList();
	//获取商品类别列表
	function getProductCategoryList() {
		$.getJSON(listUrl, function(data) {
			if(data.success) {
				var dataList = data.data;
				$(".category-wrap").html("");
				//遍历数据集合中的信息
				var tempHtml = "";
				dataList.map(function(item, index) {
					tempHtml += ''
						+ '<div class="row row-product-category now">'
						+ '<div class="col-33 product-category-name">'
						+ item.productCategoryName
						+ '</div>'
						+ '<div class="col-33">'
						+ item.priority
						+ '</div>'
						+ '<div class="col-33"><a href="#" class="button delete" data-id="'
						+ item.productCategoryId
						+ '">删除</a></div>' + '</div>';
				});
				$(".category-wrap").append(tempHtml);
			}
		});
	}
	
	//新增一行 新的可编辑的商品类别信息
	$("#new").click(function() {
		var tempHtml= "<div class='row row-product-category temp'>" 
				+ "<div class='col-33'><input class='category-input category' type='text' placeholder='分类名'></div>"
				+ "<div class='col-33'><input class='category-input priority' type='number' placeholder='优先级'></div>"
				+ "<div class='col-33'><a href='#' class='button delete'>删除</a></div>";
		$(".category-wrap").append(tempHtml);
	});
	
	//删除临时添加的商品类别信息
	$(".category-wrap").on("click", ".row-product-category.temp .delete", function(event) {
		console.log($(this).parent().parent());
		//删除该临时 商品类别行信息
		$(this).parent().parent().remove();
	});
	
	//删除指定的已持久化的商品类别信息
	$(".category-wrap").on("click", ".row-product-category.now .delete", function(event) {
		//event.currentTarget: 获取当前处理该事件的节点对象
		var target = event.currentTarget;
		$.confirm("确定删除该商品分类么？", function() {
			$.ajax({
				url: deleteUrl,
				type: "POST",
				data: {
					//这里的 $(this).dataset.id 取的就是 删除节点的 data-id的值
					productCategoryId:target.dataset.id
				},
				dataType: "json",
				success: function(data) {
					if(data.success) {
						$.toast("删除成功！");
						//提交成功后，重新渲染商品类别列表页面
						getProductCategoryList();
					}else {
						$.toast("删除失败！");
						return;
					}
				}
			});
		});
	});
	
	//表单提交，批量新增 商品类别信息
	$("#submit").click(function() {
		//根据class为 temp获取 新增商品类别行节点
		var tempArr = $(".temp");
		//新建json数组存储 商品信息
		var productCategoryList = [];
		//遍历 商品类别行节点
		tempArr.map(function(index, item) {
			var tempObj = {};
			tempObj.productCategoryName = $(item).find(".category").val();
			tempObj.priority = $(item).find(".priority").val();
			if(tempObj.productCategoryName && tempObj.priority) {
				productCategoryList.push(tempObj);
			}
		});
		//进行ajax异步提交
		$.ajax({
			url : addUrl,
			type : "POST",
			data : JSON.stringify(productCategoryList),
			contentType : "application/json",
			success : function(data) {
				if(data.success) {
					$.toast("提交成功！");
					//提交成功后，重新渲染商品类别列表页面
					getProductCategoryList();
					return;
				}else {
					$.toast("提交失败！");
					return;
				}
			}
		});
	});
	
});