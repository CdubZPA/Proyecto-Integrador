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
Panel General
<img width="1600" height="827" alt="image" src="https://github.com/user-attachments/assets/f26be400-75d8-4f20-bcff-c0d9888eb31c" />
Servidores Publicos
<img width="1600" height="822" alt="image" src="https://github.com/user-attachments/assets/3aea3f9b-53b4-4ff9-a721-ae423260b271" />
<img width="1600" height="830" alt="image" src="https://github.com/user-attachments/assets/1e87269f-ec0a-485e-a6b4-9a99100124e6" />
Gestion de Cargos
<img width="1600" height="824" alt="image" src="https://github.com/user-attachments/assets/a91873c0-ff55-4fe3-bae4-7410ae8e2d5f" />
Novedades Administrativas
<img width="1600" height="827" alt="image" src="https://github.com/user-attachments/assets/c1d958a5-7a40-44ff-891c-5569b852fd2c" />
<img width="1600" height="825" alt="image" src="https://github.com/user-attachments/assets/a4ba8ba2-b7b3-401c-ba35-63aa61ab010f" />




## Autor
Proyecto desarrollado por *Camilo Sierra*.

## Licencia
Puedes agregar una licencia como *MIT* o la que prefieras.
