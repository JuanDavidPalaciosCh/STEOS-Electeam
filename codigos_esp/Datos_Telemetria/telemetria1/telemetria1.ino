// Codigo para ESP32 del circuito de sensorica para el proyecto SHARKY
// Semillero STEOS - Universidad Nacional de Colombia
// Electeam de STEOS
// Dario Garzon Roa
// Ana Maria Orozco
// Juan David Palacios

// Este es para el master o ESP remitente

#include <Arduino.h>
// Bibliotecas para la SD
#include <FS.h>
#include <SD.h>
#include <SPI.h>
// Definir pin CS para la SD
#define SD_CS 5
// Guardado de la calibracion en memoria flash
#include <Preferences.h>
Preferences preferences;

// Bibliotecas del nuevo codigo
#include <esp_now.h>
#include <WiFi.h>


/* ~~~~~ FUNCIONES Y VARIABLES ~~~~~ */
/* RF */
uint8_t slaveAddress[] = {0xD0, 0xEF, 0x76, 0x15, 0x17, 0x30}; //Address del slave
//D0:EF:76:15:60:68
//D0:EF:76:15:60:68


// Se declara el struct con los datos que se van a enviar
typedef struct datos {
  float dist_recorrida_rf;
  float velocidad_rf;
  float voltaje_rf;
  float corriente_rf;
  float acelerador_rf;
  float temperatura_rf;
};

datos datos_telemetria_rf;


/* CALIBRACION */
// Variables
bool calibracion = false;

int clicks_botones[] = {0 , 0 , 0};
const int botones[] = {2 , 4, 15}; // Pines donde estan los botones [ + , -> , - ]
const int indicador_calibracion = 13; // Pin que indica si se entro al modo de calibracion.
bool estados_botones[3][2]; // Matriz de estados de los botones, la primera columna son los estados previos, la segunda los estados actuales
int tiempo_espera_botones = 600;
unsigned long lapso_de_rebote = 50;

float valores_calibracion[5]; // Los parametros son: {Factor Voltimetro , Desfase Amperimetro , Factor Amperimetro , Desfase Temperatura , Factor Temperatura}
size_t tamano_calibracion = sizeof(valores_calibracion);
int parametro_calibracion = 0; // contador del parametro que se este modificando
int digito_calibracion = 0; // que digito del parametro se esta modificando, se plantea que se pueda calibrar hasta la tercera pocision decimal [0.001]
// Funciones
int conteo_clicks(int , bool , bool, int); // Esta funcion cuenta los clicks dados a uno de los botones
void calibrar(int , int);
void mostrar_calibracion();


/* SENSORES Y MEDICION */
// Variables
//  Pines
const int sensores[] = {14 , 32 , 33 , 34}; // Pines de los sensores de : {voltaje , corriente , acelerador , temperatura}
const int sensor_hallRueda = 27; // Sensor de efecto hall para el velocimetro

int selector = 0; // Determina que sensor se leera en el ciclo : {0 , 1 , 2 , 3} = {voltaje , corriente , acelerador , temperatura}
//  Registro de las mediciones
float mediciones[] = {0.0 , 0.0 , 0.0 , 0.0}; // Lecturas de : {voltaje , corriente , acelerador , temperatura}
int cuenta_vueltas = 0;
float dist_recorrida = 0.0;
float velocidad = 0.0;
float potencia = 0.0;
//  Para el acelerador
int limite_inferior_acc;  // [ADC] Valor del acelerador al 0%
int limite_superior_acc; // [ADC] Valor del acelerador al 100%
int rango_pull_down_acelerador = 30;     // [ADC] rango de ADC en que la lectura del acelerador sera forzada a 0%
int rango_pull_up_acelerador = 15;     // [ADC] rango de ADC en que la lectura del acelerador sera forzada a 100%
int prog_v;
// timestamps para mediciones analogas
unsigned long tiempo_ultima_medicion = 0;
int lapso_mediciones = 80; // [ms]
// Variables para el conteo de veces en las que el imán pasa por el sensor hall de la rueda
int Cuenta_vueltas = 0;
bool Lectura_hallRueda = false;
bool Lectura_hallRueda_previo = false;
unsigned long tiempo_actualHR = 0; // Para registrar el tiempo entre mediciones [ms] , HR significa Hall de la Rueda
unsigned long tiempo_previoHR = 0; // Para registrar el tiempo entre mediciones [ms]
//  Constantes para los calculos matematicos
const float diametro_rueda = 29 * 2.54; // [cm]
const float pi = 3.141592;
const float long_medicion = pi * diametro_rueda / 100.0; // [m] Longitud avanzada entre cada pasada del iman
const float sensibilidad_adc_esp32 = 3.3/4096.0; // Sensibiilidad por defecto de los ADC de la esp32 [V/ADC]

