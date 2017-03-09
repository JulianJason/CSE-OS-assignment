import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
/*
 * Single-threaded busy wait system
 * Process waits for current running node to end before running another node, that means that no two nodes are 
 * concurrently running at a given time. (could be improved by implementing threading
 * 
 * TODO:
 * - If input file did not exist, throw error and message
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
  //  private static File instructionSet = new File("graph-file");
    private static File instructionSet = new File("graph-file1");
    public static Object lock=new Object();

    public static void main(String[] args) throws InterruptedException {

        //parse the instruction file and construct a data structure, stored inside ProcessGraph class
        ParseFile.generateGraph(new File(currentDirectory + "/"+instructionSet));

        // Print the graph information
        ProcessGraph.printGraph();
        // Using index of ProcessGraph, loop through each ProcessGraphNode, to check whether it is ready to run
        
        // create an arraylist of nodes
        ArrayList<ProcessGraphNode> nodesToRun = new ArrayList<ProcessGraphNode>();
        for (ProcessGraphNode node : ProcessGraph.nodes) {
        	nodesToRun.add(node);
        }
        // nodes
        while(!ProcessGraph.allExecuted()) {
            ListIterator<ProcessGraphNode> nodeIterator = nodesToRun.listIterator();
        	while (nodeIterator.hasNext()) {
        		ProcessGraphNode node = nodeIterator.next();
        		if (node.isExecuted()) {
        			continue;
        		}
        		if (node.allParentsExecuted()) {
        			System.out.println("setting node " + node.getNodeId() + " runnable");
            		node.setRunnable();
        		}
        		
        		if (node.isRunnable()) {
                    // run the node
                    String[] commands = node.getCommand().split(" ");
                    ProcessBuilder pb = new ProcessBuilder(commands);
//                    File testDir = new File(System.getProperty("user.dir") + "/src/test");
//                    if (!testDir.exists()) {
//                    	testDir.mkdir();
//                    }
//                    pb.directory(testDir);
                    try {
                        System.out.println("process " + node.getNodeId() + " started");
                        System.out.println("command = "+ node.getCommand());
                        System.out.println("in = " + node.getInputFile() + "\t out = " + node.getOutputFile());
                        if (node.getInputFile().exists()) {
                            pb.redirectInput(node.getInputFile());
                        }else {
                        	File infile = node.getInputFile();
                        	infile.createNewFile();
                        	infile.setReadable(true);
                            pb.redirectOutput(node.getInputFile());
                        }
                        if (node.getOutputFile().exists()) {
                        	node.getOutputFile().delete();
                        }
                    	File outfile = node.getOutputFile();
                    	outfile.createNewFile();
                    	outfile.setWritable(true);
                        pb.redirectOutput(outfile);
                        }
                        Process process = pb.start();
                        int errCode = process.waitFor();
                        System.out.println("errcode " + errCode);
                        //if (errCode == 0) {
                        	node.setExecuted();
                        	
                       // } else {
                       // 	throw new IOException();
                     //   }
                        // fork process and wait, at the end remove node from nodes to run
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.out.println("could not get node streams");
                        return;
                    }
                }
        	}
        }

        System.out.println("All process finished successfully");
           
    }

}
