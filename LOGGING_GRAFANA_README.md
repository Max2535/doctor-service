# Logging Configuration for Grafana

## Overview
โปรเจคนี้ได้รับการกำหนดค่าให้ส่ง logs ในรูปแบบ JSON เพื่อใช้กับ Grafana และ Loki

## Features
- ✅ JSON logging format สำหรับ Grafana/Loki
- ✅ Standard text logging สำหรับการอ่านง่าย
- ✅ Async logging เพื่อ performance ที่ดีขึ้น
- ✅ Log rotation (10MB per file, 30 days retention)
- ✅ Actuator endpoints สำหรับ health checks และ metrics
- ✅ Prometheus metrics integration

## Log Files
Logs จะถูกเก็บไว้ที่ `logs/` directory:
- `hospital-management-system.log` - Standard text format
- `hospital-management-system-json.log` - JSON format สำหรับ Grafana/Loki

## Actuator Endpoints
เข้าถึงได้ที่: `http://localhost:8080/actuator`

### Available Endpoints:
- `/actuator/health` - Health check status
- `/actuator/metrics` - Application metrics
- `/actuator/prometheus` - Prometheus format metrics
- `/actuator/loggers` - View/modify log levels
- `/actuator/env` - Environment properties
- `/actuator/info` - Application information

## Grafana Loki Configuration

### 1. ติดตั้ง Promtail (Log collector)
```yaml
# promtail-config.yaml
server:
  http_listen_port: 9080
  grpc_listen_port: 0

positions:
  filename: /tmp/positions.yaml

clients:
  - url: http://loki:3100/loki/api/v1/push

scrape_configs:
  - job_name: hospital-management-system
    static_configs:
      - targets:
          - localhost
        labels:
          job: hospital-management-system
          __path__: /path/to/logs/hospital-management-system-json.log
```

### 2. Run Promtail
```bash
promtail -config.file=promtail-config.yaml
```

### 3. Grafana Loki Dashboard Queries
```logql
# View all logs
{job="hospital-management-system"}

# Filter by log level
{job="hospital-management-system"} |= "ERROR"
{job="hospital-management-system"} |= "WARN"

# Filter by logger name
{job="hospital-management-system"} | json | logger_name=~"com.example.doctorservice.*"

# SQL queries only
{job="hospital-management-system"} | json | logger_name="org.hibernate.SQL"

# Error rate
rate({job="hospital-management-system"} |= "ERROR" [5m])
```

## Docker Compose Example

```yaml
version: '3.8'

services:
  # Loki
  loki:
    image: grafana/loki:latest
    ports:
      - "3100:3100"
    volumes:
      - ./loki-config.yaml:/etc/loki/local-config.yaml
    command: -config.file=/etc/loki/local-config.yaml

  # Promtail
  promtail:
    image: grafana/promtail:latest
    volumes:
      - ./logs:/logs
      - ./promtail-config.yaml:/etc/promtail/config.yaml
    command: -config.file=/etc/promtail/config.yaml
    depends_on:
      - loki

  # Grafana
  grafana:
    image: grafana/grafana:latest
    ports:
      - "3000:3000"
    environment:
      - GF_SECURITY_ADMIN_PASSWORD=admin
    volumes:
      - grafana-storage:/var/lib/grafana
    depends_on:
      - loki

  # Prometheus (for metrics)
  prometheus:
    image: prom/prometheus:latest
    ports:
      - "9090:9090"
    volumes:
      - ./prometheus.yml:/etc/prometheus/prometheus.yml
    command:
      - '--config.file=/etc/prometheus/prometheus.yml'

volumes:
  grafana-storage:
```

### Prometheus Configuration
```yaml
# prometheus.yml
global:
  scrape_interval: 15s

scrape_configs:
  - job_name: 'hospital-management-system'
    metrics_path: '/actuator/prometheus'
    static_configs:
      - targets: ['host.docker.internal:8080']
```

## Useful Grafana Dashboards

### 1. Application Logs Dashboard
- Log volume over time
- Error rate
- Log levels distribution
- Recent errors

### 2. Application Metrics Dashboard (Prometheus)
- JVM memory usage
- CPU usage
- HTTP request rate
- Database connection pool
- Response times

### 3. SQL Performance Dashboard
- SQL query count
- Slow queries
- Database errors

## Log Levels
สามารถเปลี่ยน log level แบบ runtime ได้ผ่าน Actuator:

```bash
# View current log level
curl http://localhost:8080/actuator/loggers/com.example.doctorservice

# Change log level
curl -X POST http://localhost:8080/actuator/loggers/com.example.doctorservice \
  -H "Content-Type: application/json" \
  -d '{"configuredLevel":"DEBUG"}'
```

## Custom Fields in JSON Logs
JSON logs จะมี fields ดังนี้:
- `timestamp` - เวลาที่เกิด log
- `log_level` - ระดับ log (INFO, DEBUG, ERROR, etc.)
- `logger_name` - ชื่อของ logger
- `thread_name` - ชื่อ thread
- `message` - ข้อความ log
- `application` - ชื่อ application (hospital-management-system)
- `stack_trace` - Stack trace (ถ้ามี exception)

## Best Practices

1. **Use structured logging**
   ```java
   logger.info("User created doctor",
       kv("doctorId", doctor.getId()),
       kv("email", doctor.getEmail()));
   ```

2. **Use appropriate log levels**
   - ERROR: สำหรับ errors ที่ต้องแก้ไขทันที
   - WARN: สำหรับสถานการณ์ที่ผิดปกติแต่ยังทำงานได้
   - INFO: สำหรับ business events สำคัญ
   - DEBUG: สำหรับ debugging information

3. **Avoid logging sensitive data**
   - อย่า log passwords, tokens, credit cards
   - ใช้ masking สำหรับข้อมูลที่ sensitive

4. **Monitor log volume**
   - ตั้งค่า alerts สำหรับ error rate ที่สูง
   - Monitor disk space usage

## Troubleshooting

### Logs not appearing in Grafana?
1. ตรวจสอบว่า Promtail running และ config ถูกต้อง
2. ตรวจสอบว่า path ของ log file ถูกต้อง
3. ตรวจสอบ Promtail logs: `docker logs promtail`

### Performance issues?
1. เพิ่ม async queue size ใน logback-spring.xml
2. ลด log level ใน production
3. พิจารณาใช้ sampling สำหรับ high-volume logs

## Additional Resources
- [Grafana Loki Documentation](https://grafana.com/docs/loki/latest/)
- [Promtail Documentation](https://grafana.com/docs/loki/latest/clients/promtail/)
- [Spring Boot Actuator](https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html)
- [Logstash Logback Encoder](https://github.com/logfellow/logstash-logback-encoder)
