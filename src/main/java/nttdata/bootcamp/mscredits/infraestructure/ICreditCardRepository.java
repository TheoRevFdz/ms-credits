package nttdata.bootcamp.mscredits.infraestructure;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import nttdata.bootcamp.mscredits.model.CreditCard;

@Repository
public interface ICreditCardRepository extends MongoRepository<CreditCard,String> {
    public CreditCard findByNroCredit(String nroCredit);
}
