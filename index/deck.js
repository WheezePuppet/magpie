
$(document).ready(function() {

loadCardStats();

var printTableContents = function() {

    $("#cardTable").html("");

    $("#cardTable").append(
        "<THEAD><TR>" +
        "<TH class=question>question</TH>" +
        "<TH class=answer>answer</TH>" +
        "<TH class=reviews>reviews</TH>" +
        "<TH class=rate>rate</TH>" +
        "<TH class=avgtime>avg. time</TH>" +
        "<TH class=recentdate>most recent</TH>" +
        "<TH class=nextdate>next review</TH></TR>" +
        "</THEAD><TBODY></TBODY>");

    $("#cardTable tbody").html("");
    $("#cardTable tbody").css("background-color",MAGPIE.deckColor);

    for (var i=0; i<MAGPIE.cardStats.length; i++) {
        $("#cardTable tbody").append(
            "<TR><TD class=question>" + MAGPIE.cardStats[i].question +
            "</TD><TD class=answer>" + MAGPIE.cardStats[i].answer +
            "</TD><TD class=reviews>" + 
                                MAGPIE.cardStats[i].numSuccessfulReviews +
            "/" + MAGPIE.cardStats[i].numReviews +
            "</TD><TD class=rate>" + 
                MAGPIE.cardStats[i].rate.toFixed(0) +
            "%</TD><TD class=avgtime>" + 
                MAGPIE.cardStats[i].averageReviewTime +
            " ms</TD><TD class=recentdate>" + 
                                     MAGPIE.cardStats[i].mostRecentDate +
            "</TD><TD class=nextdate>" + MAGPIE.cardStats[i].nextDate +
            "</TD></TR>");
    }

    $(".rate").click(sortByRate);
    $(".avgtime").click(sortByTime);
    $(".recentdate").click(sortByDate);
    $(".nextdate").click(sortByNextDate);
    $(".reviews").click(sortByNumReviews);
    $(".question").click(sortByQuestion);
    $(".answer").click(sortByAnswer);
};

var sort = function(property, ascending) {
    for (var i=0; i<MAGPIE.cardStats.length; i++) {
        for (var j=i+1; j<MAGPIE.cardStats.length; j++) {
            if ((MAGPIE.cardStats[i][property] > 
                    MAGPIE.cardStats[j][property] && ascending)  ||
                (MAGPIE.cardStats[i][property] < 
                    MAGPIE.cardStats[j][property] && !ascending)) {
                var tmp = MAGPIE.cardStats[i];
                MAGPIE.cardStats[i] = MAGPIE.cardStats[j];
                MAGPIE.cardStats[j] = tmp;
            }
        }
    }
    printTableContents();
};

var sortByRate = function() {
    sort("rate", true);
}

var sortByTime = function() {
    sort("averageReviewTime", false);
};

var sortByDate = function() {
    sort("mostRecentDate", true);
};

var sortByNextDate = function() {
    sort("nextDate", true);
};

var sortByNumReviews = function() {
    sort("numReviews", true);
};

var sortByQuestion = function() {
    sort("question", true);
};

var sortByAnswer = function() {
    sort("answer", true);
};

printTableContents();

});
