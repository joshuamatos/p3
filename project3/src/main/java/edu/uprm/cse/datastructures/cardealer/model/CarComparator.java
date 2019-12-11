package edu.uprm.cse.datastructures.cardealer.model;

import java.util.Comparator;

import edu.uprm.cse.datastructures.cardealer.util.HashTableOA.MapEntry;

public class CarComparator implements Comparator<MapEntry<Long, Car>> {

	@Override
	public int compare(MapEntry<Long, Car> o1, MapEntry<Long, Car> o2) {

		if (o2.getValue().getCarBrand().equals(o1.getValue().getCarBrand())) {

			if (o2.getValue().getCarModel().equals(o1.getValue().getCarModel())) {

				if (o2.getValue().getCarModelOption().equals(o1.getValue().getCarModelOption())) {
					return 0;
				} else {
					return o1.getValue().getCarModelOption().compareTo(o2.getValue().getCarModelOption());
				}
			} else {
				return o1.getValue().getCarModel().compareTo(o2.getValue().getCarModel());
			}

		} else {
			return o1.getValue().getCarBrand().compareTo(o2.getValue().getCarBrand());
		}

	}

}
