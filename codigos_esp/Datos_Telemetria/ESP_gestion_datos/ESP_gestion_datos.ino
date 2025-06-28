//ESP GESTIÓN DATOS
//recibe datos de la esp de sensorica y pones unos en la sd, tiene el emisor del modulo lora
#include <esp_now.h>
#include <WiFi.h>
#include <LoRa.h>

#define SS 5
#define RST 14
#define DIO0 26


// Structure to keep the temperature and humidity data
// Is also required in the client to be able to save the data directly
typedef struct datos {
  float dist_recorrida;
  float velocidad;
  float voltaje;
  float corriente;
  float acelerador;
  float temperatura;
};
// Create a struct_message called myData
datos datos_telemetria;
// callback function executed when data is received
void OnRecv(const uint8_t * mac, const uint8_t *incomingData, int len) {
  memcpy(&datos_telemetria, incomingData, sizeof(datos_telemetria));
  Serial.print("Bytes received: ");
  Serial.println(len);
  Serial.print("Distancia: ");
  Serial.println(datos_telemetria.dist_recorrida);
  Serial.print("Velocidad: ");
  Serial.println(datos_telemetria.velocidad);
  Serial.print("Voltaje: ");
  Serial.println(datos_telemetria.voltaje);
  Serial.print("Corriente: ");
  Serial.println(datos_telemetria.corriente);
  Serial.print("Acelerador: ");
  Serial.println(datos_telemetria.acelerador);
  Serial.print("Temperatura: ");
  Serial.println(datos_telemetria.temperatura);
}



/* GUARDADO EN TARJETA SD */
String datos_telemetria_sd; // Mensaje que se escribira en cada linea del archivo de texto

// Funciones
// Write to the SD card (DON'T MODIFY THIS FUNCTION)

void writeFile(fs::FS &fs, const char * path, const char * message) {
  Serial.printf("Writing file: %s\n", path);

  File file = fs.open(path, FILE_WRITE);
  if(!file) {
    Serial.println("Fallo al abrir el archivo para escribir");
    return;
  }
  if(file.print(message)) {
    Serial.println("Archivo escrito");
  } else {
    Serial.println("Fallo al escribir");
  }
  file.close();
}
// Append data to the SD card (DON'T MODIFY THIS FUNCTION)
void appendFile(fs::FS &fs, const char * path, const char * message) {
  Serial.printf("Appending to file: %s\n", path);

  File file = fs.open(path, FILE_APPEND);
  if(!file) {
    Serial.println("Fallo al abrir el archivo para añadir");
    return;
  }
  if(file.print(message)) {
    Serial.println("Mensaje añadido");
  } else {
    Serial.println("Fallo al añadir");
  }
  file.close();
}
// Escribir en la tarjeta SD la informacion de los sensores
// Formato: tiempo [s] ; distancia recorrida [m] ; velocidad [m] ; voltaje [V] ; corriente [A] ; acelerador [%]
void logSDCard() {
  datos_telemetria_sd = String(0.001 * millis()) + ";" + String(datos_telemetria.dist_recorrida) + ";" + String(datos_telemetria.velocidad) + ";" + String(datos_telemetria.voltaje) + ";" + String(datos_telemetria.corriente) + ";" + String(datos_telemetria.acelerador) + ";" + String(datos_telemetria.temperatura) + "\r\n";
  Serial.print("Saving data: ");
  Serial.println(datos_telemetria_sd);
  appendFile(SD, "/data.txt", datos_telemetria_sd.c_str());
  
}


void setup() {
  // Initialize Serial Monitor
  Serial.begin(115200);
  
  // Set device as a Wi-Fi Station
  WiFi.mode(WIFI_STA);
  // Init ESP-NOW
  if (esp_now_init() != ESP_OK) {
    Serial.println("There was an error initializing ESP-NOW");
    return;

  // Serial.begin(9600);
  while (!Serial);

  // Configurar pines LoRa
  LoRa.setPins(SS, RST, DIO0);

  if (!LoRa.begin(915E6)) {  // frecuencia (puede ser 433E6 o 868E6 según el módulo)
    Serial.println("LoRa init failed. Check your connections.");
    while (true);
  }

  Serial.println("LoRa init succeeded.");
  }
  
  // Once the ESP-Now protocol is initialized, we will register the callback function
  // to be able to react when a package arrives in near to real time without pooling every loop.
  esp_now_register_recv_cb(OnRecv);
}
void loop() {
   Serial.println("Sending packet...");
  LoRa.beginPacket();
  LoRa.print(datos_telemetria);
  LoRa.endPacket();
  delay(2000);
}
