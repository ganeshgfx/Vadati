package ganesh.gfx.vadati.data.Room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {LocalMessage.class,MyLocalMessage.class},version = 13)
public abstract class AppDatabase extends RoomDatabase{
    public abstract LocalMessageDao localMessageDao();
    public abstract MyLocalMessageDao myLocalMessage();
    private static  AppDatabase INSTANCE;
    public static AppDatabase getInstance(Context context){
        if(INSTANCE==null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),AppDatabase.class,"vadati")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }
}
