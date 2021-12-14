package ganesh.gfx.vadati.data.Room;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class LocalMessage {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "send_by")
    public String sender;
    @ColumnInfo(name = "massage")
    public String msgJson;
}

