import esp32
esp_NVS='gemy_space'

    
def storeStringStore(key,value):
    nvs = esp32.NVS(esp_NVS)
    nvs_set_str = value
    nvs.set_blob(key, nvs_set_str.encode('utf-8'))
    nvs.commit()


def getStringStore(key):
    try :#to not give error when no value
        nvs = esp32.NVS(esp_NVS)
        # Read the string from NVS
        # First, get the size of the blob
        size = nvs.get_blob(key, bytearray())
        # Then, create a buffer of the appropriate size
        buffer = bytearray(size)
        # Now, read the blob into the buffer
        nvs.get_blob(key, buffer)
        # Convert the buffer back to a string
        nvs_get_str = buffer.decode('utf-8')
        return(nvs_get_str)  
    except Exception:
        return ""

def clear_keyStore(key):
    nvs = esp32.NVS(esp_NVS)
    nvs.erase_key(key)
    nvs.commit()  

#storeString("ssid","ggggg")
#clear_key("ssid")
print(getString("ssid"))
