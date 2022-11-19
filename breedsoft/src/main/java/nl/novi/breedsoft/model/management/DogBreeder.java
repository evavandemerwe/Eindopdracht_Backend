package nl.novi.breedsoft.model.management;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

public class DogBreeder extends DogOwner {
    HashMap<LocalDateTime,List<DomesticatedDog>> litters;
    List<WaitingListItem> waitingList;
}
