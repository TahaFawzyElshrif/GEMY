from machine import Pin, PWM
import time

buzzer = PWM(Pin(5))

def play_tone(frequency, duration):
    buzzer.init()
    buzzer.freq(frequency)   # Set frequency
    buzzer.duty(512)         # Set duty cycle (range: 0-1023, 512 is 50%)
    time.sleep(duration)     # Play for the specified duration
    buzzer.duty(0)           # Stop playing



def playFreqTone(freq_arr):
    for freq in freq_arr:
        play_tone(freq, 0.08)
        time.sleep(0.08)
    buzzer.deinit()
    

def default_Mute():
    buzzer.deinit()

def playHello():
    playFreqTone([262, 294, 330, 349, 392, 440, 494, 523])
    