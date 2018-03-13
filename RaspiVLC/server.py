import socket
import subprocess
import sys

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
    subprocess.call(['mplayer',data])
    c.send('Your video ' + data +' is playing')
    c.close()
