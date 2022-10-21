package nttdata.bootcamp.mscredits.application;

import java.util.Collections;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import nttdata.bootcamp.mscredits.dto.CustomerDTO;
import nttdata.bootcamp.mscredits.interfaces.ICreditService;
import nttdata.bootcamp.mscredits.interfaces.ICustomerService;
import nttdata.bootcamp.mscredits.model.Credit;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class CreditController {

    @Autowired
    private ICreditService service;

    @Autowired
    private ICustomerService customerService;

    @PostMapping
    public ResponseEntity<?> createCredit(@RequestBody Credit credit) {
        try {
            Optional<CustomerDTO> customer = customerService.findCustomerByNroDoc(credit.getNroDoc());

            if (customer.isPresent()) {
                credit.setDateReg(new Date());
                final Mono<Credit> response = service.createCredit(Mono.just(credit));
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(String.format("No se encontró cliente con documento: %s", credit.getNroDoc()));
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().value() == HttpStatus.NOT_FOUND.value()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Collections.singletonMap("message",
                                "No se encontró cliente con número de documento: " +
                                        credit.getNroDoc()));
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("message", "Error al crear cuenta bancaria."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> findCredits() {
        final Flux<Credit> response = service.findAllCredits();
        return ResponseEntity.ok(response);
    }
}
