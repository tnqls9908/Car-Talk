import RPi.GPIO as GPIO   
import time
RED = 18
GREEN = 15
YELLOW = 23
GPIO.setmode(GPIO.BCM)      
GPIO.setup(RED, GPIO.IN)
GPIO.setup(GREEN, GPIO.IN)
GPIO.setup(YELLOW, GPIO.IN)
try:
    while True:
        if GPIO.input(GREEN)==1:
            print ("Green Button was Pressed!" )
        elif GPIO.input(RED)==1:     
            print ("Red Button was Pressed!" )  
        elif GPIO.input(YELLOW)==1:
            print ("Yellow Button was Pressed!" )                                                                                       
        else:
            print ("Button was Not Pressed!")
        time.sleep(0.5)
except KeyboardInterrupt:
    GPIO.cleanup()

