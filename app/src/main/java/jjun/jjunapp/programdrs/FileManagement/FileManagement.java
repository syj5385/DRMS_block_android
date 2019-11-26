package jjun.jjunapp.programdrs.FileManagement;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;



import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.FieldPosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import jjun.jjunapp.programdrs.Activity.MainActivity;
import jjun.jjunapp.programdrs.Command.CommandIcon;
import jjun.jjunapp.programdrs.CustomAdapter.CustomAdapter1.Custom1_Item;
import jjun.jjunapp.programdrs.CustomAdapter.CustomAdapter1.CustomAdapter1;
import jjun.jjunapp.programdrs.CustomAdapter.CustomAdatper2.Custom2_Item;
import jjun.jjunapp.programdrs.R;

/**
 * Created by jjunj on 2017-09-15.
 */

public class FileManagement {

    private static final String TAG = "FileManagement.class";
    private static final int FILEMANAGEMENT = 4;
    public static final int FINISHED_WRITE_DATA = 41;
    public static final int FINISHED_READ_DATA = 42;
    public static final int FAILED_READ_DATA = 43;
    public static final int REQUEST_SAVE_TEMP = 44;
    public static final int REQUEST_OPEN_TEMP = 45;

    private Context context;
    private ArrayList<File> myFile_list = new ArrayList<File>();

    private String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath();
    private String filePath = Environment.getExternalStorageDirectory().getAbsolutePath();
    private String tempPath = Environment.getExternalStorageDirectory().getAbsolutePath();

    private String BTdirPath = Environment.getExternalStorageDirectory().getAbsolutePath();

    private File myFile;

    private File[] filelist ;

    private CustomAdapter1 Adapter;

    private FileOutputStream outputStream;
    private FileInputStream inputStream;

    private Handler mHandler ;


    public FileManagement(Context context, Handler mHandler ){
        this.context = context;
        this.mHandler = mHandler;

        dirPath += "/DrsProgramming/firmwareData";
        BTdirPath += "/DrsProgramming/bluetooth";
        tempPath +="/DrsProgramming/temp";

        myFile = new File(dirPath);
        myFile.mkdirs(); // if the directory does not exist, Create new Directory in ExternalStorage in Android

        filelist = myFile.listFiles();

        myFile = new File(BTdirPath);
        myFile.mkdirs();

        myFile = new File(tempPath);
        myFile.mkdir();

        createBTFile();
    }

    public boolean isTempExist(){
        boolean isExsit;
        myFile = new File(tempPath+"/temp.drs");
        if(myFile.exists()){
            isExsit = true;
        }
        else{
            isExsit = false;
        }

        return isExsit;
    }

    public void initializeTempFile(){
        myFile = new File(tempPath + "/temp.drs");
        if(myFile.exists()){
            Log.d(TAG,"temp file exists");
            mHandler.obtainMessage(FILEMANAGEMENT,REQUEST_OPEN_TEMP,-1,readCommandFromTempFile()).sendToTarget();
        }
        else{
            Log.d(TAG,"temp file doesn`t exist");
            try{
                myFile.createNewFile();
                Log.d(TAG,"create temp file");
            }catch (IOException e){
                Log.e(TAG,"IOException during making temp file");
            }
        }
    }

    public void removeTempFile(){
        myFile = new File(tempPath+"/temp.drs");
        if(myFile.exists()){
            Log.d(TAG,"temp file exists");
            myFile.delete();
        }
        else{

        }
    }


    public CustomAdapter1 getMyFileAdapter(){
        CustomAdapter1 mAdapter = new CustomAdapter1(context);

        myFile = new File(dirPath);

        for(int i=0; i<filelist.length; i++){
            String temp = filelist[i].getName();

            int index = 0 ;
            for(int j=0; j< temp.length() ; j++){
                if(temp.charAt(index++) == '.'){
                    break;
                }
            }

            char[] temp2 = new char[index];
            for(int k=0 ; k<index-1 ; k++){
                temp2[k] = temp.charAt(k);
            }

            String filename = new String(temp2);

            mAdapter.addItem(new Custom1_Item(
                    context.getResources().getDrawable(R.mipmap.file_icon)
                    ,filename));
        }

        return mAdapter;
    }

