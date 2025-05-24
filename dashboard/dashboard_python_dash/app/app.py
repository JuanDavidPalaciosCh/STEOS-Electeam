import dash
import time
import plotly
import pandas as pd
import plotly.graph_objects as go
import dash_bootstrap_components as dbc
import plotly.express as px
from dash.dependencies import Input, Output, State
from dash import dcc, html
from threading import Thread
from collections import deque
from get_data import get_data

# Inicializar la app Dash
app = dash.Dash(
    __name__, external_stylesheets=[dbc.themes.BOOTSTRAP], update_title=None
)

# Título de la pestaña del navegador
app.title = "STEOS Dashboard"

# Datos

# Función para el ciclo continuo de recolección de datos
velocidad = deque(maxlen=100000)
distancia = deque(maxlen=100000)
aceleracion = deque(maxlen=100000)
voltaje = deque(maxlen=100000)
corriente = deque(maxlen=100000)
potencia = deque(maxlen=100000)
potencia_prom = deque(maxlen=100000)
temperatura = deque(maxlen=100000)
latitud = deque(maxlen=100000)
longitud = deque(maxlen=100000)
data_store = deque(maxlen=100000)

# zoom mapa
default_zoom = 16


def data_collector():
    while True:
        data = get_data()
        if data["data"]:
            data_store.append(data["data"])

            velocidad.append(data["data"]["Velocidad"])
            distancia.append(data["data"]["Distancia"])
            aceleracion.append(data["data"]["Aceleracion"])
            voltaje.append(data["data"]["Voltaje"])
            corriente.append(data["data"]["Corriente"])
            temperatura.append(data["data"]["Temperatura"])
            potencia.append(
                float(data["data"]["Voltaje"]) * float(data["data"]["Corriente"])
            )

            potencia_prom.append(sum(potencia) / len(potencia))

            latitud.append(data["data"]["Latitud"])
            longitud.append(data["data"]["Longitud"])

        else:
            print("No data received, retrying in 5 seconds...")
            time.sleep(5)  # Espera 5 segundos antes de intentar nuevamente

        time.sleep(0.05)


# Lanza el hilo para recolectar datos
thread = Thread(target=data_collector, daemon=True)
thread.start()


