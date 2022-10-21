package nttdata.bootcamp.mscredits.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document("credits")
public class Credit {
    @Id
    private String id;
    private String nroDoc;
    private Double creditLine;
    private String type;
    private Date dateReg;
}
