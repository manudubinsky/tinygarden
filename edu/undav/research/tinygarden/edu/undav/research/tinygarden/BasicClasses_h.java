package edu.undav.research.tinygarden;

public class BasicClasses_h
{
  public interface VecInt_h
  {
    // constructors :
    // public VecInt()
    // public VecInt(int N);
    public void    erase();  
    public int     size();  
    public void    pushBack(int v);
    // throws ArrayIndexOutOfBoundsException if(j<0 || j>= size())
    public int     get(int j) throws ArrayIndexOutOfBoundsException;
    // throws ArrayIndexOutOfBoundsException if(size()==0)
    public int     getFront() throws ArrayIndexOutOfBoundsException;
    // throws ArrayIndexOutOfBoundsException if(size()==0)
    public int     getBack() throws ArrayIndexOutOfBoundsException;
    // throws ArrayIndexOutOfBoundsException if(size()==0)
    public void    popBack() throws ArrayIndexOutOfBoundsException;
    // throws ArrayIndexOutOfBoundsException if(j<0||j>=size())
    public void    set(int j, int vj) throws ArrayIndexOutOfBoundsException;
    // throws Exception if((other instanceof VecInt)==false)
    public void    swap(VecInt_h other) throws Exception;
  }

  public interface VecFloat_h
  {
    // constructors :
    // public VecFloat()
    // public VecFloat(int N);
    public void    erase();  
    public int     size();  
    public void    pushBack(float v);
    // throws ArrayIndexOutOfBoundsException if(j<0||j>= size())
    public float   get(int j) throws ArrayIndexOutOfBoundsException;
    // throws ArrayIndexOutOfBoundsException if size()==0
    public float   getFront() throws ArrayIndexOutOfBoundsException;
    // throws ArrayIndexOutOfBoundsException if size()==0
    public float   getBack() throws ArrayIndexOutOfBoundsException;
    // throws ArrayIndexOutOfBoundsException if size()==0
    public void    popBack() throws ArrayIndexOutOfBoundsException;
    // throws ArrayIndexOutOfBoundsException if(j<0 || j>= size())
    public void    set(int j, float vj) throws ArrayIndexOutOfBoundsException;
    // throws Exception if((other instanceof VecFloat)==false)
    public void    swap(VecFloat_h other) throws Exception;
  }

  //////////////////////////////////////////////////////////////////////
  public interface Partition_h
  {
    // constructors :
    // throws Exception if(n<=0)
    // public Partition(int n) throws Exception ;

    // throws Exception if(n<=0)
    public void reset(int n) throws Exception;

    public int  getNumberOfElements();
    public int  getNumberOfParts();
    
    // if(i>=0 && i<n)
    //   returns the part ID
    // else
    //   returns -1
    public int find(int i);
      
    // if(i>=0 && i<n && j>=0 && j<n && find(i)!=find(j))
    //   returns joined part ID
    // else
    //   returns -1
    public int join(int i, int j);
    
    // if(i>=0 && i<n) {
    //   if(i is a tree root)
    //     returns size of tree
    //   else
    //     returns 0
    // } else {
    //   returns -1
    // }
    public int getSize(int i);
  }

  //////////////////////////////////////////////////////////////////////
  public interface SplittablePartition_h extends Partition_h
  {
    // constructors :
    // throws Exception if(n<=0)
    // public SplittablePartition(int n) throws Exception ;
    
    // it doesn't do anything if i is out of range
    public void split(int i);
    

  }

  //////////////////////////////////////////////////////////////////////
  public interface Faces_h
  {
    // constructor
    // throws exception if(nV<0), if(coordIndex==null),
    // and if an element iV of coordIndex is iV<-1 or iV>=nV
    // public Faces(int nV, VecInt coordIndex) throws Exception;
    
    public int getNumberOfVertices();
    public int getNumberOfFaces();
    public int getNumberOfCorners(); // including the -1's
    // throws exception if iF is out of range
    public int getFaceSize(int iF) throws Exception;
    // throws exception if iF is out of range
    public int getFaceFirstCorner(int iF) throws Exception;
    // throws exception if iF or j are out of range
    public int getFaceVertex(int iF, int j) throws Exception;
  }

  //////////////////////////////////////////////////////////////////////
  public interface Graph_h
  {
    // constructor
    // creates a non-oriented graph with 0 vertices and 0 edges
    // public Graph();
    // creates a non-oriented graph with N vertices and 0 edges
    // public Graph(int N);

    public void    erase();
    public int     getNumberOfVertices();
    public int     getNumberOfEdges();
    // returns edge index if edge exists and -1 otherwise
    public int     getEdge(int iV0, int iV1);
    // returns edge index if new edge was created and -1 otherwise
    public int     insertEdge(int iV0, int iV1);
    // return the proper vertex index if edge index is in range, and -1 otherwise
    public int     getVertex0(int iE);
    public int     getVertex1(int iE);

    public interface GraphTraversal_h
    {
      // public GraphTraversal(Graph g);
      public void reset();
      // returns edge index
      public int  next();
    }
  }

  //////////////////////////////////////////////////////////////////////
  public interface PolygonMesh_h extends Faces_h
  {
    // once created, this class represents a read-only polygon mesh

    // throws Exception if parameters do not define a valid polygon mesh
    // public PolygonMesh(VecFloat coord, VecInt  coordIndex) throws Exception;

    // public int     getNumberOfVertices();
    // public int     getNumberOfEdges();
    public int     getNumberOfFaces();
    // public int     getNumberOfCorners();

    // throws exception if iV or j are out of range
    public float   getVertexCoord(int iV, int j) throws Exception;
    // throws exception if iC or j are out of range
    public float   getCornerCoord(int iC, int j) throws Exception;

    // throws exception if iF is out of range
    public int     getFaceSize(int iF) throws Exception;
    // throws exception if iF is out of range
    public int     getFaceFirstCorner(int iF) throws Exception;
    // throws exception if iF or j are out of range
    public int     getFaceVertex(int iF, int j) throws Exception;

    // read only Graph 
    public int     getEdge(int iC);
    public int     getEdge(int iV0, int iV1);
    public int     getVertex0(int iE);
    public int     getVertex1(int iE);

    // the constructor should create lists of faces incident to edges
    // returns 0 if iE is out of range
    public int     getNumberOfEdgeFaces(int iE);
    // returns -1 if iE or j are out of range
    public int     getEdgeFace(int iE, int j);

    // the constructor should classify vertices and edges
    public boolean isRegular();
    public boolean hasBoundary();
    // throw Exception if iV is out of range
    public boolean isBoundaryVertex(int iV) throws Exception;
    public boolean isRegularVertex(int iV) throws Exception;
    public boolean isSingularVertex(int iV) throws Exception;
    // throw Exception if iE is out of range
    public boolean isBoundaryEdge(int iE) throws Exception;
    public boolean isRegularEdge(int iE) throws Exception;
    public boolean isSingularEdge(int iE) throws Exception;
  }

  //////////////////////////////////////////////////////////////////////
  public interface HalfEdges_h
  {
  }

}
