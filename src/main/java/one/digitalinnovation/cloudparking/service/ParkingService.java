package one.digitalinnovation.cloudparking.service;

import one.digitalinnovation.cloudparking.exception.ParkingNotFoundException;
import one.digitalinnovation.cloudparking.model.Parking;
import org.springframework.stereotype.Service;


import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ParkingService {

    private static Map<String, Parking> parkingMap = new HashMap();

/*
    static{
        var id = getUUID();
        var id1 = getUUID();
        Parking parking = new Parking(id, "DMS-1111","SC", "CELTA","PRETO",LocalDateTime.now());
        Parking parking1 = new Parking(id1, "WAS-1234","Sp", "VW GOL","VERMELHO",LocalDateTime.now());
        parkingMap.put(id,parking);
        parkingMap.put(id1,parking1);
    }
 */
    public List<Parking> findAll(){
        return parkingMap.values().stream().collect(Collectors.toList());
    }

    private static String getUUID(){
        return UUID.randomUUID().toString().replace("-", "-");
    }

    public Parking findById(String id){
        Parking parking = parkingMap.get(id);
        if(parking == null){
            throw new ParkingNotFoundException(id);
        }
        return parking;
    }

    public Parking create(Parking parkingCreate) {
        String uuid = getUUID();
        parkingCreate.setId(uuid);
        parkingCreate.setEntryDate(LocalDateTime.now());
        parkingMap.put(uuid, parkingCreate);
        return parkingCreate;
    }

    public void delete(String id){
        findById(id);
        parkingMap.remove(id);
    }

    public Parking update(String id, Parking parkingCreate){
        Parking parking = findById(id);
        parking.setColor(parkingCreate.getColor());
        parkingMap.replace(id, parking);
        return parking;
    }


    public Parking exit(String id){
        Parking parking = findById(id);
        parking.setExitDate(LocalDateTime.now());
        var entrada = parking.getEntryDate();
        var saida = parking.getExitDate();

        // 10 reais a hora
        // o calculo é feito em minutos
        double resultado = ((10/60) * Double.valueOf(ChronoUnit.MINUTES.between(entrada,saida)));

        DecimalFormat df = new DecimalFormat("#,##");
        parking.setBill(Double.valueOf(df.format(resultado)));

        return parking;
    }



}
