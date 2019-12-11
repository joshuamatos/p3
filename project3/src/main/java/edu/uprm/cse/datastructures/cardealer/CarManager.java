package edu.uprm.cse.datastructures.cardealer;

import java.util.ArrayList;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import edu.uprm.cse.datastructures.cardealer.model.Car;
import edu.uprm.cse.datastructures.cardealer.model.CarComparator;
import edu.uprm.cse.datastructures.cardealer.model.CarComparator;
import edu.uprm.cse.datastructures.cardealer.util.CarTable;
import edu.uprm.cse.datastructures.cardealer.util.CircularSortedDoublyLinkedList;
import edu.uprm.cse.datastructures.cardealer.util.HashTableOA;
import edu.uprm.cse.datastructures.cardealer.util.HashTableOA.MapEntry;
import edu.uprm.cse.datastructures.cardealer.util.LongComparator;
import edu.uprm.cse.datastructures.cardealer.util.SortedList;

@Path("/cars")
public class CarManager {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Car[] getAllCars() {

		// Calls the existing HashTableOA object
		HashTableOA<Long, Car> carTable = CarTable.getInstance();

		// Helper method in the HashTableOA method to sort items.
		CircularSortedDoublyLinkedList<MapEntry<Long, Car>> sortedCarTable = (CircularSortedDoublyLinkedList<MapEntry<Long, Car>>) carTable
				.getSortedList();
		// array that is going to be populated with sorted cars
		Car[] newList = new Car[carTable.size()];

		if (!sortedCarTable.isEmpty()) {

			int index = 0;
			for (MapEntry<Long, Car> ME : sortedCarTable) {

				newList[index++] = ME.getValue();

			}
		}

		return newList;

	}

	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Car getCar(@PathParam("id") long id) throws NotFoundException {

		HashTableOA<Long, Car> carList = CarTable.getInstance();

		Car ctr = carList.get(id);

		if (ctr == null)
			throw new WebApplicationException(404);
		else
			return ctr;

	}

	@POST
	@Path("/add")
	@Produces(MediaType.APPLICATION_JSON)
	public Response addCar(Car newCar) {

		HashTableOA<Long, Car> carList = CarTable.getInstance();

		carList.put(newCar.getCarId(), newCar);
		return Response.status(201).build();

	}

	@PUT
	@Path("/{id}/update")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateCar(Car updateCar) {
		HashTableOA<Long, Car> carList = CarTable.getInstance();
		if (carList.get(updateCar.getCarId()) != null) {
			carList.put(updateCar.getCarId(), updateCar);
			return Response.status(Response.Status.OK).build();
		} else
			return Response.status(Response.Status.NOT_FOUND).build();

	}

	@DELETE
	@Path("/{id}/delete")
	public Response deleteCar(@PathParam("id") long id) {
		HashTableOA<Long, Car> carList = CarTable.getInstance();
		Car ctr = carList.remove(id);

		if (ctr == null) {
			return Response.status(Response.Status.NOT_FOUND).build();
		} else {
			return Response.status(Response.Status.OK).build();
		}

	}

}
