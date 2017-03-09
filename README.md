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

#### Single Threaded Busywaiting

### Improvements
