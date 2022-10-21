package nttdata.bootcamp.mscredits.interfaces;

import nttdata.bootcamp.mscredits.model.Credit;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ICreditService {
    public Mono<Credit> createCredit(Mono<Credit> credit);

    public Flux<Credit> findAllCredits();

    public Mono<Credit> updateCredit(Credit credit);

    public Flux<Credit> findCreditByNroDoc(String nroDoc);
}
