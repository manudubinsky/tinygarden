package edu.undav.research.tinygarden;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;


public class BasicClasses extends BasicClasses_h
{
  //////////////////////////////////////////////////////////////////////
  public static class VecInt  implements VecInt_h
  {
	private int _vecLen;
	private int _size;
	private int[] _vec;

	public VecInt() {
		_vecLen = 1024;
		_reset();
	}

	public VecInt(int N) {
		_vecLen = N;
		_reset();
	}

	public VecInt(int[] vec) {
		_vec = vec;
		_size = _vecLen = vec.length;
	}

	public VecInt(int N, int initValue) {
		_vecLen = N;
		init(initValue);
	}

	public static VecInt buildIncreasing(int N) {
		VecInt v = new VecInt(N, 0);
		for(int i = 0; i < N; i++) {
			v.set(i,i);
		}
		return v;
	}
	
	public void _reset() {
		_vec = new int[_vecLen];
		_size = 0;
	}
	
	public void erase() {
		_size = 0;
	}

	public int size() {
		return _size;
	}

	public int vecLen() {
		return _vecLen;
	}

	public void pushBack(int v) {
		if (_size == _vecLen)
			_resize();
		_vec[_size++] = v;
	}

	public void pushBackCoord(int x, int y, int z) {
		pushBack(x);
		pushBack(y);
		pushBack(z);
	}

	public void pushBackTriFace(int iV0, int iV1, int iV2) {
		pushBack(iV0);
		pushBack(iV1);
		pushBack(iV2);
		pushBack(-1);
	}
	
	private void _resize() {
		// System.out.println("_resize: _vecLen: " + _vecLen);
		int[] newVec = new int[_vecLen*2];
		for (int i = 0; i < _vecLen; i++) {
			newVec[i] = _vec[i];
		}
		_vec = newVec;
		_vecLen *= 2;
	}
	
	public int get(int j) throws ArrayIndexOutOfBoundsException {
		if (j < 0 || j >= _size)
				throw new ArrayIndexOutOfBoundsException();
		return _vec[j];
	}

	public int getFront() throws ArrayIndexOutOfBoundsException {
		if (_size == 0)
			throw new ArrayIndexOutOfBoundsException();
		return _vec[0];
	}

	public int getBack() throws ArrayIndexOutOfBoundsException {
		if (_size == 0)
			throw new ArrayIndexOutOfBoundsException();
		return _vec[_size-1];
	}

	public void popBack() throws ArrayIndexOutOfBoundsException {
		if (_size == 0)
			throw new ArrayIndexOutOfBoundsException();
		_size--;
	}

	public int getPopBack() {
		return _vec[--_size];		
	}

	public void popBackN(int n) throws ArrayIndexOutOfBoundsException {
		if (_size < n)
			throw new ArrayIndexOutOfBoundsException();
		_size -= n;
	}

	public void set(int j, int vj) throws ArrayIndexOutOfBoundsException {
		if (j < 0 || j >= _size)
			throw new ArrayIndexOutOfBoundsException();
		_vec[j] = vj;
	}

	public void add(int j, int vj) throws ArrayIndexOutOfBoundsException {
		if (j < 0 || j >= _size)
			throw new ArrayIndexOutOfBoundsException();
		_vec[j] += vj;
	}
	
	public void inc(int j) throws ArrayIndexOutOfBoundsException {
		if (j < 0 || j >= _size)
			throw new ArrayIndexOutOfBoundsException();
		_vec[j]++;
	}

	public void dec(int j) throws ArrayIndexOutOfBoundsException {
		if (j < 0 || j >= _size)
			throw new ArrayIndexOutOfBoundsException();
		_vec[j]--;
	}

	public void swap(int i, int j) throws ArrayIndexOutOfBoundsException {
		if (i < 0 || i >= _size || j < 0 || j >= _size)
			throw new ArrayIndexOutOfBoundsException();
		int swap = _vec[i];
		_vec[i] = _vec[j];
		_vec[j] = swap;
	}

	public void swap(VecInt_h other) throws Exception {
	}
	
	public void dump() {
		System.out.println("*****");
		System.out.println("dump() _vecLen: " + _vecLen + " _size: " + _size);
		for (int i = 0; i < _vecLen; i++) {
			System.out.print(" " + _vec[i] + " ("+i+")");
		}
		System.out.println("");
		System.out.println("*****");
	}

	public int[] getVec() {
		return _vec;
	}
	
	public void init(int initValue) {
		_reset();
		if (initValue == 0) { // dafault is 0
			_size = _vecLen;
		} else {
			for (int i = 0; i < _vecLen; i++) {
				pushBack(initValue);
			}
		}
	}

	
	public boolean contains(int value) {
		boolean found = false;
		for (int i = 0; i < _size && !found; i++) {
			if (_vec[i] == value)
				found = true;
		}
		return found;
	}

	public int normOne() {
		int value = 0;
		for (int i = 0; i < _size; i++) {
			value += _vec[i] >= 0 ? _vec[i] : -_vec[i];
		}
		return value;
	}

	public int squareNorm() {
		int value = 0;
		for (int i = 0; i < _size; i++) {
			value += _vec[i] * _vec[i];
		}
		return value;
	}

	public void pushBackUnique(int value) {
		if (!contains(value))
			pushBack(value);
	}

	public VecInt clone() {
		VecInt copy = new VecInt(_vecLen);
		for (int i = 0; i < _size; i++) {
			copy.pushBack(_vec[i]);
		}
		return copy;
	}

	public void invert() {
		for (int i = 0; i < _size; i++) {
			_vec[i] = -_vec[i];
		}
	}

	public VecFloat toFloat() {
		VecFloat floatCopy = new VecFloat(_size);
		for (int i = 0; i < _size; i++) {
			floatCopy.pushBack((float)_vec[i]);
		}
		return floatCopy;
	}
	
	public VecInt add(VecInt v) throws Exception {
		if (_size != v.size())
			throw new Exception("Dimensions differ! this.size:" + _size + " v.size: " + v.size());
		for (int i = 0; i < _size; i++) {
			_vec[i] += v._vec[i];
		}
		return this;
	}

	public String join(String sep) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < _size; i++) {
			sb.append((sb.length() > 0 ? sep : "") + _vec[i]);
		}
		return sb.toString();
	}

  }

  public static class VecObject 
  {
	private int _vecLen;
	private int _size;
	private Object[] _vec;

	public VecObject() {
		_vecLen = 1024;
		_reset();
	}

	public VecObject(int N) {
		_vecLen = N;
		_reset();
	}

	public void _reset() {
		_vec = new Object[_vecLen];
		_size = 0;
	}
	
	public int size() {
		return _size;
	}

	public int vecLen() {
		return _vecLen;
	}

	public void pushBack(Object v) {
		if (_size == _vecLen)
			_resize();
		_vec[_size++] = v;
	}
	private void _resize() {
		// System.out.println("_resize: _vecLen: " + _vecLen);
		Object[] newVec = new Object[_vecLen*2];
		for (int i = 0; i < _vecLen; i++) {
			newVec[i] = _vec[i];
		}
		_vec = newVec;
		_vecLen *= 2;
	}
	public Object get(int j) throws ArrayIndexOutOfBoundsException {
		if (j < 0 || j >= _size)
				throw new ArrayIndexOutOfBoundsException();
		return _vec[j];
	}
  }
  
  
  public static class VecBool {
	private int _vecLen;
	private int _size;
	private boolean[] _vec;

	public VecBool() {
		_vecLen = 1024;
		_reset();
	}

	public VecBool(int N) {
		_vecLen = N;
		_reset();
	}

	public VecBool(int N, boolean initValue) {
		_vecLen = N;
		init(initValue);
	}

	public void init(boolean initValue) {
		_reset();
		if (initValue == false) { // default is false
			_size = _vecLen;
		} else {
			for (int i = 0; i < _vecLen; i++) {
				pushBack(initValue);
			}
		}
	}

	private void _reset() {
		_vec = new boolean[_vecLen];
		_size = 0;
	}
	
	public void erase() {
		_size = 0;
	}

	public int size() {
		return _size;
	}

	public void pushBack(boolean v) {
		if (_size == _vecLen)
			_resize();
		_vec[_size++] = v;
	}

	private void _resize() {
		// System.out.println("_resize: _vecLen: " + _vecLen);
		boolean[] newVec = new boolean[_vecLen*2];
		for (int i = 0; i < _vecLen; i++) {
			newVec[i] = _vec[i];
		}
		_vec = newVec;
		_vecLen *= 2;
	}
	
	public boolean get(int j) throws ArrayIndexOutOfBoundsException {
		if (j < 0 || j >= _size)
				throw new ArrayIndexOutOfBoundsException();
		return _vec[j];
	}

	public boolean getFront() throws ArrayIndexOutOfBoundsException {
		if (_size == 0)
			throw new ArrayIndexOutOfBoundsException();
		return _vec[0];
	}

	public boolean getBack() throws ArrayIndexOutOfBoundsException {
		if (_size == 0)
			throw new ArrayIndexOutOfBoundsException();
		return _vec[_size-1];
	}

	public void popBack() throws ArrayIndexOutOfBoundsException {
		if (_size == 0)
			throw new ArrayIndexOutOfBoundsException();
		_size--;
	}

	public boolean getPopBack() {
		return _vec[--_size];		
	}
	
	public void set(int j, boolean vj) throws ArrayIndexOutOfBoundsException {
		if (j < 0 || j >= _size)
			throw new ArrayIndexOutOfBoundsException();
		_vec[j] = vj;
	}

	public void swap(int i, int j) throws ArrayIndexOutOfBoundsException {
		if (i < 0 || i >= _size || j < 0 || j >= _size)
			throw new ArrayIndexOutOfBoundsException();
		boolean swap = _vec[i];
		_vec[i] = _vec[j];
		_vec[j] = swap;
	}

	public void dump() {
		System.out.println("*****");
		System.out.println("dump() _vecLen: " + _vecLen + " _size: " + _size);
		for (int i = 0; i < _vecLen; i++) {
			System.out.print(" " + _vec[i]);
		}
		System.out.println("");
		System.out.println("*****");
	}

	public boolean[] getVec() {
		return _vec;
	}
		
  }



