import os

#button 초기화
import RPi.GPIO as GPIO   
import time
RED = 18
GREEN = 15
YELLOW = 23
GPIO.setmode(GPIO.BCM)      
GPIO.setup(RED, GPIO.IN)
GPIO.setup(GREEN, GPIO.IN)
GPIO.setup(YELLOW, GPIO.IN)

#adverting mode
os.system('sudo rfkill unblock all')
os.system('sudo hciconfig hci0 up')
os.system('sudo hciconfig hci0 leadv')
 
 #start
os.system('sudo hcitool -i hci0 cmd 0X08 0X0008 1E 02 01 1A 1A FF 4C 00 02 15 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 01 00 C8 00')
state = 'not'
try:
    while True:
        if GPIO.input(GREEN)==1:
            print ("Green Button was Pressed!" )
            if(state != 'green'):
                os.system('sudo hcitool -i hci0 cmd 0X08 0X0008 1E 02 01 1A 1A FF 4C 00 02 15 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 01 00 C8 00')
                state = 'green'
        elif GPIO.input(RED)==1:
            print ("Red Button was Pressed!" )
            if(state != 'red'):
                os.system('sudo hcitool -i hci0 cmd 0X08 0X0008 1E 02 01 1A 1A FF 4C 00 02 15 02 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 01 00 C8 00')
                state = 'red'
        elif GPIO.input(YELLOW)==1:
            print ("Yellow Button was Pressed!" )
            if(state != 'yellow'):
                os.system('sudo hcitool -i hci0 cmd 0X08 0X0008 1E 02 01 1A 1A FF 4C 00 02 15 03 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 01 00 C8 00')                                                                                       
                state = 'yellow'
        else:
            print ("Button was Not Pressed!")
            if(state != 'not'):
                state = 'not'
                os.system('sudo hcitool -i hci0 cmd 0X08 0X0008 1E 02 01 1A 1A FF 4C 00 02 15 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 01 00 C8 00')
        time.sleep(0.5)
except KeyboardInterrupt:
    GPIO.cleanup()