    public boolean isFileExist(String fileName){
        myFile = new File(dirPath + "/" + fileName);
        Log.d("File","check file name = " + fileName);
        if(myFile.exists()){
            return true;
        }
        else{
            return false;
        }
    }

    public boolean createFiles(String fileName){
        boolean success = false;
        filePath = dirPath +"/" + fileName + ".drs";
        myFile = new File(filePath);
        Log.d("File","New file name is " + fileName);

        if(!myFile.exists()) {
            try {
                myFile.createNewFile();

            } catch (IOException e) {
                Log.e(getClass().getSimpleName(), "IOException");
            }
            success= true;

        }
        else{
            Toast.makeText(context, "'" + fileName + ".drs'" + " 이름의 파일이 이미 존재합니다.\n" +
                    "다른이름의 파일로 저장해주세요.", Toast.LENGTH_SHORT).show();
            success = false;
        }
        return success;

    }
    private boolean createBTFile(){
        boolean success = false;
        String Btfile_Path = BTdirPath+"/"+"bluetoothAddr.txt";
        myFile = new File(Btfile_Path);
        if(!myFile.exists()){
            try{
                myFile.createNewFile();
                success= true;
            }catch (IOException e){
                Log.e(getClass().getSimpleName(), "IOException");
                success= false;
            }

        }
        return success;
    }

    public boolean writeCommandOnFile(String fileName, ArrayList<CommandIcon> command_icon) {
        boolean success = false;
        int sizeOfCommand = command_icon.size();

        Log.d("SaveFile","command size = " + String.valueOf(sizeOfCommand));
        ArrayList<Byte> datafile = new ArrayList<Byte>();
        Character[][] dataarray = new Character[7][sizeOfCommand];
        for(int i=0; i<sizeOfCommand; i++){
            dataarray[0][i] = (char)(i & 0xff);
            dataarray[1][i] = (char)((i >> 8) & 0xff);
            dataarray[2][i] = (char)(command_icon.get(i).getCommand() & 0xff);
            dataarray[3][i] = (char)(command_icon.get(i).getValue() & 0xff);
            dataarray[4][i] = (char)((command_icon.get(i).getValue() >> 8) & 0xff);
            dataarray[5][i] = (char)(command_icon.get(i).getValue2() & 0xff);
            dataarray[6][i] = (char)((command_icon.get(i).getValue2() >> 8 ) & 0xff);
        }

        return sendRequest_WriteOnFile(fileName, requestCommand(dataarray,7,sizeOfCommand));
    }

    public boolean writeCommandOnTempFile( ArrayList<CommandIcon> command_icon) {
        boolean success = false;
        int sizeOfCommand = command_icon.size();

        Log.d("SaveFile","command size = " + String.valueOf(sizeOfCommand));
        ArrayList<Byte> datafile = new ArrayList<Byte>();
        Character[][] dataarray = new Character[7][sizeOfCommand];
        for(int i=0; i<sizeOfCommand; i++){
            dataarray[0][i] = (char)(i & 0xff);
            dataarray[1][i] = (char)((i >> 8) & 0xff);
            dataarray[2][i] = (char)(command_icon.get(i).getCommand() & 0xff);
            dataarray[3][i] = (char)(command_icon.get(i).getValue() & 0xff);
            dataarray[4][i] = (char)((command_icon.get(i).getValue() >> 8) & 0xff);
            dataarray[5][i] = (char)(command_icon.get(i).getValue2() & 0xff);
            dataarray[6][i] = (char)((command_icon.get(i).getValue2() >> 8 ) & 0xff);
        }

        return sendRequest_WriteOnTempFile(requestCommand(dataarray,7,sizeOfCommand));
    }