/*
  public static void main(String[] args) {

	  VecFloat v = new VecFloat();
	for (int i = 0; i < 5000; i++) {		
		v.pushBack(i + 0.0f);
	}
	System.out.println("v[0]: " + v.get(0) + " v[1024]: " + v.get(1024) + " v[2048]: " + v.get(2048) + " v[4096]: " + v.get(4096));
  }
*/

  public static void main(String[] args) {

	  Graph g = Graph.buildReduction(2);
	  System.out.println("nV: " + g.getNumberOfVertices() + " nE: " + g.getNumberOfEdges());
	  g.dump();
  }

  //////////////////////////////////////////////////////////////////////
  public static class VecFloat  implements VecFloat_h
  {
		private int _vecLen;
		private int _size;
		private float[] _vec;

	public VecFloat() {
		_vecLen = 1024;
		_reset();		
	}
	
	public VecFloat(int N) {
		_vecLen = N;
		_reset();				
	}

	public VecFloat(float[] vec) {
		_vec = vec;
		_size = _vecLen = vec.length;			
	}

	public VecFloat(int N, float initValue) {
		_vecLen = N;
		init(initValue);
	}

	public VecFloat clone() {
		VecFloat copy = new VecFloat(_vecLen);
		for (int i = 0; i < _size; i++) {
			copy.pushBack(_vec[i]);
		}
		return copy;
	}

	public boolean allEqual() {
		boolean equal = true;
		for (int i = 0; (i < _size) && equal; i++) {
			if (Math.abs(_vec[i] - _vec[0]) > 0.0001f)
				equal = false;
		}
		return equal;
	}

	public float innerProd(VecFloat v) {
		float innerProd = 0;
		for (int i = 0; i < _size; i++) {
			innerProd +=  _vec[i] * v._vec[i];
		}
		return innerProd;
	}

	public float squareNorm() {
		return innerProd(this);
	}

	public VecFloat multiplyByScalar(float scalar) {
		for (int i = 0; i < _size; i++) {
			_vec[i] *= scalar;
		}
		return this;
	}

	private void _reset() {
		_vec = new float[_vecLen];
		_size = 0;
	}

	public void erase() {
		_size = 0;
	}

	public int size() {
		return _size;
	}

	public void pushBack(float v) {
		if (_size == _vecLen)
			_resize();
		_vec[_size++] = v;
	}

	public void pushBackCoord(float x, float y, float z) {
		pushBack(x);
		pushBack(y);
		pushBack(z);
	}

	private void _resize() {
		//System.out.println("_resize: _vecLen: " + _vecLen);
		float[] newVec = new float[_vecLen*2];
		for (int i = 0; i < _vecLen; i++) {
			newVec[i] = _vec[i];
		}
		_vec = newVec;
		_vecLen *= 2;
	}

	public float get(int j) throws ArrayIndexOutOfBoundsException {
		if (j < 0 || j >= _size)
			throw new ArrayIndexOutOfBoundsException();
		return _vec[j];
	}

	public float getFront() throws ArrayIndexOutOfBoundsException {
		if (_size == 0)
			throw new ArrayIndexOutOfBoundsException();
		return _vec[0];
	}

	public float getBack() throws ArrayIndexOutOfBoundsException {
		if (_size == 0)
			throw new ArrayIndexOutOfBoundsException();
		return _vec[_size-1];
	}

	public void popBack() throws ArrayIndexOutOfBoundsException {
		if (_size == 0)
			throw new ArrayIndexOutOfBoundsException();
		_size--;
	}

	public void set(int j, float vj) throws ArrayIndexOutOfBoundsException {
		if (j < 0 || j >= _size)
			throw new ArrayIndexOutOfBoundsException();
		_vec[j] = vj;
	}

	public void add(int j, float vj) throws ArrayIndexOutOfBoundsException {
		if (j < 0 || j >= _size)
			throw new ArrayIndexOutOfBoundsException();
		_vec[j] += vj;
	}

	public void swap(VecFloat_h other) throws Exception {
		// TODO Auto-generated method stub
		
	}

	public float[] getVec() {
		return _vec;
	}

	public void dump() {
		System.out.println("*****");
		System.out.println("dump() _vecLen: " + _vecLen + " _size: " + _size);
		for (int i = 0; i < _size; i++) {
			System.out.print(" " + _vec[i]);
		}
		System.out.println("");
		System.out.println("*****");
	}

	public void dump(String tag) {
		System.out.println("*****");
		System.out.println(tag + " dump() _vecLen: " + _vecLen + " _size: " + _size);
		for (int i = 0; i < _size; i++) {
			System.out.print(" " + _vec[i]);
		}
		System.out.println("");
		System.out.println("*****");
	}

	public void init(float initValue) {
		_reset();
		if (initValue == 0) { // el dafault es 0
			_size = _vecLen;
		} else {
			for (int i = 0; i < _vecLen; i++) {
				pushBack(initValue);
			}
		}
	}

	public VecFloat add(VecFloat v) throws Exception {
		if (_size != v.size())
			throw new Exception("Dimensions differ! this.size:" + _size + " v.size: " + v.size());
		for (int i = 0; i < _size; i++) {
			float value = get(i);
			set(i, value + v.get(i));
		}
		return this;
	}

	public VecFloat subtract(VecInt v) throws Exception {
		if (_size != v.size())
			throw new Exception("Dimensions differ! this.size:" + _size + " v.size: " + v.size());
		for (int i = 0; i < _size; i++) {
			_vec[i] -= (float)v.get(i);
		}
		return this;
	}

	public VecFloat subtract(VecFloat v) throws Exception {
		return addMultiple(v, -1);
	}

	public VecFloat addMultiple(VecFloat v, float lamda) throws Exception {
		if (_size != v.size())
			throw new Exception("Dimensions differ! this.size:" + _size + " v.size: " + v.size());
		for (int i = 0; i < _size; i++) {
			float value = get(i);
			set(i, value + v.get(i) * lamda);
		}
		return this;
	}

	private boolean inRange(int index) {
		return (index >= 0 && index < _size);
	}
	
	// subvector: [from,from+count-1]
	public VecFloat subVec(int from, int count) throws Exception {		
		if (!inRange(from) || !inRange(from + count - 1))
			throw new Exception("Not in range. _size: " + _size + " from: " + from + " count: " + count + " (from+count-1): " + (from+count-1));
		VecFloat subVec = new VecFloat(count);
		for (int i = from; i < from+count; i++) {
			subVec.pushBack(get(i));
		}
		return subVec;
	}
	
	// discards last elems 
	public VecFloat head(int pos){
		_size = pos;
		return this;
	}


  }

  //////////////////////////////////////////////////////////////////////
/*
  public static void main(String[] args) {
	  try {
		Partition p = new Partition(10);
		p.dump();
		p.join(1,2);
		p.dump();
		p.join(3,4);
		p.dump();
		p.join(2,5);
		p.dump();
		p.join(2,4);
		p.dump();
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
  }
*/
  public static class Partition implements Partition_h
  { 
	  private int[] _elems;
	  private VecInt[] _parts;
	  private int _numElems;
	  private int _numParts;
	  private VecInt _emptyParts;
	  
	  public Partition(int n) throws Exception {
		  reset(n);
	  }
	  
	public void reset(int n) throws Exception {
		_numElems = n;
		_numParts = n;
		_elems = new int[n];
		_parts = new VecInt[n];
		_emptyParts = new VecInt(n / 10);
		for (int i = 0; i < _elems.length; i++) {
			_elems[i] = i;
			_parts[i] = new VecInt(1);			
			_parts[i].pushBack(i);
		}
	}

	public int getNumberOfElements() {
		return _numElems;
	}

	public int getNumberOfParts() {
		return _numParts;
	}

	public int find(int i) {
		if (i >= 0 && i < _numElems)
			return _elems[i];
		else
			return -1;
	}

	public int join(int i, int j) {
		int source = getSize(i) <= getSize(j) ? i : j;
		int target = (source == i) ? j : i;
		int srcSize = _parts[source].size();
		for (int k = 0; k < srcSize; k++) {
			int elem = _parts[source].get(k);
			_parts[target].pushBack(elem);
			_elems[elem] = target;
		}
		_parts[source] = new VecInt(1);
		_emptyParts.pushBack(source);
		return target;
	}

	public int getSize(int i) {
		if (i >= 0 && i < _numElems)
			return _parts[i].size();
		else
			return 0;
	}

	public void dump(){
		StringBuffer sb = new StringBuffer();
		sb.append("elems: "); 
		for (int i = 0; i < _elems.length; i++) {
			sb.append(" " + i + " = " + _elems[i] + " ");
		}
		System.out.println(sb);
		
		for (int i = 0; i < _parts.length; i++) {
			sb = new StringBuffer();
			sb.append("part: " + i);
			for (int j = 0; j < _parts[i].size(); j++) {
				sb.append(" " + _parts[i].get(j));
			}
			System.out.println(sb);				
		}
		
		sb = new StringBuffer();
		sb.append("emptyParts: ");
		for (int i = 0; i < _emptyParts.size(); i++) {
			sb.append(" " + _emptyParts.get(i));
		}
		System.out.println(sb);
	}
  }

  //////////////////////////////////////////////////////////////////////
