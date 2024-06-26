import network
import time
import urequests
import ujson
#from lcd_eye import *
#from nvs_store import *

FIREBASE_URL ='https://gemy-78e67-default-rtdb.firebaseio.com/' 
API_KEY='AIzaSyAWLGMTXwcUe0KLRPUqYD2DJ176DrLoCIs'



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
def send_data_firebaseAuth(path, data, auth_token):
    url = FIREBASE_URL + path + '.json?auth=' + auth_token
    response = urequests.put(url, data=ujson.dumps(data))
    response.close()

def send_data_firebase(path, data):
    import gc
    try :
        gc.collect()
        global FIREBASE_URL 
        global API_KEY
        url = FIREBASE_URL + path + '.json?auth=' + API_KEY
        response = urequests.put(url, data=ujson.dumps(data))
        response.close()
    except OSError as e:
        gc.collect()
        send_data_firebase(path, data)

def get_data_firebase(path):
    global FIREBASE_URL 
    global API_KEY 
    url = FIREBASE_URL + path + '.json?auth=' + API_KEY
    response = urequests.get(url)
    data = response.json()
    response.close()
    return data

def get_data_firebaseAuth(path, auth_token):
    url = FIREBASE_URL + path + '.json?auth=' + auth_token
    response = urequests.get(url)
    data = response.json()
    response.close()
    return data

    

#connect_wifi(ssid, password)
#auth_token = connect_firebase_input_keypad()#firebase_authenticate(firebase_email, firebase_password)
#print(auth_token)
#path = 'test'

