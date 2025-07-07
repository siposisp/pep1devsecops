#!/bin/bash

# Script para verificar que todas las herramientas necesarias estén disponibles
# para ejecutar el pipeline de Jenkins con OWASP Dependency Check y OWASP ZAP

set -e

# Colores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

print_message() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

print_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

echo "=== VERIFICACIÓN DE HERRAMIENTAS PARA PIPELINE PEP3 DEVSECOPS ==="
echo

# Verificar Docker
print_message "Verificando Docker..."
if command -v docker &> /dev/null; then
    DOCKER_VERSION=$(docker --version)
    print_success "Docker encontrado: $DOCKER_VERSION"
else
    print_error "Docker no está instalado"
    exit 1
fi

# Verificar Maven
print_message "Verificando Maven..."
if command -v mvn &> /dev/null; then
    MAVEN_VERSION=$(mvn --version | head -n 1)
    print_success "Maven encontrado: $MAVEN_VERSION"
else
    print_error "Maven no está instalado"
    exit 1
fi

# Verificar Node.js y npm
print_message "Verificando Node.js y npm..."
if command -v node &> /dev/null && command -v npm &> /dev/null; then
    NODE_VERSION=$(node --version)
    NPM_VERSION=$(npm --version)
    print_success "Node.js encontrado: $NODE_VERSION"
    print_success "npm encontrado: $NPM_VERSION"
else
    print_error "Node.js o npm no están instalados"
    exit 1
fi

# Verificar curl
print_message "Verificando curl..."
if command -v curl &> /dev/null; then
    print_success "curl encontrado"
else
    print_error "curl no está instalado"
    exit 1
fi

# Verificar archivos de configuración
print_message "Verificando archivos de configuración..."

# Verificar pom.xml
if [ -f "LAB-TBD/pom.xml" ]; then
    print_success "pom.xml encontrado"
else
    print_error "pom.xml no encontrado en LAB-TBD/"
    exit 1
fi

# Verificar package.json
if [ -f "Frontend/package.json" ]; then
    print_success "package.json encontrado"
else
    print_error "package.json no encontrado en Frontend/"
    exit 1
fi

# Verificar docker-compose.yml
if [ -f "docker-compose.yml" ]; then
    print_success "docker-compose.yml encontrado"
else
    print_error "docker-compose.yml no encontrado"
    exit 1
fi

# Verificar JenkinsFile
if [ -f "jenkinsFile" ]; then
    print_success "jenkinsFile encontrado"
else
    print_error "jenkinsFile no encontrado"
    exit 1
fi

# Verificar owasp-suppressions.xml
if [ -f "LAB-TBD/owasp-suppressions.xml" ]; then
    print_success "owasp-suppressions.xml encontrado"
else
    print_warning "owasp-suppressions.xml no encontrado - se creará automáticamente"
fi

# Verificar imágenes Docker necesarias
print_message "Verificando imágenes Docker..."

# Verificar OWASP Dependency Check
if docker images | grep -q "owasp/dependency-check"; then
    print_success "Imagen OWASP Dependency Check disponible"
else
    print_warning "Imagen OWASP Dependency Check no encontrada - se descargará automáticamente"
fi

# Verificar OWASP ZAP
if docker images | grep -q "owasp/zap2docker-stable"; then
    print_success "Imagen OWASP ZAP disponible"
else
    print_warning "Imagen OWASP ZAP no encontrada - se descargará automáticamente"
fi

# Verificar puertos disponibles
print_message "Verificando puertos disponibles..."

# Puerto 3000 (Frontend)
if netstat -tuln 2>/dev/null | grep -q ":3000 "; then
    print_warning "Puerto 3000 está en uso"
else
    print_success "Puerto 3000 disponible"
fi

# Puerto 8097 (Backend)
if netstat -tuln 2>/dev/null | grep -q ":8097 "; then
    print_warning "Puerto 8097 está en uso"
else
    print_success "Puerto 8097 disponible"
fi

# Puerto 5432 (PostgreSQL)
if netstat -tuln 2>/dev/null | grep -q ":5432 "; then
    print_warning "Puerto 5432 está en uso"
else
    print_success "Puerto 5432 disponible"
fi

echo
echo "=== RESUMEN DE VERIFICACIÓN ==="
print_success "✅ Todas las herramientas básicas están disponibles"
print_success "✅ Archivos de configuración encontrados"
print_success "✅ Pipeline listo para ejecutar"

echo
echo "=== PRÓXIMOS PASOS ==="
echo "1. Ejecutar el pipeline en Jenkins"
echo "2. Revisar los reportes generados en los artifacts"
echo "3. Corregir vulnerabilidades encontradas"
echo "4. Re-ejecutar el pipeline después de las correcciones"

echo
echo "=== COMANDOS ÚTILES ==="
echo "# Ejecutar escaneo manual de dependencias del backend:"
echo "cd LAB-TBD && mvn org.owasp:dependency-check-maven:check"
echo
echo "# Ejecutar escaneo manual de dependencias del frontend:"
echo "cd Frontend && docker run --rm -e NVD_API_KEY=bc4587bd-a2ed-47fa-8db2-6622440565e2 -v \$(pwd):/src owasp/dependency-check:latest --project pep3-frontend --scan /src --format HTML --out /src"
echo
echo "# Ejecutar escaneo manual con ZAP:"
echo "docker run --rm -v \$(pwd)/zap-reports:/zap/reports -t owasp/zap2docker-stable:latest zap-baseline.py -t http://localhost:3000 -r zap-report.html -I" 