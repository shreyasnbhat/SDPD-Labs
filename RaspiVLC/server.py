import socket
import subprocess
import sys

def get_data


BUFFER_SIZE = 1024
PORT = int(sys.argv[1])

s = socket.socket()
print "Socket successfully created"

s.bind(('', PORT))
s.listen(5)

while True:
    c, addr = s.accept()
    print 'Got connection from', addr
    data = c.recv(BUFFER_SIZE)
    data = get_data()
    c.send(data)
    c.close()