    public ArrayList<int[]> readCommandFromFile(String Filename){
        ArrayList<int[]> readCommand = new ArrayList<int[]>();

        myFile = new File(dirPath+"/"+Filename);
        if(myFile.exists()){
            try {
                inputStream = new FileInputStream(dirPath + "/" + Filename);
                BufferedInputStream buffered = new BufferedInputStream(inputStream);
            }catch (IOException e){

            }
            ArrayList<byte[]> array_file = new ArrayList<byte[]>();

            ArrayList<ArrayList<Integer>> readfileArray = new ArrayList<ArrayList<Integer>>();

            BufferedInputStream bufferReader = new BufferedInputStream(inputStream);

            int index = 0;
            Log.d("File","q : " + myFile.length()/13 +  " / r : " + myFile.length()%13);
            for(int i=0; i<myFile.length()/13 ; i++){
                byte[] data_temp = new byte[12];
                byte checksum = 0;
                try {
                    for(int j=0; j<13; j++){
                        int temp = (bufferReader.read() & 0xff)  ;
                        if(j != 12) {
                            data_temp[index++] = (byte) temp;
                            if(j >= 4 && j<11){
                                checksum ^= (byte)temp;
                            }
                        }
                        else if(j == 12){
                            if(checksum == data_temp[11]) {
                                Log.d("File", "checksum");
                                array_file.add(data_temp);
                                index = 0;
                            }
                            else{
                                i = (int)(myFile.length()/13 -1);
                                mHandler.obtainMessage(FILEMANAGEMENT,FAILED_READ_DATA,-1).sendToTarget();
                                break;
                            }
                        }

                    }

                }catch (IOException e){}
            }
            try{
                inputStream.close();
                bufferReader.close();
            }catch (IOException e){};
            Log.d("File", "size Of arrayfile = " + array_file.size());


            for(int i=0; i<array_file.size(); i++){
                String header = "";
                for(int j=0; j<4; j++){
                    header += (char)array_file.get(i)[j];
                }
                Log.d("file","header : " + header);

                int command_index = 4;
                int count = read16(array_file.get(i)[command_index++],array_file.get(i)[command_index++]);
                int command = read8(array_file.get(i)[command_index++]);
                int value0 = read16(array_file.get(i)[command_index++],array_file.get(i)[command_index++]);
                int value1 = read16(array_file.get(i)[command_index++],array_file.get(i)[command_index++]);

                int[] readtemp = {command, value0, value1};
                readCommand.add(readtemp);

                Log.w("File", "count : " + count + " / command : " + command +" / value0 : " + value0 + "/ value1 : " + value1);
            }

        }
        else{
            Log.e("File",Filename +"is not exist");
        }

        return readCommand;
    }

