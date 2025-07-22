Chimbote Computer Map - Guía de Instalación
Esta aplicación móvil para Android muestra en un mapa todos los establecimientos donde se venden equipos de cómputo en Chimbote, Perú.

📋 Características
🗺️ Mapa interactivo con Google Maps
📍 Detección de ubicación del usuario
🏪 Lista de 6 centros comerciales principales
📱 Diseño Material Design con ConstraintLayout
🔍 Recomendaciones de tiendas cercanas
📊 Bottom Sheet con información detallada
🚀 Configuración del Proyecto
1. Crear el Proyecto en Android Studio
Abre Android Studio
Crea un nuevo proyecto:
Template: Empty Activity
Name: Chimbote Computer Map
Package: com.example.chimbotecomputermap
Language: Kotlin
Minimum SDK: API 24
2. Obtener API Key de Google Maps
Ve a Google Cloud Console
Crea un nuevo proyecto o selecciona uno existente
Habilita las siguientes APIs:
Maps SDK for Android
Places API (opcional)
Geocoding API (opcional)
Crea credenciales (API Key)
Restringe la API Key a tu aplicación Android
3. Configurar el Proyecto
3.1 Actualizar build.gradle (Project level)
kotlin
plugins {
    id 'com.google.android.libraries.mapsplatform.secrets-gradle-plugin' version '2.0.1' apply false
}
3.2 Configurar strings.xml
Reemplaza TU_API_KEY_AQUI en strings.xml con tu API Key real:

xml
<string name="my_api_key">AIzaSyC...</string>
4. Estructura de Archivos
Crea los siguientes archivos en tu proyecto:

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
🛠️ Instalación
Paso 1: Copiar los archivos
Copia todos los archivos proporcionados en los artefactos a sus respectivas carpetas.

Paso 2: Sincronizar el proyecto
En Android Studio, haz clic en "Sync Now"
Espera a que se descarguen las dependencias
Paso 3: Configurar permisos
Los permisos ya están configurados en el AndroidManifest.xml:

ACCESS_FINE_LOCATION
ACCESS_COARSE_LOCATION
INTERNET
ACCESS_NETWORK_STATE
Paso 4: Probar en dispositivo
Conecta un dispositivo Android o usa un emulador
Ejecuta la aplicación
Acepta los permisos de ubicación cuando se soliciten
📍 Ubicaciones Incluidas
La aplicación incluye estos centros comerciales de Chimbote:

Galerías Shopping Center (-9.07370, -78.57712)
Centro Comercial Chimbote (-9.07376, -78.58978)
Galerías Alfa (-9.07486, -78.59126)
Chimbote Plaza Center (-9.0741361, -78.5880261)
MegaPlaza Chimbote (-9.1024389, -78.5576856)
Tu Centro Comercial Nuevo Chimbote (-9.12798, -78.51976)
🎨 Características de Diseño
Material Design 3: Colores y componentes modernos
ConstraintLayout: Layouts eficientes y responsivos
Bottom Sheet: Información deslizable desde abajo
FAB: Botón flotante para ubicación actual
RecyclerView: Lista eficiente de tiendas
Cards: Presentación elegante de información
🔧 Funcionalidades
Detección de Ubicación
Solicita permisos automáticamente
Muestra la ubicación actual en el mapa
Calcula distancias a las tiendas
Recomendaciones
Ordena tiendas por proximidad
Muestra las 3 más cercanas
Información detallada de cada establecimiento
Interactividad
Toca marcadores para información
Desliza el bottom sheet para ver lista
Toca items de la lista para centrar mapa
🚨 Troubleshooting
Problemas Comunes
Mapa no se muestra:
Verifica que la API Key sea válida
Asegúrate de habilitar Maps SDK for Android
Revisa que no haya restricciones en la API Key
Error de permisos:
Los permisos se solicitan automáticamente
Puedes otorgarlos manualmente en Configuración > Aplicaciones
Ubicación no funciona:
Verifica que el GPS esté habilitado
Prueba en un dispositivo físico (mejor que emulador)
Asegúrate de otorgar permisos de ubicación
Logs Útiles
bash
adb logcat | grep ChimboteComputerMap
🔄 Personalización
Agregar más tiendas
Modifica el JSON en MainActivity.kt en el método loadComputerStores():

kotlin
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
Cambiar colores
Modifica colors.xml para personalizar la apariencia:

xml
<color name="primary_color">#TU_COLOR_AQUI</color>
📱 Requisitos del Sistema
Minimum SDK: Android 7.0 (API 24)
Target SDK: Android 14 (API 34)
Kotlin: 1.8+
Google Play Services: Requerido para Maps
📄 Licencia
Este proyecto es de uso educativo y demostrativo. Asegúrate de cumplir con los términos de uso de Google Maps API.

