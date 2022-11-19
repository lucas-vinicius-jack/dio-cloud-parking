package one.digitalinnovation.cloudparking.service;

import one.digitalinnovation.cloudparking.exception.ParkingNotFoundException;
import one.digitalinnovation.cloudparking.model.Parking;
import one.digitalinnovation.cloudparking.repository.ParkingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ParkingService {

    //private static Map<String, Parking> parkingMap = new HashMap();

    private final ParkingRepository parkingRepository;

    public ParkingService(ParkingRepository parkingRepository) {
        this.parkingRepository = parkingRepository;
    }

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
    @Transactional(readOnly = true,propagation = Propagation.SUPPORTS)
    public List<Parking> findAll(){
        return parkingRepository.findAll();
    }

    private static String getUUID(){
        return UUID.randomUUID().toString().replace("-", "-");
    }

    @Transactional(readOnly = true)
    public Parking findById(String id){
        return parkingRepository.findById(id).orElseThrow(() ->
                    new ParkingNotFoundException(id));
    }

    @Transactional
    public Parking create(Parking parkingCreate) {
        String uuid = getUUID();
        parkingCreate.setId(uuid);
        parkingCreate.setEntryDate(LocalDateTime.now());
        parkingRepository.save(parkingCreate);
        return parkingCreate;
    }

    @Transactional
    public void delete(String id){
        findById(id);
        parkingRepository.deleteById(id);
    }

    public Parking update(String id, Parking parkingCreate){
        Parking parking = findById(id);
        parking.setColor(parkingCreate.getColor());
        parking.setState(parkingCreate.getState());
        parking.setModel(parkingCreate.getModel());
        parking.setLicense(parkingCreate.getLicense());
        parkingRepository.save(parking);
        return parking;
    }


    public Parking checkOut(String id){
        Parking parking = findById(id);
        parking.setExitDate(LocalDateTime.now());

        parking.setBill(ParkingCheckOut.getBill(parking));

        parkingRepository.save(parking);
        return new Parking();
    }



}
