import argparse
import matplotlib.pyplot as plt
import numpy as np
from datetime import datetime

parser = argparse.ArgumentParser()
parser.add_argument('in_val', help='input file')
parser.add_argument('out_val', help='output file')

args = parser.parse_args()

# opens file with measurements
with open(args.in_val) as f:
    content = f.readlines()

### parses input data (make sure to adjust when needed) ###

# parse memory
mem = [float(s.strip().split('\t')[0])/(1024*1024) for s in content]

# parse time
time = [datetime.strptime(s.strip().split("at ")[1], '%H:%M:%S') for s in content]
offset = time[0]
time = [(x-offset).total_seconds() for x in time]


#oneGig = [1024*1024 for x in content]
#twoGig = [2*1024*1024 for x in content]
limit = [5.0 for x in content]

# only for debugging purposes
#print(mem)
#print()
#print(time)

# create plot

fig, ax = plt.subplots()

ax.set_xlabel("t in seconds")
ax.set_ylabel("memory consumption in gigabytes")
ax.legend()

ax.plot(time, limit, color="red", label="allocation limit")
ax.plot(time, mem)
#plt.scatter(time, mem)

#ax.plot(time, twoGig, color="black")

plt.savefig(args.out_val)
plt.show()