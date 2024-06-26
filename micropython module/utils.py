import network
import time
from machine import Pin ,ADC , TouchPad
from lcd_eye import blink_eyes_one_time,lcd
from Servo import optimized_helloServo
from speaker import playHello,default_Mute
from nvs_store import getStringStore,storeStringStore
from firebase import firebase_authenticate
#import firebase
import urequests
import ujson #to prevent some errors from firebase
import gc
import micropython

#playing_led_pin=18 ##intialization ok or giving messages
playing_led_pin = Pin(18, Pin.OUT)
playing_led_pin.value(0)

#ldr_pin=34
ldr_pin = ADC(Pin(34))
ldr_pin.width(ADC.WIDTH_12BIT)    # Set the width to 12 bits
#ldr_pin.atten(ADC.ATTN_11DB)      # Set attenuation to 11dB for full range #this 2 lies as pin 2 may produce conflictions with esp blue light pin

#ir_pin=35
ir_pin = ADC(Pin(35))

touch_pin = TouchPad(Pin(4))

mic_pin=ADC(Pin(2))

# Define the row and column pins
rows = [Pin(i, Pin.OUT) for i in (32,33,25,26)]#(13,12,14,27)]
cols = [Pin(i, Pin.IN, Pin.PULL_UP) for i in (27,14,12,13)]#(32,33,25,26)]#(26,25,33,32)]

# Keymap for 4x4 keypad
keymap = [
    ['1', '2', '3', 'A'],
    ['4', '5', '6', 'B'],
    ['7', '8', '9', 'C'],
    ['*', '0', '#', 'D']
]

def scan_keypad():
    for row in range(4):
        rows[row].value(0)  # Set the current row to low
        for col in range(4):
            if not cols[col].value():  # Check if the column is low
                rows[row].value(1)  # Set the row back to high
                return keymap[row][col]
        rows[row].value(1)  # Set the row back to high
    return None



def get_text_touchpad_until(stop_char):
    input_string = ""
    lcd.set_cursor(0, 1)  
    while True:
        key = scan_keypad()
        if key:
            if key == stop_char:
                return(input_string)
                
            else:
                input_string += key
                lcd.print( key)
            while scan_keypad() is not None:
                time.sleep(0.01)  # Debounce delay
        time.sleep(0.01)



def is_number(s):
    try:
        float(s)
        return True
    except ValueError:
        return False


def connect_wifi(ssid, password):
    try:
        wlan = network.WLAN(network.STA_IF)
        wlan.active(True)
        wlan.disconnect()  # Disconnect if already connected
        wlan.connect(ssid, password)
        
        timeout = 10  # seconds
        start = time.time()
        
        while not wlan.isconnected():
            if time.time() - start > timeout:
                print("Failed to connect to Wi-Fi")
                return False
            time.sleep(1)
        
        print("Connected to Wi-Fi")
        print("Network config:", wlan.ifconfig())
        return True
    except Exception:
        return False

def connect_wifi_input_keypad():
    lcd.clear()
    lcd.set_cursor(0, 0)  
    lcd.print("Wifi Connect...")
    stored_ssid=getStringStore("Wifi_SSID")
    stored_password=getStringStore("Wifi_Password")
    if (connect_wifi(stored_ssid, stored_password)): 
        lcd.clear()
        lcd.print("Connected")
        time.sleep(.5)
        return
    
    lcd.clear()
    lcd.set_cursor(0, 0)  
    lcd.print("Wifi SSID : ")
    ssid_key=get_text_touchpad_until('D')
    time.sleep(.5)
    lcd.clear()
    lcd.set_cursor(0, 0)  
    lcd.print("Wifi Password :")
    password_key=get_text_touchpad_until('D')
    lcd.clear()
    lcd.set_cursor(0, 0)
    lcd.print("Connecting....")
    if (connect_wifi(ssid_key, password_key)): 
        lcd.clear()
        lcd.print("Connected")
        storeStringStore("Wifi_SSID",ssid_key)
        storeStringStore("Wifi_Password",password_key)
        return
    else:
       connect_wifi_input_keypad() 

def read_LDR():
    global ldr_pin
    light_value = ldr_pin.read()
    return ( light_value)

def read_IR():
    global ir_pin
    value = ir_pin.read()
    return ( value)


def check_touched(touching_pin):
    try:## may get error when long touch ,solve this by consider "long touch"/error is touch so stop
        touch_value = touching_pin.read() 
        if((touch_value < 400)):# pressed , stop
             return True
        return False
    except Exception as e:
            return True
            
 
def hello_condition():
    if(check_touched(touch_pin)):
        print("touched")
        optimized_helloServo()
        playHello()
    if(read_IR()<3500):
        print("near ir"+str(read_IR()))
        optimized_helloServo()
        playHello()
    if(read_LDR()>2550):#>3900):
        print("near ldr"+str(read_LDR()))
        optimized_helloServo()
        playHello()

    time.sleep(1)
    
def hello_condition_firebase(hello_cond,speaker_cond):#auth_token,user):
    
    #hello_cond=bool(get_data_firebaseAuth(user+"/"+"helloCond", auth_token))
    #speaker_cond=bool(get_data_firebaseAuth(user+"/"+"speakerCond", auth_token))
    
    if(hello_cond):  
        if(check_touched(touch_pin)):
            print("touched")
            optimized_helloServo()
            if speaker_cond :
                playHello()
        if(read_IR()<3500):
            print("near ir"+str(read_IR()))
            optimized_helloServo()
            if speaker_cond :
                playHello()
        
        if(read_LDR()>2550):
            print("near ldr"+str(read_LDR()))
            optimized_helloServo()
            if speaker_cond :
                playHello()
      
    time.sleep(1)
    
def repeated_hello_firebase(hello_cond,speaker_cond):
    while True:
        hello_condition_firebase(hello_cond,speaker_cond)
        
        

def connect_firebase_input_keypad_store():
    lcd.clear()
    lcd.set_cursor(0, 0)  
    lcd.print("LogIn.... ")
    name_stored=getStringStore("USER_NAME")
    user_password_stored=getStringStore("USER_Password")
    try :
        auth_token=firebase_authenticate(name_stored+"@gemy.com", user_password_stored)
        lcd.clear()
        lcd.print("Logged In.")
        return auth_token
    except Exception:
        pass
    
    lcd.clear()
    lcd.set_cursor(0, 0)  
    lcd.print("User Name : ")
    name=get_text_touchpad_until('D')
    
    time.sleep(.5) #to prevent overlapping screen
    lcd.clear()
    lcd.set_cursor(0, 0)  
    lcd.print("User Password :")
    password=get_text_touchpad_until('D')

    lcd.clear()
    lcd.set_cursor(0, 0)
    lcd.print("Logging in....")
    
    try :
        auth_token=firebase_authenticate(name+"@gemy.com", password)
        lcd.clear()
        lcd.print("Logged In.")
        storeStringStore("USER_NAME",name)
        storeStringStore("USER_Password",password)
        return auth_token
    except Exception:
        lcd.clear()
        lcd.print("Wrong Cred.")
        time.sleep(2)
        connect_firebase_input_keypad_store()
#default_Mute()
#connect_wifi_input_keypad()