/*
  public static void main(String[] args) {
	  try {
		PartitionUnionFind p = new PartitionUnionFind(10);
		p.dump();
		System.out.println("(0,1)");
		p.join(0,1);
		p.dump();
		System.out.println("(1,2)");
		p.join(1,2);
		p.dump();
		System.out.println("(3,4)");
		p.join(3,4);
		p.dump();
		System.out.println("(4,5)");
		p.join(4,5);
		p.dump();
		System.out.println("(5,6)");
		p.join(5,6);		
		p.dump();
		System.out.println("(6,7)");
		p.join(6,7);		
		p.dump();
		System.out.println("(6,8)");
		p.join(6,8);		
		p.dump();
		System.out.println("(7,9)");
		p.join(7,9);		
		p.dump();
		System.out.println("(5,1)");
		p.join(5,1);		
		p.dump();
		System.out.println(p.find(0));
		p.dump();
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
  }
*/
  
  public static class PartitionUnionFind implements Partition_h
  {
	  private int[] _elems;
	  private int[] _partSizes;
	  private int _numElems;
	  private int _numParts;	  
	  
	  public PartitionUnionFind(int n) throws Exception {
		  reset(n);
	  }
	  
	public void reset(int n) throws Exception {
		_numElems = n;
		_numParts = n;
		_elems = new int[n];
		_partSizes = new int[n];
		for (int i = 0; i < _elems.length; i++) {
			_elems[i] = i;
			_partSizes[i] = 1;
		}
	}

	public int getNumberOfElements() {
		return _numElems;
	}

	public int getNumberOfParts() {
		return _numParts;
	}

	public int[] getElems() {
		return _elems;
	}
	
	public int find(int i) {
		if (i >= 0 && i < _numElems)
			if (_elems[_elems[i]] == _elems[i]) {
				return _elems[i];
			} else {
				_elems[i] = find(_elems[i]);
				return _elems[i];				
			}
		else
			return -1;
	}

	public boolean checkHasToJoin(int i, int j) {
		int iPart = find(i);
		int jPart = find(j);
		return (iPart != jPart);
	}
	
	public boolean checkJoin(int i, int j) {
		int iPart = find(i);
		int jPart = find(j);
		int joinPart = join(i,j);
		return (iPart != joinPart) || (jPart != joinPart);
	}

	public int join(int i, int j) {
		int minSizePart = find(i);
		int maxSizePart = find(j);
		if (minSizePart != maxSizePart) {
			if (_partSizes[minSizePart] > _partSizes[maxSizePart]) {  
				int aux = minSizePart;
				minSizePart = maxSizePart;
				maxSizePart = aux;
			}
			_elems[minSizePart] = maxSizePart;
			_partSizes[maxSizePart] += _partSizes[minSizePart];
			_numParts--;
		}
		return maxSizePart;
	}

	public int getSize(int i) {
		if (i >= 0 && i < _numElems)
			return _partSizes[i];
		else
			return 0;
	}

	public void dump(){
		System.out.println("_numParts: " + _numParts);
		
		StringBuffer sb = new StringBuffer();
		sb.append("elems: "); 
		for (int i = 0; i < _elems.length; i++) {
			sb.append(" " + i + " = " + _elems[i] + " ");
		}
		System.out.println(sb);		
	}
  }
  //////////////////////////////////////////////////////////////////////
/*
  public static void main(String[] args) throws Exception {
	Graph g = new Graph(4);	  
	g.insertEdge(0, 1);
	g.insertEdge(0, 2);
	//g.insertEdge(0, 3);
	g.insertEdge(1, 2);
	g.insertEdge(1, 3);
	g.insertEdge(2, 3);
	SpanningTree s = new SpanningTree(g);
	SparseMatrix m = s.getTree();
	m.fullDump();
	System.out.println("******************");
	m.resize(g.getNumberOfEdges());
	m.fullDump();
	s.getTreeEdges().dump();
  }
*/  
  public static class SpanningTree {
	  private SparseMatrixInt _spannigTree;
	  private VecInt _vertex2Label;
	  private VecInt _label2Vertex;
	  int _nV, _nE, _label;
	  private VecInt _treeEdges;
	  
	  public SpanningTree (Graph graph) throws Exception {
		  _label = 0;
		  _nV = graph.getNumberOfVertices();
		  _nE = graph.getNumberOfEdges();		  
		  _vertex2Label = new VecInt(_nV,-1);
		  _label2Vertex = new VecInt(_nV,-1);
		  _treeEdges = new VecInt(_nV-1);
		  _spannigTree = new SparseMatrixInt(_nV-1);		  
		  build(graph);
	  }
	  
	  private boolean addVertex(int iV) {
		  boolean addVertex = false;
		  if (_vertex2Label.get(iV) == -1) {
			  _vertex2Label.set(iV, _label);
			  _label2Vertex.set(_label, iV);
			  _label++;
			  addVertex = true;
		  }
		  return addVertex;
	  }
	  
	  private int minLabel(int v0, int v1) {
		  return _vertex2Label.get(v0) < _vertex2Label.get(v1) ? _vertex2Label.get(v0) : _vertex2Label.get(v1);
	  }

	  private int maxLabel(int v0, int v1) {
		  return _vertex2Label.get(v0) > _vertex2Label.get(v1) ? _vertex2Label.get(v0) : _vertex2Label.get(v1);
	  }

	  private void build(Graph graph) throws Exception {
		  PartitionUnionFind unionFind = new PartitionUnionFind(_nV);
		  int edgeIdx = 0;
		  int vertexIdx = 0;
		  VecInt vertexes = new VecInt(_nV);
		  vertexes.pushBack(0);
		  addVertex(0);
		  //System.out.println(_nV);
		  while (vertexIdx < vertexes.size() && unionFind.getNumberOfParts() > 1) {
			  //System.out.println("particiones: " + unionFind.getNumberOfParts() + " " + vertexIdx + " " + vertexes.size());
			  int v = vertexes.get(vertexIdx++);
			  VecInt vertexEdges = graph.getVertexEdges(v);
			  for (int j = 0; j < vertexEdges.size() && unionFind.getNumberOfParts() > 1; j++) {
				  int edge = vertexEdges.get(j);
				  int neighbor = graph.getNeighbor(v, edge);
				  if (neighbor >= 0 && unionFind.checkJoin(v, neighbor)) { // particiones distintas => agregar el eje al arbol generador
					  //System.out.println("ACA !!! " + vertexIdx);
						  if (addVertex(neighbor))  // vertice aun no visitado, agregar a la lista
							  vertexes.pushBack(neighbor);
						  _spannigTree.add(edgeIdx, minLabel(v,neighbor), -1);
						  _spannigTree.add(edgeIdx, maxLabel(v,neighbor), 1);
						  edgeIdx++;
						  _treeEdges.pushBack(edge);
				  }
			  }
		  }
		  //System.out.println(_spannigTree.getRows() + " " + _spannigTree.getCols());
	  }
	  
	  public SparseMatrixInt getTree () {
		  return _spannigTree;
	  }
	  
	  public int label2Vertex(int label) {
		  return _label2Vertex.get(label);
	  }
	  
	  public int vertex2Label(int iV) {
		  return _vertex2Label.get(iV);
	  }

	  public VecInt getTreeEdges() {
		  return _treeEdges;
	  }
  }
  //////////////////////////////////////////////////////////////////////
  public class SplittablePartition
    extends Partition implements SplittablePartition_h
  {

	  public SplittablePartition(int n) throws Exception {
		  super(n);
	  }
	  
	public void split(int i) {
		// TODO Auto-generated method stub
		
	}

  }



  //////////////////////////////////////////////////////////////////////
  /*	  
  0 1 2 -1 # F0
  3 1 0 -1 # F1
  2 1 3 -1 # F2
  2 3 0 -1 # F3
*/

