function checkValue(check) {
	var checkList = document.getElementsByClassName("checkList");
	var checkFlg = 0;
	for (var i = 0; i < checkList.length; i++ ) {
		if (checkFlg == 0) {
			if (checkList[i].checked) {
				checkFlg = 1;
				break;
			}
		}
	}
	if (checkFlg == 1) {
		document.getElementById('deleteButton').disabled = "";
	} else {
		document.getElementById('deleteButton').disabled = "true";
	}
}

function goSettlementConfirmAction() {
	document.getElementById("cartForm").action = "SettlementConfirmAction";
}

function goDeleteCartAction() {
	document.getElementById("cartForm").action = "DeleteCartAction";
}