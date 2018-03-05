import sys
# Read a line
s = sys.stdin.readline().strip()

# Convert the read line into upper case, write it back.
# Keep reading until 'quit' is seen.
while s not in ["quit"]:
    sys.stdout.write(s.upper() + '\n')
    sys.stdout.flush()
    s = sys.stdin.readline().strip()