/*
  public static void main(String[] args) {
	  VecInt coordIndex = new VecInt(20);
	  coordIndex.pushBack(0);
	  coordIndex.pushBack(1);
	  coordIndex.pushBack(2);
	  coordIndex.pushBack(-1);
	  coordIndex.pushBack(3);
	  coordIndex.pushBack(1);
	  coordIndex.pushBack(0);
	  coordIndex.pushBack(-1);
	  coordIndex.pushBack(2);
	  coordIndex.pushBack(1);
	  coordIndex.pushBack(3);
	  coordIndex.pushBack(-1);
	  coordIndex.pushBack(2);
	  coordIndex.pushBack(3);
	  coordIndex.pushBack(0);
	  coordIndex.pushBack(-1);

	  try {

		Faces f = new Faces(3, coordIndex);
		System.out.println("faces: " + f.getNumberOfFaces());
		System.out.println("getFaceFirstCorner(1): " + f.getFaceFirstCorner(1));
		int iC = f.getFaceFirstCorner(2);
		System.out.println("getFaceFirstCorner(2): " + iC);
		iC = f.getNextCorner(iC);
		System.out.println("getNextCorner: " + iC);
		iC = f.getNextCorner(iC);
		System.out.println("getNextCorner: " + iC);
		iC = f.getNextCorner(iC);
		System.out.println("getNextCorner: " + iC);
		iC = f.getNextCorner(iC);
		System.out.println("getNextCorner: " + iC);
		iC = f.getNextCorner(iC);
		System.out.println("getNextCorner: " + iC);

		System.out.println("vertices: " + f.getNumberOfVertices());
		System.out.println("faces: " + f.getNumberOfFaces());
		System.out.println("corners: " + f.getNumberOfCorners());
		System.out.println("face 2 size: " + f.getFaceSize(2));
		System.out.println("face 2 #corners: " + f.getNumberOfCorners(2));
		System.out.println("face 4 vertex 1: " + f.getFaceVertex(4, 1));
		
		System.out.println("getCornerFace(0): " + f.getCornerFace(0));
		System.out.println("getCornerFace(4): " + f.getCornerFace(4));
		System.out.println("getCornerFace(7): " + f.getCornerFace(7));
		System.out.println("getCornerFace(13): " + f.getCornerFace(13));
		System.out.println("getCornerFace(15): " + f.getCornerFace(15));
		System.out.println("getCornerFace(8): " + f.getCornerFace(8));
		
		System.out.println("getNextCorner(0): " + f.getNextCorner(0));
		System.out.println("getNextCorner(1): " + f.getNextCorner(1));
		System.out.println("getNextCorner(2): " + f.getNextCorner(2));
		System.out.println("getNextCorner(3): " + f.getNextCorner(3));
		System.out.println("getNextCorner(4): " + f.getNextCorner(4));
	  } catch (Exception e) {
		e.printStackTrace();
	  }
  }

*/

  public static class Faces implements Faces_h
  {
	  private int _numVertices;
	  protected VecInt _coordIndex;
	  private VecInt _facesIndex;
	  
	// throws exception if(nV<0), if(coordIndex==null),
	// and if an element iV of coordIndex is iV<-1 or iV>=nV
	public Faces(int nV, VecInt coordIndex) throws Exception {
		if (nV < 0)
			throw new Exception("nV < 0");
		if (coordIndex == null)
			throw new Exception("coordIndex == null");
		
		_numVertices = nV;
		_coordIndex = coordIndex;
		_facesIndex = new VecInt(128);
				
		int coordSize = _coordIndex.size();
		int idx = 0;
		while (idx < coordSize) {
			int coordValue = _coordIndex.get(idx);
			while (coordValue != -1) {				
				if (coordValue < -1 || coordValue > _numVertices)
					throw new Exception("coordIndex[" + idx + "] = " + coordValue + " out of range");
				coordValue = _coordIndex.get(++idx);
			}
			_facesIndex.pushBack(idx);
			idx++;
		}
	}
	
	public int getNumberOfVertices() {
		return _numVertices;
	}

	public int getNumberOfFaces() {
		return _facesIndex.size();
	}

	public int getNumberOfCorners() { // including the -1's
		return _coordIndex.size();
	}
	
	public int getNumberOfCorners(int iF) {
		// System.out.println("getNumberOfCorners iF: " + iF);
		int beginIdx = iF == 1 ? 0 : (_facesIndex.get(iF - 2) + 1);
		// System.out.println("getNumberOfCorners iF: " + iF + " beginIdx: " + beginIdx);
		int endIdx = _facesIndex.get(iF - 1);
		int size = endIdx - beginIdx;
		return size;
	}

	public int getFaceSize(int iF) throws Exception {
		if (iF > getNumberOfFaces())
			throw new Exception("Invalid face number");
		return getNumberOfCorners(iF);
	}

	public int getFaceFirstCorner(int iF) throws Exception {
		return getFaceVertex(iF, 1);
	}

	public int getFaceVertex(int iF, int j) throws Exception {
		if (iF > getNumberOfFaces())
			throw new Exception("Invalid face number");

		if (getNumberOfCorners(iF) < j)
			throw new Exception("Invalid face size");
		
		int beginIdx = iF == 1 ? 0 : (_facesIndex.get(iF - 2) + 1);		
		return beginIdx + j - 1;
	}

	public int getCornerFace(int iC) {
		int face = -1;
		int i = 0;		
		int facesSize = getNumberOfFaces();
		boolean found = false;
		while (i < facesSize && !found) {
			if (_facesIndex.get(i) > iC) {
				face = i + 1;
				found = true;
			} else if (_facesIndex.get(i) == iC)
				found = true;
			i++;
		}
		return face;
	}
	
	public int getNextCorner(int iC) throws Exception {
		int corner = -1;
		int face = getCornerFace(iC);
		// System.out.println("DBG face: " + face);
		if (face != -1) {
			int faceSize = getFaceSize(face);
			// System.out.println("DBG faceSize: " + faceSize);
			int firstCorner = getFaceFirstCorner(face);
			// System.out.println("DBG firstCorner: " + firstCorner);
			corner = ((iC - firstCorner) + 1) % faceSize + firstCorner;
		}
		return corner;
	}
	
  }


  //////////////////////////////////////////////////////////////////////
  /*
   N = 5
   1 - 2
   2 - 3
   3 - 4
   4 - 5
   1 - 5
   */

 /*
  public static void main(String[] args) {
	  Graph g = new Graph(5);	  
//	  System.out.println("insertEdge(0, 1): " + g.insertEdge(0, 1));
	  System.out.println("insertEdge(1, 2): " + g.insertEdge(1, 2));
//	  System.out.println("insertEdge(2, 3): " + g.insertEdge(2, 3));
	  System.out.println("insertEdge(3, 4): " + g.insertEdge(3, 4));
	  System.out.println("insertEdge(0, 4): " + g.insertEdge(0, 4));
	  	  
	  System.out.println("isConnected?: " + g.isConnected());
  }
*/
  
  public static class Graph implements Graph_h
  {
	  protected VecInt _edges;
	  protected VecInt[] _vertexEdges;
	  protected int _nV;
	  protected int _nE;
	  
	  public Graph() {
		  _nV = 0;
	  }
	  
	  public Graph(int N) {
		  _nV = N;
		  erase();
	  }

		public Graph clone() {
			Graph copy = new Graph(_nV);
			copy._nE = _nE;
			copy._edges = _edges.clone();
			copy._vertexEdges = new VecInt[_vertexEdges.length];
			for (int i = 0; i < _vertexEdges.length; i++) {
				if (_vertexEdges[i] != null) 
					copy._vertexEdges[i] = _vertexEdges[i].clone();
			}
			return copy;
		}

	public void erase() {
		_edges = new VecInt(_nV);  
		_vertexEdges = new VecInt[_nV];
		_nE = 0;
	}

	public int getNumberOfVertices() {
		return _nV;
	}

	public int getNumberOfEdges() {
		return _nE;
	}

	private boolean _inRange(int iV0, int iV1) {
		return (0 <= iV0 && iV1 < _nV && (iV1 - iV0) > 0);
	}
	
	public int getEdge(int iV0, int iV1) {
		if (iV0 > iV1) {
			int swap = iV0;
			iV0 = iV1;
			iV1 = swap;
		}

		int index = -1;				
		if (_inRange(iV0, iV1)) { 
			if (_vertexEdges[iV0] != null) {
				int i = 0;
				boolean found = false;
				while (i < _vertexEdges[iV0].size() && !found) {
					int idx = _vertexEdges[iV0].get(i);
					if (_edges.get(idx * 2 + 1) == iV1) {
						found = true;
						index = idx;
					}
					i++;
				}
			}
		}
		
		return index;
	}

	public int insertVertex() {
		VecInt[] aux = new VecInt[_nV+1];
		for (int i = 0; i < _nV; i++) {
			aux[i] = _vertexEdges[i];
		}
		_vertexEdges = aux;
		return _nV++;
	}
	
	public int insertEdge(int iV0, int iV1) {
		// System.out.println("insertEdge() iV0: " + iV0 + " iV1: " + iV1);
		if (iV0 > iV1) {
			int swap = iV0;
			iV0 = iV1;
			iV1 = swap;
		}

		if (getEdge(iV0, iV1) != -1 || !_inRange(iV0, iV1)) // si ya existe el eje o nodos fuera de rango, devolver -1 
			return -1;
			
		_edges.pushBack(iV0);
		_edges.pushBack(iV1);

		if (_vertexEdges[iV0] == null)
			_vertexEdges[iV0] = new VecInt(2);

		if (_vertexEdges[iV1] == null)
			_vertexEdges[iV1] = new VecInt(2);

		int index = _nE++;
		_vertexEdges[iV0].pushBack(index);
		_vertexEdges[iV1].pushBack(index);
		
		return index;
	}

	public int degree(int iV) {
		return (0 <= iV && iV < _nV) ? _vertexEdges[iV].size() : -1;
	}

	public int[] getDegrees() {
		int[] degrees = new int[_nV];
		for (int i = 0; i < _nV; i++) {
			degrees[i] = _vertexEdges[i].size();
		}
		return degrees;
	}
	
	public int getVertex0(int iE) {
		return (0 <= iE && iE < _nE) ? _edges.get(iE * 2) : -1;
	}

	public int getVertex1(int iE) {
		return (0 <= iE && iE < _nE) ? _edges.get(iE * 2 + 1) : -1;
	}

	public int getNeighbor(int iV, int iE) {
		int iV0 = getVertex0(iE);
		int iV1 = getVertex1(iE);
		return iV == iV0 ? 
				iV1 : 
					iV == iV1 ? 
							iV0 : 
								-1;
	}

	public VecInt getVertexEdges(int iV) {
		return _vertexEdges[iV];
	}

	public VecInt getEdges() {
		return _edges;
	}
	
	public int _vertexLeastEdges() {
		int iV = -1;
		int i = 0;
		boolean finished = false;
		while (i < _nV && !finished) {
			if (_vertexEdges[i] == null || _vertexEdges[i].size() == 0) {
				iV = -1;
				finished = true;
			} else if (iV == -1 || _vertexEdges[i].size() < _vertexEdges[iV].size()) {
				iV = i;
			}
			i++;
		}		
		return iV;
	}
	
	public boolean isConnected() {
		boolean isConnected = false;
		VecInt visited = new VecInt(_nV, 0);
		VecInt haveToVisit = new VecInt(_nV);		
		int firstVertex = _vertexLeastEdges();
		if (firstVertex > -1) {
			haveToVisit.pushBack(firstVertex);
			visited.set(firstVertex, 1);
			while (haveToVisit.size() > 0) {
				int currentVertex = haveToVisit.getPopBack();
				for (int i = 0; i < _vertexEdges[currentVertex].size(); i++) {
					int iE = _vertexEdges[currentVertex].get(i);
					int iV0 = getVertex0(iE);
					int iV1 = getVertex1(iE);
					if (visited.get(iV0) == 0) {
						haveToVisit.pushBack(iV0);
						visited.set(iV0, 1);
					}
					if (visited.get(iV1) == 0) {
						haveToVisit.pushBack(iV1);
						visited.set(iV1, 1);
					}
				}
			}
			int i = 0;
			boolean finished = false;
			while (i < _nV && !finished) {
				if (visited.get(i) == 0)
					finished = true;
				else
					i++;
			}
			if (i == _nV)
				isConnected = true;
		}
		return isConnected;
	}
	
	public SparseMatrixInt buildDirectedIncidenceMatrix() throws Exception {		
		SparseMatrixInt m = new SparseMatrixInt(_nE);
		for (int i = 0; i < _nE; i++) {
			int iV0 = getVertex0(i);
			int iV1 = getVertex1(i);
			m.set(i, iV0, -1);
			m.set(i, iV1, 1);
		}
		return m;
	}	

	public SparseMatrixInt buildLaplacianMatrix() throws Exception {
		SparseMatrixInt m = new SparseMatrixInt(_nV);
		for (int i = 0; i < _nV; i++) {
			VecInt neighbors = getVertexEdges(i);
			m.set(i, i, neighbors.size());
			for (int j = 0; j < neighbors.size(); j++) {
				m.set(i, getNeighbor(i, neighbors.get(j)), -1);
			}
		}
		return m;
	}	

	public void dump() {
		int size = _edges.size();
		int i = 0;
		while (i < size) {
			int iE = i / 2;
			int iV0 = _edges.get(i++);
			int iV1 = _edges.get(i++);
			System.out.println(iE + ": " + iV0 + " -> " + iV1);
		}
	}
	
	public static Graph buildCompleteGraph(int n) {
		Graph g = new Graph(n);
		for (int i = 0; i < n-1; i++) {
			for (int j = i+1; j < n; j++) {
				g.insertEdge(i, j);
			}
		}
		return g;
	}

	public static Graph buildCompleteGraphExceptEdges(int n, int[] except) {
		Graph g = new Graph(n);
		for (int i = 0; i < n-1; i++) {
			for (int j = i+1; j < n; j++) {
				boolean found = false;
				int k = 0; 
				while (k < except.length && !found) {
					if (except[k] == i && except[k+1] == j)
						found = true;
					k+=2;
				}
				if (!found)
					g.insertEdge(i, j);
			}
		}
		return g;
	}

	public static Graph buildCycleGraph(int n) {
		Graph g = new Graph(n);
		for (int i = 0; i < n; i++) {
			g.insertEdge(i, (i + 1) % n);
		}
		return g;
	}

	public static Graph buildCompleteBipartite(int n, int m) {
		Graph g = new Graph(n+m);
		for (int i = 0; i < n; i++) {
			for (int j = n; j < n + m; j++) {
				g.insertEdge(i, j);
			}
		}
		return g;
	}

	/*
	 * MSTCI <- 3DM reduction
	 * 
	 */
	public static Graph buildReduction(int q) {
		int idx = 0;
		int[][] nodes = new int[9][q];
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < q; j++) {
				nodes[i][j] = idx++;
			}
		}
		int root = idx++;
		Graph g = new Graph(idx);

		for (int i = 0; i < 3; i++) { //3 element sets 
			int base = 3*i;
			for (int j = 0; j < q; j++) {
				g.insertEdge(nodes[base][j], nodes[base+1][j]);
				g.insertEdge(nodes[base][j], nodes[base+2][j]);
			}
		}
		
		for (int i = 0; i < q; i++) { //complete bipartite
			for (int j = 0; j < q; j++) {
				g.insertEdge(nodes[2][i], nodes[4][j]);
				g.insertEdge(nodes[5][i], nodes[7][j]);
				g.insertEdge(nodes[8][i], nodes[1][j]);
			}
		}
		
		for (int i = 0; i < q; i++) {
			g.insertEdge(root, nodes[0][i]);
		}
		
		return g;
	}

  }

	public static class EdgeRelabelGraph {
		private Graph _graph;
		private VecInt _edgeLabels; 
		private VecInt _reverseLabels;
		private int _maxRelabel;
		
		public EdgeRelabelGraph(Graph g) {
			this(g, g.getNumberOfEdges());
		}
		
		public EdgeRelabelGraph(Graph g, int size) {
			_graph = g;
			_edgeLabels = new VecInt(size, -1);
			_reverseLabels = new VecInt(size, -1);
			relabelEdges();
		}

		public void setMapping(VecInt edgeLabels, VecInt reverseLabels) {
			_edgeLabels = edgeLabels;
			_reverseLabels = reverseLabels;
		}
		
		public void addExtraEdge(int graphIdx) {
			setEdgeLabels(graphIdx, ++_maxRelabel);
		}
		
		private void setEdgeLabels(int graphIdx, int relabelIdx) {
			if (relabelIdx > _maxRelabel) {
				_maxRelabel = relabelIdx;
			}
			_edgeLabels.set(relabelIdx, graphIdx);
			//System.out.println("graphIdx: " + graphIdx + " relabelIdx: " + relabelIdx);
			_reverseLabels.set(graphIdx, relabelIdx);
		}
		
		public int getLabel(int graphIdx) {
			return _reverseLabels.get(graphIdx);
		}

		public int getGraphIdx(int relabelIdx) {
			return _edgeLabels.get(relabelIdx);
		}
		
		public Graph getGraph() {
			return _graph;
		}

		//default spanning tree: BFS from vertex 0
		protected void relabelEdges() {
			VecBool visitedEdges = new VecBool(_graph.getNumberOfEdges(), false);
			int treeEdgeIdx = 0; 
			int loopEdgeIdx = _graph.getNumberOfVertices() - 1; 
			VecBool visitedNodes = new VecBool(_graph.getNumberOfVertices(), false);
			VecInt queue = new VecInt(_graph.getNumberOfVertices());
			queue.pushBack(0);
			int currentNodeIdx = 0;
			while (currentNodeIdx < _graph.getNumberOfVertices()) {
				int iV = queue.get(currentNodeIdx);
				if (!visitedNodes.get(iV)) {
					visitedNodes.set(iV, true);
					VecInt vertexEdges = _graph.getVertexEdges(iV);
					for (int i = 0; i < vertexEdges.size(); i++) {
						int iE = vertexEdges.get(i);
						if (!visitedEdges.get(iE)) {
							visitedEdges.set(iE,true);
							int neighbor = _graph.getNeighbor(iV, iE);
							//System.out.print(iV + " -> " + neighbor + " (" + iE + ") ");
							if (!visitedNodes.get(neighbor) && !queue.contains(neighbor)) {
								//System.out.println(" SI!");
								queue.pushBack(neighbor);
								setEdgeLabels(iE, treeEdgeIdx++);
							} else {
								//System.out.println(" NO!");
								setEdgeLabels(iE, loopEdgeIdx++);
							}
						}
					}
				}
				currentNodeIdx++;
			}
		}

		public void dump() {
			for (int i = 0; i < _edgeLabels.size(); i++) {
				int iE = _edgeLabels.get(i);
				System.out.println(iE + ": " + _graph.getVertex0(iE) + " -> " + _graph.getVertex1(iE));
			}
			_reverseLabels.dump();
		}
	}

  //////////////////////////////////////////////////////////////////////
  /*	  
  point [
          1.633 -0.943 -0.667 # V0
          0.000  0.000  2.000 # V1
         -1.633 -0.943 -0.667 # V2
          0.000  1.886 -0.667 # V3
          0.000  1.886 -0.667 # V4
  ]
}
coordIndex [
  0 1 2 -1 # F0
  3 4 2 -1 # F1
]
*/
/*
  public static void main(String[] args) {
	  VecFloat coord = new VecFloat(16);
	  coord.pushBack(1.633f);
	  coord.pushBack(-0.943f);
	  coord.pushBack(-0.667f);
	  coord.pushBack(0.000f);
	  coord.pushBack(0.000f);
	  coord.pushBack(2.000f);
	  coord.pushBack(-1.633f);
	  coord.pushBack(-0.943f);
	  coord.pushBack(-0.667f);
	  coord.pushBack(0.000f);
	  coord.pushBack(1.886f);
	  coord.pushBack(-0.667f);
	  coord.pushBack(0.000f);
	  coord.pushBack(1.886f);
	  coord.pushBack(-0.667f);
	   
	  VecInt coordIndex = new VecInt(10);
	  coordIndex.pushBack(0);
	  coordIndex.pushBack(1);
	  coordIndex.pushBack(2);
	  coordIndex.pushBack(-1);
	  coordIndex.pushBack(3);
	  coordIndex.pushBack(4);
	  coordIndex.pushBack(2);
	  coordIndex.pushBack(-1);

	  try {
		PolygonMesh pm = new PolygonMesh(coord, coordIndex);
		for (int i = 0; i < 2; i++) {
			System.out.println("edgeFaces edge: " + i + " #edgeFaces: " + pm.getNumberOfEdgeFaces(i));
		}
		System.out.println("isRegular?: " + pm.isRegular());
		System.out.println("hasBoundary?: " + pm.hasBoundary());
		
	 } catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
  }
*/
  /*	  
       point [
          1.633 -0.943 -0.667 # V0
          0.000  0.000  2.000 # V1
         -1.633 -0.943 -0.667 # V2
          0.000  1.886 -0.667 # V3
       ]
     }
     coordIndex [
       0 1 2 -1 # F0
       3 1 0 -1 # F1
       2 1 3 -1 # F2
       2 3 0 -1 # F3
     ]
  */
