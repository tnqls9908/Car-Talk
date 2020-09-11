# test BLE Scanning software

import blescanner
import sys

import bluetooth._bluetooth as bluez

dev_id = 0
try:
    sock = bluez.hci_open_dev(dev_id)
    print("ble thread started")
except:
    print("error accessing bluetooth device...")
    sys.exit(1)

blescanner.hci_le_set_scan_parameters(sock)
blescanner.hci_enable_le_scan(sock)

while True:
    returnedList = blescanner.parse_events(sock, 10)
    print("----------")
    for beacon in returnedList:
        print(beacon) 