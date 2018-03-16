
public class BinaryHeap {

	private class Item 
	{
		private double key; //Key is distance
		private Object data; //Data is vertex

		private Item(double k, Object d) {
			key = k;
			data = d;
		}
	}
	

	Item contents[];
	int size;
	int numEntries;

	public BinaryHeap(int s) 
	{
		contents = new Item[s+1];
		size = 0;
		numEntries = 0;
	}

	public void removeMin() 
	{
	//PRE: !empty()
		int parent;
		int child;
		Item x = contents[size];
		size--;
		child = 2;
		while (child <= size) {
			parent = child/2;
			if (child < size && contents[child+1].key < contents[child].key)
				child++;
			if (x.key < contents[child].key) break;
			else {
				contents[parent] = contents[child];
				child = 2*child;
			}
		}
		contents[child/2] = x;
	}

	public boolean empty() 
	{
		return size == 0;
	}

	public double getMinKey() 
	{
	//PRE: !empty()
		return contents[1].key;
	}

	public Object getMinData() 
	{
	//PRE: !empty()
		return contents[1].data;
	}

	public boolean full() 
	{
		return size == contents.length-1;
	}

	public void insert(double k, Object d) 
	{
	//PRE !full()
		int parent;
		int child;
		size++;
		numEntries++;
		child = size;
		parent = child/2;
		contents[0] = new Item(k,d); 
		while (contents[parent].key > k) {
			contents[child] = contents[parent];
			child = parent;
			parent = child/2;
		}
		contents[child] = contents[0];	
	}

	public int getSize() 
	{
		return size;
	}
	
	public int getNumEntries()
	{
		return numEntries;
	}

}
