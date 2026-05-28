# Proyecto Integrador
Sistema de gestión de hojas de vida, perfiles de servidores públicos, dependencias y solicitudes administrativas para un entorno institucional.

## Descripción
*Proyecto Integrador* es una aplicación de escritorio desarrollada en *JavaFX* para centralizar y administrar información de talento humano.  
Permite consultar, registrar, actualizar y eliminar datos de servidores públicos, cargos y novedades administrativas desde una interfaz gráfica moderna conectada a una base de datos.

## Funcionalidades
- Panel principal con indicadores generales del sistema.
- Gestión de *servidores públicos*:
  - registro
  - actualización
  - eliminación
  - búsqueda por número de cédula
- Gestión de *cargos*:
  - registro
  - actualización
  - eliminación
  - búsqueda por número de cédula
- Gestión de *novedades administrativas*:
  - vacaciones
  - licencias
  - permisos
  - incapacidades
  - traslados
  - encargos
  - otros
- Navegación por módulos desde un menú lateral.
- Persistencia de datos en base de datos mediante JPA/Hibernate.

## Tecnologías utilizadas
- *Java 21*
- *JavaFX 21*
- *Gradle*
- *Hibernate / JPA*
- *MySQL*
- *FXML*
- *CSS*

## Requisitos
Antes de ejecutar el proyecto asegúrate de tener instalado:
- Java 21
- MySQL
- Gradle o el wrapper incluido en el proyecto

## Configuración de la base de datos
El proyecto está configurado para trabajar con la base de datos:
```sql
talento_humano
```
La configuración se encuentra en:
- `src/main/resources/META-INF/persistence.xml`
- `src/main/java/controller/MainController.java`

Si tu instalación de MySQL usa otro usuario, contraseña, host o puerto, debes actualizar esos valores.

## Instalación
1. Clona el repositorio:
```bash
git clone https://github.com/CdubZPA/Proyecto-Integrador.git
cd Proyecto-Integrador
```
2. Crea la base de datos en MySQL:
```sql
CREATE DATABASE talento_humano;
```
3. Verifica la configuración de conexión en `persistence.xml`.

4. Ejecuta el proyecto con Gradle:
```bash
./gradlew run
```
En Windows:
```bash
gradlew.bat run
```

## Estructura del proyecto
```
src/main/java/
├── controller/
├── dao/
├── model/
├── util/
└── org/example/

src/main/resources/
├── META-INF/
├── view/
├── images/
└── style.css
```

## Dependencias principales
- JavaFX Controls
- JavaFX FXML
- MySQL Connector/J
- Hibernate Core
- Jakarta Persistence API

## Capturas de pantalla
Puedes agregar aquí imágenes del dashboard y de cada módulo para mejorar la presentación del repositorio.

## Autor
Proyecto desarrollado por *Camilo Sierra*.

## Licencia
Puedes agregar una licencia como *MIT* o la que prefieras.
