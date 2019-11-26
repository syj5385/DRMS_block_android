package jjun.jjunapp.programdrs.Command;

/**
 * Created by jjunj on 2017-10-23.
 */

public class ErrorContent {

    // Error Code
    public static final int BasicCommand = 0;
    public static final int Command_size = 1;
    public static final int START_duplicate = 2;
    public static final int END_duplicate = 3;
    public static final int START_location = 4;
    public static final int END_location = 5;
    public static final int Timer_value = 6;
    public static final int Jump_value = 7;
    public static final int Jump_location = 8;
    public static final int Jump_set = 9;

    private static final String[] error = {
            "'Start' 명령어와 'End' 명령어는 항상 처음과 뒤에 한쌍이 반드시 배치 되어야 합니다.",
            "명령어가 존재하지 않습니다.",
            "'Start' 명령어는 두 개 이상을 배치 할 수 없습니다.",
            "'End' 명령어는 두 개 이상 배치 할 수 없습니다.",
            "'Start' 명령어는 항상 가장 앞에 배치되어야 합니다.",
            "'End' 명령어는 항상 가장 뒤에 배치되어야 합니다.",
            "'Timer' 명령어의 지연 시간은 0이 될 수 없습니다.",
            "'Jump' 명령어의 반복 횟수는 0이 될 수 없습니다.",
            "'Jump 명령어는 'Down' -> 'Up' 순서로 배치되어야 합니다.",
            "'Jump' 명령어는 Up 과 Down이 쌍으로 배치되어야 합니다. n(UP) == n(Down)"
    };

    private int index ;
    private int errorcode;
    private String content;

    public ErrorContent(int errorcode, int index) {
        this.index = index;
        this.errorcode = errorcode;
        content = error[errorcode];
    }

    public int getIndex(){
        return index;
    }

    public int getErrorcode(){
        return errorcode;
    }

    public String getContent(){
        return content;
    }

    public String getContent(int code){
        return error[code];
    }
}
