import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
/*
 * Programming Assignment 1
 * Author : Jason Julian, Tasya Aditya Rukmana
 * ID : 1001693, 1001694
 * Date : 09/03/2017
 *
 * Single-threaded busy wait system
 * Process waits for current running node to end before running another node, that means that no two nodes are 
 * concurrently running at a given time. (could be improved by implementing threading)
 * 
 * TODO:
 * - display unix perror message appropriately
 * - Implement threading on each node
 *   - create a new process thread on each node that busywaits for isRunnable
 *   - if parents are all executed, run
 *   - on end call processmanagement that node has executed successfully, use synchronized locks
 * - DOCUMENTATIONS
 * - FUCKING FIX THE PARSEFILE GLOBAL THROW SHIT
 * 
 * TODO DOCUMENTATIONS:
 * - record how node id is always tied to index, but there is no safety measures for this
 * - record how process builder only checks the root directory of project
 * */
 
public class ProcessManagement {

    //set the working directory
    private static File currentDirectory = new File(System.getProperty("user.dir") + "/src");
    //set the instructions file
    // private static File instructionSet = new File("testproc.txt");
//    private static File instructionSet = new File("graph-file");
    private static File instructionSet = new File("graph-file1");
    public static Object lock=new Object();

    public static void main(String[] args) throws InterruptedException {

        // parse the instruction file and construct a data structure, stored inside ProcessGraph class
        ParseFile.generateGraph(new File(currentDirectory + "/" + instructionSet));

        // Print the graph information
        ProcessGraph.printGraph();

        // check for all nodes until all are executed
        while(!ProcessGraph.allExecuted()) {
        	
        	// get node list iterator
            ListIterator<ProcessGraphNode> nodeIterator = ProcessGraph.nodes.listIterator();
            // check if there are still nodes left
        	while (nodeIterator.hasNext()) {
        		ProcessGraphNode node = nodeIterator.next();
        		// skip executed nodes
        		if (node.isExecuted()) {
        			continue;
        		}
        		
        		// if node parents has finished, set this node to runnable
        		if (node.allParentsExecuted()) {
            		node.setRunnable();
        		}
        		
        		if (node.isRunnable()) {
                    // run the node by splitting commands
                    String[] commands = node.getCommand().split(" ");
                    ProcessBuilder pb = new ProcessBuilder(commands);
                    
                    // should we dedicate a directory?
//                    File testDir = new File(System.getProperty("user.dir") + "/src/test");
//                    if (!testDir.exists()) {
//                    	testDir.mkdir();
//                    }
//                    pb.directory(testDir);
                    
                    try {
                        System.out.println("process " + node.getNodeId() + " started");
                        System.out.println("command = "+ node.getCommand());
                        System.out.println("in = " + node.getInputFile() + "\t out = " + node.getOutputFile());
                        System.out.println();
                        
                        
                        if (node.getInputFile().exists()) {
                            pb.redirectInput(node.getInputFile());
                        } else {
                        	throw new IOException("Input file does not exist");
                        }
                        
                        // if output file is present, delete and create a new one
                        if (node.getOutputFile().exists()) {
                        	node.getOutputFile().delete();
                        }
                    	File outfile = node.getOutputFile();
                    	outfile.createNewFile();
                    	outfile.setWritable(true);
                        pb.redirectOutput(outfile);
                        
                        // show error on system out
                    	pb.redirectErrorStream(true);
                        // start process and wait for it (single threaded busy wait)
                        Process process = pb.start();
                        int errCode = process.waitFor();
                        if (errCode == 0) {
                        	node.setExecuted();
                        } else {
                            System.out.println("errcode " + errCode);
                        	throw new Exception();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        return;
                    } catch (Exception e) {
                    	System.out.println("There is an error processing the commands");
                    	return;
                    }
                }
        	}
        }

        System.out.println("All process finished successfully");
           
    }

}
