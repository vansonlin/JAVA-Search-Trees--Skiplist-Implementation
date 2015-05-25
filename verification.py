__author__ = 'vanson'
import time
t = time.time()
s = set()
f = open("query_insert_delete.txt", "r")
for line in f:
	line = line[:-1].split("\t")
	if line[0] == "1":
		s.add(line[1])
	elif line[1] in s:
		s.remove(line[1])
f.close()
print "Total Time (s):", time.time() - t
print "Number of Elements:", len(s)
print
