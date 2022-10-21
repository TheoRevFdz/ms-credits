package nttdata.bootcamp.mscredits.application;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import nttdata.bootcamp.mscredits.dto.CustomerDTO;
import nttdata.bootcamp.mscredits.enums.CustomerTypes;
import nttdata.bootcamp.mscredits.interfaces.ICreditService;
import nttdata.bootcamp.mscredits.interfaces.ICustomerService;
import nttdata.bootcamp.mscredits.interfaces.ITypeCreditService;
import nttdata.bootcamp.mscredits.model.Credit;
import nttdata.bootcamp.mscredits.model.TypeCredit;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class CreditController {

    @Autowired
    private ICreditService service;

    @Autowired
    private ICustomerService customerService;

    @Autowired
    private ITypeCreditService typeService;

    @PostMapping
    public ResponseEntity<?> createCredit(@RequestBody Credit credit) {
        try {
            Optional<CustomerDTO> customer = customerService.findCustomerByNroDoc(credit.getNroDoc());

            if (customer.isPresent()) {
                Optional<TypeCredit> type = typeService.findByType(customer.get().getTypePerson());

                if (type.isPresent()) {
                    List<Credit> creditRecord = service.findCreditByNroDoc(credit.getNroDoc());

                    if (customer.get().getTypePerson().equals(CustomerTypes.PERSONAL.toString())
                            && creditRecord.size() == type.get().getAllowedAmount()) {
                        return ResponseEntity.badRequest().body(
                                String.format(
                                        "El cliente ya cuenta con crédito registrado y por ser de tipo PERSONAL solo puede tener un máximo de %x crédito",
                                        type.get().getAllowedAmount()));
                    }

                    return saveCredit(credit, customer.get());
                }

                List<TypeCredit> types = typeService.findAll();
                String strTypes = types.stream()
                        .map(t -> t.getType())
                        .collect(Collectors.joining(", "));
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(String.format("El tipo ingresado es invalido, los tipos validos son: [%s]",
                                strTypes));
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

    private ResponseEntity<?> saveCredit(Credit credit, CustomerDTO customer) {
        credit.setDateReg(new Date());
        credit.setType(customer.getTypePerson());

        final Mono<Credit> response = service.createCredit(Mono.just(credit));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<?> findCredits() {
        final Flux<Credit> response = service.findAllCredits();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable String id) {
        final Optional<Credit> resp = service.findCreditById(id);
        if (resp.isPresent()) {
            return ResponseEntity.ok().body(resp.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
