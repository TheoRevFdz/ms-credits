package nttdata.bootcamp.mscredits.interfaces;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import nttdata.bootcamp.mscredits.dto.CreditTransactionDTO;
import nttdata.bootcamp.mscredits.dto.TransactionListDTO;

@Service
public class CreditTransactionServiceImpl implements ICreditTransactionService {

    @Autowired
    private RestTemplate rest;

    @Override
    public TransactionListDTO findTransactionByNroCreditAndType(String nroCredit, String type) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("nroCredit", nroCredit);
        params.put("type", type);
        String uri = "http://localhost:8090/api/credits_transactions/{nroCredit}/{type}";

        ResponseEntity<List<CreditTransactionDTO>> response = rest.exchange(
                uri,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<CreditTransactionDTO>>() {
                }, params);
        TransactionListDTO dto = TransactionListDTO.builder()
                .transactions(response.getBody())
                .build();
        return dto;
    }

    @Override
    public Boolean validateFee(String nroDoc) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("nroDoc", nroDoc);
        String uri = "http://localhost:8090/api/credits_transactions/validateFee/{nroDoc}";
        Boolean resp = rest.getForObject(uri, Boolean.class, params);
        return resp;
    }

}
