from bluepy.btle import Scanner, DefaultDelegate
class ScanDelegate(DefaultDelegate):
    def __init__(self):
        DefaultDelegate.__init__(self)
    def handleDiscovery(self, dev, isNewDev, isNewData):
        if isNewDev:
            print("Discovered device %s" % dev.addr)
        elif isNewData:
            print("Received new data from %s", dev.addr)

while(True):
    scanner = Scanner().withDelegate(ScanDelegate())
    devices = scanner.scan(1.0)
    for dev in devices:
        b_check = False
        for check in dev.getScanData():
            if(check[1] == 'Manufacturer'):
                c = str(check[2][:4])
                e = str(check[2][-2:])
                if(c == '4c00' and e == 'c8'):
                    print('right') 
                    b_check = True
                    break  

        if(b_check==False):
            print(dev.addr,'not')
            continue
        print("Device %s (%s), RSSI=%d dB" % (dev.addr, dev.addrType, dev.rssi))
        for (adtype, desc, value) in dev.getScanData():
            button_number = str(value[8:10])
            # 조건문으로 만약에 --면 마이크 입력 --면 마이크 입력 이렇게 적기 
            # 이 부분이 4c00 02 15 01(02, 03) 오는 부분에서 01부분
            if(desc == 'Manufacturer'):
                print("  %s = company: %s distance: %s data: %s major: %s minor: %s tx: %s" % (desc, value[:4], value[4:8], value[8:40], value[40:44], value[44:48], value[48:]))


"""
sudo apt-get install python-pip libglib2.0-dev
sudo pip install bluepy

비콘 스캔하는 코드
Manufacturer가 4c00이면 비콘! Manufacturer가 비콘이 몇 개인지 알 면 될듯
"""