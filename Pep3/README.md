# Pep3 - Sistema de Monitoreo Completo con Grafana y Prometheus

Este proyecto incluye una aplicación Spring Boot completa con un sistema de monitoreo integrado usando Grafana y Prometheus, además de un pipeline de Jenkins automatizado.

## 🏗️ Arquitectura del Sistema

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Frontend      │    │   Backend       │    │   Database      │
│   (Vue.js)      │◄──►│   (Spring Boot) │◄──►│   (PostgreSQL)  │
│   Port: 3000    │    │   Port: 8097    │    │   Port: 5432    │
└─────────────────┘    └─────────────────┘    └─────────────────┘
                                │
                                ▼
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Prometheus    │◄──►│   Grafana       │    │   Node Exporter │
│   Port: 9090    │    │   Port: 3001    │    │   Port: 9100    │
└─────────────────┘    └─────────────────┘    └─────────────────┘
                                │
                                ▼
                       ┌─────────────────┐
                       │ Postgres Exporter│
                       │   Port: 9187    │
                       └─────────────────┘
```

## 🚀 Servicios Incluidos

### Aplicación Principal
- **Backend**: Spring Boot con Actuator y Micrometer
- **Frontend**: Vue.js con Vite
- **Database**: PostgreSQL

### Monitoreo
- **Prometheus**: Recolector de métricas
- **Grafana**: Visualización de métricas
- **Node Exporter**: Métricas del sistema
- **Postgres Exporter**: Métricas de PostgreSQL

## 📋 Requisitos Previos

- Docker y Docker Compose
- Java 17
- Node.js 18+
- Maven 3.8+
- Jenkins (para el pipeline)

## 🛠️ Instalación y Configuración

### 1. Clonar el Repositorio
```bash
git clone https://github.com/siposisp/pep1devsecops.git
cd pep1devsecops/Pep3
```

### 2. Configurar Credenciales de Jenkins
En Jenkins, crear las siguientes credenciales:
- **ID**: `dckrhubpassword`
- **Tipo**: Secret text
- **Descripción**: Docker Hub Password

### 3. Ejecutar el Pipeline de Jenkins
1. Crear un nuevo job en Jenkins
2. Configurar como "Pipeline"
3. Usar el archivo `jenkinsFile` del repositorio
4. Ejecutar el pipeline

## 🔧 Configuración Manual (Sin Jenkins)

### Backend
```bash
cd LAB-TBD
mvn clean install
mvn spring-boot:run
```

### Frontend
```bash
cd Frontend
npm install
npm run dev
```

### Con Docker Compose
```bash
# Backend con monitoreo completo
cd LAB-TBD
docker-compose up -d

# Frontend
cd Frontend
docker-compose up -d
```

## 📊 Acceso a los Servicios

Una vez desplegado, los servicios estarán disponibles en:

| Servicio | URL | Credenciales |
|----------|-----|--------------|
| **Frontend** | http://localhost:3000 | - |
| **Backend** | http://localhost:8097 | - |
| **Grafana** | http://localhost:3001 | admin/admin |
| **Prometheus** | http://localhost:9090 | - |
| **Node Exporter** | http://localhost:9100 | - |
| **Postgres Exporter** | http://localhost:9187 | - |

## 📈 Métricas Disponibles

### Spring Boot Actuator
- **Health Check**: `/actuator/health`
- **Métricas Prometheus**: `/actuator/prometheus`
- **Info**: `/actuator/info`
- **Métricas Generales**: `/actuator/metrics`

### Métricas Monitoreadas
- **HTTP Request Rate**: Tasa de peticiones HTTP
- **Average Response Time**: Tiempo promedio de respuesta
- **JVM Memory Usage**: Uso de memoria de la JVM
- **PostgreSQL Transactions**: Transacciones de base de datos
- **System Metrics**: Métricas del sistema operativo

## 🔍 Análisis de Calidad

El pipeline incluye:

### SpotBugs
- Análisis estático de código Java
- Detección de bugs potenciales
- Reporte HTML disponible en Jenkins

### SonarQube
- Análisis de calidad de código
- Métricas de cobertura
- Detección de code smells
- Vulnerabilidades de seguridad

## 🐳 Docker Images

Las imágenes se construyen y publican en Docker Hub:
- `derflinger/pep2devsecops_backend`
- `derflinger/pep2devsecops_frontend`

## 📝 Pipeline de Jenkins

El pipeline automatiza:

1. **Checkout**: Clonar código del repositorio
2. **Build Backend**: Compilar aplicación Spring Boot
3. **Test Backend**: Ejecutar pruebas unitarias
4. **SpotBugs Analysis**: Análisis estático
5. **SonarQube Analysis**: Análisis de calidad
6. **Build Docker Images**: Construir imágenes
7. **Push Docker Images**: Publicar en Docker Hub
8. **Build Frontend**: Compilar aplicación Vue.js
9. **Deploy**: Desplegar con Docker Compose
10. **Verificar Monitoreo**: Comprobar servicios

## 🔧 Configuración de Grafana

### Dashboards Incluidos
- **Spring Boot Application Dashboard**: Métricas de la aplicación
- **System Metrics**: Métricas del sistema
- **PostgreSQL Metrics**: Métricas de base de datos

### Configuración Automática
- Datasource de Prometheus configurado automáticamente
- Dashboards provisionados al iniciar Grafana

## 🚨 Troubleshooting

### Problemas Comunes

1. **Prometheus no puede conectar con la aplicación**
   - Verificar que el puerto 8097 esté expuesto
   - Comprobar que Actuator esté habilitado

2. **Grafana no muestra datos**
   - Verificar que Prometheus esté funcionando
   - Comprobar la configuración del datasource

3. **Docker Compose falla**
   - Verificar que los puertos no estén en uso
   - Comprobar permisos de Docker

### Logs Útiles
```bash
# Ver logs de todos los servicios
docker-compose logs -f

# Ver logs específicos
docker-compose logs -f app
docker-compose logs -f prometheus
docker-compose logs -f grafana
```

## 📚 Recursos Adicionales

- [Spring Boot Actuator Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html)
- [Prometheus Documentation](https://prometheus.io/docs/)
- [Grafana Documentation](https://grafana.com/docs/)
- [Micrometer Documentation](https://micrometer.io/docs)

## 🤝 Contribución

1. Fork el proyecto
2. Crear una rama para tu feature
3. Commit tus cambios
4. Push a la rama
5. Abrir un Pull Request

## 📄 Licencia

Este proyecto está bajo la Licencia MIT. Ver el archivo `LICENSE` para más detalles.