// Funciones
float medir(int , int);
float medicion_voltaje(int);
float medicion_corriente(int);
float medicion_acelerador(int);
float medicion_temperatura(int);

/* GUARDADO EN TARJETA SD */
String datos_telemetria; // Mensaje que se escribira en cada linea del archivo de texto

// Funciones
// Write to the SD card (DON'T MODIFY THIS FUNCTION)
void writeFile(fs::FS &fs, const char * path, const char * message) {
  Serial.printf("Writing file: %s\n", path);

  File file = fs.open(path, FILE_WRITE);
  if(!file) {
    Serial.println("Failed to open file for writing");
    return;
  }
  if(file.print(message)) {
    Serial.println("File written");
  } else {
    Serial.println("Write failed");
  }
  file.close();
}
// Append data to the SD card (DON'T MODIFY THIS FUNCTION)
void appendFile(fs::FS &fs, const char * path, const char * message) {
  // Serial.printf("Appending to file: %s\n", path);

  File file = fs.open(path, FILE_APPEND);
  if(!file) {
    Serial.println("Failed to open file for appending");
    return;
  }
  if(file.print(message)) {
    Serial.println("Message appended");
  } else {
    Serial.println("Append failed");
  }
  file.close();
}
// Escribir en la tarjeta SD la informacion de los sensores
// Formato: tiempo [s] ; distancia recorrida [m] ; velocidad [m] ; voltaje [V] ; corriente [A] ; acelerador [%]
void logSDCard() {
  datos_telemetria = String(0.001 * millis()) + ";" + String(dist_recorrida) + ";" + String(velocidad) + ";" + String(mediciones[0]) + ";" + String(mediciones[1]) + ";" + String(mediciones[2]) + ";" + String(mediciones[3]) + "\r\n";
  appendFile(SD, "/data.txt", datos_telemetria.c_str());
}

 
/* ENVIO A ESP POR ESP-NOW */
void Envio(const uint8_t *mac_addr, esp_now_send_status_t status) {
  Serial.print("\r\nEnviar estado del mensaje:\t");
  Serial.println(status == ESP_NOW_SEND_SUCCESS ? "Envio exitoso" : "Envio fallido");
}

/* ~~~~~ DEFINICIONES DE LAS FUNCIONES ~~~~~ */
/* CALIBRACION */
// Esta funcion cuenta los clicks dados a uno de los botones
int conteo_clicks(int pin_a_usar , bool estado_previo , bool estado_actual , int tiempo_espera) {
  if (estado_actual && (estado_actual != estado_previo)) {    
    unsigned long tiempo_inicial = millis();
    unsigned long TEsp_ul = tiempo_espera;
    unsigned long tiempo_de_rebote = 0;
    
    int conteodeclicks = 0;
    estado_previo = false;
    estado_actual = false;

    while (millis() < (tiempo_inicial + TEsp_ul)) {
      bool lectura_boton = digitalRead(pin_a_usar);

      if (lectura_boton != estado_previo) {
        tiempo_de_rebote = millis();
      }

      if (millis() > (tiempo_de_rebote + lapso_de_rebote)) {
        if (lectura_boton != estado_actual) {
          estado_actual = lectura_boton;
          if (estado_actual) {
            conteodeclicks ++;
          }
        }
      }
    estado_previo = lectura_boton;
    }
    return conteodeclicks;
  }
  return 0;
}

