import network
import time
import urequests
import ujson
from utils import *
from lcd_eye import *

FIREBASE_URL ='https://gemy-78e67-default-rtdb.firebaseio.com/' 
API_KEY='AIzaSyAWLGMTXwcUe0KLRPUqYD2DJ176DrLoCIs'
ssid = 'V2024'
password = '1000##30'

# Firebase Authentication credentials
firebase_email = ''
firebase_password = ''


# Function to authenticate with Firebase using email and password
def firebase_authenticate(email, password):
    auth_url = f'https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key={API_KEY}'
    auth_data = {
        'email': email,
        'password': password,
        'returnSecureToken': True
    }
    response = urequests.post(auth_url, data=ujson.dumps(auth_data))
    auth_result = response.json()
    response.close()
    if 'idToken' in auth_result:
        return auth_result['idToken']
    else:
        raise Exception('Firebase authentication failed')


# Function to send data to Firebase with authentication
def send_data_firebase(path, data, auth_token):
    url = FIREBASE_URL + path + '.json?auth=' + auth_token
    response = urequests.put(url, data=ujson.dumps(data))
    response.close()

def send_data_firebase(path, data):
    global FIREBASE_URL 
    global API_KEY
    url = FIREBASE_URL + path + '.json?auth=' + API_KEY
    response = urequests.put(url, data=ujson.dumps(data))
    response.close()

def get_data_firebase(path):
    global FIREBASE_URL 
    global API_KEY 
    url = FIREBASE_URL + path + '.json?auth=' + API_KEY
    response = urequests.get(url)
    data = response.json()
    response.close()
    return data

def get_data_firebase(path, auth_token):
    url = FIREBASE_URL + path + '.json?auth=' + auth_token
    response = urequests.get(url)
    data = response.json()
    response.close()
    return data
def connect_firebase_input_keypad():
    lcd.clear()
    lcd.set_cursor(0, 0)  
    lcd.print("name(1 itmpt)")
    name=get_text_touchpad_until('D')
    
    time.sleep(.5) #to prevent overlapping screen
    lcd.clear()
    lcd.set_cursor(0, 0)  
    lcd.print("password(1 itmpt)")
    password=get_text_touchpad_until('D')

    lcd.clear()
    lcd.set_cursor(0, 0)
    lcd.print("Connecting....")
    
    auth_token=firebase_authenticate(name+"@gemy.com", password)
    lcd.clear()
    lcd.print("Connected")
    return auth_token
#connect_wifi(ssid, password)
#auth_token = connect_firebase_input_keypad()#firebase_authenticate(firebase_email, firebase_password)
#print(auth_token)
#path = 'test'
#data = {'temperature': 25, 'humidity': 60}
#send_data_firebase(path, data)
#print(get_data_firebase('p',auth_token))

