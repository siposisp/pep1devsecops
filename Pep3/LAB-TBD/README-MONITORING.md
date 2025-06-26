# Integración de Herramientas de Monitoreo y Seguridad

Este proyecto incluye la integración de las siguientes herramientas:

## 🛡️ Herramientas de Seguridad

### 1. OWASP Dependency Check
- **Propósito**: Análisis de vulnerabilidades en dependencias
- **Configuración**: Plugin Maven en `pom.xml`
- **Ejecución**: `mvn org.owasp:dependency-check-maven:8.4.3:check`
- **Reportes**: Generados en `target/dependency-check-reports/`

### 2. OWASP ZAP
- **Propósito**: Análisis de seguridad dinámico (DAST)
- **Configuración**: Integrado en Jenkins pipeline
- **Ejecución**: Automática después del despliegue
- **Reportes**: Generados en `zap-reports/`

## 📊 Herramientas de Monitoreo

### 3. Prometheus
- **Propósito**: Recolección y almacenamiento de métricas
- **Puerto**: 9090
- **Configuración**: `prometheus.yml`
- **Métricas**: Expuestas en `/actuator/prometheus`

### 4. Grafana
- **Propósito**: Visualización de métricas
- **Puerto**: 3000
- **Credenciales**: admin/admin
- **Dashboards**: Configurados automáticamente

## 🚀 Instrucciones de Uso

### Ejecución Manual

1. **Iniciar el stack de monitoreo**:
   ```bash
   docker-compose -f docker-compose-monitoring.yml up -d
   ```

2. **Ejecutar OWASP Dependency Check**:
   ```bash
   mvn org.owasp:dependency-check-maven:8.4.3:check
   ```

3. **Ejecutar OWASP ZAP** (requiere aplicación desplegada):
   ```bash
   docker run --rm -v $(pwd)/zap-reports:/zap/wrk/:rw -t owasp/zap2docker-stable zap-baseline.py -t http://localhost:8090 -J zap-report.json -r zap-report.html -I
   ```

### Jenkins Pipeline

El pipeline de Jenkins incluye automáticamente:
1. OWASP Dependency Check
2. Build y test
3. SonarQube analysis
4. Despliegue
5. Inicio del stack de monitoreo
6. OWASP ZAP scan

### Acceso a Herramientas

- **Prometheus**: http://localhost:9090
- **Grafana**: http://localhost:3000 (admin/admin)
- **Métricas de la aplicación**: http://localhost:8090/actuator/prometheus

## 📋 Configuración de Métricas

La aplicación Spring Boot expone las siguientes métricas:
- HTTP requests (contador y duración)
- JVM memory usage
- Database connections
- Custom business metrics

## 🔧 Personalización

### Agregar Métricas Personalizadas

```java
@RestController
public class MetricsController {
    
    private final Counter customCounter = Counter.builder("custom_requests_total")
        .description("Custom requests counter")
        .register(Metrics.globalRegistry);
    
    @GetMapping("/custom-endpoint")
    public String customEndpoint() {
        customCounter.increment();
        return "Custom response";
    }
}
```

### Configurar Alertas en Prometheus

Agregar reglas en `prometheus.yml`:
```yaml
rule_files:
  - "alerts.yml"
```

### Crear Dashboards Personalizados en Grafana

1. Acceder a Grafana (http://localhost:3000)
2. Crear nuevo dashboard
3. Agregar paneles con queries de Prometheus

## 🛠️ Troubleshooting

### Problemas Comunes

1. **Prometheus no puede conectar con la aplicación**:
   - Verificar que la aplicación esté corriendo en puerto 8090
   - Verificar configuración en `prometheus.yml`

2. **OWASP ZAP falla**:
   - Verificar que la aplicación esté completamente desplegada
   - Aumentar el tiempo de espera en el pipeline

3. **Grafana no muestra datos**:
   - Verificar conexión con Prometheus
   - Verificar que Prometheus esté recolectando métricas

### Logs

- **Prometheus**: `docker logs prometheus`
- **Grafana**: `docker logs grafana`
- **Aplicación**: `docker logs <container-name>` 