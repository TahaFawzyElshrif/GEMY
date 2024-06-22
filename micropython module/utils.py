import network
import time
from machine import Pin ,ADC , TouchPad
from lcd_eye import *
from Servo import *

playing_led_pin=18 ##intialization ok or giving messages
playing_led_pin = Pin(playing_led_pin, Pin.OUT)
playing_led_pin.value(0)

ldr_pin=2
ldr_pin = ADC(Pin(ldr_pin))
#ldr_pin.atten(ADC.ATTN_11DB)  # Full range: 3.3v

ir_pin=35
ir_pin = ADC(Pin(ir_pin))

touch_pin = TouchPad(Pin(4))



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

def get_text_until_touch():
    input_string = ""
    lcd.set_cursor(0, 1)  
    while True:
        key = scan_keypad()
        if key:
            if check_touched(touch_pin) :
                return(input_string)
            else:
                input_string += key
                lcd.print( key)
            while scan_keypad() is not None:
                time.sleep(0.01)
                
                

def is_number(s):
    try:
        float(s)
        return True
    except ValueError:
        return False

def connect_wifi(ssid, password):
    wlan = network.WLAN(network.STA_IF)
    wlan.active(True)
    wlan.connect(ssid, password)
    
    while not wlan.isconnected():
        print('Connecting to WiFi...')
        time.sleep(1)
    
    print('WiFi connected:', wlan.ifconfig())
    playing_led_pin.value(1)
    
def connect_wifi_input_keypad():
    lcd.clear()
    lcd.set_cursor(0, 0)  
    lcd.print("Wifi_ssid(1 itmpt)")
    ssid=get_text_touchpad_until('D')
    time.sleep(.5)
    lcd.clear()
    lcd.set_cursor(0, 0)  
    lcd.print("Wifi_password(1 itmpt)")
    password=get_text_touchpad_until('D')
    lcd.clear()
    lcd.set_cursor(0, 0)
    lcd.print("Connecting....")
    connect_wifi(ssid, password)
    lcd.clear()
    lcd.print("Connected")

def read_LDR():
    light_value = ldr_pin.read()
    return ( light_value)

def read_IR():
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
    if(read_IR()<3500):
        print("near ir")
        optimized_helloServo()
    if(read_LDR()>1200):
        print("near ldr")
        optimized_helloServo()
    time.sleep(1)
    
def prepareLcd():#just for savity ,as sometimes LCD produce strange values
    blink_eyes_one_time()
    lcd.clear()
    blink_eyes_one_time()
    lcd.clear()

