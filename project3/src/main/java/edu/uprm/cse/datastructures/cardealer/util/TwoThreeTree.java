package edu.uprm.cse.datastructures.cardealer.util;

import java.util.Comparator;

public class TwoThreeTree<K, V> extends BTree<K, V> {

	public TwoThreeTree(Comparator<K> keyComparator) {
		super(keyComparator);
		// TODO Auto-generated constructor stub
	}
	@Override
	public int size() {
		// TODO Auto-generated method stub
		return this.currentSize;
	}
	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return this.size() == 0;
	}
	@Override
	public V get(K key) 
	{
		// TODO Auto-generated method stub
		if (key.equals(null)) {
			throw new IllegalArgumentException("Key is null");
		} else if (this.isEmpty()) {
			return null;
		}
		return getAux(this.root, key);
	}

	private V getAux(BTree<K, V>.TreeNode N, K key) {
		// check if the current TreeNode is null.
		if (N == null)
		{
			return null;
		} 
		else
		{
			// Key comparator for first and last;
			int first = N.comparator.compare(key, N.entries.first().key),last = N.comparator.compare(key, N.entries.last().key);
			if(first==0 || last ==0)
			{
				// Verify if the first value is the desired key
				if (first == 0) 
				{
					return N.entries.first().value;
				}
				//Else return the lastvalue of the key
				else
				{
					return N.entries.last().value;
				}
			}
			// if it is less than the smallest number in the entries than there is no need
			// Verify last's value and it can proceed to go to the left node.
			if (first < 0) 
			{
				return this.getAux(N.left, key);
			}
			// same applies here but with the greatest number and it moves to the right
			if (last > 0) 
			{
				return this.getAux(N.right, key);
			}
			// if it is in between then go to the middle TreeNode
			if (first > 0 && last < 0)
			{

				return this.getAux(N.center, key);
			}
		}
		return null;
	}

	@Override
	// returns previous value of that key
	public V put(K key, V value)
	{
		if (this.isEmpty())
		{
			this.root = new TreeNode(key, null, value, null, this.keyComparator);
			this.currentSize++;
			return null;
		} 
		else 
		{
			return this.putAux(this.root, key, value);
		}
	}

	private V putAux(BTree<K, V>.TreeNode N, K key, V value) 
	{
		int first = N.comparator.compare(key, N.entries.first().key),last = N.comparator.compare(key, N.entries.last().key);

		if (first == 0) 
		{
			V v = N.entries.first().value;
			N.entries.first().value = value;
			return v;
		}

		if (last == 0)
		{
			V v = N.entries.last().value;
			N.entries.last().value = value;
			return v;
		}

		if (first < 0) 
		{
			if (this.isLeaf(N)) 
			{
				N.entries.add(new MapEntry(key, value, N.comparator));
				this.currentSize++;
				if (N.entries.size() == 3)
				{
					this.split(N);
				}
				return null;
			}
			else {
				return this.putAux(N.left, key, value);
			}
		}

		if (last > 0) {
			if (this.isLeaf(N)) {
				N.entries.add(new MapEntry(key, value, N.comparator));
				this.currentSize++;
				if (N.entries.size() == 3) {
					this.split(N);
				}
				return null;

			} else {
				return this.putAux(N.right, key, value);
			}

		} else {
			if (this.isLeaf(N)) {
				N.entries.add(new MapEntry(key, value, N.comparator));
				this.currentSize++;
				if (N.entries.size() == 3) {
					this.split(N);
				}
				return null;

			} else {
				return this.putAux(N.center, key, value);
			}

		}

	}

	@Override
	public V remove(K key) {
		if (key == null)
			throw new IllegalArgumentException("Invalid key");

		else if (this.isEmpty())
			return null;

		else
			return removeAux(this.root, key);
	}


	private V removeAux(BTree<K, V>.TreeNode N, K key) {
		int first = N.comparator.compare(key, N.entries.first().key),last = N.comparator.compare(key, N.entries.last().key);
//verify if the first element in the entries is the desired key
		V v = null;
		if (first == 0) {
			v = N.entries.first().value;
			// unsplitting
			this.unsplit(N, key);

		}
//check if the last element in the entries is the desired key.
		if (last == 0) {

			// unsplitting
			v = N.entries.first().value;
			this.unsplit(N, key);

		}
//key is less than the key of the elements in the current node therefore search left.
		if (first < 0) {
			return this.removeAux(N.left, key);
		}

		// key is greater than the key of the elements in the current node therefore
		// search right.

		if (last > 0) {
			return this.removeAux(N.right, key);
		}

		// nothing was found
		return null;

	}

	private void unsplit(BTree<K, V>.TreeNode treeNode, K key) 
	{
		return;
		// TODO Auto-generated method stub
	}

	@Override
	public boolean contains(K key) {
		// TODO Auto-generated method stub
		return this.get(key) != null;
	}

	@Override
	public List<K> getKeys() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<V> getValues() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	boolean isLeaf(BTree<K, V>.TreeNode treeNode) {
		// TODO Auto-generated method stub
		return (treeNode.left == null && treeNode.right == null);
	}

	@Override
	void split(BTree<K, V>.TreeNode treeNode) {
		// if the node is the treeNode
		if (treeNode.equals(this.root)) {

			// save the first and last values in the entries
			MapEntry left = treeNode.entries.first();
			MapEntry right = treeNode.entries.last();
			// remove them from the entries
			treeNode.entries.remove(left);
			treeNode.entries.remove(right);

			// make them new treeNodes
			TreeNode NewLeftNode = new TreeNode(left, null, treeNode.comparator);
			NewLeftNode.parent = treeNode;
			TreeNode NewRightNode = new TreeNode(right, null, treeNode.comparator);
			NewRightNode.parent = treeNode;

			// transfer the old node links
			if (this.size() > 3) {

				NewLeftNode.left = treeNode.left;
				NewLeftNode.right = treeNode.center;
				NewRightNode.left = treeNode.right;
				NewRightNode.right = treeNode.temp;
				NewLeftNode.left.parent = NewLeftNode;
				NewLeftNode.right.parent = NewLeftNode;
				NewRightNode.right.parent = NewRightNode;
				NewRightNode.left.parent = NewRightNode;
				treeNode.center = null;
				treeNode.temp = null;

			}

			// link them to the root
			treeNode.left = NewLeftNode;
			treeNode.right = NewRightNode;

		} else {
			// parent node where we're going to push up the middle value of treeNode
			TreeNode parent = treeNode.parent;
			// The value we're pushing up to split(Middle node)
			MapEntry val = treeNode.entries.get(1);
			// remove the value from the original
			treeNode.entries.remove(val);
			// put it in the entries of the parent
			parent.entries.add(val);
			// left and right values in the treeNode entries
			MapEntry left = treeNode.entries.first();
			MapEntry right = treeNode.entries.last();

			// The other 2 nodes need to be split and put in order.

			// verify if our treeNode is the left one.

			// The order of the nodes I chose is left center righ temp
			if (parent.left != null) {

				if (parent.left.equals(treeNode)) {
					// remove he right value
					treeNode.entries.remove(right);
					// check if the right child is null to place the new split child
					if (parent.right == null) {
						parent.right = new TreeNode(right, null, treeNode.comparator);
						parent.right.parent = parent;
						// check if the center is null to place the new split child
					} else if (parent.center == null) {
						parent.center = new TreeNode(right, null, treeNode.comparator);
						// gives the new create node a reference to the parent
						parent.center.parent = parent;
					}
					// slide everything to the right
					else {
						//
						parent.temp = parent.right;
						parent.right = parent.center;
						parent.center = new TreeNode(right, null, treeNode.comparator);
						parent.center.parent = parent;
					}

					// we need to reorganize the tree and its' nodes.
					if (!this.isLeaf(treeNode)) {
						// we have to add the nodes to an actual parent
						if (parent.right != null) {
							if (this.isLeaf(parent.right)) {
								parent.right.left = treeNode.right;
								parent.right.right = treeNode.temp;

								treeNode.right = treeNode.center;
								treeNode.center = null;
								treeNode.temp = null;
							}

						}
						if (parent.center != null) {
							if (this.isLeaf(parent.center)) {
								parent.center.left = treeNode.right;
								parent.center.right = treeNode.temp;

								treeNode.right = treeNode.center;
								treeNode.center = null;
								treeNode.temp = null;
							}

						}
						if (parent.left != null) {
							if (this.isLeaf(parent.left)) {
								parent.left.left = treeNode.right;
								parent.left.right = treeNode.temp;

								treeNode.right = treeNode.center;
								treeNode.center = null;
								treeNode.temp = null;
							}

						}
					}

				}
			}
			if (parent.right != null) {
				// same as the left
				if (parent.right.equals(treeNode)) {

					treeNode.entries.remove(left);

					if (parent.left == null) {
						parent.left = new TreeNode(left, null, treeNode.comparator);
						parent.left.parent = parent;
					} else if (parent.center == null) {
						parent.center = new TreeNode(left, null, treeNode.comparator);
						parent.center.parent = parent;
					} else {
						// shift from right to temp and that is it.
						parent.temp = parent.right;
						parent.right = new TreeNode(left, null, treeNode.comparator);
						parent.right.parent = parent;
					}

					if (!this.isLeaf(treeNode)) {

						if (parent.left != null) {
							if (this.isLeaf(parent.left)) {
								parent.left.left = treeNode.left;
								parent.left.right = treeNode.center;
								treeNode.left = treeNode.right;
								treeNode.right = treeNode.temp;
								treeNode.center = null;
								treeNode.temp = null;
							}

						}
						if (parent.center != null) {
							if (this.isLeaf(parent.center)) {
								parent.center.left = treeNode.left;
								parent.center.right = treeNode.center;
								treeNode.left = treeNode.right;
								treeNode.right = treeNode.temp;
								treeNode.center = null;
								treeNode.temp = null;
							}

						}
						if (parent.right != null) {
							if (this.isLeaf(parent.right)) {
								parent.right.left = treeNode.left;
								parent.right.right = treeNode.center;
								treeNode.left = treeNode.right;
								treeNode.right = treeNode.temp;
								treeNode.center = null;
								treeNode.temp = null;
							}

						}
					}

				}
			}

			if (parent.center != null) {
				if (parent.center.equals(treeNode)) {
					// shifting from middle to right
					treeNode.entries.remove(right);
					parent.temp = parent.right;
					parent.right = new TreeNode(right, null, treeNode.comparator);
					parent.right.parent = parent;

				}
			}

			// keep shifting if the parent has 3 entries

			if (parent.entries.size() == 3) {
				this.split(parent);
			}

		}

	}
	@Override
	public V remove(K key) {
		if (key == null)
			throw new IllegalArgumentException("Invalid key");

		else if (this.isEmpty())
			return null;

		else
			return removeAux(this.root, key);
	}

	private V removeAux(BTree<K, V>.TreeNode N, K key) {
		int first = N.comparator.compare(key, N.entries.first().key),last = N.comparator.compare(key, N.entries.last().key);
//verify if the first element in the entries is the desired key
		V v = null;
		if (first == 0) {
			v = N.entries.first().value;
			N.entries.first().deleted = true;
			this.deleted++;
		
			

		}
//check if the last element in the entries is the desired key.
		if (last == 0) {

			
			v = N.entries.first().value;
			N.entries.last().deleted = true;
			this.deleted++;

		}
//key is less than the key of the elements in the current node therefore search left.
		if (first < 0) {
			return this.removeAux(N.left, key);
		}

		// key is greater than the key of the elements in the current node therefore
		// search right.

		if (last > 0) {
			return this.removeAux(N.right, key);
		}

		// nothing was found
		return v;
}
//vtr