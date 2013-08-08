
$(document).ready(function() {

    var maxQuestionNum = 1;

    var init = function() {
        $("#addNew").click(addNewRow);
    };

    
    var addNewRow = function() {
        maxQuestionNum++;
        $("#cardTable .widget").html("");
        $("#cardTable").append("<tr><td> <textarea cols=40 rows=3 name=q" +
            maxQuestionNum + 
            "></textarea> </td> <td> <textarea cols=40 rows=3 name=a" +
            maxQuestionNum + 
            "></textarea> </td>" +
            "<td class=widget > <img id=addNew src=\"images/plus.png\" />" +
            "</td> </tr>"); 
        $("#cardTable .widget img").click(addNewRow);
    };

    init();
});
