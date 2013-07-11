var buttonsDisabled = true;
var MAGPIE = {};

$(document).keypress(function(e) {
	// space bar
	if(e.which == 32) {
		handleAnswerClick();
		return false;
	}

	// 0-5
	if(e.which >= 48 && e.which <= 53 && $.cookie("gradingGroup") == "score") {
		if(!buttonsDisabled)
			gradeCard(e.which-48);
		return false;
	}

	// y
	if(e.which == 121 && $.cookie("gradingGroup") == "timer") {
		if(!buttonsDisabled)
			scoreCard(true);
		return false;
	}

	// n
	else if(e.which == 110 && $.cookie("gradingGroup") == "timer") {
		if(!buttonsDisabled)
			scoreCard(false);
		return false;
	}
});

function getCard(url) {
	var request = new XMLHttpRequest();
	request.onreadystatechange = function() {
		handleGetCardResponse(request);
	};
	request.open("GET", url);
	request.send("");
}

function handleGetCardResponse(ajaxCall) {
	if (ajaxCall.readyState != 4 || ajaxCall.status != 200)
		return;

	var response = JSON.parse(ajaxCall.responseText);
	if (response.status == "nologin") {
		window.location = "login.jsp";
		return;
	}

	$('#time').text(response.time);

	if (response.status == "done") {
		var testarea = $('#testarea');
		testarea.empty();
		testarea.append($('<span>')
			.addClass('doneMessage')
			.html('Good job! You have finished all of your Magpie cards for the day.<br />See you tomorrow! :)'));
		return;
	}

	MAGPIE.card = response.card;
	MAGPIE.card.question = MAGPIE.card.question.replace(/\n/g, "<br />");
	MAGPIE.card.answer = MAGPIE.card.answer.replace(/\n/g, "<br />");

	$('#question .centereddisplay').html(MAGPIE.card.question);

	var answer = $('#answer .centereddisplay');
	answer.text("(Click or press space to show answer)");
	answer.removeClass('visibleAnswer');
	answer.addClass('hiddenAnswer');

	$('#prompt').removeClass('visible');
	$('#prompt').addClass('hidden');
	MAGPIE.hideTime = new Date().getTime();
	MAGPIE.hidden = true;
}

function handleAnswerClick() {
	if (!MAGPIE.hidden)
		return;

	MAGPIE.hidden = false;
	
	var answer = $('#answer .centereddisplay');
	answer.removeClass('hiddenAnswer');
	answer.addClass('visibleAnswer');
	answer.html(MAGPIE.card.answer);

	MAGPIE.showTime = new Date().getTime();

	setButtonsDisabled(false);
	$('#prompt').removeClass('hidden');
	$('#prompt').addClass('visible');
}

function setButtonsDisabled(disabled) {
	buttonsDisabled = disabled;

	var selector = $('#response').children('button');
	if (disabled)
		selector.attr('disabled', 'true');
	else
		selector.removeAttr('disabled');
}

function getFirstCard() {
	setButtonsDisabled(true);
	getCard("web_services/getCard.jsp?dontcache=" + new Date().getTime());
}

function gradeCard(grade) {
	setButtonsDisabled(true);
	getCard(getGradeUrl(grade > 2, grade));
}

function scoreCard(success) {
	setButtonsDisabled(true);
	getCard(getGradeUrl(success));
}

function getGradeUrl(success, grade) {
	return 'web_services/gradeCard.jsp' +
		"?cid=" + MAGPIE.card.cid +
		(grade != null ? "&grade=" + grade : "") +
		"&success=" + success +
		"&time=" + (MAGPIE.showTime - MAGPIE.hideTime) +
		"&dontcache=" + new Date().getTime();
}

function getButton(score) {
	return $('<button>')
		.attr('id', 'b' + score)
		.click(function() { gradeCard(score); })
		.text(score);
}

function getPrompt(text) {
	return $('<div>')
		.attr('id', 'prompt')
		.addClass('hidden')
		.text(text);
}

function populateResponseDiv() {
	var response = $('#response');
	response.empty();

	if ($.cookie("gradingGroup") == "score") {
		response.append(getPrompt('How well do you know this card?'));

		for(var i = 0; i <= 2; i++) {
			response.append(getButton(i));
			response.append('&nbsp;');
		}
		response.append('&nbsp;|&nbsp;&nbsp;');
		for(var i = 3; i <= 5; i++) {
			response.append(getButton(i));
			response.append('&nbsp;');
		}
	} else {
		response.append(getPrompt('Did you know this card?'));

		response.append($('<button>')
			.attr('id', 'b0')
			.click(function() { scoreCard(false); })
			.text('No'));
		response.append('&nbsp;&nbsp;');
		response.append($('<button>')
			.attr('id', 'b1')
			.click(function() { scoreCard(true); })
			.text('Yes'));
	}

	response.append('<br /><br />');
}
