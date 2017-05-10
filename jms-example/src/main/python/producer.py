import stomp
import sys

conn = stomp.Connection10([('127.0.0.1', 61613)])
conn.start()
conn.connect()
conn.send('STOMPQueue', 'Rafael Chies')
conn.disconnect()
os._exit(1)
