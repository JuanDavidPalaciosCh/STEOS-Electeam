#include <esp_now.h>
#include <WiFi.h>

uint8_t slaveAddress[] = {0xD0, 0xEF, 0x76, 0x15, 0x17, 0x30}; // Dirección del slave

typedef struct datos {
  float dist_recorrida;
  float velocidad;
  float voltaje;
  float corriente;
  float acelerador;
  float temperatura;
};

datos datos_telemetria;

void Envio(const uint8_t *mac_addr, esp_now_send_status_t status) {
  Serial.print("\r\nEnviar estado del mensaje:\t");
  Serial.println(status == ESP_NOW_SEND_SUCCESS ? "Envio exitoso" : "Envio fallido");
}

void setup() {
  Serial.begin(9600);
  
  WiFi.mode(WIFI_STA);
  
  if (esp_now_init() != ESP_OK) {
    Serial.println("Error inicializando ESP-NOW");
    return;
  } else{
    Serial.println("ESP-NOW inicializado! :)");
  }
  
  esp_now_register_send_cb(Envio);
  
  esp_now_peer_info_t slaveInfo;
  memcpy(slaveInfo.peer_addr, slaveAddress, 6);
  slaveInfo.channel = 0;  // Asegúrate de que esto coincida
  slaveInfo.encrypt = false;

  if (esp_now_add_peer(&slaveInfo) != ESP_OK) {
    Serial.println("There was an error registering the slave");
    return;
  }
}

void loop() {
  datos_telemetria.dist_recorrida = random(15, 100) / 2;
  datos_telemetria.velocidad = random(0, 100);
  datos_telemetria.voltaje = random(44.4, 55.4);
  datos_telemetria.corriente = random(0, 10);
  datos_telemetria.acelerador = random(0, 100);
  datos_telemetria.temperatura = random(20, 40);

  Serial.print("Distancia: "); Serial.println(datos_telemetria.dist_recorrida);
  Serial.print("Velocidad: "); Serial.println(datos_telemetria.velocidad);
  Serial.print("Voltaje: "); Serial.println(datos_telemetria.voltaje);
  Serial.print("Corriente: "); Serial.println(datos_telemetria.corriente);
  Serial.print("Acelerador: "); Serial.println(datos_telemetria.acelerador);
  Serial.print("Temperatura: "); Serial.println(datos_telemetria.temperatura);

  esp_err_t result = esp_now_send(slaveAddress, (uint8_t *)&datos_telemetria, sizeof(datos_telemetria));

  delay(2000);
}
