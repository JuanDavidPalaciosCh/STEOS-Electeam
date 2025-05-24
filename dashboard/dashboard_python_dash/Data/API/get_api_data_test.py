import requests
import time

url = "http://127.0.0.1:8000/get-data/"

# Variables to store the data
Velocidad = None
Distancia = None
Voltaje = None
Corriente = None
Aceleracion = None
Temperatura = None


def get_data():
    global Velocidad, Distancia, Voltaje, Corriente, Aceleracion, Temperatura
    try:
        # Make the GET request to the server
        response = requests.get(url)

        # If the request was successful (status code 200)
        if response.status_code == 200:
            data = response.json()  # Get the data in JSON format
            if data["data"]:
                # Update the variables with the received data
                Velocidad = data["data"].get("Velocidad", Velocidad)
                Distancia = data["data"].get("Distancia", Distancia)
                Voltaje = data["data"].get("Voltaje", Voltaje)
                Corriente = data["data"].get("Corriente", Corriente)
                Aceleracion = data["data"].get("Acelerador", Aceleracion)
                Temperatura = data["data"].get("Temperatura", Temperatura)

                # Print the updated values
                print(f"Velocidad: {Velocidad} km/h")
                print(f"Distancia: {Distancia} km")
                print(f"Voltaje: {Voltaje} V")
                print(f"Corriente: {Corriente} A")
                print(f"Aceleracion: {Aceleracion} m/s²")
                print(f"Temperatura: {Temperatura} °C")
            else:
                print("No valid data received.")
        else:
            print(f"Request error. Status code: {response.status_code}")
    except requests.exceptions.RequestException as e:
        print(f"Connection error: {e}")


while True:
    get_data()
    time.sleep(1)  # Wait for 1 second
