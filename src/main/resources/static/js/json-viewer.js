$(document).ready(function () {
    try {
        var json = JSON.parse($('#json-display').text());
        $('#json-display').html("");
        new JsonEditor('#json-display', json);
    } catch (ex) {
        alert('Wrong JSON Format: ' + ex);
    }
    $('#run').bind("click", function () {
        var data = $('#json-display').text();
        $.ajax({
            type: 'POST',
            url: "/",
            data: {json: data},
            success: function (resultData) {
                $('#json-form').html(resultData);
            },
        });
    });
});

