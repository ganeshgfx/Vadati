package ganesh.gfx.vadati.data.Room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface MyLocalMessageDao {
    @Query("Select * FROM mylocalmessage")
    List<MyLocalMessage> getAllMsg();

    @Query("Select * FROM mylocalmessage WHERE send_to = :senders")
    List<MyLocalMessage> getMsgBySender(String senders);

    @Insert
    void insertMessage(MyLocalMessage... msg);

    @Delete
    void deleteMessage(MyLocalMessage msg);
}
