from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
import time

app = FastAPI()

# Almacenar datos recibidos del ESP32
data_store = {"data": None, "timestamp": None}


class ESP32Data(BaseModel):
    value: dict


@app.post("/update-data/")
# Funcion post
async def update_data(payload: ESP32Data):
    """Recibir datos del ESP32."""
    global data_store
    data_store["data"] = payload.value
    data_store["timestamp"] = time.time()
    return {"message": "Data updated successfully"}


@app.get("/get-data/")
async def get_data():
    # Funcion get
    global data_store
    if data_store["data"] is None or time.time() - data_store["timestamp"] > 2:
        return {"data": None, "status": "disconnected"}
    return {"data": data_store["data"], "status": "connected"}
