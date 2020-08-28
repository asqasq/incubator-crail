#!/usr/bin/env python3
import socket
import struct

# TODO: Initialize with IP and PORT of CRAIL namenode (the port must equal the port which is supposed to receive the trigger call)
NAMENODE_IP = "127.0.0.1"
NAMENODE_PORT = 9999

# TODO: Initialize with IP and PORT of CRAIL datanode which should leave
datanode_ip = "9.4.222.25"
datanode_port = 50020

def ip2int(ip):
  return struct.unpack("!I", socket.inet_aton(ip))[0]

def connect_until_succeed(HOSTNAME, PORT):
  connected = 1
  print("Connecting to metadata server....")
  while connected != 0:
    try:
      s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
      connected = s.connect_ex((HOSTNAME, PORT))
    except:
      print("Connection to metadata server refused...trying again")
      continue;
  return s

def dn_remove(socket, datanodeIp, datanodePort):
  datanodeIp = ip2int(datanodeIp)
  datanodePort = int(datanodePort)
  msg_packer = struct.Struct("!II") # ipaddr (INT) + port (INT)
  sampleMsg = (datanodeIp, datanodePort)
  pkt = msg_packer.pack(*sampleMsg)
  print("\ndn_remove(): send now...\n")
  socket.sendall(pkt)
  print("\ndn_remove(): sent\n")
  return

metadata_socket = connect_until_succeed(NAMENODE_IP, NAMENODE_PORT)
dn_remove(metadata_socket, datanode_ip, datanode_port)
