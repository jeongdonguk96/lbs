package io.spring.test.mysql;

import io.spring.test.entity.Geospatial;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MysqlService {

    private final GeospatialRepository geospatialRepository;

    @Transactional
    public void insertMmsData(List<Document> documentList) {
        long startTime;
        long endTime;
        long timeDiff;
        double transactionTime;

        List<Geospatial> geospatialList = new ArrayList<>();

        startTime = System.currentTimeMillis();
        for (Document document : documentList) {
            Geospatial geospatial = convertDocumentToGeospatial(document);
            geospatialList.add(geospatial);
        }
        endTime = System.currentTimeMillis();

        timeDiff = (endTime - startTime);
        transactionTime = timeDiff / 1000.0;

        System.out.println("Geospatial 객체 수 = " + geospatialList.size() + ", document -> entity 매핑 시간 = " + transactionTime + "s");

        startTime = System.currentTimeMillis();
        geospatialRepository.saveAll(geospatialList);
        endTime = System.currentTimeMillis();

        timeDiff = (endTime - startTime);
        transactionTime = timeDiff / 1000.0;
        System.out.println("영속화 TRX 시간 = " + transactionTime + "s");
        System.out.println();
    }

    public Geospatial convertDocumentToGeospatial(Document document) {
        Geospatial geospatial = new Geospatial();
        geospatial.setId(document.getInteger("_id"));
        geospatial.setNameCode(document.getInteger("name_code"));
        Document locationDocument = (Document) document.get("location");
        String type = locationDocument.getString("type");
        String coordinates = locationDocument.get("coordinates", List.class).toString();
        geospatial.setLocation(new Geospatial.Location(type, coordinates));
        geospatial.setUseYn(document.getString("use_yn"));
        geospatial.setCreate_dt(document.getDate("create_dt"));

        return geospatial;
    }

}
