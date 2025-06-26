# 🚀 Configuración de Jenkins para el Pipeline

## ⚠️ **ESTADO ACTUAL DEL PROYECTO**

✅ **Pipeline Actualizado con:**
- OWASP Dependency Check integrado
- Prometheus y Grafana para monitoreo
- OWASP ZAP para escaneo de seguridad
- SpotBugs para análisis estático
- JaCoCo para cobertura de código
- Credenciales seguras con Jenkins Credentials
- Health check automático

## 🔐 **Configuración Requerida en Jenkins**

### 1. Credenciales de Docker Hub

En Jenkins, ir a **Manage Jenkins** > **Manage Credentials** y agregar:

- **Kind**: Secret text
- **Scope**: Global
- **Secret**: tu_password_de_docker_hub
- **ID**: dckrhubpassword
- **Description**: Docker Hub password for derflinger

### 2. Plugins Requeridos

Instalar estos plugins en Jenkins:

1. **HTML Publisher Plugin** (versión 1.25+)
2. **Docker Pipeline** (versión 1.4+)
3. **Credentials Binding Plugin** (versión 1.27+)
4. **Warnings Next Generation Plugin** (para SpotBugs)

## 📋 **Pasos para Ejecutar el Pipeline**

### **Paso 1: Subir Cambios al Repositorio**

```bash
# Commit y push de todos los cambios
git add .
git commit -m "Pipeline completo con monitoreo y seguridad"
git push origin main
```

### **Paso 2: Configurar Job en Jenkins**

1. **Crear nuevo Pipeline Job**
   - Name: `pep3-devsecops-pipeline`
   - Type: Pipeline

2. **Configurar Pipeline**
   - Definition: Pipeline script from SCM
   - SCM: Git
   - Repository URL: `https://github.com/siposisp/pep1devsecops`
   - Branch: `*/main`
   - Script Path: `Pep3/jenkinsFile`

3. **Configurar Build Triggers**
   - Poll SCM: `H/5 * * * *` (cada 5 minutos)

### **Paso 3: Ejecutar Pipeline**

1. **Build Now** en Jenkins
2. **Monitorear** la ejecución en tiempo real
3. **Revisar** los reportes generados

## 🔍 **Verificación de Funcionamiento**

### **Endpoints a Verificar:**

- **Aplicación**: http://localhost:8090
- **Health Check**: http://localhost:8090/actuator/health
- **Métricas**: http://localhost:8090/actuator/prometheus
- **Prometheus**: http://localhost:9090
- **Grafana**: http://localhost:3000 (admin/admin)

### **Reportes Generados:**

- **OWASP Dependency Check**: `${BUILD_URL}OWASP_Dependency_Check_Report/`
- **JaCoCo Coverage**: `${BUILD_URL}JaCoCo_Coverage_Report/`
- **SpotBugs**: `${BUILD_URL}SpotBugs_HTML_Report/`
- **OWASP ZAP Security**: `${BUILD_URL}OWASP_ZAP_Security_Report/`

## 📊 **Flujo del Pipeline Actualizado**

1. **Checkout** - Clonar repositorio
2. **OWASP Dependency Check** - Análisis de vulnerabilidades en dependencias
3. **Build Backend** - Compilar aplicación
4. **Test Backend** - Ejecutar tests con cobertura JaCoCo
5. **SpotBugs Analysis** - Análisis estático de código
6. **SonarQube Analysis** - Análisis de calidad de código
7. **Build Docker Images** - Construir imágenes
8. **Push Docker Images** - Subir al registro con credenciales seguras
9. **Deploy Applications** - Desplegar apps
10. **Start Monitoring** - Iniciar Prometheus/Grafana
11. **Health Check** - Verificar aplicación
12. **OWASP ZAP** - Escaneo de seguridad dinámico
13. **Publicar Reportes** - Generar reportes HTML

## 🛠️ **Troubleshooting**

### **Problemas Comunes:**

1. **Credenciales de Docker fallan**
   ```
   Error: docker login failed
   Solución: Verificar que la credencial 'dckrhubpassword' esté configurada
   ```

2. **OWASP Dependency Check falla**
   ```
   Error: No vulnerabilities database found
   Solución: Verificar conexión a internet para descargar base de datos
   ```

3. **Aplicación no responde**
   ```
   Error: Connection refused
   Solución: Verificar que la aplicación esté desplegada en puerto 8090
   ```

4. **OWASP ZAP falla**
   ```
   Error: No targets found
   Solución: Verificar health check y que la app esté funcionando
   ```

5. **Prometheus no recolecta métricas**
   ```
   Error: No metrics available
   Solución: Verificar que /actuator/prometheus esté accesible
   ```

### **Comandos de Debug:**

```bash
# Verificar contenedores
docker ps

# Ver logs de la aplicación
docker logs pep3devsecops_backend

# Verificar health check
curl http://localhost:8090/actuator/health

# Verificar métricas
curl http://localhost:8090/actuator/prometheus

# Verificar Prometheus
curl http://localhost:9090/api/v1/targets

# Verificar Grafana
curl http://localhost:3000/api/health
```

## ✅ **Checklist Pre-Ejecución**

- [ ] Credencial 'dckrhubpassword' configurada en Jenkins
- [ ] Plugins requeridos instalados
- [ ] SonarQube funcionando en puerto 9000
- [ ] Repositorio actualizado con todos los cambios
- [ ] Job de Jenkins configurado correctamente
- [ ] Docker y Docker Compose disponibles en Jenkins

## 🎯 **Resultado Esperado**

Al finalizar el pipeline exitosamente:

- ✅ Aplicación desplegada y funcionando en puerto 8090
- ✅ Prometheus recolectando métricas en puerto 9090
- ✅ Grafana mostrando dashboards en puerto 3000
- ✅ Reportes de seguridad generados (OWASP Dependency Check, ZAP)
- ✅ Reportes de calidad generados (SpotBugs, JaCoCo)
- ✅ Todas las herramientas integradas y funcionando

## 📈 **Métricas y KPIs**

### **Métricas de Seguridad**
- Vulnerabilidades críticas detectadas por OWASP Dependency Check
- Vulnerabilidades de aplicación detectadas por OWASP ZAP
- Tiempo de remediación de vulnerabilidades

### **Métricas de Calidad**
- Cobertura de código (JaCoCo)
- Issues de SpotBugs
- Code smells de SonarQube
- Technical debt

### **Métricas de Rendimiento**
- Tiempo de respuesta HTTP (Prometheus)
- Uso de memoria JVM (Prometheus)
- Database performance (Prometheus)

## 📞 **Soporte**

Si encuentras problemas:

1. Revisar logs del pipeline en Jenkins
2. Verificar configuración de credenciales
3. Comprobar que todos los servicios estén funcionando
4. Revisar la documentación de cada herramienta
5. Verificar conectividad de red para descargas de bases de datos 