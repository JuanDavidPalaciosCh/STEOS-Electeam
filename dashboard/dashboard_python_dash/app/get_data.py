import requests
import time

url = "http://127.0.0.1:8000/get-data/"


def get_data():
    """
    Fetches data from the API and returns it as a dictionary.
    Returns {"data": None} if no valid data is received.
    """
    try:
        # Make the GET request to the server
        response = requests.get(url)

        # If the request was successful (status code 200)
        if response.status_code == 200:
            data = response.json()  # Get the data in JSON format
            if data.get("data"):  # Check if "data" key exists and is not empty
                # Extract the data
                result = {
                    "Velocidad": data["data"].get("Velocidad"),
                    "Distancia": data["data"].get("Distancia"),
                    "Voltaje": data["data"].get("Voltaje"),
                    "Corriente": data["data"].get("Corriente"),
                    "Aceleracion": data["data"].get("Aceleracion"),
                    "Temperatura": data["data"].get("Temperatura"),
                    "Longitud": data["data"].get("Longitud"),
                    "Latitud": data["data"].get("Latitud"),
                }
                return {"data": result}
            else:
                # No valid data received
                return {"data": None}
        else:
            # Request error
            return {
                "data": None,
                "error": f"Request error. Status code: {response.status_code}",
            }
    except requests.exceptions.RequestException as e:
        # Connection error
        return {"data": None, "error": f"Connection error: {e}"}
