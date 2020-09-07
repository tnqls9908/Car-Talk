# test BLE Scanning software
import blescanner_uuid
import sys
import bluetooth._bluetooth as bluez

dev_id = 0

try:
    sock = bluez.hci_open_dev(dev_id)
    print("ble thread started")
except:
    print("error accessing bluetooth device...")
    sys.exit(1)

blescanner_uuid.hci_le_set_scan_parameters(sock)
blescanner_uuid.hci_enable_le_scan(sock)

while True:
    returnedList = blescanner_uuid.parse_events(sock, 10)
    print("----------")
    print(returnedList) 
    print("++++++++++++")

