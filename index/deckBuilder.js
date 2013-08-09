
$(document).ready(function() {

    var maxQuestionNum = 1;

    var init = function() {
        $("#addNew").click(addNewRow);
        $("#deckName").focus();
    };

    var catchTab = function(e) {
        var keyCode = e.keyCode || e.which; 
        if (keyCode == 9) { 
            e.preventDefault(); 
            addNewRow();
        } 
    };
    
    var addNewRow = function() {
        maxQuestionNum++;
        $("#cardTable .widget").html("");
        $("#cardTable .answerbox").unbind("keydown");
        $("#cardTable .answerbox").removeClass("answerbox");
        $("#cardTable .questionbox").removeClass("questionbox");
        $("#cardTable").append("<tr>" +
            "<td> <textarea class=questionbox cols=40 rows=3 name=q" +
            maxQuestionNum + 
            "></textarea> </td> " +
            "<td> <textarea class=answerbox cols=40 rows=3 name=a" +
            maxQuestionNum + 
            "></textarea> </td>" +
            "<td class=widget > <img id=addNew src=\"images/plus.png\" />" +
            "</td> </tr>"); 
        $("#cardTable .widget img").click(addNewRow);
        $(".answerbox").keydown(catchTab);
        $("#cardTable .questionbox").focus();
    };

    $(".answerbox").keydown(catchTab);

    init();
});
