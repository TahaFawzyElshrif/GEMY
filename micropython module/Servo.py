from machine import Pin, PWM
import time
servo_pin=15
pwm = PWM(Pin(servo_pin), freq=50)
  
def set_servo_angle(angle):
    global pwm
    min_duty = 40
    max_duty = 115
    duty = min_duty + (max_duty - min_duty) * (angle / 180)
    pwm.duty(int(duty))


def helloServo():
    set_servo_angle(90)
    time.sleep(.25)
    set_servo_angle(0)
    time.sleep(.25)
    set_servo_angle(179)
    time.sleep(.25)
    set_servo_angle(90)
    time.sleep(.25)
def set_servo_angle_optimized(fro,to,delay):
    if (fro<to):
        for i in range (fro,to,10):
            set_servo_angle(i)
            time.sleep(delay)
    else:
        for i in range (fro,to,-10):
            set_servo_angle(i)
            time.sleep(delay)
            
def optimized_helloServo():
    
    for i in range(2):
        delay=.3      
        set_servo_angle(0)
        time.sleep(delay)
        set_servo_angle(5)
        time.sleep(delay)
        set_servo_angle(10)
        time.sleep(delay)
        set_servo_angle(15)
        time.sleep(delay)
        set_servo_angle(15)
        time.sleep(delay)
        set_servo_angle(10)
        time.sleep(delay)
        set_servo_angle(5)
        time.sleep(delay)
        set_servo_angle(0)
        time.sleep(delay)
    
    #set_servo_angle_optimized(0,90,.5)

#while True :
#    print("start")
#    optimized_helloServo()
#    print("end")
#    time.sleep(2)
