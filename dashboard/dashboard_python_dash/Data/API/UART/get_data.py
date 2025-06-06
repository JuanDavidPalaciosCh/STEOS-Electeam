import json
import time
import serial
import serial.tools.list_ports


def get_port() -> str:
    available_ports = list(serial.tools.list_ports.comports())
    available_ports = [port for port in available_ports if port.description != "n/a"]

    try:
        port = available_ports[0]
        return port

    except IndexError:
        return "n/a"


def initialize_port():
    port = get_port()

    if port != "n/a":
        global ser
        ser = serial.Serial(port.device, 9600, timeout=1)
        print("Initializing port")
        time.sleep(2)  # Wait port to initialize
        print("Port initialized")

    else:
        print("Port not founded, retrying")
        time.sleep(2)
        initialize_port()


def get_data() -> dict:
    global ser

    try:
        if ser.in_waiting > 0:  # Si hay datos disponibles en el puerto serial
            try:
                # Leemos todas las líneas disponibles en el buffer
                all_data = ser.read(ser.in_waiting).decode("utf-8", errors="ignore")

                # Dividimos el buffer en líneas y tomamos la última línea
                lines = all_data.splitlines()
                if lines:
                    # Tomamos la última línea
                    data = lines[-2]
                    data = json.loads(data)  # Intentamos convertir la línea a JSON
                    return {"data": data}
                else:
                    return {"data": None}
            except:
                return {"data": None}
    except:
        return {"data": None}


# initialize_port()
# while True:
#     time.sleep(0.2)
#     print(get_data()["data"])
