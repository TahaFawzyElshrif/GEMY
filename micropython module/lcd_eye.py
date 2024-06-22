from lcd_i2c import LCD
from machine import I2C, Pin
import time

# PCF8574 on 0x27
I2C_ADDR = 0x27
NUM_ROWS = 2
NUM_COLS = 16

# Initialize I2C interface
i2c = I2C(0, scl=Pin(22), sda=Pin(21))

lcd = LCD(addr=I2C_ADDR, cols=NUM_COLS, rows=NUM_ROWS, i2c=i2c)

eye_11=[0b00000,0b00000,0b00001,0b00010,0b00100,0b01000,0b10000,0b10000]
eye_12=[0b00000,0b00111,0b11000,0b00000,0b00000,0b00111,0b01111,0b11111]
eye_13=[0b00000,0b10000,0b01100,0b00011,0b00000,0b00000,0b10000,0b11000]
eye_14=[0b00000,0b00000,0b00000,0b00000,0b10000,0b01000,0b00100,0b00010]
eye_21=[0b10000,0b10000,0b01000,0b00100,0b00010,0b00001,0b00000,0b00000]
eye_22=[0b11111,0b01111,0b00111,0b00000,0b00000,0b11000,0b00111,0b00000]
eye_23=[0b11000,0b10000,0b00000,0b00000,0b00011,0b01100,0b10000,0b00000]
eye_24=[0b00010,0b00100,0b01000,0b10000,0b00000,0b00000,0b00000,0b00000]



def setupeye():
    lcd.create_char(0, eye_11)
    lcd.create_char(1, eye_12)
    lcd.create_char(2, eye_13)
    lcd.create_char(3, eye_14)
    lcd.create_char(4, eye_21)
    lcd.create_char(5,eye_22)
    lcd.create_char(6, eye_23)
    lcd.create_char(7, eye_24)
    
    
def draw_eye(start):
        lcd.set_cursor(start, 0)  
        lcd.print(chr(0))
        lcd.set_cursor(start, 1)  
        lcd.print(chr(4))
        lcd.set_cursor(start+1, 0)  
        lcd.print(chr(1))
        lcd.set_cursor(start+1, 1)  
        lcd.print(chr(5))
        lcd.set_cursor(start+2, 0)  
        lcd.print(chr(2))
        lcd.set_cursor(start+2, 1)  
        lcd.print(chr(6))
        lcd.set_cursor(start+3, 0)  
        lcd.print(chr(3))
        lcd.set_cursor(start+3, 1)  
        lcd.print(chr(7))
        
        
def blink_eyes():
    try:
        setupeye()
        lcd.begin() 
        while True:
            lcd.clear()
            draw_eye(2)
            draw_eye(10)
            time.sleep(3)
    except Exception as e:
        pass
        #print(str(e))
def blink_eyes_one_time():
        setupeye()
        lcd.begin() 
        lcd.clear()
        draw_eye(2)
        draw_eye(10)
        time.sleep(3)
def prepareLcd():#just for savity ,as sometimes LCD produce strange values
    blink_eyes_one_time()
    lcd.clear()
    blink_eyes_one_time()
    lcd.clear()