# Layout de la app
app.layout = html.Div(
    children=[
        # Parte superior
        dbc.Row(
            [
                dbc.Col(
                    html.Img(src="./assets/LogoImg.png", height="50px"),
                    xs=12,  # Ocupa todo el ancho en pantallas extra pequeñas
                    md=6,  # Ocupa la mitad del ancho en pantallas medianas y mayores
                    className="d-flex justify-content-center justify-content-md-start",
                ),  # Logo centrado en pantallas pequeñas y alineado a la izquierda en pantallas medianas y grandes
            ],
            className="mb-4 px-md-4 px-lg-4 pt-md-4 pt-lg-4",  # Padding en dispositivos medianos y grandes
        ),
        # Parte media (mapa a la izquierda, batería, botón limpiar, y gráfico) responsiva
        dbc.Row(
            [
                dbc.Col(
                    dcc.Graph(
                        id="live-update-map",
                        config={"scrollZoom": True},
                    ),
                    width=12,  # En pantallas pequeñas ocupa todo el ancho
                    md=5,  # En pantallas medianas y grandes ocupa 5 columnas
                    className="mb-3 mb-md-0",  # Margen inferior en pantallas pequeñas
                ),
                # Columna con batería y botón de limpiar
                dbc.Col(
                    children=[
                        html.Div(
                            html.Img(
                                src="./assets/Bat1.jpg", height="50px"
                            ),  # Imagen de la batería
                            className="mb-2 d-flex justify-content-center",
                        ),
                        html.Div(
                            html.Button(
                                "Limpiar",
                                id="clear-button",
                                className="btn btn-secondary w-100",
                            ),
                            className="d-flex justify-content-center",  # Centrado horizontal
                        ),
                    ],
                    width=12,  # En pantallas pequeñas ocupa todo el ancho
                    md=2,  # En pantallas medianas y grandes ocupa 1 columna
                    className="d-flex flex-column justify-content-center mb-3 mb-md-0",  # Centrado vertical y margen inferior
                ),
                # Columna con la gráfica
                dbc.Col(
                    [
                        dcc.Graph(
                            id="live-update-graph",
                            animate=True,
                        ),
                        # Almacenar el identificador del botón seleccionado
                        dcc.Store(
                            id="button-store",
                            data=(
                                "vel-button",
                                "acc-button",
                                "temp-button",
                                "volt-button",
                                "curr-button",
                                "pot-button",
                                "potmed-button",
                            ),
                            modified_timestamp=0.05,
                            storage_type="session",
                        ),
                        # Intervalo para actualizar el gráfico cada segundo
                        dcc.Interval(
                            id="interval-component",
                            interval=750,
                            n_intervals=0,
                            disabled=False,
                        ),
                    ],
                    width=12,  # En pantallas pequeñas ocupa todo el ancho
                    md=5,  # En pantallas medianas y grandes ocupa 5 columnas
                ),
            ],
            className="mb-4",
        ),
        # Parte inferior con botones responsivos
        dbc.Row(
            [
                # Botón de velocidad
                dbc.Col(
                    html.Div(
                        children=[
                            html.P("Velocidad", className="text-center"),
                            html.Img(
                                src="./assets/GaugeVel.png",
                                height="40px",
                                className="d-block mx-auto",
                            ),
                            html.P("1", className="text-center mt-2", id="vel-text"),
                        ],
                        className="border p-3 rounded button-panel",
                        id="vel-button",  # ID para activar el botón
                        style={
                            "backgroundColor": "lightblue"
                        },  # Color por defecto de Velocidad
                    ),
                    width=12,  # Por defecto 12 columnas (full width)
                    sm=6,  # En pantallas pequeñas, ocupa 6 columnas
                    md=2,  # En pantallas medianas y grandes, ocupa 2 columnas
                ),
                # Botón de aceleración
                dbc.Col(
                    html.Div(
                        children=[
                            html.P("Aceleración", className="text-center"),
                            html.Img(
                                src="./assets/GaugeVel.png",
                                height="40px",
                                className="d-block mx-auto",
                            ),
                            html.P("2", className="text-center mt-2", id="acc-text"),
                        ],
                        className="border p-3 rounded button-panel",
                        id="acc-button",  # ID para activar el botón
                    ),
                    width=12,  # Por defecto 12 columnas (full width)
                    sm=6,  # En pantallas pequeñas, ocupa 6 columnas
                    md=2,  # En pantallas medianas y grandes, ocupa 2 columnas
                ),
                # Botón de temperatura
                dbc.Col(
                    html.Div(
                        children=[
                            html.P("Temperatura", className="text-center"),
                            html.Img(
                                src="./assets/Temp.png",
                                height="40px",
                                className="d-block mx-auto",
                            ),
                            html.P("3", className="text-center mt-2", id="temp-text"),
                        ],
                        className="border p-3 rounded button-panel",
                        id="temp-button",  # ID para activar el botón
                    ),
                    width=12,  # Por defecto 12 columnas (full width)
                    sm=6,  # En pantallas pequeñas, ocupa 6 columnas
                    md=2,  # En pantallas medianas y grandes, ocupa 2 columnas
                ),
                # Botón de voltaje
                dbc.Col(
                    html.Div(
                        children=[
                            html.P("Voltaje", className="text-center"),
                            html.Img(
                                src="./assets/Volt.png",
                                height="40px",
                                className="d-block mx-auto",
                            ),
                            html.P("4", className="text-center mt-2", id="volt-text"),
                        ],
                        className="border p-3 rounded button-panel",
                        id="volt-button",  # ID para activar el botón
                    ),
                    width=12,  # Por defecto 12 columnas (full width)
                    sm=6,  # En pantallas pequeñas, ocupa 6 columnas
                    md=2,  # En pantallas medianas y grandes, ocupa 2 columnas
                ),
                # Botón de corriente
                dbc.Col(
                    html.Div(
                        children=[
                            html.P("Corriente", className="text-center"),
                            html.Img(
                                src="./assets/Corr.png",
                                height="40px",
                                className="d-block mx-auto",
                            ),
                            html.P("5", className="text-center mt-2", id="curr-text"),
                        ],
                        className="border p-3 rounded button-panel",
                        id="curr-button",  # ID para activar el botón
                    ),
                    width=12,  # Por defecto 12 columnas (full width)
                    sm=6,  # En pantallas pequeñas, ocupa 6 columnas
                    md=2,  # En pantallas medianas y grandes, ocupa 2 columnas
                ),
                # Botón de potencia
                dbc.Col(
                    html.Div(
                        children=[
                            html.P("Potencia", className="text-center"),
                            html.Img(
                                src="./assets/Pot.png",
                                height="40px",
                                className="d-block mx-auto",
                            ),
                            html.P("6", className="text-center mt-2", id="pot-text"),
                        ],
                        className="border p-3 rounded button-panel",
                        id="pot-button",  # ID para activar el botón
                    ),
                    width=12,  # Por defecto 12 columnas (full width)
                    sm=6,  # En pantallas pequeñas, ocupa 6 columnas
                    md=2,  # En pantallas medianas y grandes, ocupa 2 columnas
                ),
                # Botón de potencia promedio
                dbc.Col(
                    html.Div(
                        children=[
                            html.P("Potencia promedio", className="text-center"),
                            html.Img(
                                src="./assets/Pot.png",
                                height="40px",
                                className="d-block mx-auto",
                            ),
                            html.P("7", className="text-center mt-2", id="potmed-text"),
                        ],
                        className="border p-3 rounded button-panel",
                        id="potmed-button",  # ID para activar el botón
                    ),
                    width=12,  # Por defecto 12 columnas (full width)
                    sm=6,  # En pantallas pequeñas, ocupa 6 columnas
                    md=2,  # En pantallas medianas y grandes, ocupa 2 columnas
                ),
                dcc.Interval(
                    id="interval-component2",
                    interval=750,
                    n_intervals=0,
                    disabled=False,
                ),
            ],
            className="mb-4",
        ),
    ]
)


