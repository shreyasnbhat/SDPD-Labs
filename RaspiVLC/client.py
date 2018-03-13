import socket
import sys

IP = '10.42.0.86'
#IP = 'localhost'
PORT = int(sys.argv[1])
BUFFER_SIZE = 1024

s = socket.socket(socket.AF_INET,socket.SOCK_STREAM)
s.connect((IP,PORT))
MESSAGE = str(raw_input().strip())
s.send(MESSAGE)
data = s.recv(BUFFER_SIZE)
s.close()

print data
