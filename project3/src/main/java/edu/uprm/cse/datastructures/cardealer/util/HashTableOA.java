package edu.uprm.cse.datastructures.cardealer.util;

import java.util.Comparator;

public class HashTableOA<K, V> implements Map<K, V> {

	public static class MapEntry<K, V> {
		V value;
		K key;

		public V getValue() {
			return value;
		}

		public MapEntry(K key, V value) {
			super();
			this.value = value;
			this.key = key;
		}

		public MapEntry() {
			this(null, null);
		}

		public V setValue(V value) {

			V oldV = this.value;

			this.value = value;
			return oldV;
		}

		public K getKey() {
			return key;
		}

		public void setKey(K key) {
			this.key = key;
		}
	}

	private MapEntry<K, V>[] table;
	private MapEntry<K, V> DEFUNCT = new MapEntry<>();
	private int currentSize;
	private static final int DEFAULT_SIZE = 11;

	// originally this class didn't have Comparator parameters at all. All the
	// sorting used to be done in the Car manager.
	// There are 2 comparators:
	// KeyComp and MapEntryComp = value comparator which were implemented because of
	// the SortedList
	// specs added for the getKeys and getValues functions.
	private Comparator<MapEntry<K, V>> MapEntryComp;
	private Comparator<K> KeyComp;

	public HashTableOA(Comparator<MapEntry<K, V>> MapEntryComp, Comparator<K> KeyComp) {
		this(DEFAULT_SIZE, MapEntryComp, KeyComp);
	}

	@SuppressWarnings("unchecked")
	public HashTableOA(int cap, Comparator<MapEntry<K, V>> MapEntryComp, Comparator<K> KeyComp) {
		this.table = (MapEntry<K, V>[]) new MapEntry[cap];
		this.currentSize = 0;
		this.MapEntryComp = MapEntryComp;
		this.KeyComp = KeyComp;

	}

	private boolean isAvailable(int j) {

		if (j < 0) {
			return false;
		}

		return (table[j] == null || table[j] == DEFUNCT);

	}

	// looks for the nearest prime less than the size of the table. It is only
	// called in the second hash function.

	private int prime() {

		int n = this.table.length - 1;
		for (int i = n; i >= 2; i--) {
			if (n % i != 0) {
				return n;
			}
		}
		return 1;
	}

	// This is the hash function in the class slides, book and videos.
	private int firstHashFunction(K key) {
		return Math.abs(key.hashCode()) % this.table.length;
	}

	// This is also a second hash function taken from the class video on Open
	// Addressing
	private int secondHashFunction(K key) {

		return this.prime() - (Math.abs(key.hashCode()) % this.prime());
	}

	// Auxiliary method from the textbook adapted to double hash.
	// Verifies if the current position is taken if it isn't return that position,
	// if it is then use the second hash, if that doesn't work then use linear
	// probing.
	private int findSlot(K key) {
		int availableIndex = -1;

		int firstHash = this.firstHashFunction(key);
		int currentIndex = firstHash;
		boolean linearProbing = false;

		do {

			if (this.isAvailable(currentIndex)) {
				if (availableIndex == -1) {
					availableIndex = currentIndex;
				}
				if (table[currentIndex] == null) {
					break;
				}

			} else if (table[currentIndex].getKey().equals(key)) {

				return currentIndex;
			} else if (linearProbing == false) {

				int secondHash = this.secondHashFunction(key);
				if (this.isAvailable(secondHash)) {
					if (availableIndex == -1) {
						availableIndex = secondHash;
					}
					if (table[secondHash] == null) {
						break;
					}

				} else if (table[secondHash].getKey().equals(key)) {

					return secondHash;
				}

				linearProbing = true;
			}

			currentIndex = (1 + currentIndex) % this.table.length;

		} while (currentIndex != firstHash);

		return -(availableIndex + 1);
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return this.currentSize;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return this.currentSize == 0;
	}

	// taken and adapted from the textbook
	@Override
	public V get(K key) {

		if (key == null)
			throw new IllegalArgumentException("Key cannot be null");

		int j = this.findSlot(key);

		if (j < 0)
			return null;
		return table[j].getValue();
	}

	// taken and adapted from the textbook
	@Override
	public V put(K key, V value) {
		// TODO Auto-generated method stub

		if (key == null)
			throw new IllegalArgumentException("Key cannot be null!");
		if (value == null)
			throw new IllegalArgumentException("Value cannot be null!");

		if (this.size() == this.table.length) {
			this.reAllocate();
		}

		int j = this.findSlot(key);
		if (j > 0)
			return table[j].setValue(value);
		table[-(j + 1)] = new MapEntry<K, V>(key, value);
		this.currentSize++;
		return null;
	}

	private void reAllocate() {
		@SuppressWarnings("unchecked")
		MapEntry<K, V>[] temp = (MapEntry<K, V>[]) new MapEntry[this.size() * 2];

		for (int i = 0; i < this.currentSize; i++) {
			temp[i] = this.table[i];
		}

		this.table = temp;

	}

	// taken and adapted from textbook
	@Override
	public V remove(K key) {
		// TODO Auto-generated method stub

		if (key == null)
			throw new IllegalArgumentException("Key cannot be null!");
		int j = this.findSlot(key);

		if (j < 0)
			return null;
		V answer = table[j].getValue();
		table[j] = DEFUNCT;
		this.currentSize--;
		return answer;
	}

	@Override
	public boolean contains(K key) {

		if (key == null)
			throw new IllegalArgumentException("Key cannot be null!");
		return this.get(key) != null;
	}

	// Auxiliary method added, Originally this was done in the car manager, but
	// since we had to adapt this class with sorted Lists for keys & values I added
	// this here as well. It makes sense to do so since we're already passing a
	// comparator parameter in the constructor that can be used.

	@SuppressWarnings("unchecked")
	public List<MapEntry<K, V>> getSortedList() {
		CircularSortedDoublyLinkedList<MapEntry<K, V>> cl = new CircularSortedDoublyLinkedList<MapEntry<K, V>>(
				this.MapEntryComp);

		for (int i = 0; i < this.table.length; i++) {
			if (this.table[i] != null && this.table[i] != this.DEFUNCT)
				cl.add(this.table[i]);
		}

		return (List<MapEntry<K, V>>) cl;

	}

	// return the sorted keys of the hash-table in order. This was modified from the
	// original implementation which was a regular list. This is where the
	// KeyComp comes into play

	@SuppressWarnings("unchecked")
	@Override
	public List<K> getKeys() {
		CircularSortedDoublyLinkedList<K> cl = new CircularSortedDoublyLinkedList<K>(this.KeyComp);

		for (int i = 0; i < this.table.length; i++) {
			if (this.table[i] != null && this.table[i] != this.DEFUNCT)
				cl.add(this.table[i].getKey());
		}

		return (List<K>) cl;

	}
	// return the sorted value of the hash-table in order. This was modified from
	// the original implementation which was a regular list. This is where
	// MapEntryComp
	// comes into play

	@SuppressWarnings("unchecked")
	@Override
	public List<V> getValues() {
		@SuppressWarnings("unchecked")
		CircularSortedDoublyLinkedList<V> cl = new CircularSortedDoublyLinkedList<V>((Comparator<V>) this.MapEntryComp);

		for (int i = 0; i < this.table.length; i++) {
			if (this.table[i] != null && this.table[i] != this.DEFUNCT)
				cl.add(this.table[i].getValue());
		}

		return (List<V>) cl;

	}

}