# Callback 1: Actualizar los colores de los botones y guardar el identificador del botón seleccionado
@app.callback(
    [
        Output("vel-button", "style"),
        Output("acc-button", "style"),
        Output("temp-button", "style"),
        Output("volt-button", "style"),
        Output("curr-button", "style"),
        Output("pot-button", "style"),
        Output("potmed-button", "style"),
        Output("button-store", "data"),
    ],  # Almacenamos el identificador del botón seleccionado
    [
        Input("vel-button", "n_clicks"),
        Input("acc-button", "n_clicks"),
        Input("temp-button", "n_clicks"),
        Input("volt-button", "n_clicks"),
        Input("curr-button", "n_clicks"),
        Input("pot-button", "n_clicks"),
        Input("potmed-button", "n_clicks"),
    ],
)
def update_buttons_and_store(*args):
    # Identificar el botón seleccionado
    button_id = dash.callback_context.triggered[0]["prop_id"].split(".")[0]

    # Colores de los botones
    colors = {
        "vel-button": {"backgroundColor": "lightblue"},
        "acc-button": {"backgroundColor": "lightgreen"},
        "temp-button": {"backgroundColor": "lightcoral"},
        "volt-button": {"backgroundColor": "lightyellow"},
        "curr-button": {"backgroundColor": "lightpink"},
        "pot-button": {"backgroundColor": "lightgrey"},
        "potmed-button": {"backgroundColor": "lightred"},
    }

    # Resetear color de todos los botones
    for key in colors.keys():
        colors[key] = {"backgroundColor": "white"}

    # Asignar el color original del botón seleccionado
    if button_id in colors:
        colors[button_id] = {"backgroundColor": "#7fcd5f"}

    # Retornar los colores de los botones y el identificador del botón seleccionado
    return (
        colors["vel-button"],
        colors["acc-button"],
        colors["temp-button"],
        colors["volt-button"],
        colors["curr-button"],
        colors["pot-button"],
        colors["potmed-button"],
        button_id,  # Guardamos el identificador del botón seleccionado
    )


