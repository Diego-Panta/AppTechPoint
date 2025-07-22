Aquí tienes la información convertida al formato de un archivo Markdown (`.md`), ideal para usar como documentación en GitHub o en otro sistema de control de versiones:

````markdown
# Chimbote Computer Map - Guía de Instalación

**Chimbote Computer Map** es una aplicación móvil para Android que muestra en un mapa todos los establecimientos donde se venden equipos de cómputo en Chimbote, Perú.
````
---

## 📋 Características

- 🗺️ Mapa interactivo con Google Maps  
- 📍 Detección de ubicación del usuario  
- 🏪 Lista de 6 centros comerciales principales  
- 📱 Diseño Material Design con ConstraintLayout  
- 🔍 Recomendaciones de tiendas cercanas  
- 📊 Bottom Sheet con información detallada  

---
````
## 📱 Vistas de la aplicación

* Vista del mapa de Chimbote
<img width="717" height="1600" alt="image" src="https://github.com/user-attachments/assets/ddd9d31c-ff93-447f-9c05-373c7656b1c1" />

* Vista de alerta de los puntos más cercanos
<img width="717" height="1600" alt="image" src="https://github.com/user-attachments/assets/f2b1a0bd-a294-44ae-be59-301c0cb7978c" />

* Vista del punto más cercano
<img width="717" height="1600" alt="image" src="https://github.com/user-attachments/assets/382b791c-6d29-4a83-a103-650da0af3e84" />

* Vista de lista de todos los puntos de venta
<img width="717" height="1600" alt="image" src="https://github.com/user-attachments/assets/4413646b-0a0c-4c90-946e-5c61d5a0f7d9" />

---

## 🚀 Configuración del Proyecto

### 1. Crear el Proyecto en Android Studio

1. Abre Android Studio  
2. Crea un nuevo proyecto con los siguientes parámetros:
   - Template: Empty Activity  
   - Name: `AppTechPoint`  
   - Package: `com.example.apptechpoint`  
   - Language: Kotlin  
   - Minimum SDK: API 24  

### 2. Obtener API Key de Google Maps

1. Ve a [Google Cloud Console](https://console.cloud.google.com)  
2. Crea un proyecto o selecciona uno existente  
3. Habilita las siguientes APIs:
   - Maps SDK for Android  
   - Places API (opcional)  
   - Geocoding API (opcional)  
4. Crea una API Key y restríngela a tu aplicación Android  

### 3. Configurar el Proyecto

#### 3.1 `build.gradle` (nivel de proyecto)

```kotlin
plugins {
    id 'com.google.android.libraries.mapsplatform.secrets-gradle-plugin' version '2.0.1' apply false
}
````

#### 3.2 `strings.xml`

Reemplaza `TU_API_KEY_AQUI` con tu API Key real:

```xml
<string name="my_api_key">AIzaSyC...</string>
```

---

## 📁 Estructura de Archivos

```
app/src/main/
├── java/com/example/chimbotecomputermap/
│   ├── MainActivity.kt
│   ├── ComputerStore.kt
│   └── StoresAdapter.kt
├── res/
│   ├── layout/
│   │   ├── activity_main.xml
│   │   └── item_store.xml
│   ├── values/
│   │   ├── colors.xml
│   │   ├── strings.xml
│   │   └── themes.xml
│   └── drawable/
│       ├── bottom_sheet_background.xml
│       ├── bottom_sheet_handle.xml
│       ├── ic_location.xml
│       ├── ic_store.xml
│       └── ic_arrow_forward.xml
└── AndroidManifest.xml
```

---

## 🛠️ Instalación

1. **Copiar archivos**: Añade los archivos proporcionados en sus carpetas correspondientes.
2. **Sincronizar proyecto**: Haz clic en “Sync Now” en Android Studio.
3. **Permisos**: Verifica que los siguientes permisos estén en `AndroidManifest.xml`:

   * `ACCESS_FINE_LOCATION`
   * `ACCESS_COARSE_LOCATION`
   * `INTERNET`
   * `ACCESS_NETWORK_STATE`
4. **Probar la aplicación**:

   * Usa un emulador o dispositivo físico
   * Otorga los permisos de ubicación cuando se soliciten

---

## 📍 Ubicaciones Incluidas

* **Galerías Shopping Center** (-9.07370, -78.57712)
* **Centro Comercial Chimbote** (-9.07376, -78.58978)
* **Galerías Alfa** (-9.07486, -78.59126)
* **Chimbote Plaza Center** (-9.0741361, -78.5880261)
* **MegaPlaza Chimbote** (-9.1024389, -78.5576856)
* **Tu Centro Comercial Nuevo Chimbote** (-9.12798, -78.51976)

---

## 🎨 Características de Diseño

* Material Design 3
* ConstraintLayout
* Bottom Sheet
* FAB (Floating Action Button)
* RecyclerView
* Cards

---

## 🔧 Funcionalidades

### Detección de Ubicación

* Solicita permisos automáticamente
* Muestra ubicación actual en el mapa
* Calcula distancias a tiendas

### Recomendaciones

* Ordena tiendas por proximidad
* Muestra las 3 más cercanas
* Información detallada

### Interactividad

* Tocar marcadores para ver información
* Deslizar Bottom Sheet
* Tocar ítems de la lista para centrar el mapa

---

## 🚨 Troubleshooting

### Problemas Comunes

* **Mapa no se muestra**:

  * Verifica que la API Key sea válida
  * Habilita `Maps SDK for Android`
  * Verifica restricciones de la API Key

* **Error de permisos**:

  * Se solicitan automáticamente
  * También puedes otorgarlos manualmente:
    `Configuración > Aplicaciones`

* **Ubicación no funciona**:

  * Verifica que el GPS esté activado
  * Usa un dispositivo físico para mejor resultado
  * Verifica permisos otorgados

### Logs Útiles

```bash
adb logcat | grep ChimboteComputerMap
```

---

## 🔄 Personalización

### Agregar más tiendas

Edita el método `loadComputerStores()` en `MainActivity.kt`:

```kotlin
val storesJson = """
[
    {
        "name": "Nueva Tienda",
        "latitude": -9.0000,
        "longitude": -78.0000,
        "description": "Descripción de la tienda",
        "stores": ["Lista", "de", "establecimientos"]
    }
]
""".trimIndent()
```

### Cambiar colores

Edita `colors.xml`:

```xml
<color name="primary_color">#TU_COLOR_AQUI</color>
```

---

## 📱 Requisitos del Sistema

* Minimum SDK: Android 7.0 (API 24)
* Target SDK: Android 14 (API 34)
* Kotlin: 1.8+
* Google Play Services requerido para Maps

---
