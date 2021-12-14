package ganesh.gfx.vadati.data.Room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface LocalMessageDao {
    @Query("Select * FROM localMessage")
    List<LocalMessage> getAllMsg();

    @Query("Select * FROM localMessage WHERE send_by = :sender")
    List<LocalMessage> getMsgBySender(String sender);

    @Insert
    void insertMessage(LocalMessage... msg);

    @Delete
    void deleteMessage(LocalMessage msg);
}
