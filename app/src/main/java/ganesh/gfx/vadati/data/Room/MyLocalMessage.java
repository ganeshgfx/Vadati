package ganesh.gfx.vadati.data.Room;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class MyLocalMessage {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "send_to")
    public String sender;
    @ColumnInfo(name = "massage")
    public String msgJson;
}
