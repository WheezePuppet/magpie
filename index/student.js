
$(document).ready(function() {

var buttonsDisabled = true;

var showTime = new Date().getTime();
var hideTime = new Date().getTime();

// Used exclusively for multiple choice mode.
var correctAnswerNumber = 0;
var numChoices = 4;

var getCard = function(url) {
	var request = new XMLHttpRequest();
	request.onreadystatechange = function() {
		handleGetCardResponse(request);
	};
	request.open("GET", url);
	request.send("");
}

var handleGetCardResponse = function(ajaxCall) {
	if (ajaxCall.readyState != 4 || ajaxCall.status != 200)
		return;

	var response = JSON.parse(ajaxCall.responseText);
	if (response.status == "nologin") {
		window.location = "login.jsp";
		return;
	}

	$("#time").text(response.time);

	if (response.status == "done") {
		var testarea = $("#testarea");
		testarea.empty();
		testarea.append($("<span>")
			.addClass("doneMessage")
			.html("Good job! You have finished all of your Magpie cards for the day.<br/>See you tomorrow! :)"));
		return;
	}

	card = response.card;
	card.question = card.question.replace(/\n/g, "<br/>");
	card.answer = card.answer.replace(/\n/g, "<br/>");

	$("#question .centereddisplay").html(card.question);

	var answer = $("#answer .centereddisplay");
	answer.text("(Click or press space to show answer)");
	answer.removeClass("visibleAnswer");
	answer.addClass("hiddenAnswer");
    answer.click(handleAnswerClick);

	$("#prompt").removeClass("visible");
	$("#prompt").addClass("hidden");
	hideTime = new Date().getTime();
	hidden = true;

    if ($.cookie("gradingGroup") == "multi") {
        populateMultipleChoiceAnswers(response);
    }
};

var populateMultipleChoiceAnswers = function(response) {
    var other = response.otherAnswers;
    correctAnswerNumber = Math.floor((Math.random()*numChoices)+1); 
    for (var i=0; i<numChoices; i++) {
        if (i+1 == correctAnswerNumber) {
            $("#choice" + (i+1)).html(response.card.answer);
        } else {
            $("#choice" + (i+1)).html(other[i]);
        }
    }
};

var handleAnswerClick = function() {
	if (!hidden)
		return;

	hidden = false;
	
	var answer = $("#answer .centereddisplay");
	answer.removeClass("hiddenAnswer");
	answer.addClass("visibleAnswer");
	answer.html(card.answer);

	showTime = new Date().getTime();

	setButtonsDisabled(false);
	$("#prompt").removeClass("hidden");
	$("#prompt").addClass("visible");
}

var setButtonsDisabled = function(disabled) {
	buttonsDisabled = disabled;

	var selector = $("#response").children("button");
	if (disabled)
		selector.attr("disabled", "true");
	else
		selector.removeAttr("disabled");
}

var getFirstCard = function() {
	setButtonsDisabled(true);
	getCard("web_services/getCard.jsp?dontcache=" + new Date().getTime());
}

var gradeCard = function(grade) {
	setButtonsDisabled(true);
	getCard(getGradeUrl(grade > 2, grade));
}

var scoreCard = function(success) {
	setButtonsDisabled(true);
	getCard(getGradeUrl(success));
}

var getGradeUrl = function(success, grade) {
	return 'web_services/gradeCard.jsp' +
		"?cid=" + card.cid +
		(grade != null ? "&grade=" + grade : "") +
		"&success=" + success +
		"&time=" + (showTime - hideTime) +
		"&dontcache=" + new Date().getTime();
}

var getButton = function(score) {
	return $("<button>")
		.attr("id", "b" + score)
		.click(function() { gradeCard(score); })
		.text(score);
}

var getPrompt = function(text) {
	return $("<div>")
		.attr("id", "prompt")
		.addClass("hidden")
		.text(text);
}

var choose = function(choice) {
    return function() {
	    showTime = new Date().getTime();
        $("#choice"+choice).addClass("confirmedChoice");
        if (choice == correctAnswerNumber) {
            // ?? $("#choice"+choice).css("background-color","blue");
            //setTimeout(getCard, 500, getGradeUrl(true, 6));
            getCard(getGradeUrl(true, 6));
        } else {
            alert("Correct answer was (" + correctAnswerNumber +
                ") " + $("#choice"+correctAnswerNumber).text());
	        getCard(getGradeUrl(false, 0));
        }
        for (var i=0; i<numChoices+1; i++) {
            $("#choice"+(i+1)).removeClass("confirmedChoice");
        $("#choice"+(i+1)).css("background-color","");
        }
    };
    
};

var populateResponseDiv = function() {
	var response = $("#response");
	response.empty();

	if ($.cookie("gradingGroup") == "score") {
		response.append(getPrompt("How well do you know this card?"));

		for(var i = 0; i <= 2; i++) {
			response.append(getButton(i));
			response.append("&nbsp;");
		}
		response.append("&nbsp;|&nbsp;&nbsp;");
		for(var i = 3; i <= 5; i++) {
			response.append(getButton(i));
			response.append("&nbsp;");
		}
	} else if ($.cookie("gradingGroup") == "timer") {
		response.append(getPrompt("Did you know this card?"));

		response.append($("<button>")
			.attr("id", "b0")
			.click(function() { scoreCard(false); })
			.text("No"));
		response.append("&nbsp;&nbsp;");
		response.append($("<button>")
			.attr("id", "b1")
			.click(function() { scoreCard(true); })
			.text("Yes"));
	} else {
		response.append(getPrompt("Select answer:"));

        var table = $("<table>");
        table.attr("id", "multiplechoice");
        for (var i=0; i<numChoices; i++) {
            var tableRow = $("<tr>"),
                keyLabelCell = $("<td>"),
                tableCell = $("<td>");
            tableRow.append(keyLabelCell);
            tableRow.append(tableCell);
            keyLabelCell.html("(" + (i+1) + ")");
            keyLabelCell.addClass("keylabel");
            tableCell.attr("id", "choice"+(i+1));
            tableCell.addClass("choice");
            table.append(tableRow);
            tableCell.click(choose(i+1));
            tableCell.mouseenter(function() { 
                $(this).addClass("highlitrow");
            });
            tableCell.mouseleave(function() { 
                $(this).removeClass("highlitrow");
            });
        }
        var tableRow = $("<tr>"),
            keyLabelCell = $("<td>"),
            tableCell = $("<td>");
        tableRow.append(keyLabelCell);
        tableRow.append(tableCell);
        keyLabelCell.html("(space)");
        keyLabelCell.addClass("keylabel");
        tableCell.attr("id", "choice"+(numChoices+1));
        tableCell.addClass("choice");
        tableCell.html("Not sure");
        table.append(tableRow);
        tableCell.click(choose(numChoices+1));
        tableCell.mouseenter(function() { 
            $(this).addClass("highlitrow");
        });
        tableCell.mouseleave(function() { 
            $(this).removeClass("highlitrow");
        });

        response.append(table);
        $("#answer").html("");
	}

	response.append("<br/><br/>");
};

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

populateResponseDiv();
getFirstCard();
});
