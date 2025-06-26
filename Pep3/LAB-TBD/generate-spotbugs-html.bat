@echo off
echo Generando reporte HTML de SpotBugs...

REM Verificar si existe el archivo XML
if not exist "target\spotbugsXml.xml" (
    echo Error: No se encontró el archivo spotbugsXml.xml
    exit /b 1
)

REM Crear directorio para HTML si no existe
if not exist "target\spotbugs-html" mkdir "target\spotbugs-html"

REM Copiar archivos XSLT de SpotBugs si están disponibles
if exist "%USERPROFILE%\.m2\repository\com\github\spotbugs\spotbugs\4.5.3.0\spotbugs-4.5.3.0.jar" (
    echo Extrayendo archivos XSLT de SpotBugs...
    jar xf "%USERPROFILE%\.maven\repository\com\github\spotbugs\spotbugs\4.5.3.0\spotbugs-4.5.3.0.jar" "xsl/default.xsl"
    if exist "xsl\default.xsl" (
        copy "xsl\default.xsl" "target\spotbugs-html\"
        rmdir /s /q "xsl"
    )
)

REM Generar HTML usando XSLT si xsltproc está disponible
where xsltproc >nul 2>&1
if %errorlevel% equ 0 (
    echo Generando HTML con xsltproc...
    xsltproc -o target\spotbugs-html\index.html target\spotbugsXml.xml
) else (
    echo xsltproc no está disponible, creando reporte HTML básico...
    echo ^<!DOCTYPE html^>^<html^>^<head^>^<title^>SpotBugs Report^</title^>^</head^>^<body^>^<h1^>SpotBugs Report^</h1^>^<p^>XML report generated successfully.^</p^>^<p^>File: ^<a href="../spotbugsXml.xml"^>spotbugsXml.xml^</a^>^</p^>^</body^>^</html^> > target\spotbugs-html\index.html
)

echo Reporte HTML generado en target\spotbugs-html\index.html 