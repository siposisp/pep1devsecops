# PEP3 - Sistema de Gestión de Emergencias

## 🚀 Descripción

Sistema completo de gestión de emergencias desarrollado con Vue.js (Frontend) y Spring Boot (Backend), incluyendo monitoreo con Prometheus y Grafana.

## 🏗️ Arquitectura

- **Frontend**: Vue.js 3 + Vite + Tailwind CSS
- **Backend**: Spring Boot 3 + Java 17 + PostgreSQL
- **Base de Datos**: PostgreSQL 15
- **Monitoreo**: Prometheus + Grafana
- **Contenedores**: Docker + Docker Compose
- **CI/CD**: Jenkins Pipeline

## 📋 Prerrequisitos

- Docker y Docker Compose
- Node.js 18+ (para desarrollo)
- Java 17+ (para desarrollo)
- Maven 3.9+ (para desarrollo)

## 🚀 Despliegue Rápido

### Opción 1: Script Automático
```bash
# Desde la carpeta Pep3
chmod +x deploy.sh
./deploy.sh
```

### Opción 2: Manual
```bash
# 1. Crear red de Docker
docker network create pep3-network

# 2. Construir imágenes
cd Frontend && docker build -t derflinger/pep3_frontend:latest . && cd ..
cd LAB-TBD && docker build -t derflinger/pep3_backend:latest . && cd ..

# 3. Desplegar servicios
docker-compose up -d
```

## 🌐 Puertos de Acceso

- **Frontend**: http://localhost:3000
- **Backend API**: http://localhost:8097
- **Grafana**: http://localhost:3001 (admin/admin)
- **Prometheus**: http://localhost:9090
- **PostgreSQL**: localhost:5432

## 📊 Monitoreo

### Grafana Dashboards
- **Dashboard Principal**: http://localhost:3001/d/pep3/main
- **Métricas de Aplicación**: http://localhost:3001/d/pep3/application
- **Métricas de Sistema**: http://localhost:3001/d/pep3/system

### Endpoints de Monitoreo
- **Health Check Backend**: http://localhost:8097/actuator/health
- **Metrics Backend**: http://localhost:8097/actuator/metrics
- **Prometheus Backend**: http://localhost:8097/actuator/prometheus

## 🔧 Desarrollo

### Frontend (Vue.js)
```bash
cd Frontend
npm install
npm run dev
```

### Backend (Spring Boot)
```bash
cd LAB-TBD
mvn spring-boot:run
```

## 🐳 Docker

### Construir imágenes
```bash
# Frontend
cd Frontend
docker build -t derflinger/pep3_frontend .

# Backend
cd LAB-TBD
docker build -t derflinger/pep3_backend .
```

### Subir imágenes
```bash
docker push derflinger/pep3_frontend
docker push derflinger/pep3_backend
```

## 🔄 CI/CD con Jenkins

El proyecto incluye un Jenkinsfile completo que automatiza:

1. **Checkout** del código desde el repositorio pep1devsecops
2. **Build** de Frontend y Backend
3. **Tests** unitarios e integración
4. **Security Scans** (SonarQube, OWASP, SpotBugs)
5. **Build** de imágenes Docker
6. **Push** a Docker Hub
7. **Deploy** automático

### Configurar Jenkins

1. Crear credenciales en Jenkins:
   - `sonar-token`: Token de SonarQube
   - `docker-hub-credentials`: Credenciales de Docker Hub

2. Configurar SonarQube:
   - Instalar plugin SonarQube Scanner
   - Configurar servidor SonarQube

3. Ejecutar pipeline:
   ```bash
   # En Jenkins
   # Crear nuevo pipeline desde SCM
   # URL: <repository-url>
   # Script path: Jenkinsfile
   ```

## 🔒 Seguridad

### Análisis de Seguridad Incluidos
- **SonarQube**: Análisis de código estático
- **OWASP Dependency Check**: Verificación de vulnerabilidades en dependencias
- **SpotBugs**: Detección de bugs potenciales
- **Health Checks**: Monitoreo de salud de servicios

### Configuración de Seguridad
- Headers de seguridad en nginx
- Usuario no-root en contenedores
- Health checks para todos los servicios
- Monitoreo continuo con alertas

## 📈 Métricas y Alertas

### Métricas Recolectadas
- **Aplicación**: Requests/sec, Response time, Error rate
- **Sistema**: CPU, Memory, Disk, Network
- **Base de Datos**: Connections, Query performance
- **JVM**: Memory usage, GC metrics

### Alertas Configuradas
- Servicios no saludables
- Alto uso de recursos
- Errores de aplicación
- Tiempo de respuesta lento

## 🛠️ Troubleshooting

### Problemas Comunes

1. **Servicios no inician**:
   ```bash
   docker-compose logs <service-name>
   ```

2. **Problemas de red**:
   ```bash
   docker network ls
   docker network inspect pep3-network
   ```

3. **Problemas de base de datos**:
   ```bash
   docker-compose exec postgres-db psql -U postgres -d LabTBD-2-2024
   ```

4. **Limpiar todo**:
   ```bash
   docker-compose down -v
   docker system prune -a
   ```

## 📝 Logs

### Ver logs en tiempo real
```bash
# Todos los servicios
docker-compose logs -f

# Servicio específico
docker-compose logs -f backend-pep3
```

## 🔧 Comandos Útiles

```bash
# Ver estado de servicios
docker-compose ps

# Reiniciar servicios
docker-compose restart

# Ver logs de un servicio específico
docker-compose logs -f front-pep3

# Acceder al contenedor
docker-compose exec backend-pep3 sh

# Backup de base de datos
docker-compose exec postgres-db pg_dump -U postgres LabTBD-2-2024 > backup.sql
```

## 📁 Estructura del Proyecto

```
Pep3/
├── Frontend/                 # Aplicación Vue.js
│   ├── src/
│   ├── Dockerfile
│   ├── docker-compose.yml
│   └── nginx.conf
├── LAB-TBD/                 # Aplicación Spring Boot
│   ├── src/
│   ├── Dockerfile
│   └── docker-compose.yml
├── monitoring/              # Configuración de monitoreo
│   ├── prometheus.yml
│   └── grafana/
├── docker-compose.yml       # Compose principal
├── deploy.sh               # Script de despliegue
└── README.md              # Este archivo
```

## 🤝 Contribución

1. Fork el proyecto
2. Crear feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push al branch (`git push origin feature/AmazingFeature`)
5. Abrir Pull Request

## 📄 Licencia

Este proyecto está bajo la Licencia MIT - ver el archivo [LICENSE](../LICENSE) para detalles.

---

**PEP3** - Sistema de Gestión de Emergencias con Monitoreo Avanzado 