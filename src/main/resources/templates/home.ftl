
<#import "./elements.ftl" as elem>
<#compress>
    <!DOCTYPE html>
    <html lang="en">
    <head>
        <meta charset="utf-8"/>
        <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
        <title>${definitions.page.title}</title>
        <#list layout.definitions.css_url as cssURL>
            <link rel="stylesheet" href="${cssURL}"/>
        </#list>
    </head>
    <body>
    <#if elements??>
        <#if list_value??>
            <@elem.templateParser template=(definitions.page.snippet) elements=elements list_value=list_value uri=uri></@elem.templateParser>
        <#else >
            <@elem.templateParser template=(definitions.page.snippet) elements=elements list_value="" uri=uri></@elem.templateParser>
        </#if>

    </#if>

    <#if type?? && type="search">
        <div id="searchList"></div>
    </#if>
    <#--    footer part start from here -->
    <#list layout.definitions.js_url as jsURL>
        <script src="${jsURL}"></script>
    </#list>
    <script>
        $(document).ready(function () {
            var $form = $('form');
            var fieldHTML = '<input type="hidden" name="uri" value="${uri}"/>' + <#if _id??>'<input type="hidden" name="_id" value="${_id}"/>'
            <#else>''</#if>;
            $($form).append(fieldHTML);
            <#if _id??>
            loadData();
            </#if>

            <#if _id??>

            function loadData() {
                $.ajax({
                    url: "/auth/process/getId?uri=${uri}&id=${_id}",
                    type: 'GET',
                    dataType: 'json', // added data type
                    success: function (data) {
                        for (var i in data) {
                            $('input[name="'+i+'"]').val(data[i]);
                        }
                    }
                });
            }

            </#if>

            <#if type?? && type=="search">
            $('button[type="submit"]').on('click', function(e) { //use on if jQuery 1.7+
                e.preventDefault();  //prevent form from submitting
                var data = $("form :input").serializeArray();
                console.log(data); //use the console for debugging, F12 in Chrome, not alerts
                $.ajax({
                    url: "/auth/process/search",
                    type: 'POST',
                    data:data,
                    success: function (res) {
                        $('#searchList').html(res);
                    }
                });
            });

            </#if>
        });

    </script>
    </body>
    </html>
</#compress>
