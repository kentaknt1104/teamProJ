<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix = "s" uri = "/struts-tags" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel = "stylesheet" href = "./css/cart.css" />
<link rel = "stylesheet" href = "./css/style.css" />
<title>カート画面</title>
</head>
<body>
<script type= "text/javascript" src = "./js/cart.js"></script>

	<div id = "header"><jsp:include page = "header.jsp" /></div>
	<div id = "main">
		<div id = "top">
			<h1>カート画面</h1>
		</div>
		<div>
			<s:if test = "(cartInfoDTOList != null && cartInfoDTOList.size() > 0)">
				<s:form id = "cartForm">
				<table id = "item_box">
				<thead>
					<tr>
						<th><s:label value = "#" /></th>
						<th><s:label value = "商品名" /></th>
						<th><s:label value = "商品名ふりがな" /></th>
						<th><s:label value = "商品画像" /></th>
						<th><s:label value = "値段" /></th>
						<th><s:label value = "発売会社名" /></th>
						<th><s:label value = "発売年月日" /></th>
						<th><s:label value = "購入個数" /></th>
						<th><s:label value = "合計金額" /></th>
					</tr>
				</thead>
				<tbody>
				<s:iterator value = "cartInfoDTOList">
					<tr>
<!-- 					nameの値で受け取られる(複数の場合があるため受け取りのときはCollection<String>で受け取る) -->
<!-- 					classはjsの関数で使いたいから記載。 -->
<!-- 					valueはchecked属性のことで、checkされたらcheckedになる(trueのようなこと) -->
<!-- 					fieldvalueは今までのvalueのようなものでnameを受け取るところに渡す。 -->

						<td><s:checkbox name = "checkList" cssClass = "checkList" label = "partsS" value = "checked" fieldValue = "%{productId}" onchange = "checkValue(this)" /></td>
						<s:hidden name = "productId" value = "%{productId}" />
						<td><s:property value = "productName" /></td>
						<td><s:property value = "productNameKana" /></td>
						<td><img src = '<s:property value = "imageFilePath" />/<s:property value = "imageFileName" />' width = "50px" height = "50px" /></td>
						<td><s:property value = "price" />円</td>
						<td><s:property value = "releaseCompany" /></td>
						<td><s:property value = "releaseDate" /></td>
						<td><s:property value = "productCount" /></td>
						<td><s:property value = "itemSumPrice" />円</td>
					</tr>
				</s:iterator>
				</tbody>
				</table>
				<div id = "cartbox">
					<h3>
						<s:label value = "カート合計金額 ：" />
						<s:property value = "totalPrice" />円
					</h3><br />
					<div id = "btn_area">
						<div id = "left_cartBox">
							<s:submit value = "決済" id = "nextButton" class = "btn"  onclick = "goSettlementConfirmAction()" />
						</div>
						<div id = "right_cartBox">
							<s:submit value = "削除" id = "deleteButton" class = "btn" onclick = "goDeleteCartAction()" disabled = "true" />
						</div>
					</div>
				</div>
			</s:form>
			</s:if>
			<s:else>
				<h3 class = "select_box">カート情報がありません。</h3>
			</s:else>

		</div>
	</div>
</body>
</html>