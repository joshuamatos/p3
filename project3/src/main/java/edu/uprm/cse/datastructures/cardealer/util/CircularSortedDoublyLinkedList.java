package edu.uprm.cse.datastructures.cardealer.util;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class CircularSortedDoublyLinkedList<E> implements SortedList<E> {

	private static class Node<E> {

		private Node<E> prev;
		private Node<E> next;
		private E element;

		public Node(E element, Node<E> prev, Node<E> next) {
			this.element = element;
			this.prev = prev;
			this.next = next;
		}

		@SuppressWarnings("unused")
		public Node(Node<E> prev, Node<E> next) {
			this(null, prev, next);
		}

		public Node() {
			this(null, null, null);
		}

		public Node<E> getPrev() {
			return prev;
		}

		public void setPrev(Node<E> prev) {
			this.prev = prev;
		}

		public Node<E> getNext() {
			return next;
		}

		public void setNext(Node<E> next) {
			this.next = next;
		}

		public E getElement() {
			return element;
		}

		public void setElement(E element) {
			this.element = element;
		}

	}

	@SuppressWarnings("hiding")
	private class CSDListIterator<E> implements Iterator<E> {
		private Node<E> nextNode;

		@SuppressWarnings("unchecked")
		public CSDListIterator() {
			this.nextNode = (Node<E>) header.getNext();
		}

		@Override
		public boolean hasNext() {
			return nextNode != header;
		}

		@Override
		public E next() {
			if (this.hasNext()) {
				E result = this.nextNode.getElement();
				this.nextNode = this.nextNode.getNext();
				return result;
			} else {
				throw new NoSuchElementException();
			}
		}

	}

	@Override
	public Iterator<E> iterator() {
		// TODO Auto-generated method stub
		return new CSDListIterator<E>();
	}

	private Node<E> header;
	private int currentSize;
	private Comparator<E> comp;

	public CircularSortedDoublyLinkedList(Comparator<E> comp) {

		header = new Node<E>();
		this.comp = comp;
		currentSize = 0;

	}

	@Override
	public boolean add(E e) {

		Node<E> newNode;

		if (this.isEmpty()) {
			newNode = new Node<E>(e, header, header);
			this.header.setNext(newNode);
			this.header.setPrev(newNode);

			this.currentSize++;
			return true;
		} else {
			for (Node<E> temp = header.getNext(); temp != this.header; temp = temp.getNext()) {
				if (comp.compare(e, temp.getElement()) <= 0) {

					newNode = new Node<E>(e, temp.getPrev(), temp);
					temp.getPrev().setNext(newNode);
					temp.setPrev(newNode);
					this.currentSize++;
					return true;
				}
			}

			Node<E> prev = this.header.getPrev();
			newNode = new Node<>(e, prev, header);
			header.setPrev(newNode);
			prev.setNext(newNode);
			this.currentSize++;
			return true;
		}

	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return currentSize;
	}

	@Override
	public boolean remove(E obj) {
		Node<E> temp = header.getNext();

		while (temp != header) {

			if (temp.element.equals(obj)) {

				temp.getNext().setPrev(temp.getPrev());
				temp.getPrev().setNext(temp.getNext());
				temp.setElement(null);
				temp.setNext(null);
				temp.setPrev(null);
				this.currentSize--;
				return true;

			}

			temp = temp.getNext();
		}
		return false;
	}

	@Override

	public boolean remove(int index) {
		if ((index < 0) || (index >= this.currentSize)) {
			throw new IndexOutOfBoundsException();
		} else {
			Node<E> temp = this.header;
			int currentPosition = 0;
			Node<E> target = null;

			while (currentPosition != index) {
				temp = temp.getNext();
				currentPosition++;
			}

			target = temp.getNext();

			temp.setNext(target.getNext());
			target.getNext().setPrev(temp);
			target.setElement(null);
			target.setNext(null);
			target.setPrev(null);
			this.currentSize--;
			return true;

		}
	}

	@Override
	public int removeAll(E obj) {
		int count = 0;

		while (this.remove(obj)) {
			count++;
		}
		return count;
	}

	@Override
	public E first() {
		// TODO Auto-generated method stub
		return this.header.getNext().getElement();
	}

	@Override
	public E last() {
		// TODO Auto-generated method stub
		return this.header.getPrev().getElement();
	}

	private Node<E> getPosition(int index) {
		int currentPosition = 0;
		Node<E> temp = this.header.getNext();

		while (currentPosition != index) {
			temp = temp.getNext();
			currentPosition++;
		}
		return temp;

	}

	@Override
	public E get(int position) {
		if ((position < 0) || position >= this.currentSize) {
			throw new IndexOutOfBoundsException();
		}

		Node<E> temp = this.getPosition(position);
		return temp.getElement();

	}

	@Override
	public void clear() {
		while (!this.isEmpty())
			this.remove(0);

	}

	@Override
	public boolean contains(E e) {
		return this.firstIndex(e) > 0;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return this.size() == 0;
	}

	@Override
	public int firstIndex(E e) {
		// TODO Auto-generated method stub
		Node<E> temp = header.getNext();
		int count = 0;
		while (temp != header) {

			if (temp.element.equals(e)) {

				return count;
			}
			temp = temp.getNext();
			count++;

		}
		return -1;
	}

	@Override
	public int lastIndex(E e) {
		Node<E> temp = header.getPrev();
		int count = this.size() - 1;
		while (temp != header) {

			if (temp.element.equals(e)) {

				return count;
			}
			temp = temp.getPrev();
			count--;

		}
		return -1;
	}

	public Object[] toArray() {
		Object[] result = new Object[this.size()];
		Node<E> temp = header.getNext();
		int index = 0;
		while (temp != header) {
			result[index] = temp;
			temp = temp.getNext();
			index++;
		}

		return result;
	}

}
