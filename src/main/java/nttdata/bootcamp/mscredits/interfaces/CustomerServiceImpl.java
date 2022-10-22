package nttdata.bootcamp.mscredits.interfaces;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import nttdata.bootcamp.mscredits.dto.CustomerDTO;

@Service
public class CustomerServiceImpl implements ICustomerService {
    
    @Autowired
    private RestTemplate customerRest;

    @Override
    public Optional<CustomerDTO> findCustomerByNroDoc(String nroDoc) throws ParseException {
        Map<String, String> pathVar = new HashMap<String, String>();
        pathVar.put("nroDoc", nroDoc);
        String uri = "http://localhost:8090/api/customers/byNroDoc/{nroDoc}";
        // String uri = "http://localhost:8081/byNroDoc/{nroDoc}";
        CustomerDTO dto = customerRest.getForObject(
                uri,
                CustomerDTO.class,
                pathVar);
        return Optional.ofNullable(dto);
    }

}
