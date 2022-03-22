<#macro registrationLayout bodyClass="" displayInfo=false displayMessage=true displayRequiredFields=false showAnotherWayIfPresent=true>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" class="${properties.kcHtmlClass!}">

<head>
    <meta charset="utf-8">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="robots" content="noindex, nofollow">

    <#if properties.meta?has_content>
        <#list properties.meta?split(' ') as meta>
            <meta name="${meta?split('==')[0]}" content="${meta?split('==')[1]}"/>
        </#list>
    </#if>
    <title>${msg("loginTitle",(realm.displayName!''))}</title>
    <link rel="icon" href="${url.resourcesPath}/img/favicon.ico" />
    <#if properties.stylesCommon?has_content>
        <#list properties.stylesCommon?split(' ') as style>
            <link href="${url.resourcesCommonPath}/${style}" rel="stylesheet" />
        </#list>
    </#if>
    <#if properties.styles?has_content>
        <#list properties.styles?split(' ') as style>
            <link href="${url.resourcesPath}/${style}" rel="stylesheet" />
        </#list>
    </#if>
    <#if properties.scripts?has_content>
        <#list properties.scripts?split(' ') as script>
            <script src="${url.resourcesPath}/${script}" type="text/javascript"></script>
        </#list>
    </#if>
    <#if scripts??>
        <#list scripts as script>
            <script src="${script}" type="text/javascript"></script>
        </#list>
    </#if>
</head>