// Funcion para calibrar las mediciones
void calibrar(int boton , int clicks) {
  if (clicks > 2) { // En caso de registrar mas de 2 clicks no se hara nada, pues no se consideraran como entradas validas
    return;
  }
  switch (boton)
  {
  case 0: // Boton [+]
    if (clicks == 1) { // 1 click: suma valores positivos a los parametros de calibracion
      if (parametro_calibracion < 5) {
        valores_calibracion[parametro_calibracion] += pow(10 , -1*digito_calibracion);
      }
      else { // Calibracion del acelerador al 100%
        Serial.println(" Calibrando acelerador al 100% ...");
        Serial.println("  ... Espere 2 segundos ...");
        limite_superior_acc = 0;
        for (int i = 0; i < 200 ; i++){
          limite_superior_acc += analogRead(sensores[2]);
          delay(10);
        }
        limite_superior_acc /= 200;
        Serial.print("Acelerador calibrado, valor adc al 100% :");
        Serial.println(limite_superior_acc);
      }
    }
    else { // 2 clicks: pasa al siguiente digito hacia la derecha en el parametro de calibracion
      if (digito_calibracion < 4) {
        digito_calibracion ++;
      }
    }
    break;
  
  case 1: // Boton [->]
    if (clicks == 1) { // 1 click: pasa al siguiente parametro de calibracion
      parametro_calibracion ++;
    }
    else { // 2 clicks: pasa al parametro de calibración anterior
      parametro_calibracion --;
    }
    digito_calibracion = 0; // Reinicio del digito de calibracion
    break;
  
  case 2: // Boton [-]
    if (clicks == 1) { // 1 click: suma valores negativos (resta) a los parametros de calibracion
      if (parametro_calibracion < 5) {
        valores_calibracion[parametro_calibracion] -= pow(10 , -1*digito_calibracion);
      }
      else { // Calibracion del acelerador al 0%
        Serial.println(" Calibrando acelerador al 0% ...");
        Serial.println("  ... Espere 2 segundos ...");
        limite_inferior_acc = 0;
        for (int i = 0; i < 200 ; i++){
          limite_inferior_acc += analogRead(sensores[2]);
          delay(10);
        }
        limite_inferior_acc /= 200;
        Serial.print("Acelerador calibrado, valor adc al 0% :");
        Serial.println(limite_inferior_acc);
      }
    }
    else { // 2 clicks: pasa al siguiente digito hacia la izquierda en el parametro de calibracion
      if (digito_calibracion > 0) {
        digito_calibracion --;
      }
    }
    break;
  
  default:
    break;
  }

  if (parametro_calibracion == -1) {
    parametro_calibracion = 5;
  }
  if (parametro_calibracion == 6) {
    parametro_calibracion = 0;
  }
  mostrar_calibracion();
  return;
}

void mostrar_calibracion() {
  Serial.println("Nuevos valores de calibración:");
  Serial.print(" -Factor voltímetro : ");
  Serial.println(valores_calibracion[0]);
  Serial.print(" -Desfase amperímetro : ");
  Serial.println(valores_calibracion[1]);
  Serial.print(" -Factor amperímetro : ");
  Serial.println(valores_calibracion[2]);
  Serial.print(" -Desfase temperatura : ");
  Serial.println(valores_calibracion[3]);
  Serial.print(" -Factor temperatura : ");
  Serial.println(valores_calibracion[4]);
  Serial.print(" -Valor adc del acelerador al 0% :");
  Serial.println(limite_inferior_acc);
  Serial.print(" -Valor adc del acelerador al 100% :");
  Serial.println(limite_superior_acc);

  Serial.println("Estado de la calibración: ");
  Serial.print(" -Valor a calibrar: ");
  Serial.println(parametro_calibracion + 1);
  Serial.print(" -Digito a modificar: ");
  Serial.println(digito_calibracion + 1);
}

