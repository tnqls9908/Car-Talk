from bluepy.btle import Scanner, DefaultDelegate
class ScanDelegate(DefaultDelegate):
    def __init__(self):
        DefaultDelegate.__init__(self)
    def handleDiscovery(self, dev, isNewDev, isNewData):
        if isNewDev:
            print("Discovered device %s" % dev.addr)
        elif isNewData:
            print("Received new data from %s", dev.addr)

scanner = Scanner().withDelegate(ScanDelegate())
devices = scanner.scan(10.0)

for dev in devices:
    b_check = False
    for check in dev.getScanData():
        if(check[1] == 'Manufacturer'):
            if(str(check[2][:4]) == '4c00' and str(check[-2:]) == '00'):
                b_check = True
                break  
    if(b_check==False):
        continue
    print("Device %s (%s), RSSI=%d dB" % (dev.addr, dev.addrType, dev.rssi))
    for (adtype, desc, value) in dev.getScanData():
        print("  %s = %s" % (desc, value))



"""
sudo apt-get install python-pip libglib2.0-dev
sudo pip install bluepy

비콘 스캔하는 코드
Manufacturer가 4c00이면 비콘! Manufacturer가 비콘이 몇 개인지 알 면 될듯
"""