<body class="${properties.kcBodyClass!}">
<div class="${properties.kcLoginClass!}">
    <div id="kc-header" class="${properties.kcHeaderClass!}">
        <div id="kc-header-wrapper"
             class="${properties.kcHeaderWrapperClass!}">
            <div class="logo">
                <svg class="logo-gaia-full" viewBox="0 0 156 86.8" xmlns="http://www.w3.org/2000/svg"><defs>
                    <style type="text/css"> .st0{fill:#283375;}</style>
                    </defs><g class="logo-gaia-full__wrapper"><g class="logo-gaia-full__text"><path class="st0" d="M 43.2 57.7 C 42.6 57.7 42.1 58.2 42.1 58.8 L 42.1 59.6 C 42.1 60.2 42.6 60.8 43.2 60.8 C 43.8 60.8 44.4 60.3 44.4 59.6 L 44.4 58.8 C 44.4 58.2 43.8 57.7 43.2 57.7 Z"/><path class="st0" d="M 15 63.5 C 14.4 63.5 13.8 64 13.8 64.6 L 13.8 66.1 C 12.3 64.5 10.2 63.5 8 63.5 C 3.6 63.5 0 67.3 0 71.9 C 0 76.5 3.6 80.3 8 80.3 C 10.2 80.3 12.3 79.4 13.8 77.7 L 13.8 78.8 C 13.8 82 11.3 84.5 8 84.5 C 7.2 84.5 6.4 84.3 5.7 84 C 4.9 83.7 4.3 83.2 3.8 82.7 C 3.6 82.5 3.3 82.3 3 82.3 L 3 82.3 C 2.7 82.3 2.4 82.4 2.2 82.6 C 1.7 83 1.8 83.7 2.1 84.2 C 2.8 84.9 3.6 85.5 4.4 85.9 L 4.5 86 C 4.6 86 4.6 86.1 4.7 86.1 L 4.9 86.2 C 5 86.2 5 86.3 5.1 86.3 C 6 86.7 7 86.8 8 86.8 C 8.6 86.8 9.1 86.7 9.7 86.6 L 9.8 86.6 C 10.3 86.5 10.8 86.3 11.3 86.1 C 11.7 85.9 12.1 85.7 12.5 85.4 C 12.6 85.3 12.7 85.2 12.8 85.2 L 13 85.1 C 13.2 85 13.4 84.8 13.6 84.6 C 13.8 84.4 14 84.3 14.2 84 C 14.3 83.9 14.4 83.8 14.4 83.8 C 15.6 82.3 16.3 80.6 16.3 78.7 L 16.3 64.6 C 16.1 64 15.6 63.5 15 63.5 Z M 8.1 78.1 C 4.9 78.1 2.4 75.3 2.4 71.9 C 2.4 68.5 5 65.7 8.1 65.7 C 11.3 65.7 13.9 68.5 13.9 71.9 C 13.8 75.3 11.3 78.1 8.1 78.1 Z"/><path class="st0" d="M 75 70.9 L 69.9 70.9 C 69.4 70.9 68.9 71.3 68.9 71.9 C 68.9 72.4 69.3 72.9 69.9 72.9 L 75 72.9 C 75.5 72.9 76 72.5 76 71.9 C 76 71.4 75.6 70.9 75 70.9 Z"/><path class="st0" d="M 85.8 71.8 L 92.5 65.1 C 92.9 64.7 92.9 63.9 92.5 63.5 C 92.1 63.1 91.3 63.1 90.9 63.5 L 84.2 70.2 L 77.5 63.5 C 77.1 63.1 76.3 63.1 75.9 63.5 C 75.5 63.9 75.5 64.7 75.9 65.1 L 82.6 71.8 L 75.9 78.5 C 75.5 78.9 75.5 79.7 75.9 80.1 C 76.1 80.3 76.4 80.4 76.7 80.4 C 77 80.4 77.3 80.3 77.5 80.1 L 84.2 73.4 L 90.9 80.1 C 91.1 80.3 91.4 80.4 91.7 80.4 C 92 80.4 92.3 80.3 92.5 80.1 C 92.9 79.7 92.9 78.9 92.5 78.5 L 85.8 71.8 Z"/><path class="st0" d="M 35.4 63.5 C 34.8 63.5 34.3 64 34.3 64.6 L 34.3 66.1 C 32.8 64.4 30.7 63.5 28.5 63.5 C 24.1 63.5 20.5 67.3 20.5 72 C 20.5 76.7 24.1 80.5 28.5 80.5 C 30.7 80.5 32.8 79.5 34.3 77.9 L 34.3 79.4 C 34.3 80 34.8 80.5 35.4 80.5 C 36 80.5 36.5 80 36.5 79.4 L 36.5 64.6 C 36.5 64 36 63.5 35.4 63.5 Z M 28.5 78.2 C 25.3 78.2 22.7 75.4 22.7 72 C 22.7 68.6 25.3 65.7 28.5 65.7 C 31.7 65.7 34.3 68.5 34.3 72 C 34.3 75.4 31.7 78.2 28.5 78.2 Z"/><path class="st0" d="M 43.2 63.5 C 42.6 63.5 42.1 64 42.1 64.6 L 42.1 79.3 C 42.1 79.9 42.6 80.4 43.2 80.4 C 43.8 80.4 44.4 79.9 44.4 79.3 L 44.4 64.6 C 44.4 64 43.9 63.5 43.2 63.5 Z"/><path class="st0" d="M 63.9 63.5 C 63.3 63.5 62.8 64 62.8 64.6 L 62.8 66.1 C 61.3 64.4 59.2 63.5 57 63.5 C 52.6 63.5 49 67.3 49 72 C 49 76.7 52.6 80.5 57 80.5 C 59.2 80.5 61.3 79.5 62.8 77.9 L 62.8 79.4 C 62.8 80 63.3 80.5 63.9 80.5 C 64.5 80.5 65 80 65 79.4 L 65 64.6 C 65 64 64.5 63.5 63.9 63.5 Z M 57 78.2 C 53.8 78.2 51.2 75.4 51.2 72 C 51.2 68.6 53.8 65.7 57 65.7 C 60.2 65.7 62.8 68.5 62.8 72 C 62.7 75.4 60.2 78.2 57 78.2 Z"/></g><g class="logo-gaia-full__rays"><path class="st0" d="M 102.1 5.2 C 101.8 5.2 101.5 5.1 101.3 4.9 L 98.3 1.9 C 97.9 1.5 97.9 0.7 98.3 0.3 C 98.7 -0.1 99.5 -0.1 99.9 0.3 L 102.9 3.3 C 103.3 3.7 103.3 4.5 102.9 4.9 C 102.7 5.1 102.4 5.2 102.1 5.2 Z"/><path class="st0" d="M 151.9 5.2 C 151.6 5.2 151.3 5.1 151.1 4.9 C 150.7 4.5 150.7 3.7 151.1 3.3 L 154.1 0.3 C 154.5 -0.1 155.3 -0.1 155.7 0.3 C 156.1 0.7 156.1 1.5 155.7 1.9 L 152.7 4.9 C 152.5 5.1 152.2 5.2 151.9 5.2 Z"/><path class="st0" d="M 99.1 58 C 98.8 58 98.5 57.9 98.3 57.7 C 97.9 57.3 97.9 56.5 98.3 56.1 L 101.3 53.1 C 101.7 52.7 102.5 52.7 102.9 53.1 C 103.3 53.5 103.3 54.3 102.9 54.7 L 99.9 57.7 C 99.7 57.9 99.4 58 99.1 58 Z"/><path class="st0" d="M 154.9 58 C 154.6 58 154.3 57.9 154.1 57.7 L 151.1 54.7 C 150.7 54.3 150.7 53.5 151.1 53.1 C 151.5 52.7 152.3 52.7 152.7 53.1 L 155.7 56.1 C 156.1 56.5 156.1 57.3 155.7 57.7 C 155.5 57.9 155.2 58 154.9 58 Z"/><path class="st0" d="M 112.4 5.9 C 112 5.9 111.6 5.7 111.4 5.3 L 109.2 1.7 C 108.9 1.2 109.1 0.5 109.6 0.1 C 110.1 -0.2 110.8 0 111.2 0.5 L 113.4 4.1 C 113.7 4.6 113.5 5.3 113 5.7 C 112.8 5.8 112.6 5.9 112.4 5.9 Z"/><path class="st0" d="M 122.2 6.4 C 121.7 6.4 121.2 6 121.1 5.5 L 120.3 1.3 C 120.2 0.7 120.6 0.1 121.2 0 C 121.8 -0.1 122.4 0.3 122.5 0.9 L 123.3 5.1 C 123.4 5.7 123 6.3 122.4 6.4 C 122.4 6.4 122.3 6.4 122.2 6.4 Z"/><path class="st0" d="M 141.5 5.9 C 141.3 5.9 141.1 5.8 140.9 5.7 C 140.4 5.4 140.2 4.7 140.5 4.1 L 142.7 0.5 C 143 0 143.7 -0.2 144.3 0.1 C 144.8 0.4 145 1.1 144.7 1.7 L 142.5 5.3 C 142.3 5.7 141.9 5.9 141.5 5.9 Z"/><path class="st0" d="M 131.7 6.4 C 131.6 6.4 131.6 6.4 131.5 6.4 C 130.9 6.3 130.5 5.7 130.6 5.1 L 131.4 0.9 C 131.5 0.3 132.1 -0.1 132.7 0 C 133.3 0.1 133.7 0.7 133.6 1.3 L 132.8 5.5 C 132.7 6 132.3 6.4 131.7 6.4 Z"/><path class="st0" d="M 110.3 58 C 110.1 58 109.9 57.9 109.7 57.8 C 109.2 57.5 109 56.8 109.3 56.2 L 111.5 52.6 C 111.8 52.1 112.5 51.9 113.1 52.2 C 113.6 52.5 113.8 53.2 113.5 53.8 L 111.3 57.4 C 111 57.8 110.6 58 110.3 58 Z"/><path class="st0" d="M 121.4 58 C 121.3 58 121.3 58 121.2 58 C 120.6 57.9 120.2 57.3 120.3 56.7 L 121.1 52.5 C 121.2 51.9 121.8 51.5 122.4 51.6 C 123 51.7 123.4 52.3 123.3 52.9 L 122.5 57.1 C 122.4 57.6 121.9 58 121.4 58 Z"/><path class="st0" d="M 143.7 58 C 143.3 58 142.9 57.8 142.7 57.4 L 140.5 53.8 C 140.2 53.3 140.4 52.6 140.9 52.2 C 141.4 51.9 142.1 52.1 142.5 52.6 L 144.7 56.2 C 145 56.7 144.8 57.4 144.3 57.8 C 144.1 58 143.9 58 143.7 58 Z"/><path class="st0" d="M 132.6 58 C 132.1 58 131.6 57.6 131.5 57.1 L 130.7 52.9 C 130.6 52.3 131 51.7 131.6 51.6 C 132.2 51.5 132.8 51.9 132.9 52.5 L 133.7 56.7 C 133.8 57.3 133.4 57.9 132.8 58 C 132.7 58 132.6 58 132.6 58 Z"/><path class="st0" d="M 151.2 15.6 C 150.8 15.6 150.4 15.4 150.2 15 C 149.9 14.5 150.1 13.8 150.6 13.4 L 154.2 11.2 C 154.7 10.9 155.4 11.1 155.8 11.6 C 156.1 12.1 155.9 12.8 155.4 13.2 L 151.8 15.4 C 151.6 15.5 151.4 15.6 151.2 15.6 Z"/><path class="st0" d="M 150.7 25.4 C 150.2 25.4 149.7 25 149.6 24.5 C 149.5 23.9 149.9 23.3 150.5 23.2 L 154.7 22.4 C 155.3 22.3 155.9 22.7 156 23.3 C 156.1 23.9 155.7 24.5 155.1 24.6 L 150.9 25.4 C 150.9 25.4 150.8 25.4 150.7 25.4 Z"/><path class="st0" d="M 154.9 46.9 C 154.7 46.9 154.5 46.8 154.3 46.7 L 150.7 44.5 C 150.2 44.2 150 43.5 150.3 42.9 C 150.6 42.4 151.3 42.2 151.9 42.5 L 155.5 44.7 C 156 45 156.2 45.7 155.9 46.3 C 155.6 46.7 155.3 46.9 154.9 46.9 Z"/><path class="st0" d="M 154.9 35.7 C 154.8 35.7 154.8 35.7 154.7 35.7 L 150.5 34.9 C 149.9 34.8 149.5 34.2 149.6 33.6 C 149.7 33 150.3 32.6 150.9 32.7 L 155.1 33.4 C 155.7 33.5 156.1 34.1 156 34.7 C 155.9 35.3 155.4 35.7 154.9 35.7 Z"/><path class="st0" d="M 122.5 16.6 C 122 16.6 121.6 16.3 121.4 15.8 L 120.1 11.8 C 119.9 11.2 120.2 10.6 120.8 10.4 C 121.4 10.2 122 10.5 122.2 11.1 L 123.5 15.1 C 123.7 15.7 123.4 16.3 122.8 16.5 C 122.7 16.5 122.6 16.6 122.5 16.6 Z"/><path class="st0" d="M 131.5 16.6 C 131.4 16.6 131.3 16.6 131.1 16.5 C 130.5 16.3 130.2 15.7 130.4 15.1 L 131.7 11.1 C 131.9 10.5 132.5 10.2 133.1 10.4 C 133.7 10.6 134 11.2 133.8 11.8 L 132.5 15.8 C 132.4 16.3 132 16.6 131.5 16.6 Z"/><path class="st0" d="M 121.1 47.7 C 121 47.7 120.9 47.7 120.7 47.6 C 120.1 47.4 119.8 46.8 120 46.2 L 121.3 42.2 C 121.5 41.6 122.1 41.3 122.7 41.5 C 123.3 41.7 123.6 42.3 123.4 42.9 L 122.1 46.9 C 122.1 47.4 121.6 47.7 121.1 47.7 Z"/><path class="st0" d="M 132.9 47.7 C 132.4 47.7 132 47.4 131.8 46.9 L 130.5 42.9 C 130.3 42.3 130.6 41.7 131.2 41.5 C 131.8 41.3 132.4 41.6 132.6 42.2 L 133.9 46.2 C 134.1 46.8 133.8 47.4 133.2 47.6 C 133.1 47.7 133 47.7 132.9 47.7 Z"/><path class="st0" d="M 144.6 47.7 C 144.3 47.7 144 47.6 143.8 47.4 L 140.8 44.4 C 140.4 44 140.4 43.2 140.8 42.8 C 141.2 42.4 142 42.4 142.4 42.8 L 145.4 45.8 C 145.8 46.2 145.8 47 145.4 47.4 C 145.2 47.6 144.9 47.7 144.6 47.7 Z"/><path class="st0" d="M 140.5 25.6 C 140 25.6 139.6 25.3 139.4 24.8 C 139.2 24.2 139.5 23.6 140.1 23.4 L 144.1 22.1 C 144.7 21.9 145.3 22.2 145.5 22.8 C 145.7 23.4 145.4 24 144.8 24.2 L 140.8 25.5 C 140.8 25.6 140.7 25.6 140.5 25.6 Z"/><path class="st0" d="M 144.6 36 C 144.5 36 144.4 36 144.2 35.9 L 140.2 34.6 C 139.6 34.4 139.3 33.8 139.5 33.2 C 139.7 32.6 140.3 32.3 140.9 32.5 L 144.9 33.8 C 145.5 34 145.8 34.6 145.6 35.2 C 145.5 35.7 145.1 36 144.6 36 Z"/><path class="st0" d="M 102.7 15.6 C 102.5 15.6 102.3 15.5 102.1 15.4 L 98.5 13.2 C 98 12.9 97.8 12.2 98.1 11.6 C 98.4 11.1 99.1 10.9 99.7 11.2 L 103.3 13.4 C 103.8 13.7 104 14.4 103.7 15 C 103.5 15.4 103.1 15.6 102.7 15.6 Z"/><path class="st0" d="M 103.3 25.4 C 103.2 25.4 103.2 25.4 103.1 25.4 L 98.9 24.6 C 98.3 24.5 97.9 23.9 98 23.3 C 98.1 22.7 98.7 22.3 99.3 22.4 L 103.5 23.2 C 104.1 23.3 104.5 23.9 104.4 24.5 C 104.3 25 103.8 25.4 103.3 25.4 Z"/><path class="st0" d="M 113.4 25.6 C 113.3 25.6 113.2 25.6 113 25.5 L 109 24.2 C 108.4 24 108.1 23.4 108.3 22.8 C 108.5 22.2 109.1 21.9 109.7 22.1 L 113.7 23.4 C 114.3 23.6 114.6 24.2 114.4 24.8 C 114.4 25.3 113.9 25.6 113.4 25.6 Z"/><path class="st0" d="M 99.1 46.9 C 98.7 46.9 98.3 46.7 98.1 46.3 C 97.8 45.8 98 45.1 98.5 44.7 L 102.1 42.5 C 102.6 42.2 103.3 42.4 103.7 42.9 C 104 43.4 103.8 44.1 103.3 44.5 L 99.7 46.7 C 99.5 46.8 99.3 46.9 99.1 46.9 Z"/><path class="st0" d="M 99.1 35.7 C 98.6 35.7 98.1 35.3 98 34.8 C 97.9 34.2 98.3 33.6 98.9 33.5 L 103.1 32.7 C 103.7 32.6 104.3 33 104.4 33.6 C 104.5 34.2 104.1 34.8 103.5 34.9 L 99.3 35.7 C 99.3 35.7 99.2 35.7 99.1 35.7 Z"/><path class="st0" d="M 109.4 36 C 108.9 36 108.5 35.7 108.3 35.2 C 108.1 34.6 108.4 34 109 33.8 L 113 32.5 C 113.6 32.3 114.2 32.6 114.4 33.2 C 114.6 33.8 114.3 34.4 113.7 34.6 L 109.7 35.9 C 109.7 36 109.5 36 109.4 36 Z"/><path class="st0" d="M 109.4 47.7 C 109.1 47.7 108.8 47.6 108.6 47.4 C 108.2 47 108.2 46.2 108.6 45.8 L 111.6 42.8 C 112 42.4 112.8 42.4 113.2 42.8 C 113.6 43.2 113.6 44 113.2 44.4 L 110.2 47.4 C 110 47.6 109.7 47.7 109.4 47.7 Z"/><path class="st0" d="M 112.4 15.5 C 112.1 15.5 111.8 15.4 111.6 15.2 L 108.6 12.2 C 108.2 11.8 108.2 11 108.6 10.6 C 109 10.2 109.8 10.2 110.2 10.6 L 113.2 13.6 C 113.6 14 113.6 14.8 113.2 15.2 C 113 15.4 112.7 15.5 112.4 15.5 Z"/><path class="st0" d="M 141.6 15.5 C 141.3 15.5 141 15.4 140.8 15.2 C 140.4 14.8 140.4 14 140.8 13.6 L 143.8 10.6 C 144.2 10.2 145 10.2 145.4 10.6 C 145.8 11 145.8 11.8 145.4 12.2 L 142.4 15.2 C 142.2 15.4 141.9 15.5 141.6 15.5 Z"/></g><g class="logo-gaia-full__x"><path class="st0" d="M 123.3 27.6 C 122.7 27.6 122.1 27.4 121.7 26.9 L 118.7 23.9 C 117.8 23 117.8 21.6 118.7 20.7 C 119.6 19.8 121 19.8 121.9 20.7 L 124.9 23.7 C 125.8 24.6 125.8 26 124.9 26.9 C 124.5 27.4 123.9 27.6 123.3 27.6 Z"/><path class="st0" d="M 130.7 27.6 C 130.1 27.6 129.5 27.4 129.1 26.9 C 128.2 26 128.2 24.6 129.1 23.7 L 132.1 20.7 C 133 19.8 134.4 19.8 135.3 20.7 C 136.2 21.6 136.2 23 135.3 23.9 L 132.3 26.9 C 131.8 27.4 131.2 27.6 130.7 27.6 Z"/><path class="st0" d="M 120.3 37.9 C 119.7 37.9 119.1 37.7 118.7 37.2 C 117.8 36.3 117.8 34.9 118.7 34 L 121.7 31 C 122.6 30.1 124 30.1 124.9 31 C 125.8 31.9 125.8 33.3 124.9 34.2 L 121.9 37.2 C 121.5 37.7 120.9 37.9 120.3 37.9 Z"/><path class="st0" d="M 133.7 37.9 C 133.1 37.9 132.5 37.7 132.1 37.2 L 129.1 34.2 C 128.2 33.3 128.2 31.9 129.1 31 C 130 30.1 131.4 30.1 132.3 31 L 135.3 34 C 136.2 34.9 136.2 36.3 135.3 37.2 C 134.8 37.7 134.2 37.9 133.7 37.9 Z"/></g></g>
                </svg>
            </div>
        </div>
    </div>
    <div class="${properties.kcFormCardClass!}">
        <header class="${properties.kcFormHeaderClass!}">
            <#if realm.internationalizationEnabled  && locale.supported?size gt 1>
                <div class="${properties.kcLocaleMainClass!}" id="kc-locale">
                    <div id="kc-locale-wrapper" class="${properties.kcLocaleWrapperClass!}">
                        <div id="kc-locale-dropdown" class="${properties.kcLocaleDropDownClass!}">
                            <a href="#" id="kc-current-locale-link">${locale.current}</a>
                            <ul class="${properties.kcLocaleListClass!}">
                                <#list locale.supported as l>
                                    <li class="${properties.kcLocaleListItemClass!}">
                                        <a class="${properties.kcLocaleItemClass!}" href="${l.url}">${l.label}</a>
                                    </li>
                                </#list>
                            </ul>
                        </div>
                    </div>
                </div>
            </#if>
        <#if !(auth?has_content && auth.showUsername() && !auth.showResetCredentials())>
            <#if displayRequiredFields>
                <div class="${properties.kcContentWrapperClass!}">
                    <div class="${properties.kcLabelWrapperClass!} subtitle">
                        <span class="subtitle"><span class="required">*</span> ${msg("requiredFields")}</span>
                    </div>
                    <div class="col-md-10">
                        <h1 id="kc-page-title"><#nested "header"></h1>
                    </div>
                </div>
            <#else>
                <h1 id="kc-page-title"><#nested "header"></h1>
            </#if>
        <#else>
            <#if displayRequiredFields>
                <div class="${properties.kcContentWrapperClass!}">
                    <div class="${properties.kcLabelWrapperClass!} subtitle">
                        <span class="subtitle"><span class="required">*</span> ${msg("requiredFields")}</span>
                    </div>
                    <div class="col-md-10">
                        <#nested "show-username">
                        <div id="kc-username" class="${properties.kcFormGroupClass!}">
                            <label id="kc-attempted-username">${auth.attemptedUsername}</label>
                            <a id="reset-login" href="${url.loginRestartFlowUrl}">
                                <div class="kc-login-tooltip">
                                    <i class="${properties.kcResetFlowIcon!}"></i>
                                    <span class="kc-tooltip-text">${msg("restartLoginTooltip")}</span>
                                </div>
                            </a>
                        </div>
                    </div>
                </div>
            <#else>
                <#nested "show-username">
                <div id="kc-username" class="${properties.kcFormGroupClass!}">
                    <label id="kc-attempted-username">${auth.attemptedUsername}</label>
                    <a id="reset-login" href="${url.loginRestartFlowUrl}">
                        <div class="kc-login-tooltip">
                            <i class="${properties.kcResetFlowIcon!}"></i>
                            <span class="kc-tooltip-text">${msg("restartLoginTooltip")}</span>
                        </div>
                    </a>
                </div>
            </#if>
        </#if>
      </header>
      <div id="kc-content">
        <div id="kc-content-wrapper">

          <#-- App-initiated actions should not see warning messages about the need to complete the action -->
          <#-- during login.                                                                               -->
          <#if displayMessage && message?has_content && (message.type != 'warning' || !isAppInitiatedAction??)>
              <div class="alert-${message.type} ${properties.kcAlertClass!} pf-m-<#if message.type = 'error'>danger<#else>${message.type}</#if>">
                  <div class="pf-c-alert__icon">
                      <#if message.type = 'success'><span class="${properties.kcFeedbackSuccessIcon!}"></span></#if>
                      <#if message.type = 'warning'><span class="${properties.kcFeedbackWarningIcon!}"></span></#if>
                      <#if message.type = 'error'><span class="${properties.kcFeedbackErrorIcon!}"></span></#if>
                      <#if message.type = 'info'><span class="${properties.kcFeedbackInfoIcon!}"></span></#if>
                  </div>
                      <span class="${properties.kcAlertTitleClass!}">${kcSanitize(message.summary)?no_esc}</span>
              </div>
          </#if>

          <#nested "form">

            <#if auth?has_content && auth.showTryAnotherWayLink() && showAnotherWayIfPresent>
                <form id="kc-select-try-another-way-form" action="${url.loginAction}" method="post">
                    <div class="${properties.kcFormGroupClass!}">
                        <input type="hidden" name="tryAnotherWay" value="on"/>
                        <a href="#" id="try-another-way"
                           onclick="document.forms['kc-select-try-another-way-form'].submit();return false;">${msg("doTryAnotherWay")}</a>
                    </div>
                </form>
            </#if>

          <#if displayInfo>
              <div id="kc-info" class="${properties.kcSignUpClass!}">
                  <div id="kc-info-wrapper" class="${properties.kcInfoAreaWrapperClass!}">
                      <#nested "info">
                  </div>
              </div>
          </#if>
        </div>
      </div>

    </div>
  </div>
</body>
</html>
</#macro>