/* SENSORES Y MEDICION */
// Funcion para determinar las medidas sensor que se ingrese al parametro
float medir(int cual_sensor , int lectura_sensor) {
  switch (cual_sensor)
  {
  case 0:
    return medicion_voltaje(lectura_sensor);
  case 1:
    return medicion_corriente(lectura_sensor);
  case 2:
    return medicion_acelerador(lectura_sensor);
  case 3:
    return medicion_temperatura(lectura_sensor);
  default:
    break;
  }
  return 0.0;
}

float medicion_voltaje(int lectura_sensor) {
  float resultado_medicion = static_cast<float>(lectura_sensor);
  resultado_medicion *= sensibilidad_adc_esp32; // Interpretacion ADC --> V
  resultado_medicion *= 8213.0/593.0;           // Divisor de voltaje [(5600 + 330) / (5600 + 330 + 8200 + 68k)]^-1
  return resultado_medicion * valores_calibracion[0];
}

float medicion_corriente(int lectura_sensor) {
  float resultado_medicion = static_cast<float>(lectura_sensor);
  resultado_medicion *= sensibilidad_adc_esp32;  // Interpretacion ADC --> V
  resultado_medicion *= 3.0/2.0;                 // Divisor de voltaje
  resultado_medicion = 2.5 - resultado_medicion; // Desfase inicial del medidor de corriente a 0A
  resultado_medicion *= 10.0;                    // Sensibilidad del sensor V --> A [100 mV/A]^-1
  return resultado_medicion * valores_calibracion[2] + valores_calibracion[1];
}

float medicion_acelerador(int lectura_sensor) {
  int rango_acelerador = limite_superior_acc - limite_inferior_acc; // determinación del rango completo del acelerador en [ADC]
  
  if (lectura_sensor <= (limite_inferior_acc + rango_pull_down_acelerador)) {
    lectura_sensor = limite_inferior_acc;
  }
  else if (lectura_sensor >= (limite_superior_acc - rango_pull_up_acelerador)){
    lectura_sensor = limite_superior_acc;
  }

  lectura_sensor -= limite_inferior_acc; // se resta el desfase en 0%

  float numerador = static_cast<float>(lectura_sensor);
  float denominador = static_cast<float>(rango_acelerador);
  return 100.0 * numerador / denominador; // salida en %
}

float medicion_temperatura(int lectura_sensor) {
  float resultado_medicion = static_cast<float>(lectura_sensor);
  resultado_medicion *= sensibilidad_adc_esp32;  // Interpretacion ADC --> V
  resultado_medicion *= 100.0;                   // Sensibilidad del sensor V --> °C [10 mV/°C]^-1
  return resultado_medicion * valores_calibracion[4] + valores_calibracion[3];
}




/* ~~~~~ SETUP Y LOOP ~~~~~ */
void setup() {
  estados_botones[0][0] = false;
  estados_botones[0][1] = false;
  estados_botones[1][0] = false;
  estados_botones[1][1] = false;
  estados_botones[2][0] = false;
  estados_botones[2][1] = false;
  
  //pinMode(BUILTIN_LED , OUTPUT);
  pinMode(15 , INPUT);
  pinMode(2 , INPUT);
  pinMode(4 , INPUT);
  pinMode(13 , OUTPUT);
  digitalWrite(13 , calibracion);

  pinMode(14 , INPUT);
  pinMode(32 , INPUT);
  pinMode(33 , INPUT);
  pinMode(34 , INPUT);
  pinMode(27 , INPUT);
  //digitalWrite(BUILTIN_LED , LOW);

  // Obtencion de los valores de calibracion guardados
  preferences.begin("calibracion" , true);
  preferences.getBytes("valores" , valores_calibracion , tamano_calibracion);
  limite_inferior_acc = preferences.getInt("acc_0" , 563); // [ADC] corresponde a 0.68 V a la salida directa del acelerador
  limite_superior_acc = preferences.getInt("acc_100" , 3525); // [ADC] corresponde a 4.26 V a la salida directa del acelerador
  preferences.end();

  Serial.begin(9600);
  Serial2.begin(9600);
  Serial.println("");


  // Para comunicacion RF
  // Set device as a Wi-Fi Station
  WiFi.mode(WIFI_STA);
  // Inicia ESP-NOW
  if (esp_now_init() != ESP_OK) {
    Serial.println("Error inicializando ESP-NOW");
    return;
  }
  // Registra la funcion de callback para responder al evento
  esp_now_register_send_cb(Envio);
  
  // Registro del slave
  esp_now_peer_info_t slaveInfo;
  memcpy(slaveInfo.peer_addr, slaveAddress, 6);
  slaveInfo.channel = 0;  
  slaveInfo.encrypt = false;
  
  // Add slave        
  if (esp_now_add_peer(&slaveInfo) != ESP_OK){
    Serial.println("Error registrando el slave");
    return;
  }
}