/*
  public static void main(String[] args) {
	  VecFloat coord = new VecFloat(16);
	  coord.pushBack(1.633f);
	  coord.pushBack(-0.943f);
	  coord.pushBack(-0.667f);
	  coord.pushBack(0.000f);
	  coord.pushBack(0.000f);
	  coord.pushBack(2.000f);
	  coord.pushBack(-1.633f);
	  coord.pushBack(-0.943f);
	  coord.pushBack(-0.667f);
	  coord.pushBack(0.000f);
	  coord.pushBack(1.886f);
	  coord.pushBack(-0.667f);
	   
	  VecInt coordIndex = new VecInt(20);
	  coordIndex.pushBack(0);
	  coordIndex.pushBack(1);
	  coordIndex.pushBack(2);
	  coordIndex.pushBack(-1);
	  coordIndex.pushBack(3);
	  coordIndex.pushBack(1);
	  coordIndex.pushBack(0);
	  coordIndex.pushBack(-1);
	  coordIndex.pushBack(2);
	  coordIndex.pushBack(1);
	  coordIndex.pushBack(3);
	  coordIndex.pushBack(-1);
	  coordIndex.pushBack(2);
	  coordIndex.pushBack(3);
	  coordIndex.pushBack(0);
	  coordIndex.pushBack(-1);

	  try {
		PolygonMesh pm = new PolygonMesh(coord, coordIndex);
		for (int i = 0; i < pm.getNumberOfFaces(); i++) {
			//pm.getFaceEdges(i+1).dump();
			pm.getFaceNeighbors(i+1).dump();
		}
		
//		for (int i = 0; i < 6; i++) {
//			System.out.println("edgeFaces edge: " + i + " #edgeFaces: " + pm.getNumberOfEdgeFaces(i));
//		}
//		System.out.println("isRegular?: " + pm.isRegular());
//		System.out.println("hasBoundary?: " + pm.hasBoundary());
		
	 } catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
  }
*/  
  public static class PolygonMesh
    extends Faces implements PolygonMesh_h
  {
	  protected int _nV;
	  protected Graph _graph;
	  protected VecFloat _coord;
	  private Vector<VecInt> _edgeFaces; // le asocia a cada eje sus Faces incidentes
	  private static final int BOUNDARY_TYPE = 1;
	  private static final int SINGULAR_TYPE = 2;
	  private Boolean _isRegular;
	  private Boolean _hasBoundary;
	  
	  public PolygonMesh(VecFloat coord, VecInt  coordIndex) throws Exception {
		super(coord.size()/3, coordIndex);
		_nV = coord.size()/3;
		_coord = coord;
		_edgeFaces = new Vector<VecInt>(); ;
		_buildGraph();
	  }
	  
	private void _buildGraph() throws Exception {
		// System.out.println("_buildGraph() begin... _nV: " + _nV);
		_graph = new Graph(_nV);
		// System.out.println("_buildGraph() num faces: " + getNumberOfFaces());
		for (int i = 0; i < getNumberOfFaces(); i++) {
			int faceNum = i+1;
			int firstCorner = getFaceFirstCorner(faceNum);
			int currentCorner = firstCorner;
			int nextCorner = getNextCorner(currentCorner);
			while (nextCorner != firstCorner) {
				// System.out.println("_buildGraph() insertEdge face: " + i + " currentCorner: " + currentCorner + " nextCorner: " + nextCorner);
				_insertEdge(faceNum, currentCorner, nextCorner);
				currentCorner = nextCorner;
				nextCorner = getNextCorner(currentCorner);
			}
			// System.out.println("_buildGraph() insertEdge face: " + i + " firstCorner: " + firstCorner + " currentCorner: " + currentCorner);
			_insertEdge(faceNum, firstCorner, currentCorner); //agregar el eje del primero al ultimo nodo de la Face
		}
		// _graph.dump();
	}
	
	public Graph getGraph() {
		return _graph;
	}
	
	private void _insertEdge(int iF, int iC0, int iC1) {
		int iV0 = _coordIndex.get(iC0);
		int iV1 = _coordIndex.get(iC1);
		//System.out.println("iV1: " + iV1 + " iC1: " + iC1);
		int edge = _graph.insertEdge(iV0, iV1);
		// System.out.println("_insertEdge() iF: " + iF + " iV0: " + iV0 + " iV1:" + iV1 + " edge: " + edge);
		if (edge == -1) {
			edge = _graph.getEdge(iV0, iV1);
		}		
		if (edge == _edgeFaces.size()) {
			_edgeFaces.add(edge, new VecInt(1));
		}
		_edgeFaces.get(edge).pushBack(iF); 
	}
		
	public float getVertexCoord(int iV, int j) throws Exception {		
		return _coord.get(iV * 3 + j);
	}

	public float getCornerCoord(int iC, int j) throws Exception {
		int iV = _coordIndex.get(iC);
		return getVertexCoord(iV, j);
	}

	public int getVertex(int iC) {
		return _coordIndex.get(iC);
	}
	
	public int getEdge(int iC) {		
		try {
			int iV0 = _coordIndex.get(iC);
			int iV1 = _coordIndex.get(getNextCorner(iC));
			return _graph.getEdge(iV0,iV1);
		} catch (ArrayIndexOutOfBoundsException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	public int getEdge(int iV0, int iV1) {
		return _graph.getEdge(iV0, iV1);
	}

	public int getVertex0(int iE) {
		return _graph.getVertex0(iE);
	}

	public int getVertex1(int iE) {
		return _graph.getVertex1(iE);
	}

	public int getNumberOfEdgeFaces(int iE) {		
		return _edgeFaces.get(iE).size();
	}

	public int getEdgeFace(int iE, int j) {
		return _edgeFaces.get(iE-1).get(j);
	}

	public boolean isRegular() {
		if (_isRegular == null) {
			_isRegular = true;
			int i = 0;
			boolean finished = false;
			while (i < _edgeFaces.size() && !finished) {
				try {
					if (!isRegularEdge(i)) {
						_isRegular = false;
						finished = true;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				i++;
			}

			if (_isRegular) { 
				i = 0;
				finished = false;
				while (i < _nV && !finished) {
					try {
						if (!isRegularVertex(i)) {
							_isRegular = false;
							finished = true;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					i++;
				}				
			}
		}
		return _isRegular.booleanValue();
	}

	public boolean hasBoundary() {
		if (_hasBoundary == null) {
			_hasBoundary = false;
			int i = 0;
			boolean finished = false;
			while (i < _edgeFaces.size() && !finished) {
				try {
					if (isBoundaryEdge(i)) {
						_hasBoundary = true;
						finished = true;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				i++;
			}
		}
		return _hasBoundary.booleanValue();
	}

	public boolean isBoundaryVertex(int iV) throws Exception {
		return (getVertexType(iV) & BOUNDARY_TYPE) == BOUNDARY_TYPE;
	}

	public boolean isRegularVertex(int iV) throws Exception {
		return !isSingularVertex(iV);
	}

	public boolean isSingularVertex(int iV) throws Exception {
		return (getVertexType(iV) & SINGULAR_TYPE) == SINGULAR_TYPE;
	}

	public int getVertexType(int iV) throws Exception {
		int vertexType = 0;
		VecInt vertexEdges = _graph.getVertexEdges(iV);
		for (int i = 0; i < vertexEdges.size(); i++) {
			if ((vertexType & BOUNDARY_TYPE) == 0 && isBoundaryEdge(i))
				vertexType |= BOUNDARY_TYPE; 
			
			if ((vertexType & SINGULAR_TYPE) == 0 && isSingularEdge(i))
				vertexType |= SINGULAR_TYPE; 				
		}
		
		if ((vertexType & SINGULAR_TYPE) == 0) 		
			vertexType |= !_vertexDualSubgraph(iV).isConnected() ? SINGULAR_TYPE : 0;

		return vertexType;
	}
	
	public Graph _vertexDualSubgraph(int iV) throws Exception {		
		VecInt vertexEdges = _graph.getVertexEdges(iV);
		Map<Integer, Integer> faceToVertex = new HashMap<Integer, Integer>();
		int vertexIndex = 0;
		for (int i = 0; i < vertexEdges.size(); i++) {
			VecInt edgeFaces = _edgeFaces.get(vertexEdges.get(i));
			for (int j = 0; j < edgeFaces.size(); j++) {
				int iF = edgeFaces.get(j);
				if (!faceToVertex.containsKey(iF)) {
					faceToVertex.put(iF, vertexIndex++);
				}
			}
		}

		Graph dualGraph = new Graph(faceToVertex.size());
		for (int i = 0; i < vertexEdges.size(); i++) {
			int iE = vertexEdges.get(i);
			if (isRegularEdge(iE)) {
				VecInt edgeFaces = _edgeFaces.get(iE);
				dualGraph.insertEdge(faceToVertex.get(edgeFaces.get(0)),
										faceToVertex.get(edgeFaces.get(1)));
			}
		}
		
		
		return dualGraph;
	}
	
	public boolean isBoundaryEdge(int iE) throws Exception {
		return _edgeFaces.get(iE).size() == 1;
	}

	public boolean isRegularEdge(int iE) throws Exception {
		return _edgeFaces.get(iE).size() == 2;
	}

	public boolean isSingularEdge(int iE) throws Exception {
		return _edgeFaces.get(iE).size() > 2;
	}
	
	public VecInt getEdgeFaces(int iE) {
		return _edgeFaces.get(iE);
	}
	
	public VecInt getFaceEdges(int iF) throws Exception {
		VecInt edges = new VecInt(3);
		int totalEdges = getFaceSize(iF);
		int iC = getFaceFirstCorner(iF);
		for (int i = 0; i < totalEdges; i++) {
			edges.pushBack(getEdge(iC));
			iC = getNextCorner(iC);
		}
		return edges;
	}
	public VecInt getFaceNeighbors(int iF) throws Exception {
		VecInt neighbors = new VecInt(3);
		VecInt faceEdges = getFaceEdges(iF); 
		for (int i = 0; i < faceEdges.size(); i++) {
			int edge = faceEdges.get(i);
			for (int j = 0; j < _edgeFaces.get(edge).size(); j++) {
				int face = _edgeFaces.get(edge).get(j);
				if (face != iF) {
					neighbors.pushBack(face);
				}
			}
		}
		return neighbors;
	}
  }

/*
    1 0 0
    0 1 0
    0 0 1
*/
  
 /* 
  public static void main(String[] args) {
	  int dim = 3;
	 SparseMatrix m = new SparseMatrix(dim);
	 m.set(0, 0, 3);
	 m.set(0, 1, 1);
	 m.set(1, 1, 3);
	 m.set(2, 2, 3);
	 VecFloat v = new VecFloat(3);
	 v.pushBack(1);
	 v.pushBack(2);
	 v.pushBack(3);
	 VecFloat v2 = m.multiplyByVector(v);
	 v2.dump();
	 
	 m.add(0, 0, 1);
	 m.add(1, 1, 1);
	 m.add(2, 2, 1);
	 m.add(0, 2, 1);
	 
	 for (int i = 0; i < dim; i++) {
		 for (int j = 0; j < dim; j++) {
			 System.out.println("m[" + i + "," +  j + "] = " + m.get(i, j));
		 }		
	}
  }
*/  
  public static class SparseMatrix
  {
	  int _rows;
	  int _cols;
	  private VecInt[] _colIndices;
	  private VecFloat[] _values;
	  
	  public SparseMatrix(int rows) {
		  _rows = rows;
		  _init();
	  }

	  public int getRows() {
		  return _rows;
	  }

	  public int getCols() {
		  return _cols + 1;
	  }
	  
	public SparseMatrix transpose() {
		SparseMatrix transp = new SparseMatrix(getCols());
		for (int i = 0; i < _colIndices.length; i++) {
			for (int j = 0; j < _colIndices[i].size(); j++) {
			  transp.set(_colIndices[i].get(j), i, _values[i].get(j));
			}
		}
		return transp;
	}
		
	  private void _init() {
		  _colIndices = new VecInt[_rows];
		  _values = new VecFloat[_rows];
	  }
	  
	  public void set(int row, int col, float value) {
		  if (row >= 0 && row < _colIndices.length) {
			  if (_colIndices[row] == null) {
				  _colIndices[row] = new VecInt(1);
				  _values[row] = new VecFloat(1);
			  }
			  
			  // System.out.println("set(...) row: " + row + " col: " + col + " value: " + value);
			  _colIndices[row].pushBack(col);
			  _values[row].pushBack(value);
			  if (_cols < col)
				  _cols = col;
		  }
	  }
	  
	  public float get(int row, int col) {
		  float value = 0;
		  // System.out.println("get(...) row: " + row + " col: " + col);
		  if (_colIndices[row] != null) {
			  // System.out.println("get(...) not null!!! _colIndices[row]: " + _colIndices[row].size());
			  int colIndex = -1;
			  // _colIndices[row].dump();
			  for (int i = 0; i < _colIndices[row].size() && colIndex < 0; i++) {
				  // System.out.println("get(...) _colIndices[row].get(i): " + _colIndices[row].get(i));
				  if (_colIndices[row].get(i) == col)
					  colIndex = i;
			  }
			  if (colIndex >= 0 )
				  value = _values[row].get(colIndex);
		  }
		  return value;
	  }
	  
	  public void add(int row, int col, float value) {
		  if (value > 0.0000001f || value < -0.0000001f) {
			  if (_colIndices[row] != null) {			  
				  float previousValue = 0;
				  int colIndex = -1;
				  for (int i = 0; i < _colIndices[row].size() && colIndex < 0; i++) {
					  if (_colIndices[row].get(i) == col)
						  colIndex = i;
				  }
				  if (colIndex >= 0 ) {
					  previousValue = _values[row].get(colIndex);
					  _values[row].set(colIndex, previousValue + value);
				  } else {
					  set(row, col, value);
				  }
			  } else {
				  set(row, col, value);
			  }
		  }
	  }
	  
	  public VecFloat multiplyByVector(VecFloat v) {
		  VecFloat resultVec = new VecFloat(_rows, 0);		  
		  for (int i = 0; i < _colIndices.length; i++) {
			  float value = 0;
			  for (int j = 0; j < _colIndices[i].size(); j++) {
				  value += _values[i].get(j) * v.get(_colIndices[i].get(j));
			  }
			  resultVec.set(i, value);
		  }
		  return resultVec;
	  }

	  public VecFloat multiplyByVectorAndScalar(VecFloat v, float scalar) {
		  VecFloat resultVec = new VecFloat(_rows, 0);		  
		  for (int i = 0; i < _colIndices.length; i++) {
			  float value = 0;
			  if (_colIndices[i] != null) {
				  for (int j = 0; j < _colIndices[i].size(); j++) {
					  value += _values[i].get(j) * v.get(_colIndices[i].get(j));
				  }
			  }
			  resultVec.set(i, scalar * value);
		  }
		  return resultVec;
	  }

	  public void resize(int rows) {
		  VecInt[] newColIndices = new VecInt[rows];
		  VecFloat[] newValues = new VecFloat[rows];
		  //System.out.println("ACA " + _colIndices.length);
		  for (int i = 0; i < _colIndices.length; i++) {
			  //System.out.print(i + " ");
			  int rowSize = _colIndices[i].size();
			  newColIndices[i] = new VecInt(rowSize,-1);
			  newValues[i] = new VecFloat(rowSize,-1);
			  for (int j = 0; j < rowSize; j++) {
				  newColIndices[i].set(j, _colIndices[i].get(j));
				  newValues[i].set(j, _values[i].get(j));
			  }
		  }
		  _colIndices = newColIndices;
		  _values = newValues;
		  _rows = rows;
	  }
	  
	  public void fullDump() {
		  for (int i = 0; i < _rows; i++) {
			  int currentCol = 0;
			  StringBuffer row = new StringBuffer();
			  if (_colIndices[i] != null) {
				  for (int j = 0; j < _colIndices[i].size(); j++) {
					  while (currentCol++ < _colIndices[i].get(j)) {
						  row.append("\t0");						  
					  }
					  row.append("\t" + _values[i].get(j));
				  } 
			  }
			  for (int j = currentCol -1; j < _cols; j++) {
				  row.append("\t0");
			  }
			  System.out.println(row);
		  }
	  }
	  
	  public void dump() {
		  for (int i = 0; i < _rows; i++) {
			  if (_colIndices[i] != null) {
				  for (int j = 0; j < _colIndices[i].size(); j++) {
					  System.out.println("[" + i + "," + _colIndices[i].get(j) + "]: " + _values[i].get(j));
				  }
			  }
		  }
	  }
	  
	  public void dump(String tag) {
		  for (int i = 0; i < _rows; i++) {
			  if (_colIndices[i] != null) {
				  for (int j = 0; j < _colIndices[i].size(); j++) {
					  System.out.println(tag + " [" + i + "," + _colIndices[i].get(j) + "]: " + _values[i].get(j));
				  }
			  }
		  }
	  }

  }


  public static class SparseMatrixInt
  {
	  int _rows;
	  int _cols;
	  private VecInt[] _colIndices;
	  private VecInt[] _values;
	  
	  public SparseMatrixInt(int rows) {
		  _rows = rows;
		  _init();
	  }

	  public int getRows() {
		  return _rows;
	  }

	  public int getCols() {
		  return _cols + 1;
	  }
	  
	public SparseMatrixInt transpose() {
		SparseMatrixInt transp = new SparseMatrixInt(getCols());
		for (int i = 0; i < _colIndices.length; i++) {
			for (int j = 0; j < _colIndices[i].size(); j++) {
			  transp.set(_colIndices[i].get(j), i, _values[i].get(j));
			}
		}
		return transp;
	}

	public SparseMatrix toFloat() {
		SparseMatrix floatCopy = new SparseMatrix(_rows);
		for (int i = 0; i < _rows; i++) {
		  if (_colIndices[i] != null) {
			  for (int j = 0; j < _colIndices[i].size(); j++) {
				  floatCopy.set(i, _colIndices[i].get(j),(float)_values[i].get(j));
			  }
		  }
		}
		return floatCopy;
	}
		
	  private void _init() {
		  _colIndices = new VecInt[_rows];
		  _values = new VecInt[_rows];
	  }
	  
	public void set(int row, int col, int value) {
		if (row >= 0 && row < _colIndices.length) {
			//System.out.println("set(...) row: " + row + " col: " + col + " value: " + value);
			if (_colIndices[row] == null) {
			  _colIndices[row] = new VecInt(1);
			  _values[row] = new VecInt(1);
			}
			// System.out.println("set(...) row: " + row + " col: " + col + " value: " + value);
			int colIndex = -1;
			// _colIndices[row].dump();
			for (int i = 0; i < _colIndices[row].size() && colIndex < 0; i++) {
			  // System.out.println("get(...) _colIndices[row].get(i): " + _colIndices[row].get(i));
			  if (_colIndices[row].get(i) == col)
				  colIndex = i;
			}
			if (colIndex >= 0 )
				_values[row].set(colIndex,value);
			else {
				_colIndices[row].pushBack(col);
				_values[row].pushBack(value);
			}
			if (_cols < col)
			  _cols = col;
			  
	   }
	}
	  
	  public int get(int row, int col) {
		  int value = 0;
		  // System.out.println("get(...) row: " + row + " col: " + col);
		  if (_colIndices[row] != null) {
			  // System.out.println("get(...) not null!!! _colIndices[row]: " + _colIndices[row].size());
			  int colIndex = -1;
			  // _colIndices[row].dump();
			  for (int i = 0; i < _colIndices[row].size() && colIndex < 0; i++) {
				  // System.out.println("get(...) _colIndices[row].get(i): " + _colIndices[row].get(i));
				  if (_colIndices[row].get(i) == col)
					  colIndex = i;
			  }
			  if (colIndex >= 0 )
				  value = _values[row].get(colIndex);
		  }
		  return value;
	  }

	public void jacobiR() { //Jacobi A = D + R => x(k+1) = D^-1 (b - Rx(k))
		  for (int i = 0; i < _rows; i++) {
			  if (_colIndices[i] != null) {
				  _values[i].invert();
				  set(i, i, 0);
			  }
		  }
	}

	public void invertRow(int row) {
		if (_values[row] != null) {
			_values[row].invert();
		}
	}

	public void invert(int row, int col) {
		int value = 0;
		// System.out.println("get(...) row: " + row + " col: " + col);
		if (_colIndices[row] != null) {
			// System.out.println("get(...) not null!!! _colIndices[row]: " + _colIndices[row].size());
			int colIndex = -1;
			// _colIndices[row].dump();
			for (int i = 0; i < _colIndices[row].size() && colIndex < 0; i++) {
				// System.out.println("get(...) _colIndices[row].get(i): " + _colIndices[row].get(i));
				if (_colIndices[row].get(i) == col)
				  colIndex = i;
			}
			if (colIndex >= 0 ) {
				value = _values[row].get(colIndex);
				_values[row].set(colIndex, -value);
			}
		}
	}
	
	public SparseMatrixInt multiply(SparseMatrixInt B) {
		SparseMatrixInt C = new SparseMatrixInt(_rows);
		for (int i = 0; i < _rows; i++) {
			VecInt rowA = _colIndices[i];
			//rowA.dump();
			for (int j = 0; j < B.getCols(); j++) {
				int suma = 0;
				for (int k = 0; k < rowA.size(); k++) {
					//System.out.println("ACA! rowA.get(k): " + rowA.get(k) + " B.get(k, j): " + B.get(k, j));
					int col = rowA.get(k);
					suma += get(i,col) * B.get(col, j);
				}
				C.set(i, j, suma);
			}
		}
		return C;
	}
	
	  public void add(int row, int col, int value) {
		  if (value != 0) {
			  if (_colIndices[row] != null) {			  
				  int previousValue = 0;
				  int colIndex = -1;
				  for (int i = 0; i < _colIndices[row].size() && colIndex < 0; i++) {
					  if (_colIndices[row].get(i) == col)
						  colIndex = i;
				  }
				  if (colIndex >= 0 ) {
					  previousValue = _values[row].get(colIndex);
					  _values[row].set(colIndex, previousValue + value);
				  } else {
					  set(row, col, value);
				  }
			  } else {
				  set(row, col, value);
			  }
		  }
	  }

	public int multiplyRowByVector(int row, VecInt v) {
		int value = 0;
		for (int j = 0; j < _colIndices[row].size(); j++) {
			value += _values[row].get(j) * v.get(_colIndices[row].get(j));
		}
		return value;
	}
	  
	  public VecInt multiplyByVector(VecInt v) {
		  VecInt resultVec = new VecInt(_rows, 0);
		  for (int i = 0; i < _colIndices.length; i++) {
			  int value = 0;
			  for (int j = 0; j < _colIndices[i].size(); j++) {
				  value += _values[i].get(j) * v.get(_colIndices[i].get(j));
			  }
			  resultVec.set(i, value);
		  }
		  return resultVec;
	  }

	public VecFloat multiplyByVector(VecFloat v) {
		VecFloat resultVec = new VecFloat(_rows, 0);
		for (int i = 0; i < _colIndices.length; i++) {
			float value = 0;
			for (int j = 0; j < _colIndices[i].size(); j++) {
				value += (float)_values[i].get(j) * v.get(_colIndices[i].get(j));
			}
			resultVec.set(i, value);
		}
		return resultVec;
	}


	  public VecInt multiplyByVectorAndScalar(VecInt v, int scalar) {
		  VecInt resultVec = new VecInt(_rows, 0);		  
		  for (int i = 0; i < _colIndices.length; i++) {
			  int value = 0;
			  if (_colIndices[i] != null) {
				  for (int j = 0; j < _colIndices[i].size(); j++) {
					  value += _values[i].get(j) * v.get(_colIndices[i].get(j));
				  }
			  }
			  resultVec.set(i, scalar * value);
		  }
		  return resultVec;
	  }

	  public void resize(int rows) {
		  VecInt[] newColIndices = new VecInt[rows];
		  VecInt[] newValues = new VecInt[rows];
		  for (int i = 0; i < _colIndices.length; i++) {
			  int rowSize = _colIndices[i].size();
			  newColIndices[i] = new VecInt(rowSize,-1);
			  newValues[i] = new VecInt(rowSize,-1);
			  for (int j = 0; j < rowSize; j++) {
				  newColIndices[i].set(j, _colIndices[i].get(j));
				  newValues[i].set(j, _values[i].get(j));
			  }
		  }
		  _colIndices = newColIndices;
		  _values = newValues;
		  _rows = rows;
	  }
	  
	  public SparseMatrixInt[] splitByRow(int row) {
		  SparseMatrixInt[] matrices = new SparseMatrixInt[2];
		  if (row < _rows) {			  
			  matrices[0] = new SparseMatrixInt(row);
			  matrices[0]._cols = _cols;
			  for (int i = 0; i < row; i++) {
				  matrices[0]._colIndices[i] = _colIndices[i];
				  matrices[0]._values[i] = _values[i];
			  } 	
			  matrices[1] = new SparseMatrixInt(_rows - row);
			  matrices[1]._cols = _cols;
			  for (int i = row; i < _rows; i++) {
				  matrices[1]._colIndices[i - row] = _colIndices[i];
				  matrices[1]._values[i - row] = _values[i];
			  } 				  
		  }
		
		  return matrices;
	  }

	  public SparseMatrixInt dropFirstColumn() {
		  SparseMatrixInt m = new SparseMatrixInt(_rows);
		  m._cols = _cols - 1;
		  for (int i = 0; i < _rows; i++) {
			  int cols = _colIndices[i].size();			  		
			  m._colIndices[i] = new VecInt(cols-1);
			  m._values[i] = new VecInt(cols-1);
			  for (int j = 0; j < _colIndices[i].size(); j++) {
				  if (_colIndices[i].get(j) != 0) {
					  m._colIndices[i].pushBack(_colIndices[i].get(j)-1);
					  m._values[i].pushBack(_values[i].get(j));
				  }
			}
			  
		  } 			  
		  return m;
	  }
	  
	  public void fullDump() {
		  for (int i = 0; i < _rows; i++) {
			  StringBuffer row = new StringBuffer();
			  for (int j = 0; j < getCols(); j++) {
				  row.append(String.format("%1$5d", get(i,j)));
			  }
			  System.out.println(row);
		  }
		  //String sep = "\t";
		  //String sep = "   ";
//		  for (int i = 0; i < _rows; i++) {
//			  int currentCol = 0;
//			  StringBuffer row = new StringBuffer();
//			  if (_colIndices[i] != null) {
//				  _colIndices[i].dump();
//				  _values[i].dump();
//				  for (int j = 0; j < _colIndices[i].size(); j++) {
//					  while (currentCol++ < _colIndices[i].get(j)) {
//						  row.append(String.format("%1$5d", 0));						  
//					  }
//					  row.append(String.format("%1$5d", _values[i].get(j)));
//				  } 
//			  }
//			  for (int j = currentCol -1; j < _cols; j++) {
//				  row.append(String.format("%1$5d", 0));
//			  }
//			  System.out.println(row);
//		  }
	  }
	  
	  private String rowToString(int row) {
		  StringBuffer rowStr = new StringBuffer();
		  int currentCol = 0;			  
		  if (_colIndices[row] != null) {
			  for (int j = 0; j < _colIndices[row].size(); j++) {
				  while (currentCol++ < _colIndices[row].get(j)) {					  
					  rowStr.append((rowStr.length() > 0 ? "," : "") + 
							  				String.format("%1$5d", 0));
				  }
				  rowStr.append((rowStr.length() > 0 ? "," : "") + 
						  			String.format("%1$5d", _values[row].get(j)));
			  } 
		  }
		  for (int j = currentCol-1; j < _cols; j++) {
			  rowStr.append((rowStr.length() > 0 ? "," : "") +
					  			String.format("%1$5d", 0));
		  }
		  return rowStr.toString();
	  }
	  
	  public void toOctave() {
		  StringBuffer matStr = new StringBuffer();
		  for (int i = 0; i < _rows; i++) {
			  matStr.append((matStr.length() > 0 ? ";" : "") + 
					  			rowToString(i));
			  matStr.append("\n");
		  }
		  System.out.println("[" + matStr + "]");
	  }

	  public void dump() {
		  for (int i = 0; i < _rows; i++) {
			  if (_colIndices[i] != null) {
				  for (int j = 0; j < _colIndices[i].size(); j++) {
					  System.out.println("[" + i + "," + _colIndices[i].get(j) + "]: " + _values[i].get(j));
				  }
			  }
		  }
	  }
	  
	  public void dump(String tag) {
		  for (int i = 0; i < _rows; i++) {
			  if (_colIndices[i] != null) {
				  for (int j = 0; j < _colIndices[i].size(); j++) {
					  System.out.println(tag + " [" + i + "," + _colIndices[i].get(j) + "]: " + _values[i].get(j));
				  }
			  }
		  }
	  }
	  
	public SparseMatrixInt clone() {
		SparseMatrixInt copy = new SparseMatrixInt(_rows);
		for (int i = 0; i < _rows; i++) {
			if (_colIndices[i] != null) {
				copy._colIndices[i] = _colIndices[i].clone();
				copy._values[i] = _values[i].clone();
			}
		}
		copy._cols = _cols;
		return copy;  
	}

  }
  
}
