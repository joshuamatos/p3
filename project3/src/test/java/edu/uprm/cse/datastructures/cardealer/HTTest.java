package edu.uprm.cse.datastructures.cardealer;

import edu.uprm.cse.datastructures.cardealer.model.Car;
import edu.uprm.cse.datastructures.cardealer.model.CarComparator;
import edu.uprm.cse.datastructures.cardealer.util.HashTableOA;
import edu.uprm.cse.datastructures.cardealer.util.HashTableOA.MapEntry;
import edu.uprm.cse.datastructures.cardealer.util.LongComparator;
import edu.uprm.cse.datastructures.cardealer.util.SortedList;

public class HTTest {

	public static void main(String args[]) {

		HashTableOA<Long, Car> carList = new HashTableOA<Long, Car>(new CarComparator(), new LongComparator());
		Car car1 = new Car(67, "Toyota", "RAV4", "LE", 15000.50);
		Car car2 = new Car(55, "Toyota", "RAV4", "SE", 10000);
		Car car3 = new Car(33, "Honda", "Civic", "Sedan", 20000);

		carList.put(car1.getCarId(), car1);
		carList.put(car2.getCarId(), car2);
		carList.put(car3.getCarId(), car3);

		SortedList<MapEntry<Long, Car>> list = (SortedList<MapEntry<Long, Car>>) carList.getSortedList();

		MapEntry<Long, Car> curr = null;

		for (int i = 0; i < list.size(); i++) {
			curr = list.get(i);
			System.out.println("id: " + curr.getKey() + " " + "Brand: " + curr.getValue().getCarBrand() + " "
					+ "model: " + curr.getValue().getCarModel() + " " + "option: " + curr.getValue().getCarModelOption()
					+ " " + "price: " + curr.getValue().getCarPrice());
		}

		carList.remove((long) 55);
		list = (SortedList<MapEntry<Long, Car>>) carList.getSortedList();

		System.out.println("-----------------------------------------------------------------------------------");

		for (int i = 0; i < list.size(); i++) {
			curr = list.get(i);
			System.out.println("id: " + curr.getKey() + " " + "Brand: " + curr.getValue().getCarBrand() + " "
					+ "model: " + curr.getValue().getCarModel() + " " + "option: " + curr.getValue().getCarModelOption()
					+ " " + "price: " + curr.getValue().getCarPrice());
		}

//		SortedList<Long> sortedKeys = carList.getKeys();
//		for (int i = 0; i < sortedKeys.size(); i++) {
//			System.out.println(sortedKeys.get(i));
//		}
//
//		SortedList<Car> newList = carList.getValues();
//		Car newCurr = null;
//		for (int i = 0; i < newList.size(); i++) {
//			newCurr = newList.get(i);
//			System.out.println("id: " + newCurr.getCarId() + " " + "Brand: " + newCurr.getCarBrand() + " " + "model: "
//					+ newCurr.getCarModel() + " " + "option: " + newCurr.getCarModelOption() + " " + "price: "
//					+ newCurr.getCarPrice());
//		}

	}

}
