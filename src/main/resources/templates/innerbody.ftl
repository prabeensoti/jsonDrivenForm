<#import "./elements.ftl" as elem>
<#if elements??>
    <#if list_value??>
        <@elem.templateParser template=(definitions.page.snippet) elements=elements list_value=list_value uri=uri></@elem.templateParser>
    <#else >
        <@elem.templateParser template=(definitions.page.snippet) elements=elements list_value="" uri=uri></@elem.templateParser>
    </#if>

</#if>