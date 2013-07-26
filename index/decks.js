
$(document).ready(function() {

    $("select").change(function() { 
        var color = $(this).children("option").filter(":selected").val();
        $(this).css("background-color", color);
    });
});
