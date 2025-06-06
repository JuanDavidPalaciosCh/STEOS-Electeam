# 📘 Documentación de la API – Recepción y Consulta de Datos del ESP32

## 🧠 Descripción General

Esta API permite la **recepción de datos en tiempo real** desde un dispositivo ESP32 mediante una petición POST y ofrece una ruta GET para consultar dichos datos, siempre que hayan sido actualizados recientemente.  
Está desarrollada con [FastAPI](https://fastapi.tiangolo.com/) y puede utilizarse para alimentar dashboards o sistemas de monitoreo.

---

## 🚀 Endpoints

### 1. `POST /update-data/`

**Descripción:**  
Recibe un diccionario con los datos enviados por el ESP32.

**Cuerpo de la solicitud (JSON):**
```json
{
  "value": {
    "Velocidad": 10,
    "Distancia": 15,
    "Voltaje": 3.3,
    "Corriente": 0.5,
    "Aceleracion": 2.1,
    "Temperatura": 25.0,
    "Longitud": -74.0833,
    "Latitud": 4.59808
  }
}
```

**Respuesta:**
```json
{
  "message": "Data updated successfully"
}
```

---

### 2. `GET /get-data/`

**Descripción:**  
Obtiene los últimos datos enviados por el ESP32 si han sido actualizados en los últimos 2 segundos. De lo contrario, indica que el dispositivo está desconectado.

**Respuesta exitosa (con datos recientes):**
```json
{
  "data": {
    "Velocidad": 10,
    "Distancia": 15,
    "Voltaje": 3.3,
    "Corriente": 0.5,
    "Aceleracion": 2.1,
    "Temperatura": 25.0,
    "Longitud": -74.0833,
    "Latitud": 4.59808
  },
  "status": "connected"
}
```

**Respuesta si no hay datos recientes o no se han enviado aún:**
```json
{
  "data": null,
  "status": "disconnected"
}
```

---

## 🧪 Scripts de prueba

### 🔄 `send_data.py` – Simulación de envío desde ESP32 vía HTTP

Este script simula un dispositivo que envía datos cada 10 milisegundos al servidor mediante una petición `POST`.

**Variables generadas:**
- Velocidad creciente de 0 a 100 km/h.
- Distancia aleatoria entre 0 y 500 km.
- Voltaje entre 10 y 15 V.
- Corriente entre 0 y 50 A.
- Aceleración entre 0 y 5 m/s².
- Temperatura entre -20 y 50 °C.
- Coordenadas GPS que se actualizan ligeramente con el tiempo.

```python
data = {"value": generate_data()}
response = requests.post(url, json=data)
```

---

### 📥 `get_api_data_test.py` – Prueba de recepción

Este script realiza una petición GET cada segundo al endpoint `/get-data/` y muestra los datos en consola.

```python
response = requests.get("http://127.0.0.1:8000/get-data/")
data = response.json()

if data["data"]:
    print(f"Velocidad: {data['data'].get('Velocidad')} km/h")
    ...
```

---

### 📟 `send_data.py` (UART) – **Alternativa**: Recepción de datos vía puerto serial

Este script, actualmente **no en uso**, se diseñó para recibir los datos directamente por UART antes de enviarlos a la API.

**Flujo del script:**
- Detecta automáticamente el puerto serial disponible.
- Inicializa la conexión.
- Lee los datos disponibles.
- Convierte la última línea del buffer serial a un diccionario JSON.


> ⚠️ Este script puede ser reactivado si en el futuro se decide volver a utilizar comunicación serial con el ESP32.

---

## 🛠 Requisitos para correr

- Python 3.10+
- FastAPI
- Uvicorn (para el servidor)
- requests (para los scripts)
- pyserial (si se usa la opción por UART)

---

## ▶️ Ejecución

**1. Iniciar servidor FastAPI:**
```bash
uvicorn main:app --reload
```

**2. Enviar datos simulados (HTTP):**
```bash
python send_data.py
```

**3. Ver datos en tiempo real:**
```bash
python get_api_data_test.py
```

**4. (Opcional) Leer datos desde UART:**
```bash
python ./UART/get_data.py  # si se renombra y activa para su uso
```
