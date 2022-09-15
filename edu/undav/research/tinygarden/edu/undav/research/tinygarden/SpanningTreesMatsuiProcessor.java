package edu.undav.research.tinygarden;

import edu.undav.research.tinygarden.BasicClasses.SparseMatrixInt;
import edu.undav.research.tinygarden.BasicClasses.VecInt;


public class SpanningTreesMatsuiProcessor {
	public static interface Processor {
		public void processSpanningTree();
	}

	public static abstract class ProcessorBase implements Processor {
		protected SpanningTreesMatsui _stm;
		protected int _treeCnt;

		public ProcessorBase(SpanningTreesMatsui stm) {
			_stm = stm;
			_stm.setProcessor(this);
		}

		public int getTreeCnt() {
			return _treeCnt;
		}

	}

	public static class IntersectionNumberProcessor extends ProcessorBase {
		Item _item;
		int _intersectionNumber;
		String _path;
		
		public IntersectionNumberProcessor(SpanningTreesMatsui stm, Item item, String path) {
			super(stm);
			_item = item;
			 _intersectionNumber = item.getData().get(0);
			_path = path;
			//_list.dump();
		}
		
		public void processSpanningTree() {
			if (_stm.getTree().intersectionNumber() == _intersectionNumber) {
				String fileName = _path + _item.fileName() + "." + _treeCnt;
				_stm.getTree().dumpGraphViz(fileName);
			}
			_treeCnt++;
		}

	}

	public static class TreeListProcessor extends ProcessorBase {
		Item _item;
		int _current;
		VecInt _list;
		String _path;
		
		public TreeListProcessor(SpanningTreesMatsui stm, Item item, String path) {
			super(stm);
			_item = item;
			_list = item.getData();
			_current = 0;
			_path = path;
			//_list.dump();
		}
/*
		public void processSpanningTree() {
			String fileName = _path + _item.fileName() + "." + _treeCnt;
			_stm.getTree().dumpGraphViz(fileName);
			_treeCnt++;
		}
*/
		
		public void processSpanningTree() {
			if (_current < _list.size() && _list.get(_current) == _treeCnt) {
				String fileName = _path + _item.fileName() + "." + _treeCnt;
				_stm.getTree().dumpGraphViz(fileName);
				_current++;
			}
			_treeCnt++;
		}

	}

	public static class LABIMatrixListProcessor extends ProcessorBase {
		Item _item;
		int _current;
		VecInt _list;
		
		public LABIMatrixListProcessor(SpanningTreesMatsui stm, Item item) {
			super(stm);
			_item = item;
			_list = item.getData();
			_current = 0;
			//_list.dump();
		}
		

  		public void processSpanningTree() {
			if (_current < _list.size() && _list.get(_current) == _treeCnt) {
				if (_treeCnt == 47503) {
//				if (_treeCnt == 264 ||
//						_treeCnt == 265 ||
//								_treeCnt == 268 ||
//										_treeCnt == 270) {
					System.out.println("***********************");
					System.out.println(_treeCnt);
					System.out.println("*******");
					SparseMatrixInt labi =_stm.getTree().labiMatrix();
					labi.toOctave();
					System.out.println("***");
					labi.transpose().multiply(labi).toOctave();
				}
				_current++;
			}
			_treeCnt++;
		}

/*
  		public void processSpanningTree() {
			if (_current < _list.size() && _list.get(_current) == _treeCnt) {
				System.out.println("***********************");
				System.out.println(_treeCnt + ": " + _stm.getTree().labiCantZeros());
				System.out.println("*******");
				_current++;
			}
			_treeCnt++;
		}
*/
	}
	

}