    public ArrayList<int[]> readCommandFromTempFile(){
        ArrayList<int[]> readCommand = new ArrayList<int[]>();

        myFile = new File(tempPath+"/temp.drs");
        if(myFile.exists()){
            try {
                inputStream = new FileInputStream(tempPath + "/temp.drs");
                BufferedInputStream buffered = new BufferedInputStream(inputStream);
            }catch (IOException e){

            }
            ArrayList<byte[]> array_file = new ArrayList<byte[]>();

            ArrayList<ArrayList<Integer>> readfileArray = new ArrayList<ArrayList<Integer>>();

            BufferedInputStream bufferReader = new BufferedInputStream(inputStream);

            int index = 0;
            Log.d("File","q : " + myFile.length()/13 +  " / r : " + myFile.length()%13);
            for(int i=0; i<myFile.length()/13 ; i++){
                byte[] data_temp = new byte[12];
                byte checksum = 0;
                try {
                    for(int j=0; j<13; j++){
                        int temp = (bufferReader.read() & 0xff)  ;
                        if(j != 12) {
                            data_temp[index++] = (byte) temp;
                            if(j >= 4 && j<11){
                                checksum ^= (byte)temp;
                            }
                        }
                        else if(j == 12){
                            if(checksum == data_temp[11]) {
                                Log.d("File", "checksum");
                                array_file.add(data_temp);
                                index = 0;
                            }
                            else{
                                i = (int)(myFile.length()/13 -1);
                                mHandler.obtainMessage(FILEMANAGEMENT,FAILED_READ_DATA,-1).sendToTarget();
                                break;
                            }
                        }

                    }

                }catch (IOException e){}
            }
            try{
                inputStream.close();
                bufferReader.close();
            }catch (IOException e){};
            Log.d("File", "size Of arrayfile = " + array_file.size());


            for(int i=0; i<array_file.size(); i++){
                String header = "";
                for(int j=0; j<4; j++){
                    header += (char)array_file.get(i)[j];
                }
                Log.d("file","header : " + header);

                int command_index = 4;
                int count = read16(array_file.get(i)[command_index++],array_file.get(i)[command_index++]);
                int command = read8(array_file.get(i)[command_index++]);
                int value0 = read16(array_file.get(i)[command_index++],array_file.get(i)[command_index++]);
                int value1 = read16(array_file.get(i)[command_index++],array_file.get(i)[command_index++]);

                int[] readtemp = {command, value0, value1};
                readCommand.add(readtemp);

                Log.w("File", "count : " + count + " / command : " + command +" / value0 : " + value0 + "/ value1 : " + value1);
            }

        }
        else{
            Log.e("File","temp" +"is not exist");
        }

        return readCommand;
    }


    public boolean readCommandFromFile(int index){

        boolean success = false;

        final File readFile = filelist[index];

        String fileName = readFile.getName();

        try {
            inputStream = new FileInputStream(dirPath + "/" + fileName);
            success = true;
        }catch (IOException e){
            success = false;
        }

        new Thread(new Runnable() {
            ArrayList<byte[]> array_file = new ArrayList<byte[]>();
            ArrayList<Integer> command_array = new ArrayList<Integer>();
            ArrayList<Integer> value0_array= new ArrayList<Integer>();
            ArrayList<Integer> value1_array = new ArrayList<Integer>();

            ArrayList<ArrayList<Integer>> readfileArray = new ArrayList<ArrayList<Integer>>();

            @Override
            public void run() {
                BufferedInputStream bufferReader = new BufferedInputStream(inputStream);

                int index = 0;
                Log.d("File","q : " + readFile.length()/13 +  " / r : " + readFile.length()%13);
                for(int i=0; i<readFile.length()/13 ; i++){
                    byte[] data_temp = new byte[12];
                    byte checksum = 0;
                    try {
                       for(int j=0; j<13; j++){
                           int temp = (bufferReader.read() & 0xff)  ;
                           if(j != 12) {
                               data_temp[index++] = (byte) temp;
                               if(j >= 4 && j<11){
                                   checksum ^= (byte)temp;
                               }
                           }
                           else if(j == 12){
                               if(checksum == data_temp[11]) {
                                   Log.d("File", "checksum");
                                   array_file.add(data_temp);
                                   index = 0;
                               }
                               else{
                                   i = (int)(readFile.length()/13 -1);
                                   mHandler.obtainMessage(FILEMANAGEMENT,FAILED_READ_DATA,-1).sendToTarget();
                                   break;
                               }
                           }

                       }

                    }catch (IOException e){}
                }
                try{
                    inputStream.close();
                    bufferReader.close();
                }catch (IOException e){};
                Log.d("File", "size Of arrayfile = " + array_file.size());


                for(int i=0; i<array_file.size(); i++){
                    String header = "";
                    for(int j=0; j<4; j++){
                        header += (char)array_file.get(i)[j];
                    }
                    Log.d("file","header : " + header);

                    int command_index = 4;
                    int count = read16(array_file.get(i)[command_index++],array_file.get(i)[command_index++]);
                    int command = read8(array_file.get(i)[command_index++]);
                    command_array.add(command);
                    int value0 = read16(array_file.get(i)[command_index++],array_file.get(i)[command_index++]);
                    value0_array.add(value0);
                    int value1 = read16(array_file.get(i)[command_index++],array_file.get(i)[command_index++]);
                    value1_array.add(value1);


                    readfileArray.add(command_array);
                    readfileArray.add(value0_array);
                    readfileArray.add(value1_array);


                    Log.w("File", "count : " + count + " / command : " + command +" / value0 : " + value0 + "/ value1 : " + value1);
                }
                if(readfileArray.size() > 0 ) {
                    mHandler.obtainMessage(FILEMANAGEMENT, FINISHED_READ_DATA, 0, readFile.getName());
                    mHandler.obtainMessage(FILEMANAGEMENT, FINISHED_READ_DATA, -1, readfileArray).sendToTarget();
                }
                else{
                    mHandler.obtainMessage(FILEMANAGEMENT,FAILED_READ_DATA,-1).sendToTarget();
                }
            }
        }).start();

        return success;
    }

