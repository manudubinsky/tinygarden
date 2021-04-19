package edu.undav.research.tinygarden;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;


public class FileReaderSimple {
	Scanner _sc;
	String _fileName;
	
	public FileReaderSimple(String fileName) {
		_fileName = fileName;
	}
	
	public boolean hasItem() {
		boolean hasItem = false;
		try {
			if (_sc == null) {				
				File file = new File(_fileName);
				_sc = new Scanner(file);
			}
			if (_sc.hasNext()) {
				hasItem = true;
			} else {
				_sc.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return hasItem;
	}
	
	public Item nextItem() {
		Item item = null;
		String s = _sc.next();
		if (s != null) 
			item = new Item(s);
		//System.out.println(item);

		return item;
	}
	
	/*
	 String line = _fileName + "," + _treeCnt + "," + maxCantZeros + 
								"," + maxCount + "," + maxList.join(",");
	*/
/*
	public static class Item {
		String _fileName;
		int _treeCnt;
	 	int _maxCantZeros;
	 	int _maxCount;
	 	
	 	public Item(String line) {
	 		String[] fields = line.split(",");
	 		_fileName = fields[0];
	 		_treeCnt = Integer.parseInt(fields[1]);
	 		_maxCantZeros = Integer.parseInt(fields[2]);
	 		_maxCount = Integer.parseInt(fields[3]);
	 	}
	 	
	 	public String toString() {
	 		 return _fileName + "," + _treeCnt + "," + _maxCantZeros + 
						"," + _maxCount;
	 		
	 	}
	 	
	 	public String fileName() {
	 		return _fileName;
	 	}
	}
*/	
/*
	public static class Item {
		String _fileName;
		int[] _trees;
	 	
	 	public Item(String line) {
	 		String[] fields = line.split(",");
	 		_fileName = fields[0];
	 		_trees = new int[fields.length-1];
	 		for (int i = 1; i < fields.length; i++) {
	 			_trees[i-1] = Integer.parseInt(fields[i]);
	 		}
	 	}
	 	
	 	public String toString() {
			StringBuffer s = new StringBuffer();
			for (int i = 0; i < _trees.length; i++) {
				s.append("/" + _trees[i]);
			}
	 		return _fileName + s.toString();
	 		
	 	}
	 	
	 	public String fileName() {
	 		return _fileName;
	 	}
	 	
	 	public int[] trees() {
	 		return _trees;
	 	}
	}
*/
}
