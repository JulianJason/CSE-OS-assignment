# Process Management - Programming Assignment 1
A process management script that will run processes specified in an instruction file based on the dependencies of each process.

### Author
Jason Julian, 1001693
Tasya Aditya Rukmana, 1001694
09/03/2017

### Instruction File Format
Instruction file will contain processes separated by newlines in the format
`<command>:<child nodes, space separated>:<input>:<output>`

example:

```
sleep 5:1:stdin:stdout
echo "Process P1 running. Dependency to P4 is cleared.":4:stdin:out1.txt
sleep 5:3:stdin:stdout
echo "Process P3 running. Dependency to P4 is cleared.":4:stdin:out2.txt
cat out1.txt out2.txt:5:stdin:cat-out.txt
grep 3:6:cat-out.txt:grep-out.txt
wc -l:none:grep-out.txt:wc-out.txt
```

### Running The Program

#### With javac*
*Only tested on unix machines

Open terminal and run the following commands
```
$ cd path/to/CSE-OS-assignment
$ javac ProcessManagement.java
$ java ProcessManagement
```

#### With IDEs

- Eclipse :
The native IDE used in this project is eclipse so you can just import the project directly to eclipse and run ProcessManagement.java
- IntelliJ :
1. Create a new project
2. Copy over the files from `CSE-OS-assignment` to the newly created project folder
3. Run ProcessManagement.java from the IDE

### How it works
```
1. For each node, the program checks if it is runnable
2. If it is, the program constructs a processBuilder to invoke the command inside the node
3. The program will wait for the process to finish executing and return to the main program.
4. After that, it will mark said node to be executed
5. The program continues checking for other nodes until all nodes have been executed
```
#### Single Threaded Busywaiting
The program that manages the processes works by checking all the nodes inside the graph. If the program detects that the node is eligible to run, it forks a seperate process to run said program and waits for the program to finish executing before continuing with other nodes.


### Limitations
- only one process is executed at any given time, even though there may be multiple nodes that are eligible to run at a given time.
### Possible Improvements
- We can assign a thread on each node to check if it is runnable and execute it independently.
    - The main thread will only be responsible to change the node status. Synchronization is needed in this case.
- 
