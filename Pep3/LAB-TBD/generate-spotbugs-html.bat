@echo off
echo Generando reporte HTML de SpotBugs...

REM Verificar si existe el archivo XML
if not exist "target\spotbugsXml.xml" (
    echo Error: No se encontró el archivo spotbugsXml.xml
    echo Creando reporte HTML básico...
    goto :create_basic_html
)

REM Crear directorio para HTML si no existe
if not exist "target\spotbugs-html" mkdir "target\spotbugs-html"

REM Generar HTML usando XSLT si xsltproc está disponible
where xsltproc >nul 2>&1
if %errorlevel% equ 0 (
    echo Generando HTML con xsltproc...
    xsltproc -o target\spotbugs-html\index.html target\spotbugsXml.xml
    if exist "target\spotbugs-html\index.html" (
        echo Reporte HTML generado exitosamente con xsltproc
        goto :end
    )
)

:create_basic_html
echo Creando reporte HTML básico...
echo ^<!DOCTYPE html^> > target\spotbugs-html\index.html
echo ^<html^> >> target\spotbugs-html\index.html
echo ^<head^> >> target\spotbugs-html\index.html
echo ^<title^>SpotBugs Report - LAB-TBD^</title^> >> target\spotbugs-html\index.html
echo ^<style^> >> target\spotbugs-html\index.html
echo body { font-family: Arial, sans-serif; margin: 20px; } >> target\spotbugs-html\index.html
echo .header { background-color: #f0f0f0; padding: 10px; border-radius: 5px; } >> target\spotbugs-html\index.html
echo .content { margin-top: 20px; } >> target\spotbugs-html\index.html
echo .file-link { color: #0066cc; text-decoration: none; } >> target\spotbugs-html\index.html
echo .file-link:hover { text-decoration: underline; } >> target\spotbugs-html\index.html
echo ^</style^> >> target\spotbugs-html\index.html
echo ^</head^> >> target\spotbugs-html\index.html
echo ^<body^> >> target\spotbugs-html\index.html
echo ^<div class="header"^> >> target\spotbugs-html\index.html
echo ^<h1^>SpotBugs Report - LAB-TBD^</h1^> >> target\spotbugs-html\index.html
echo ^<p^>Fecha de generación: %date% %time%^</p^> >> target\spotbugs-html\index.html
echo ^</div^> >> target\spotbugs-html\index.html
echo ^<div class="content"^> >> target\spotbugs-html\index.html
echo ^<h2^>Información del Reporte^</h2^> >> target\spotbugs-html\index.html
echo ^<p^>Este reporte fue generado automáticamente por el pipeline de Jenkins.^</p^> >> target\spotbugs-html\index.html
if exist "target\spotbugsXml.xml" (
    echo ^<p^>Archivo XML disponible: ^<a href="../spotbugsXml.xml" class="file-link"^>spotbugsXml.xml^</a^>^</p^> >> target\spotbugs-html\index.html
    echo ^<p^>El análisis de SpotBugs se ejecutó correctamente.^</p^> >> target\spotbugs-html\index.html
) else (
    echo ^<p^>No se encontró el archivo XML de SpotBugs.^</p^> >> target\spotbugs-html\index.html
)
echo ^<h2^>Configuración^</h2^> >> target\spotbugs-html\index.html
echo ^<ul^> >> target\spotbugs-html\index.html
echo ^<li^>Plugin: SpotBugs Maven Plugin 4.5.3.0^</li^> >> target\spotbugs-html\index.html
echo ^<li^>Esfuerzo: Máximo^</li^> >> target\spotbugs-html\index.html
echo ^<li^>Umbral: Bajo^</li^> >> target\spotbugs-html\index.html
echo ^<li^>Tests incluidos: No^</li^> >> target\spotbugs-html\index.html
echo ^</ul^> >> target\spotbugs-html\index.html
echo ^</div^> >> target\spotbugs-html\index.html
echo ^</body^> >> target\spotbugs-html\index.html
echo ^</html^> >> target\spotbugs-html\index.html

:end
echo Reporte HTML generado en target\spotbugs-html\index.html 