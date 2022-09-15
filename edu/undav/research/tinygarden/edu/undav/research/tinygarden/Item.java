package edu.undav.research.tinygarden;

import edu.undav.research.tinygarden.BasicClasses.VecInt;

public class Item {
	String _fileName;
 	VecInt _data;

	public Item(String fileName, VecInt data) {
		_fileName = fileName;
		_data = data;
	}

 	public Item(String line) {
 		String[] fields = line.split(",");
 		_fileName = fields[0];
 		_data = new VecInt(fields.length-1);
 		 
 		for (int i = 1; i < fields.length; i++) {
 			_data.pushBack(Integer.parseInt(fields[i]));
 		}
 	}

 	public void setData(VecInt other) {
 		_data = other;
 	}

 	public VecInt getData() {
 		return _data;
 	}
 	
 	public String toString() {
 		 return _fileName + "," + (_data != null ?_data.join(",") : "");
 		
 	}
 	
 	public String fileName() {
 		return _fileName;
 	}

/*
		String _fileName;
		int _treeCnt;
	 	int _maxCantZeros;
	 	int _maxCount;
	 	VecInt _maxList;
	 	
	 	public Item(String line) {
	 		String[] fields = line.split(",");
	 		_fileName = fields[0];
	 		_treeCnt = Integer.parseInt(fields[1]);
	 		_maxCantZeros = Integer.parseInt(fields[2]);
	 		_maxCount = Integer.parseInt(fields[3]);

	 		_maxList = new VecInt(_maxCount);
	 		 
	 		for (int i = 0; i < _maxCount; i++) {
	 			_maxList.pushBack(Integer.parseInt(fields[4+i]));
	 		}

	 	}

	 	public VecInt getMaxList() {
	 		return _maxList;
	 	}
	 	
	 	public String toString() {
	 		 return _fileName + "," + _treeCnt + "," + _maxCantZeros + 
						"," + _maxCount + "," + (_maxList != null ?_maxList.join(",") : "");
	 		
	 	}
	 	
	 	public String fileName() {
	 		return _fileName;
	 	}
*/

}