    public boolean writeBtAddressOnFile(String address){
        boolean success = false;

        myFile = new File(BTdirPath + "/bluetoothAddr.txt");
        if(!myFile.exists()) {
            createBTFile();
        }

        try {
            outputStream = new FileOutputStream(myFile);
            byte[] address_temp = address.getBytes();

            outputStream.write(address_temp);


            success = true;
        } catch (IOException e) {
            success = false;
        }
        ;

        return success;
    }

    public String readBTAddress(){
        String address = null;
        myFile = new File(BTdirPath + "/bluetoothAddr.txt");
        char[] addr = new char[(int)myFile.length()];
        if(myFile.exists()){
            if(myFile.length() > 0) {
                Log.d("FILE", BTdirPath + "bluetooth.txt exist");
                try {
                    inputStream = new FileInputStream(myFile);
                    BufferedInputStream bufferReader = new BufferedInputStream(inputStream);

                    for (int i = 0; i < myFile.length(); i++) {
                        addr[i] = (char) bufferReader.read();
                        Log.w("READ DATA", String.valueOf(addr[i]));
                    }
                    inputStream.close();
                    bufferReader.close();
                } catch (IOException e) {
                }

            }
            else{
                return null;
            }
        }
        else{
            Log.d("FILE",BTdirPath + "bluetooth.txt not exist");
        }

        address = new String(addr);

        return address;
    }


