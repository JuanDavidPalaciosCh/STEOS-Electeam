package POO.Dashboard_de_Telemetria;

import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import Datos.Pista;
import Datos.Telemetria;
import Datos.Vehiculo;
import IU.GUI;

public class App {

    public static void main(String[] args) {
        String broker = "tcp://192.168.169.87:1883";
        String clientId = "esp32Client";
        MemoryPersistence persistence = new MemoryPersistence();

        String path = System.getProperty("user.dir");

        System.out.println(path);
      
        Telemetria telemetria = new Telemetria(0, 0, 0, 0, 0, false);
        Vehiculo Sharky = new Vehiculo("Sharky", "VTE", 3, telemetria, path + "/" + "Maven_Project/Dashboard_de_Telemetria/Img/Sharky.png","Santiago",80.0);
        Vehiculo McUN = new Vehiculo("McUN", "VTE", 3, telemetria, path + "/" + "Maven_Project/Dashboard_de_Telemetria/Img/McUN.png","Juliana",65.0);
        GUI gui = new GUI(telemetria,Sharky,McUN);
        gui.setVisible(true);

        try {
            MqttClient client = new MqttClient(broker, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            connOpts.setUserName("jpalaciosch");
            connOpts.setPassword("123456".toCharArray());

            System.out.println("Connecting to broker: " + broker);
            client.connect(connOpts);
            System.out.println("Connected");

            client.subscribe("Telemetría/#", new IMqttMessageListener() {
                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    String payload = new String(message.getPayload());
                    System.out.println("Topic: " + topic + ", Message: " + payload);

                   
                    switch (topic) {
                        case "Telemetría/Velocidad":
                            telemetria.setVelocidad(Double.parseDouble(payload));
                            break;
                        case "Telemetría/Distancia":
                            telemetria.setDistanciaRecorrida(Double.parseDouble(payload));
                            break;
                        case "Telemetría/Corriente":
                            telemetria.setCorriente(Double.parseDouble(payload));
                            break;
                        case "Telemetría/Voltaje":
                            telemetria.setVoltaje(Double.parseDouble(payload));
                            break;
                        case "Telemetría/Temperatura":
                            telemetria.setTemperatura(Double.parseDouble(payload));
                            break;
                        case "Telemetría/Hombre Muerto":
                            telemetria.setHombreMuerto(Boolean.parseBoolean(payload));
                            break;
                    }
                    telemetria.setPotencia(telemetria.getCorriente(),telemetria.getVoltaje());
                    telemetria.actualizarPotencia(telemetria.getPotencia());
                    gui.updateData(telemetria);
                    gui.actualizarDatosTelemetria();

       
                    System.out.println("Updated Telemetria: " +
                        "Velocidad=" + telemetria.getVelocidad() +
                        ", DistanciaRecorrida=" + telemetria.getDistanciaRecorrida() +
                        ", Corriente=" + telemetria.getCorriente() +
                        ", Voltaje=" + telemetria.getVoltaje() +
                        ", Temperatura=" + telemetria.getTemperatura() +
                        ", HombreMuerto=" + telemetria.getHombreMuerto() +
                    	", Potencia=" + telemetria.getPotencia());
                }
            });

        } catch (MqttException me) {
            System.out.println("reason " + me.getReasonCode());
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("excep " + me);
            me.printStackTrace();
        }
    }
    
    public static void definirPista(String Nombre,String Ubicacion,double Distancia) {
    	Pista pista = new Pista(Nombre,Ubicacion,Distancia);
    }


}
