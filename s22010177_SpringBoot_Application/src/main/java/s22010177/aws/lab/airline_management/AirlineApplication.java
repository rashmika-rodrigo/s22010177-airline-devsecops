
package s22010177.aws.lab.airline_management;

import jakarta.persistence.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@SpringBootApplication
@RestController
@RequestMapping("/api/flights")
public class AirlineApplication {

    public static void main(String[] args) {
        SpringApplication.run(AirlineApplication.class, args);
    }

    private final FlightRepository repository;

    public AirlineApplication(FlightRepository repository) {
        this.repository = repository;
    }

    // READ: Getting all flights
    @GetMapping
    public List<Flight> getFlights() {
        return repository.findAll();
    }

    // READ: Getting one flight with it's ID
    @GetMapping("/{id}")
    public Flight getFlight(@PathVariable Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Flight not found"));
    }

    // CREATE: Adding a new flight
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Flight addFlight(@RequestBody Flight flight) {
        return repository.save(flight);
    }

    // UPDATE: Updating flight details
    @PutMapping("/{id}")
    public Flight updateFlight(@PathVariable Long id,
                               @RequestBody Flight details) {

        Flight flight = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Flight not found"));

        flight.setFlightNumber(details.getFlightNumber());
        flight.setDepartureTime(details.getDepartureTime());
        flight.setArrivalTime(details.getArrivalTime());
        flight.setDestination(details.getDestination());

        return repository.save(flight);
    }

    // DELETE: Deleting a flight
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFlight(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Flight not found");
        }

        repository.deleteById(id);
    }
}

// Flight Entity
@Entity
@Table(name = "flights")
class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String flightNumber;
    private String departureTime;
    private String arrivalTime;
    private String destination;

    public Flight() {}

    public Long getId() {
        return id;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }
}

// Repository
interface FlightRepository extends JpaRepository<Flight, Long> {
}
