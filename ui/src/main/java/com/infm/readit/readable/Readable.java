package com.infm.readit.readable;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.infm.readit.Constants;
import com.infm.readit.R;
import com.infm.readit.database.DataBundle;
import com.infm.readit.essential.TextParser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by infm on 6/12/14. Enjoy ;)
 */
abstract public class Readable implements Serializable {

    public static final int TYPE_TEST = 0;
    public static final int TYPE_CLIPBOARD = 1;
    public static final int TYPE_FILE = 2;
    public static final int TYPE_TXT = 3;
    public static final int TYPE_EPUB = 4;
    public static final int TYPE_SHARED_LINK = 5;
    public static final int TYPE_SHARED_TEXT = 6;
    public static String LOGTAG = "Readable";
    protected StringBuilder text;
    protected String header;
    protected String textType;
    protected Long seconds;
    protected String path;
    protected Integer position;
    protected Integer type;
    protected List<String> wordList;
    protected List<Integer> delayList;
    protected List<Integer> emphasisList;
    protected List<Integer> timeSuffixSum;
    protected Boolean processFailed;
    protected DataBundle rowData;
/*
    protected Pair<Integer, Integer> existingData;
*/

    public Readable(){
        //init all
        text = new StringBuilder();
        wordList = new ArrayList<String>();
        delayList = new ArrayList<Integer>();
        emphasisList = new ArrayList<Integer>();
        timeSuffixSum = new ArrayList<Integer>();
        rowData = new DataBundle();
        processFailed = false;
    }

    public static Readable newInstance(Context context, Bundle bundle){
        Readable readable;
        if (bundle == null){
            readable = newInstance(context,
                    Readable.TYPE_TEST,
                    "",
                    "");
            readable.setPosition(0);
        } else {
            readable = newInstance(context,
                    bundle.getInt(Constants.EXTRA_TYPE, -1),
                    bundle.getString(Intent.EXTRA_TEXT, context.getResources().getString(R.string.sample_text)),
                    bundle.getString(Constants.EXTRA_PATH, ""));
            readable.setPosition(Math.max(bundle.getInt(Constants.EXTRA_POSITION), 0));
        }
        return readable;
    }

    public static Readable newInstance(Context context, Integer intentType, String intentText, String intentPath){
        Readable readable;
        if (TextUtils.isEmpty(intentText)){
            readable = new TestReadable();
            readable.setText(context.getResources().getString(R.string.sample_text));
        } else {
            if (!PreferenceManager.getDefaultSharedPreferences(context).getBoolean(Constants.PREF_CACHE, true))
                intentPath = null;
            switch (intentType){
                case TYPE_TEST:
                    readable = new TestReadable();
                    break;
                case TYPE_CLIPBOARD:
                    readable = new ClipboardReadable();
                    break;
                case TYPE_FILE:
                    readable = new FileReadable();
                    break;
                case TYPE_TXT:
                    readable = new FileReadable();
                    break;
                case TYPE_EPUB:
                    readable = new FileReadable();
                    break;
                default:
                    String link;
                    if (intentText.length() < Constants.NON_LINK_LENGTH &&
                            !TextUtils.isEmpty(link = TextParser.findLink(TextParser.compilePattern(), intentText))){
                        readable = new NetReadable(link);
                    } else {
                        readable = new ClipboardReadable(); // actually I don't know what to do here
                        readable.setText(intentText);
                    }
            }
            readable.setPath(intentPath);
        }
        return readable;
    }

    abstract public void process(Context context);

    public Integer getType(){
        return type;
    }

    public void setType(Integer type){
        this.type = type;
    }

    public Long getSeconds(){
        return seconds;
    }

    public void setSeconds(Long seconds){
        this.seconds = seconds;
    }

    public Boolean getProcessFailed(){
        return processFailed;
    }

    public void setProcessFailed(Boolean processFailed){
        this.processFailed = processFailed;
    }

    public String getText(){
        return text.toString();
    }

    public void setText(String text){
        this.text = new StringBuilder(text);
    }

    public String getHeader(){
        return header;
    }

    public void setHeader(String header){
        this.header = header;
    }

    public String getTextType(){
        return textType;
    }

    public void setTextType(String textType){
        this.textType = textType;
    }

    public Long getDateChanged(){
        return seconds;
    }

    public void setDateChanged(Long seconds){
        this.seconds = seconds;
    }

    public String getPath(){
        return path;
    }

    public void setPath(String path){
        this.path = path;
    }

    public Integer getPosition(){
        return position;
    }

    public void setPosition(Integer position){
        this.position = position;
    }

    public List<String> getWordList(){
        return wordList;
    }

    public void setWordList(List<String> wordList){
        this.wordList = wordList;
    }

    public List<Integer> getDelayList(){
        return delayList;
    }

    public void setDelayList(List<Integer> delayList){
        this.delayList = delayList;
    }

    public List<Integer> getEmphasisList(){
        return emphasisList;
    }

    public void setEmphasisList(List<Integer> emphasisList){
        this.emphasisList = emphasisList;
    }

    public List<Integer> getTimeSuffixSum(){
        return timeSuffixSum;
    }

    public void setTimeSuffixSum(List<Integer> timeSuffixSum){
        this.timeSuffixSum = timeSuffixSum;
    }
}