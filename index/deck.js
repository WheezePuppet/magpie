$(document).ready(function() {
	$('.deckCheckbox').change(function() {
		var request = new XMLHttpRequest();
		var params = "did=" + this.id + "&active=" + (this.checked ? "1" : "0");
		request.open("GET", "web_services/setDeckActive.jsp?" + params);
		request.send("");
	});
});

