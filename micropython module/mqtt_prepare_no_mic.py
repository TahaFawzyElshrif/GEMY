import network
import time
from umqtt.simple import MQTTClient
import ubinascii
import machine
from machine import Pin, PWM , TouchPad ,I2C
from utils import connect_wifi_input_keypad ,repeated_hello_firebase,connect_firebase_input_keypad_store,playing_led_pin
from lcd_eye import blink_eyes_one_time,prepareLcd,lcd
from firebase import send_data_firebaseAuth,get_data_firebaseAuth
from speaker import default_Mute
from nvs_store import getStringStore,storeStringStore
import urequests
import ujson #to prevent some errors from firebase
import _thread
import gc

# MQTT server details
mqtt_server = 'test.mosquitto.org'
mqtt_port = 1883
request_topic = b'gemy/user/messages'
response_topic = b'gemy/user/responses'
client_id = ubinascii.hexlify(machine.unique_id())#just set id
received_message = None  
isrecording=True
auth_token = ""

'''
simplingframe=None
isrecording=True
microphone_sound_chunk=[]
microphone_sound_chunk_size=20
mic_sound_SR=32000

'''



def mqtt_callback(topic, msg):
    global received_message
    if topic == response_topic:
        received_message = msg.decode('utf-8')  # Store message as string
        
            
# Reconnect to MQTT broker
def mqtt_reconnect(client):
    while True:
        try:
            client.connect()
            print('Connected to MQTT broker')
            client.subscribe(request_topic)
            client.subscribe(response_topic)
            playing_led_pin.value(1)
            break
        except OSError as e:
            print('Failed to connect to MQTT broker. Reconnecting...')
            playing_led_pin.value(0)
            time.sleep(5)

    
   


# Setup
def setup():
    global auth_token
    default_Mute()
    prepareLcd()
    connect_wifi_input_keypad()#connect_wifi(ssid,password)
    auth_token = connect_firebase_input_keypad_store()
    blink_eyes_one_time() #starting ,wait question
    client = MQTTClient(client_id, mqtt_server, port=mqtt_port)
    client.set_callback(mqtt_callback)
    mqtt_reconnect(client)
    storeStringStore("auth_token",auth_token)
    del(auth_token)
    return client

'''
def execute_stop_recording():
    global isrecording
    if (isrecording):#isrecording condition to make code done just one time
         print("Touched! ,recording stopped ,waiting server answer")
         client.publish(request_topic, f"stop")
         isrecording=False
'''        

    
      
def loop(client):
    global received_message
    global isrecording
    #global auth_token

    
    
    while True:
        #if client.
        #mqtt_reconnect(client)

        try:
                ''' ##the commented stuff is for microphone
                #print("check_touched(touch_pin)")
                #print(check_touched(touch_pin))
                #if check_touched(touch_pin):#check if stop record button pressed
                #    print("checked")
                    #    execute_stop_recording()
                        
                    
                #if(isrecording):# must is not not
                    #playing_led_pin.value(1)

                    
                    mic_value = mic_pin.read()
                    microphone_sound_chunk.append(mic_value)
                    time.sleep(1/mic_sound_SR)
                    
                    if(len(microphone_sound_chunk)==microphone_sound_chunk_size):         
                        client.publish(request_topic, str(microphone_sound_chunk)+",")
                        microphone_sound_chunk=[]
                  else:    
                '''
                client.wait_msg()
                print(received_message)
                if received_message is None:
                    blink_eyes_one_time() #this may be no message or pending 
                    playing_led_pin.value(0) 
                else:
                    playing_led_pin.value(1)
                    if(str(received_message)=="finished"):
                        print("sound stopped,recording started")
                        blink_eyes_one_time()
                        #isrecording=True
                        #gc.collect()
                        #send_data_firebaseAuth(getStringStore("USER_NAME")+"/record",True, getStringStore ("auth_token"))
                        #gc.collect()
                                            
                    else:
                        '''
                        #print("before : "+str(received_message))
                        received_message=list(map(int,received_message.strip("'\n[] ").split(" ")))#reassign at received_message to save memory
                        #print("after : "+str(received_message))
                        play_sound(received_message,1/simplingframe)
                        received_message = None
                        '''
                        lcd.clear()
                        lcd.set_cursor(0, 0)  
                        lcd.print(str(received_message)[0:16])
                        lcd.set_cursor(0, 1)  
                        lcd.print(str(received_message)[16:32])
                        time.sleep(3)
                        received_message = None

                    
                        
                        
                
        except OSError as e:
            print('Connection lost. Reconnecting...')
            mqtt_reconnect(client)
        except Exception:
            pass

client = setup()

##this part clean garbage after each time to make code work due to limit memory
gc.collect()
#send_data_firebaseAuth(getStringStore("USER_NAME")+"/error","Started", getStringStore ("auth_token"))
gc.collect()
hello_cond=True#get_data_firebaseAuth(getStringStore("USER_NAME")+"/helloCond",   getStringStore ("auth_token"))
gc.collect()
speaker_cond=True#get_data_firebaseAuth(getStringStore("USER_NAME")+"/speakerCond", getStringStore ("auth_token"))
gc.collect()
_thread.start_new_thread(repeated_hello_firebase, (hello_cond,speaker_cond))
client.publish(request_topic, f"Hello")



loop(client)



