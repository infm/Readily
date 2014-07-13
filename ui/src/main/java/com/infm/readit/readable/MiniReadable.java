package com.infm.readit.readable;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import com.infm.readit.Constants;
import com.infm.readit.database.LastReadDBHelper;
import com.infm.readit.service.LastReadService;

import java.util.ArrayList;

/**
 * Created by infm on 7/11/14. Enjoy ;)
 */
public class MiniReadable extends Readable {

	String percent;

	public MiniReadable(){}

	public MiniReadable(String path, String header, String percent, int position){
		this.path = path;
		this.header = header;
		this.percent = percent;
        this.position = position;
	}

	public static ArrayList<MiniReadable> getFromCursor(Cursor cursor){
		ArrayList<MiniReadable> result = new ArrayList<MiniReadable>();
		if (cursor != null){
			while (cursor.moveToNext())
				result.add(new MiniReadable(
						cursor.getString(LastReadDBHelper.COLUMN_PATH),
						cursor.getString(LastReadDBHelper.COLUMN_HEADER),
						cursor.getString(LastReadDBHelper.COLUMN_PERCENT),
                        cursor.getInt(LastReadDBHelper.COLUMN_POSITION)
				));
		}
		return result;
	}

	public String getPercent(){
		return percent;
	}

	public void setPercent(String percent){
		this.percent = percent;
	}

	@Override
	public void process(Context context){}

    @Override
    public String toString() {
        return header + ", " + path + ", " + percent + ".";
    }

    public static Intent createDBServiceIntent(Context context, MiniReadable readable){
        Intent intent = new Intent(context, LastReadService.class);
        intent.putExtra(Constants.EXTRA_HEADER, readable.getHeader());
        intent.putExtra(Constants.EXTRA_PATH, readable.getPath());
        intent.putExtra(Constants.EXTRA_POSITION, readable.getPosition());
        intent.putExtra(Constants.EXTRA_PERCENT, readable.getPercent()); //dirty, dirty hack..
        return intent;
    }
}