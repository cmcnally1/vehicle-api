package com.udacity.vehicles.service;

import com.udacity.vehicles.VehiclesApiApplication;
import com.udacity.vehicles.client.maps.MapsClient;
import com.udacity.vehicles.client.prices.PriceClient;
import com.udacity.vehicles.domain.Location;
import com.udacity.vehicles.domain.car.Car;
import com.udacity.vehicles.domain.car.CarRepository;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Implements the car service create, read, update or delete
 * information about vehicles, as well as gather related
 * location and price data when desired.
 */
@Service
public class CarService {

    private final CarRepository repository;
    private MapsClient mapsClient;
    private PriceClient priceClient;

    public CarService(CarRepository repository, MapsClient mapsClient, PriceClient priceClient) {
        /**
         * TODO: Add the Maps and Pricing Web Clients you create
         *   in `VehiclesApiApplication` as arguments and set them here.
         */
        this.repository = repository;
        this.mapsClient = mapsClient;
        this.priceClient = priceClient;
    }

    /**
     * Gathers a list of all vehicles
     * @return a list of all vehicles in the CarRepository
     */
    public List<Car> list() {
        return repository.findAll();
    }

    /**
     * Gets car information by ID (or throws exception if non-existent)
     * @param id the ID number of the car to gather information on
     * @return the requested car's information, including location and price
     */
    public Car findById(Long id) {
        /**
         * TODO: Find the car by ID from the `repository` if it exists.
         *   If it does not exist, throw a CarNotFoundException
         *   Remove the below code as part of your implementation.
         */

        // Retrieve all cars
        List<Car> allCars = this.list();

        // Set up a null car to hold the found car. If still null, use to throw exception
        Car car = null;

        // Search for the car by the given ID
        for(int i = 0; i < allCars.size(); i++){
            if (allCars.get(i).getId() == id) {
                car = allCars.get(i);
            }
        }
        // Set up an optional car to hold the car found or not found above. If car not found and null, throw exception
        Optional<Car> optionalCar = Optional.ofNullable(car);
        Car foundCar = optionalCar.orElseThrow(CarNotFoundException::new);

        /**
         * TODO: Use the Pricing Web client you create in `VehiclesApiApplication`
         *   to get the price based on the `id` input'
         * TODO: Set the price of the car
         * Note: The car class file uses @transient, meaning you will need to call
         *   the pricing service each time to get the price.
         */
        // Get the price of the car
        String price = priceClient.getPrice(foundCar.getId());
        // Set the price of the car
        foundCar.setPrice(price);

        // Get the location of the car
        Location location = mapsClient.getAddress(foundCar.getLocation());
        // Set the location of the car to the new location with address
        foundCar.setLocation(location);

        return foundCar;
    }

    /**
     * Either creates or updates a vehicle, based on prior existence of car
     * @param car A car object, which can be either new or existing
     * @return the new/updated car is stored in the repository
     */
    public Car save(Car car) {
        if (car.getId() != null) {
            return repository.findById(car.getId())
                    .map(carToBeUpdated -> {
                        carToBeUpdated.setDetails(car.getDetails());
                        carToBeUpdated.setLocation(car.getLocation());
                        return repository.save(carToBeUpdated);
                    }).orElseThrow(CarNotFoundException::new);
        }

        return repository.save(car);
    }

    /**
     * Deletes a given car by ID
     * @param id the ID number of the car to delete
     */
    public void delete(Long id) {
        // Retrieve all cars
        List<Car> allCars = this.list();

        // Set up a null car to hold the found car. If still null, use to throw exception
        Car car = null;

        // Search for the car by the given ID
        for(int i = 0; i < allCars.size(); i++){
            if (allCars.get(i).getId() == id) {
                car = allCars.get(i);
            }
        }
        // Set up an optional car to hold the car found or not found above. If car not found and null, throw exception
        Optional<Car> optionalCar = Optional.ofNullable(car);
        Car foundCar = optionalCar.orElseThrow(CarNotFoundException::new);

        // Delete the car from the repository
        repository.delete(foundCar);

    }
}
