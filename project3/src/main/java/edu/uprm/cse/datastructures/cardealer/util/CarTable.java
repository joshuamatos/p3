package edu.uprm.cse.datastructures.cardealer.util;

import edu.uprm.cse.datastructures.cardealer.model.Car;
import edu.uprm.cse.datastructures.cardealer.model.CarComparator;

public class CarTable {

	public static HashTableOA<Long, Car> carList = new HashTableOA<Long, Car>(new CarComparator(),
			new LongComparator());

	public static HashTableOA<Long, Car> getInstance() {
		return carList;
	}

	public static void resetCars() {
		carList = new HashTableOA<Long, Car>(new CarComparator(), new LongComparator());
	}
}