void loop() {
  clicks_botones[1] = conteo_clicks(botones[1] , estados_botones[1][0] , estados_botones[1][1] , tiempo_espera_botones);
  estados_botones[1][0] = estados_botones[1][1]; // Lectura del botón [->]
  estados_botones[1][1] = digitalRead(botones[1]);
  if (clicks_botones[1] == 3) {
    calibracion = !calibracion;
    digitalWrite(indicador_calibracion , calibracion);
    
    if (calibracion) { // Reabrir preferences para modificar los valores de calibracion guardados en la flash
      preferences.begin("calibracion",false);
      Serial.print("\nINICIO DE CALIBRACION DE LOS SENSORES\n");
    }
    else { // Guardar los valores de calibracion una vez se ha finalizado el proceso de calibracion
      preferences.putBytes("valores" , valores_calibracion , tamano_calibracion);
      preferences.putInt("acc_0" , limite_inferior_acc);
      preferences.putInt("acc_100" , limite_superior_acc);
      preferences.end();

      parametro_calibracion = 0;

      Serial.println("\nCALIBRACION FINALIZADA");
    }

    mostrar_calibracion();
  }
  if (calibracion) {
    //digitalWrite(BUILTIN_LED , HIGH);
    /* ~^^ CONTEO DE CLICKS EN LOS BOTONES EN EL MODO CALIBRACIÓN ^^~ */
    clicks_botones[0] = conteo_clicks(botones[0] , estados_botones[0][0] , estados_botones[0][1] , tiempo_espera_botones);
    //clicks_botones[1] = conteo_clicks(botones[1] , estados_botones[1][0] , estados_botones[1][1] , tiempo_espera_botones);
    clicks_botones[2] = conteo_clicks(botones[2] , estados_botones[2][0] , estados_botones[2][1] , tiempo_espera_botones);

    bool boton1 = clicks_botones[0] > 0;
    bool boton2 = clicks_botones[1] > 0;
    bool boton3 = clicks_botones[2] > 0;

    if (boton1 || boton2 || boton3) {
      for (int i = 0 ; i < 3 ; i++) {
        if (clicks_botones[i] > 0) {
          calibrar(i , clicks_botones[i]); // Calibracion de los sensores
        }
      }
    }

    // Lectura de los botones [- , +]
    estados_botones[0][0] = estados_botones[0][1];
    estados_botones[0][1] = digitalRead(botones[0]);

    estados_botones[2][0] = estados_botones[2][1];
    estados_botones[2][1] = digitalRead(botones[2]);
  }
  else {
    //digitalWrite(BUILTIN_LED , LOW);
    Lectura_hallRueda_previo = Lectura_hallRueda;
    Lectura_hallRueda = digitalRead(sensor_hallRueda);

    if (Lectura_hallRueda && !Lectura_hallRueda_previo) { // Solo se cumplirá si el imán ya pasó por el sensor y ahora no está cerca de él
      Cuenta_vueltas ++;
      tiempo_previoHR = tiempo_actualHR;
      tiempo_actualHR = millis();
      float t_medicion = tiempo_actualHR - tiempo_previoHR; // Tiempo entre mediciones

      dist_recorrida += long_medicion; // Calculo de la distancia recorrida
      velocidad = 3600.0 * long_medicion / t_medicion; // Calculo de la velocidad instantanea --> [km/h]
      
      // A la NEXTION
      Serial2.print("t4.txt=\"");
      Serial2.print(velocidad, 1);
      Serial2.print("\"");
      Serial2.write(0xff);
      Serial2.write(0xff);
      Serial2.write(0xff);

      Serial2.print("n1.val=");
      Serial2.print(int(dist_recorrida));
      Serial2.write(0xff);
      Serial2.write(0xff);
      Serial2.write(0xff);
    }
    /* medirvelocidad(); */ // Esto se puede usar con conteo_clicks() para solucionar lo de reiniciar la velocidad a 0 ;) creo...
  }
  
  // Mediciones de voltaje, corriente, acelerador y temperatura cada lapso_mediciones [ms]
  // y escritura en la NEXTION
  if ((millis() - tiempo_ultima_medicion) > lapso_mediciones) {
    int lectura; // Lectura del sensor a leer
    int decimales; // numero de decimales a mostrar
    
    lectura = analogRead(sensores[selector]);
    mediciones[selector] = medir(selector , lectura);
    
    switch (selector) {
    case 0: // Medicion de voltaje a la NEXTION
      decimales = 1;
      Serial2.print("t6.txt=\""); 
      break;
    case 1: // Medicion de corriente a la NEXTION
      decimales = 1;
      Serial2.print("t7.txt=\"");
      break;
    case 2: // Medicion del acelerador a la NEXTION
      decimales = 0;

      // Barra de acelerador
      Serial2.print("j0.val=");
      Serial2.print(int(mediciones[selector]));
      Serial2.write(0xff);
      Serial2.write(0xff);
      Serial2.write(0xff);
      
      if (mediciones[selector] < 15) {
        Serial2.print("j0.pco=63374");
        Serial2.write(0xff);
        Serial2.write(0xff);
        Serial2.write(0xff);
      }
      else if (mediciones[selector] > 85) {
        Serial2.print("j0.pco=64171");
        Serial2.write(0xff);
        Serial2.write(0xff);
        Serial2.write(0xff);
      }
      else {
        Serial2.print("j0.pco=28372");
        Serial2.write(0xff);
        Serial2.write(0xff);
        Serial2.write(0xff);
      }

      Serial2.print("t8.txt=\"");
      break;
    case 3: // Medicion de temperatura a la NEXTION
      decimales = 0;
      Serial2.print("t5.txt=\"");
      break;
    default:
      break;
    }
    Serial2.print(mediciones[selector], decimales);
    Serial2.print("\"");
    Serial2.write(0xff);
    Serial2.write(0xff);
    Serial2.write(0xff);

    selector ++;
    if (selector >= 4) {
      selector = 0; // reinicio del selector
    }
    tiempo_ultima_medicion = millis(); // actualizacion de la variable
  }



  // Para envio RF

  // Establecer valores a enviar 
  datos_telemetria_rf.dist_recorrida_rf = dist_recorrida;
  datos_telemetria_rf.velocidad_rf = velocidad;
  datos_telemetria_rf.voltaje_rf = mediciones[0];
  datos_telemetria_rf.corriente_rf = mediciones[1];
  datos_telemetria_rf.acelerador_rf = mediciones[2];
  datos_telemetria_rf.temperatura_rf = mediciones[3];

  // Envia mensaje via ESP-NOW
  esp_err_t result = esp_now_send(slaveAddress, (uint8_t *) &datos_telemetria_rf, sizeof(datos_telemetria_rf));
   
  if (result == ESP_OK) {
    Serial.println("Mensaje enviado exitosamente.");
  }
  else {
    Serial.println("Error en envio del mensaje");
  } 



  delay(10);
}
