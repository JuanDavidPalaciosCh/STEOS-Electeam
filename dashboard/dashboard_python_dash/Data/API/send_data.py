import requests
import random
import time

url = "http://127.0.0.1:8000/update-data/"


vel = 0

longitud = -74.0832996
latitud = 4.59808


# Simular datos para enviar
def generate_data():
    global vel
    return {
        "Velocidad": vel,
        "Distancia": round(random.uniform(0, 500), 2),  # Distancia en km
        "Voltaje": round(random.uniform(10, 15), 2),  # Voltaje en V
        "Corriente": round(random.uniform(0, 50), 2),  # Corriente en A
        "Aceleracion": round(random.uniform(0, 5), 2),  # Aceleración en m/s²
        "Temperatura": round(random.uniform(-20, 50), 2),  # Temperatura en °C
        "Longitud": longitud,
        "Latitud": latitud,
    }


while True:
    if vel < 100:
        vel += 0.1
    else:
        vel = 0

    # Longitud y latitud cambia de forma x2 + y2 = 1

    longitud = longitud + 0.0000005
    latitud = latitud + 0.0000005

    data = {"value": generate_data()}
    try:
        response = requests.post(url, json=data)
        if response.status_code == 200:
            print(f"Data sent successfully: {data}")
        else:
            print(f"Failed to send data. Status code: {response.status_code}")
    except requests.exceptions.RequestException as e:
        print(f"Connection error: {e}")

    time.sleep(0.01)  # 10 ms
