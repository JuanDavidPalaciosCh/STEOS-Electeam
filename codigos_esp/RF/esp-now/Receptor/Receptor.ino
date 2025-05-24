#include <WiFi.h>
#include <PubSubClient.h>
#include <esp_now.h>

// Estructura para los datos que se recibirán
typedef struct datos {
  float dist_recorrida;
  float velocidad;
  float voltaje;
  float corriente;
  float acelerador;
  float temperatura;
} datos;

datos datos_telemetria;

// Configura tus credenciales Wi-Fi
const char* ssid = "Redmi Note 11"; 
const char* password = "Aguacate123";  

// Configura la dirección del broker MQTT
const char* mqttServer = "192.168.169.87"; 
const int mqttPort = 1883;                
const char* mqttUser = "jpalaciosch2";    
const char* mqttPassword = "123456";      

WiFiClient espClient;
PubSubClient client(espClient);

void OnRecv(const esp_now_recv_info* info, const uint8_t* incomingData, int len) {
  memcpy(&datos_telemetria, incomingData, sizeof(datos_telemetria));
  Serial.println("Datos recibidos por ESP-NOW");
}

void setup() {
  Serial.begin(9600);

  WiFi.mode(WIFI_STA);
  
  // Configuración de Wi-Fi32
  WiFi.begin(ssid, password);
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  Serial.println("Connected to WiFi");

  // Inicializar MQTT
  client.setServer(mqttServer, mqttPort);
  while (!client.connected()) {
    Serial.println("Connecting to MQTT...");
    if (client.connect("ESP32Client", mqttUser, mqttPassword)) {
      Serial.println("Connected to MQTT");
    } else {
      Serial.print("Failed with state ");
      Serial.println(client.state());
      delay(2000);
    }
  }

  // Inicializar ESP-NOW
  if (esp_now_init() != ESP_OK) {
    Serial.println("There was an error initializing ESP-NOW");
    return;
  } else {
    Serial.println("Se inicializó esp-now");
    Serial.println(WiFi.macAddress());
  }

  // Configura el peer (receptor)
  esp_now_peer_info_t peerInfo;
  uint8_t peerAddr[] = {0xD0, 0xEF, 0x76, 0x15, 0x17, 0x30};  // Definir la dirección MAC en un arreglo
  memcpy(peerInfo.peer_addr, peerAddr, sizeof(peerAddr));  // Copiar la dirección MAC
  peerInfo.channel = 1;  // Asegúrate de que este canal coincida
  peerInfo.encrypt = false;
  
  if (esp_now_add_peer(&peerInfo) != ESP_OK) {
      Serial.println("Error al agregar el peer");
      return;
  }

  
  esp_now_register_recv_cb(OnRecv);
}

void loop() {
  client.loop();

  // Publicar los valores recibidos por ESP-NOW
  if (datos_telemetria.velocidad != 0) { // Verifica si se recibieron datos
    Serial.println("Publicando datos a MQTT...");
    
    if (client.publish("Telemetría/Velocidad", String(datos_telemetria.velocidad).c_str(), true)) {
      Serial.println("Datos de velocidad publicados.");
    } else {
      Serial.println("Error al publicar velocidad.");
    }
    
    if (client.publish("Telemetría/Distancia", String(datos_telemetria.dist_recorrida).c_str(), true)) {
      Serial.println("Datos de distancia publicados.");
    } else {
      Serial.println("Error al publicar distancia.");
    }
    
    if (client.publish("Telemetría/Corriente", String(datos_telemetria.corriente).c_str(), true)) {
      Serial.println("Datos de corriente publicados.");
    } else {
      Serial.println("Error al publicar corriente.");
    }
    
    if (client.publish("Telemetría/Temperatura", String(datos_telemetria.temperatura).c_str(), true)) {
      Serial.println("Datos de temperatura publicados.");
    } else {
      Serial.println("Error al publicar temperatura.");
    }
    
    if (client.publish("Telemetría/Voltaje", String(datos_telemetria.voltaje).c_str(), true)) {
      Serial.println("Datos de voltaje publicados.");
    } else {
      Serial.println("Error al publicar voltaje.");
    }
    
    if (client.publish("Telemetría/Aceleración(%)", String(datos_telemetria.acelerador).c_str(), true)) {
      Serial.println("Datos de aceleración publicados.");
    } else {
      Serial.println("Error al publicar aceleración.");
    }

    client.publish("Telemetría/Hombre Muerto", 0 ? "true" : "false", true);
  } else {
    Serial.println("Esperando datos de ESP-NOW...");
  }

  delay(10000);
}
