# Sistema de Gestión de Inventario

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.7-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Angular](https://img.shields.io/badge/Angular-12.2.0-red.svg)](https://angular.io/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-blue.svg)](https://www.mysql.com/)
[![Docker](https://img.shields.io/badge/Docker-Ready-blue.svg)](https://www.docker.com/)

Sistema de gestión de inventario basado en arquitectura de microservicios, desarrollado con Spring Boot y Angular. Permite gestionar productos e inventarios de forma eficiente y escalable.

## 📋 Tabla de Contenidos

- [Arquitectura](#-arquitectura)
- [Stack Tecnológico](#-stack-tecnológico)
- [Prerequisitos](#-prerequisitos)
- [Instalación](#-instalación)
- [Configuración](#-configuración)
- [Ejecución](#-ejecución)
- [Docker](#-docker)
- [API Endpoints](#-api-endpoints)
- [Estado del Proyecto](#-estado-del-proyecto)
- [Contribución](#-contribución)

## 🏗 Arquitectura

Arquitectura de microservicios con dos servicios backend independientes:

- *Servicio de Producto* (Puerto 8090): Gestión CRUD de productos
- *Servicio de Inventario* (Puerto 8080): Gestión de inventario (en desarrollo)
- *Frontend Angular* (Puerto 4200): Interfaz de usuario


Frontend (Angular) → Servicio Producto → MySQL
                  → Servicio Inventario → MySQL


## 💻 Stack Tecnológico

*Backend:* Java 17, Spring Boot 3.5.7, Spring Data JPA, MySQL 8.0, Lombok, Maven  
*Frontend:* Angular 12.2.0, TypeScript 4.3.5, RxJS 6.6.0  
*DevOps:* Docker, Docker Compose

## 📦 Prerequisitos

- *JDK 17+*
- *Maven 3.6+*
- *Node.js 14+* y *npm 6+*
- *MySQL 8.0+* (opcional si usas Docker)
- *Docker Desktop* (para ejecutar con Docker)
- *Git*

## 🚀 Instalación

### 1. Clonar el repositorio

bash
git clone https://github.com/tu-usuario/SistemaGestionInventario.git
cd SistemaGestionInventario


### 2. Configurar Base de Datos (solo si NO usas Docker)

sql
CREATE DATABASE IF NOT EXISTS productdb;
CREATE DATABASE IF NOT EXISTS mydb;


## ⚙ Configuración

### Servicio de Producto

*Para desarrollo local* (backend/producto/src/main/resources/application.properties):
properties
spring.datasource.url=jdbc:mysql://localhost:3307/productdb/mydb?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=tu_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
server.port=8090


*Para Docker* (backend/producto/src/main/resources/application-docker.properties):
properties
spring.datasource.url=jdbc:mysql://mysql:3306/productdb?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=root_pass
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
server.port=8090


### Servicio de Inventario

Edita el archivo backend/inventario/src/main/resources/application.properties:
properties
spring.datasource.url=jdbc:mysql://localhost:3306/mydb?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=tu_password
spring.jpa.hibernate.ddl-auto=update
server.port=8080


## ▶ Ejecución

### Ejecutar con Docker (Recomendado)

#### Servicio de Producto

bash
cd backend/producto
docker compose up --build


El servicio estará disponible en: http://localhost:8090

*Nota:* El docker-compose.yml incluye:
- MySQL 8.0 en el puerto 3307 (externo) / 3306 (interno)
- Aplicación Spring Boot en el puerto 8090
- Health checks y dependencias configuradas
- Volúmenes persistentes para MySQL

### Ejecutar sin Docker

#### Servicio de Producto

bash
cd backend/producto
./mvnw spring-boot:run
# O en Windows:
# mvnw.cmd spring-boot:run


#### Servicio de Inventario

bash
cd backend/inventario
./mvnw spring-boot:run


#### Frontend

bash
cd frontend
npm install
npm start
# O
ng serve


## 🐳 Docker

### Configuración de Docker

El servicio de Producto está completamente dockerizado:

- *Dockerfile*: Multi-stage build con Maven 3.8 y Eclipse Temurin JRE 17
- *docker-compose.yml*: Orquesta MySQL y la aplicación Spring Boot
- *application-docker.properties*: Configuración específica para Docker

### Comandos Docker útiles

bash
# Construir y levantar servicios
docker compose up --build

# Ejecutar en segundo plano
docker compose up -d --build

# Ver logs
docker compose logs -f
docker compose logs app
docker compose logs mysql

# Detener servicios
docker compose down

# Detener y eliminar volúmenes
docker compose down -v

# Verificar contenedores
docker ps


### Requisitos de Docker

- *Docker Desktop* instalado y corriendo
- *Puerto 8090* disponible para la aplicación
- *Puerto 3307* disponible para MySQL (o cambiar en docker-compose.yml)

## 🔌 API Endpoints

### Servicio de Producto

Base URL: http://localhost:8090/api/products

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | /api/products | Listar todos los productos |
| GET | /api/products/{id} | Obtener producto por ID |
| POST | /api/products | Crear nuevo producto |
| PUT | /api/products/{id} | Actualizar producto |
| DELETE | /api/products/{id} | Eliminar producto |

### Ejemplos de Uso

#### PowerShell (Windows)

*Crear un producto:*
powershell
$body = @{
    name = "Micrófono HyperX QuadCast S"
    description = "Micrófono condensador USB con RGB y filtro antipop"
    price = 720000.00
    sku = "HYPERX-001"
} | ConvertTo-Json

Invoke-RestMethod -Uri 'http://localhost:8090/api/products' `
  -Method Post `
  -ContentType 'application/json' `
  -Body $body


*O sin SKU (se genera automáticamente):*
powershell
$body = @{
    name = "Teclado Mecánico"
    description = "Teclado RGB con switches mecánicos"
    price = 350000.00
} | ConvertTo-Json

Invoke-RestMethod -Uri 'http://localhost:8090/api/products' `
  -Method Post `
  -ContentType 'application/json' `
  -Body $body


*Listar todos los productos:*
powershell
Invoke-RestMethod -Uri 'http://localhost:8090/api/products' -Method Get


*Obtener producto por ID:*
powershell
Invoke-RestMethod -Uri 'http://localhost:8090/api/products/1' -Method Get


#### Bash/Linux/Mac

*Crear un producto:*
bash
curl -X POST http://localhost:8090/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Micrófono HyperX QuadCast S",
    "description": "Micrófono condensador USB con RGB y filtro antipop",
    "price": 720000.00,
    "sku": "HYPERX-001"
  }'


*Listar productos:*
bash
curl http://localhost:8090/api/products


### Formato de Request/Response

*Request (POST):*
json
{
  "name": "Nombre del Producto",
  "description": "Descripción del producto",
  "price": 99.99,
  "sku": "SKU-12345"  // Opcional, se genera automáticamente si no se proporciona
}


*Response:*
json
{
  "id": 1,
  "name": "Nombre del Producto",
  "description": "Descripción del producto",
  "price": 99.99,
  "sku": "SKU-12345",
  "createdAt": "2024-11-04T19:05:31"
}


## 📊 Estado del Proyecto

### ✅ Completado

- [x] Servicio de Producto con CRUD completo
- [x] Configuración de base de datos MySQL
- [x] Arquitectura en capas (Controller-Service-Repository)
- [x] Generación automática de SKU
- [x] Dockerización completa del servicio de Producto
- [x] Docker Compose con MySQL y aplicación
- [x] Health checks y dependencias configuradas
- [x] Perfil de configuración para Docker

### 🚧 En Desarrollo

- [ ] Servicio de Inventario
- [ ] Frontend Angular completo
- [ ] Integración frontend-backend
- [ ] Validación de datos en DTOs
- [ ] Manejo centralizado de excepciones
- [ ] Tests unitarios e integración
- [ ] Documentación con Swagger/OpenAPI
- [ ] Dockerización del servicio de Inventario

### 📝 Pendiente

- [ ] Autenticación y autorización
- [ ] Comunicación entre microservicios
- [ ] CI/CD pipeline
- [ ] Logging y monitoreo
- [ ] Variables de entorno para configuración

## 👥 Autor

*Andres Felipe Martinez Delgado* - Desarrollo inicial - [GitHub](https://github.com/anfemardel)