# Callback 2: Actualizar la gráfica según el identificador del botón
@app.callback(
    Output("live-update-graph", "figure"),
    [
        Input("button-store", "data"),  # Usamos el identificador almacenado
        Input("interval-component", "n_intervals"),
    ],  # Este input asegura que se actualice la gráfica periódicamente
)
def update_graph(button_id, _):
    # Mapeo de los botones a los datos
    data_mapping = {
        "vel-button": velocidad,
        "acc-button": aceleracion,
        "temp-button": temperatura,
        "volt-button": voltaje,
        "curr-button": corriente,
        "pot-button": potencia,
        "potmed-button": potencia_prom,
    }

    # Acceder a los datos más recientes de acuerdo al botón seleccionado
    if button_id in data_mapping:
        selected_data = tuple(
            data_mapping[button_id]
        )  # Asegurarse de convertirlo en lista
    else:
        selected_data = []  # En caso de que no se encuentre el botón

    limx = len(selected_data) if len(selected_data) > 1 else 1
    # Generar un eje X basado en los datos seleccionados
    time_axis = list(range(len(selected_data)))

    # Crear la figura de la gráfica
    figure = {
        "data": [
            go.Scatter(
                x=time_axis,
                y=selected_data,
                mode="lines+markers",
                name=button_id.split("-")[0].capitalize(),
            ),
        ],
        "layout": go.Layout(
            title=f"{button_id.split('-')[0].capitalize()}",
            xaxis=dict(range=[0, limx], autorange=False),
            # yaxis=dict(range=[min(selected_data), max(selected_data)], autorange=False),
        ),
    }

    return figure


# Callback 3, cambia el texto en los botones con los datos en tiempo real usando el interval


@app.callback(
    [
        Output("vel-text", "children"),
        Output("acc-text", "children"),
        Output("temp-text", "children"),
        Output("volt-text", "children"),
        Output("curr-text", "children"),
        Output("pot-text", "children"),
        Output("potmed-text", "children"),
    ],
    Input("interval-component2", "n_intervals"),
)
def update_button_text(_):
    # Obtener el último dato de cada variable
    def format_value(data):
        if data and data != "None" and data[-1] is not None:
            return f"{data[-1]:.2f}"
        else:
            return "None"

    vel = format_value(velocidad)
    acc = format_value(aceleracion)
    temp = format_value(temperatura)
    volt = format_value(voltaje)
    curr = format_value(corriente)
    pot = format_value(potencia)
    potmed = f"{(sum(potencia) / len(potencia)):.2f}" if potencia else "None"

    # Actualizar el texto de los botones
    return (
        vel,
        acc,
        temp,
        volt,
        curr,
        pot,
        potmed,
    )


# Callback 4: Actualizar el mapa con la posición en tiempo real
@app.callback(
    Output("live-update-map", "figure"),
    Input("interval-component2", "n_intervals"),
    State("live-update-map", "relayoutData"),
)
def update_map(n_intervals, relayout_data):
    # Obtener el zoom actual del usuario
    user_zoom = (
        relayout_data.get("mapbox.zoom", default_zoom)
        if relayout_data
        else default_zoom
    )

    # Obtener la última posición
    if len(latitud) > 0 and len(longitud) > 0:
        lat = latitud[-1]
        lon = longitud[-1]
    else:
        lat = 4.6363895
        lon = -74.0832996

    print(lat, lon)

    # Actualizar la posición del marcador
    figure = px.scatter_mapbox(
        pd.DataFrame(
            [
                {
                    "lat": lat,
                    "lon": lon,
                }
            ]
        ),
        lat="lat",
        lon="lon",
        zoom=user_zoom,
        size=[1],
        color_discrete_sequence=["#7FCD5F"],
        opacity=1,
    )

    figure.update_layout(
        mapbox_style="open-street-map", margin={"r": 0, "t": 0, "l": 0, "b": 0}
    )

    return figure


if __name__ == "__main__":
    app.run_server(debug=True)