    public void deleteFile(String fileName){

        myFile = new File(dirPath+"/"+ fileName);

        if(myFile.exists()) {
            if(myFile.delete()){
                Toast.makeText(context,"파일을 삭제했습니다.",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(context,"파일을 삭제하지 못했습니다.",Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }

        updateFileList();
    }




    public File[] getFilelist(){
        return filelist;
    }

    private void updateFileList(){
        myFile = new File(dirPath);

        filelist = myFile.listFiles();
    }

    public int read8(byte int_8_1){
        return int_8_1 & 0xff;
    }

    public int read16(byte int_16_1, byte int_16_2){
        return ((int_16_1 & 0xff) + (int_16_2 << 8));
    }

    public List<Byte> requestCommand(Character[][] command, int width, int height){
        String header = "#DRS";
        List<Byte> payload = new LinkedList<Byte>();
        Log.d("File","command size : " + width);

        for(int i=0; i<height; i++){
            //header
            for(byte c : header.getBytes())
                payload.add(c);

//            //data
            byte checksum = 0;
            if(command != null){
                for(int j=0 ; j<width; j++){
                    payload.add((byte)(command[j][i] & 0xff));
                    checksum ^= (command[j][i] & 0xff);
                }

            }

            payload.add(checksum);
            payload.add((byte)'\n');
        }

        return payload;
    }

    private boolean sendRequest_WriteOnFile(String fileName,List<Byte> command){
        final byte[] arr = new byte[command.size()];
        int i=0;
        for(byte b : command){
            arr[i++] = b;
        }

        boolean success = true;

        myFile = new File(dirPath + "/" + fileName);

        try {
            outputStream = new FileOutputStream(myFile);
            success = true;
        } catch (IOException e) {
            Log.e(getClass().getSimpleName(), "IOException");
            success= false;
        }

        new Thread(new Runnable() {
            int index= 0 ;
            @Override
            public void run() {

                try{
                    outputStream.write(arr);
//                    Log.d("WRITE",String.valueOf(arr[index-1]));
                    Log.d("WRITE", new String(arr));
                    try{
                        Thread.sleep(10);
                    }
                    catch (InterruptedException e){};
                }catch (IOException e){

                }

                Log.d("OK","쓰으으으으으으으기이이잉완료오오옹");
                mHandler.obtainMessage(FILEMANAGEMENT,FINISHED_WRITE_DATA,-1).sendToTarget();
                try{
                    outputStream.close();
                }catch (IOException e){}
            }
        }).start();

        if(success) {

            updateFileList();
        }

        updateFileList();

        return success;
    }

    private boolean sendRequest_WriteOnTempFile(List<Byte> command){
        final byte[] arr = new byte[command.size()];
        int i=0;
        for(byte b : command){
            arr[i++] = b;
        }

        boolean success = true;

        myFile = new File(tempPath + "/temp.drs");

        try {
            outputStream = new FileOutputStream(myFile);
            success = true;
        } catch (IOException e) {
            Log.e(getClass().getSimpleName(), "IOException");
            success= false;
        }

        new Thread(new Runnable() {
            int index= 0 ;
            @Override
            public void run() {

                try{
                    outputStream.write(arr);
//                    Log.d("WRITE",String.valueOf(arr[index-1]));
                    Log.d("WRITE", new String(arr));
                    try{
                        Thread.sleep(10);
                    }
                    catch (InterruptedException e){};
                }catch (IOException e){

                }

                Log.d("OK","쓰으으으으으으으기이이잉완료오오옹");
                mHandler.obtainMessage(FILEMANAGEMENT,FINISHED_WRITE_DATA,-1).sendToTarget();
                try{
                    outputStream.close();
                }catch (IOException e){}
            }
        }).start();


        updateFileList();

        return success;
    }

    public boolean isCommandSavedinThisFile(String fileName, ArrayList<CommandIcon> data){
        boolean isSaved = true;
        ArrayList<int[]> inFile = readCommandFromFile(fileName);
        ArrayList<int[]> inCommand = new ArrayList<int[]>();
        for(int i=0; i<data.size(); i++){
            int[] inCommandtemp = {data.get(i).getCommand(),data.get(i).getValue(), data.get(i).getValue2()};
            inCommand.add(inCommandtemp);
        }

        if(inFile.size() == inCommand.size()){
            Log.w("File","The size of these files is equals");
            for(int i=0; i<inFile.size() ;i++){
                Log.d("File", "inFile -> Command : " + inFile.get(i)[0] + " / value0 : " + inFile.get(i)[1] + " / value1 : " + inFile.get(i)[2]);
                Log.d("File", "inCommand -> Command : " + inCommand.get(i)[0] + " / value0 : " + inCommand.get(i)[1] + " / value1 : " + inCommand.get(i)[2]);
                if(!inFile.get(i).equals(inCommand.get(i))){
                    if(isSaved)
                        isSaved = false;
                }
                else{
                    Log.d("File","Pass" + i);
                }
            }
        }
        else{
            isSaved = false;
        }

        return isSaved;
    }

    public boolean isCommandSavedinTempFile( ArrayList<int[]> fromTemp){
        boolean isSaved = true;
        ArrayList<int[]> inFile = readCommandFromTempFile();
        ArrayList<int[]> inTemp = fromTemp;
//        for(int i=0; i<data.size(); i++){
//            int[] inCommandtemp = {data.get(i).getCommand(),data.get(i).getValue(), data.get(i).getValue2()};
//            inCommand.add(inCommandtemp);
//        }

        if(inFile.size() == inTemp.size()){
            Log.w("File","The size of these files is equals");
            for(int i=0; i<inFile.size() ;i++){
                Log.d("File", "inFile -> Command : " + inFile.get(i)[0] + " / value0 : " + inFile.get(i)[1] + " / value1 : " + inFile.get(i)[2]);
                Log.d("File", "inCommand -> Command : " + inTemp.get(i)[0] + " / value0 : " + inTemp.get(i)[1] + " / value1 : " + inTemp.get(i)[2]);
                if(!inFile.get(i).equals(inTemp.get(i))){
                    if(isSaved)
                        isSaved = false;
                }
                else{
                    Log.d("File","Pass" + i);
                }
            }
        }
        else{
            isSaved = false;
        }

        return isSaved;
    }

    private boolean isArrayEquals(int[] inFile, int[] inCommand){
        boolean isequals = true;
        if(inFile.length == inCommand.length){
            Log.w("File","ArraySize equals");
            for(int i=0; i<inFile.length; i++){
                if(!(inFile[i] == inCommand[i])){
                    isequals = false;
                }
            }
        }
        else
            isequals = false;

        return isequals;
    }

    public String makeFileNameUsingData(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd",java.util.Locale.getDefault());
        String todayDate = dateFormat.format(new Date());
        String fileNameTemp ="";

        String month_temp = "";
        String date_temp = "";

        int index = 5;
        for(;index<7;index++)
            month_temp += todayDate.charAt(index);

        for(index++; index<todayDate.length();index++){
            date_temp += todayDate.charAt(index);
        }

        if(month_temp.equals("01")){
            fileNameTemp += "january_";
        }

        else if(month_temp.equals("02")){
            fileNameTemp += "feburary";
        }

        else if(month_temp.equals("03")){
            fileNameTemp += "march_";
        }

        else if(month_temp.equals("04")){
            fileNameTemp += "april_";
        }

        else if(month_temp.equals("05")){
            fileNameTemp += "may_";
        }

        else if(month_temp.equals("06")){
            fileNameTemp += "june_";
        }

        else if(month_temp.equals("07")){
            fileNameTemp += "july_";
        }

        else if(month_temp.equals("08")){
            fileNameTemp += "august_";
        }

        else if(month_temp.equals("09")){
            fileNameTemp += "september_";
        }

        else if(month_temp.equals("10")){
            fileNameTemp += "october_";
        }

        else if(month_temp.equals("11")){
            fileNameTemp += "november";
        }

        else if(month_temp.equals("12")){
            fileNameTemp += "december_";
        }

        fileNameTemp += date_temp;
        String checkname = fileNameTemp;
        Log.d("FIleName",checkname);

        int character = (int)'a';
        Log.d("a",String.valueOf((int)'a'));
        Log.d("Z",String.valueOf((int)'z'));

        while(true){
            fileNameTemp += String.valueOf((char)character++);
            File temp = new File(dirPath+"/"+ fileNameTemp+".drs");
            if(temp.exists()){
                if(character <= (int)'z')
                    fileNameTemp = checkname;
                else{
                    fileNameTemp = checkname;
                    fileNameTemp += 'a';
                    if(character == (int)'z' +1 ){
                        character = (int)'a';
                    }
                }

            }else{
                break;
            }
        }

        return fileNameTemp;
    }

    public boolean fileNameCheck(String filename){
        boolean isPossible = true;
        if(filename.length() > 0 && filename.length() <=15){
            char[] letters = new char[filename.length()];

            for(int i=0; i<letters.length;i++){
                Log.d("File","letter " + i + " : " + (int)letters[i]);
                if((filename.charAt(i) >= 'a' && filename.charAt(i)<='z') || (filename.charAt(i) >= '0' && filename.charAt(i) <= '9') || (filename.charAt(i) == '_')){

                }
                else{
                    if(isPossible)
                        isPossible = false;
                }
            }
        }
        else{
            isPossible = false;
        }

        return isPossible;
    }


}
