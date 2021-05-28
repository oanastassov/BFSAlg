//-----------------------------------------------------
// Author: Olivia Anastassov     
// Date:   11/4/21     
// Description: directed DGraph represented with adjacency lists
//              templates for BFS, strong components etc.
// Used template provided by professor Streinu
//-----------------------------------------------------
import java.util.*;
public class DGraphAdj{
    private ArrayList<Integer>[] adjLists;

//local var - used to help with debugging
    final static boolean DEBUG = false;

//utilities
    public static int toMathId(int n){
        return n+ 1;
    }
    public static void printDebug(String message){
        if (DEBUG){
            System.out.println(message);
        }
    }
    public static void printArrayInt(int [] array){
        for (int i = 0; i< array.length; i++){
            printDebug("" + toMathId(i)+ ": "+ array[i]);
        }
    }

//------------------------------------- 
// Constructors 
//-------------------------------------
    public DGraphAdj(int nrVertices){
        adjLists = new ArrayList[nrVertices];//throws a warning 
        for(int i = 0; i<nrVertices; i++){
            adjLists[i] = new ArrayList<Integer>();
        }
    }
    public DGraphAdj(ArrayList<Integer>[] adjLists){
        this.adjLists = adjLists;
    }

//------------------------------------- 
// Getters
//-------------------------------------
    public ArrayList<Integer>[] getAdjLists(){
        return adjLists;
    }
    public ArrayList<Integer> getAdjLists(int i){
        return adjLists[i];
    }
    public int getNrVertices(){
        return adjLists.length;
    }
    public int getNrNeighbors(int i){
        return adjLists[i].size();
    }

//------------------------------------- 
// Setters
//-------------------------------------
    public void setAdjLists(ArrayList<Integer>[] adjLists){
        this.adjLists = adjLists;
    }

//------------------------------------- 
// Serialize and print
//-------------------------------------
    public String toString(){
        int nrVertices = this.getNrVertices();
        String graphString = "";

        for(int v = 0; v<nrVertices; v++){
            graphString += toMathId(v) + ": ";
            ArrayList<Integer> adjList = getAdjLists(v);
            for(int i = 0; i < adjList.size(); i++){
                int neighbr = adjList.get(i);
                graphString += "" + toMathId(neighbr) + " ";
            }
            if (v < nrVertices - 1){
                graphString += "\n";
            }
        }
        return graphString;
    }
    // public String toMathString(){
    //     //STUB
    // }
    public void printDGraph(){
        System.out.println(this.toString());
    }
    //debugging helper
    private void printVisited(boolean[] visited){
        printDebug("\nvisited:");
        for(int j = 0; j<visited.length; j++){
            printDebug(toMathId(j)+ ": " + visited[j]);
        }
        printDebug("");
    }

//------------------------------------- 
// Modifiers
//-------------------------------------
    public void addEdge(int u, int v){
        if (u != v ){
            adjLists[u].add(v);
            adjLists[v].add(u);
        }
    }

//------------------------------------- 
// Converters
//-------------------------------------
    public DGraphEdges toDGraphEdges(){
        ArrayList<DEdge> dEdges = new ArrayList<DEdge>();
        for(int i = 0; i<this.getNrVertices(); i++){
            for(int r = 0; r<adjLists[i].size(); r++){
                dEdges.add(new DEdge(i,adjLists[i].get(r)));
            }
        }
        DGraphEdges toDGraph = new DGraphEdges(this.getNrVertices(),dEdges);
        return toDGraph;
    }

//------------------------------------- 
// Operations
//-------------------------------------
    public DGraphAdj reverseDGraph(){
        DGraphAdj revDGraph = new DGraphAdj(this.getNrVertices());
        for(int i = 0; i<this.getNrVertices(); i++){
            for(int r = 0; r<adjLists[i].size(); r++){
                revDGraph.addEdge(adjLists[i].get(r),i);
            }
        }
        return revDGraph;
    }
//---------------------------------------------------- 
// BFS
//----------------------------------------------------
   public BfsDataStructures BFS(int c){
        LinkedList<Integer> queue = new LinkedList<Integer>();//verts who's children need to be visited eventually
        BfsDataStructures BfsData = new BfsDataStructures(this.getNrVertices()); //contains all data and size of the number of verteces
        ArrayList<Integer> tempRow = new ArrayList<Integer>(); //contains all verts of a particular level
        BfsData.visited[c] = true; //starting vert set to visited
        queue.add(c); //holds all the verts whos children need to be visited
        tempRow.add(c); //holds everything on current level
        BfsData.nvCurrentRow += 1;//how many verts on current row
        BfsData.outputBFS.add(tempRow); //adds all the elements on a row to the big list of all the rows/levels 
        tempRow = new ArrayList<Integer> (); //starts fresh list for new elements on separate level
        while(queue.size() != 0){
            int v = queue.poll();//v is what we're visiting right now //removes v because we're visiting it now and don't want to visit it again
            BfsData.nvCurrentRow -= 1; 
            for(int i = 0; i< adjLists[v].size(); i++){//looping through all the children of v
                if(BfsData.visited[adjLists[v].get(i)] == false){//if not visited
                    BfsData.visited[adjLists[v].get(i)] = true; //visit it so now it is discovered
                    queue.add(adjLists[v].get(i)); //add it to the queue because need to visit the children //whats in the queue after each level are the children of what we visited
                    tempRow.add(adjLists[v].get(i));
                    BfsData.nvNextRow += 1; //keeps track of how many verts are on each level
                }
            }
            if(BfsData.nvCurrentRow == 0){//nothing left to visit on level
                BfsData.nvCurrentRow = BfsData.nvNextRow;
                BfsData.nvNextRow = 0;
                if(tempRow.size()>0){//only adds if theres things on the level
                    BfsData.outputBFS.add(tempRow);//adds level to big row
                }
                tempRow = new ArrayList<Integer> ();
            }
        }
        BfsData = disconnectedGraph(BfsData);
        return BfsData;
   }
   public BfsDataStructures disconnectedGraph(BfsDataStructures BfsData){//everything that isn't connected to the source is added to level -1
       ArrayList<Integer> temp = new ArrayList<Integer> ();
       for (int i = 0; i<BfsData.visited.length; i++){
           if(BfsData.visited[i] == false){
               temp.add(i);
           }
       }
       BfsData.outputBFS.add(0,temp);
       return BfsData;
   }

}