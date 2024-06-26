package gemy.android.gemy_v1;


import static android.content.ContentValues.TAG;

import android.util.Log;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MqttHandler {

    private MqttClient client;
    public  String new_message;

    public void connect(String brokerUrl, String clientId) {
        try {
            // Set up the persistence layer
            MemoryPersistence persistence = new MemoryPersistence();

            // Initialize the MQTT client
            client = new MqttClient(brokerUrl, clientId, persistence);

            // Set up the connection options
            MqttConnectOptions connectOptions = new MqttConnectOptions();

            connectOptions.setCleanSession(true);

            // Connect to the broker
            client.connect(connectOptions);
            client.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    Log.e("TAG", "Connection lost: " + cause.getMessage());
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) {
                    Log.d("TAG", "Message arrived. Topic: " + topic + " Message: " + new String(message.getPayload()));
                    new_message=new String(message.getPayload());

                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    Log.d("TAG", "Delivery complete");
                }
            });
        } catch (MqttException e) {
            Log.e("----------------->",e.toString());
        }
    }

    public void disconnect() {
        try {
            client.disconnect();
        } catch (MqttException e) {
            Log.e("----------------->",e.toString());
        }
    }

    public void publish(String topic, String message) {
        try {
            MqttMessage mqttMessage = new MqttMessage(message.getBytes());
            client.publish(topic, mqttMessage);
        } catch (MqttException e) {
            Log.e("----------------->",e.toString());
        }
    }

    public void subscribe(String topic) {
        try {
            client.subscribe(topic);
        } catch (MqttException e) {
            Log.e("----------------->",e.toString());
        }
    }
}