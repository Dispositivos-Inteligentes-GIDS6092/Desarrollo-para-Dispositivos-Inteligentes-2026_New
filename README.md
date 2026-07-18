# SmartHealth Monitor
[Android CI](https://img.shields.io/badge/Android-API26+-green)
[Compose](https://img.shields.io/badge/Jetpack%20Compose-MD3-blue)


Aplicación Android de monitoreo de salud personal en tiempo real.
Desarrollada como proyecto integrador — UTNG 9° Cuatrimestre 2025.



## Stack tecnológico
| Tecnología | Uso |
|---|---|
| Kotlin + Jetpack Compose | UI declarativa con Material Design 3 |
| Wearable Data Layer API  | Comunicación reloj ↔ teléfono (BLE) |
| Health Services API     | Sensor FC real en background (Wear OS) |
| Room Database           | Historial persistente de lecturas FC |
| Jetpack Navigation      | NavHost entre 4 pantallas |
| GitHub + Conventional Commits | Control de versiones profesional |

## Pantallas
| Pantalla | Descripción |
|---|---|
| LoginScreen | Autenticación con validación y State |
| DashboardScreen | FC y Pasos en tiempo real del wearable |
| HistorialScreen | Lecturas persistidas en Room con Flow reactivo |
| AlertaScreen | AlertDialog MD3 + Snackbar de confirmación |


## Unidad II — Wear OS

| Pantalla | Descripción |
|---|---|
| WearDashboardScreen | FC en tiempo real con ScalingLazyColumn y TimeText |
| WearHistorialScreen | Lista con Rotary Input (corona del reloj) |
| WearAlertaScreen | Botones circulares de confirmación |
| SmartHealth WatchFace | Hora + FC en el WatchFace nativo |

![img_1.png](img_1.png)


## Capturas de pantalla
![img_2.png](img_2.png)
![img_3.png](img_3.png)
![img_4.png](img_4.png)
## 🏗️ Arquitectura del Proyecto

```mermaid
graph TD
    subgraph "Módulo App (Teléfono)"
        A[DashboardScreen] --> B[DashboardViewModel]
        A --> C[CastButton]
        C --> D[Chromecast]
    end
    
    subgraph "Módulo Wear OS"
        E[WearDashboardScreen] --> F[WearHistorialScreen]
        E --> G[WearSensors]
    end
    
    subgraph "Módulo TV"
        H[TvCatalogScreen] --> I[TvDetailScreen]
        I --> J[TvPlaybackScreen]
        J --> K[ExoPlayer]
    end
    
    subgraph "Capa de Datos"
        L[Room Database]
        M[Repository]
        N[SensorManager]
    end
    
    A --> M
    B --> L
    E --> N
    E --> M
    F --> L
    H --> M
    I --> L
    
    M --> L
    N --> M
    
    D --> J
```

# PANTALLAS 
![img_5.png](img_5.png)
![img_6.png](img_6.png)

### Tecnologías principales:
- **App**: Jetpack Compose, Material3, Room, Cast SDK
- **Wear**: Wear Compose, Horologist, SensorManager
- **TV**: Compose for TV, ExoPlayer, D-pad Navigation
- **Arquitectura**: Clean Architecture + MVVM

## Autor
Lizeth Ramírez — UTNG — Ing. en Desarrollo y Gestión de Software
rmm.che@gmail.com