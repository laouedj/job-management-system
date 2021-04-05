package org.prototype.study.job;

import java.util.LinkedHashMap;
import java.util.Map;

public class JobInputDataList {

    private final Map<String, InputData> inputDataList;

    public JobInputDataList() {
        this.inputDataList = new LinkedHashMap<String, InputData>();
    }

    public JobInputDataList(Map<String, InputData> inputDataList) {
        this.inputDataList = new LinkedHashMap<String, InputData>(inputDataList);
    }

    public Object getInputData(String key) {
        if (!inputDataList.containsKey(key)){
            return  null;
        }
        return  inputDataList.get(key).getValue();
    }

    public Map<String, InputData> getInputDataList() {
        return new LinkedHashMap<String, InputData>(inputDataList);
    }

    public boolean isEmpty(){
        return inputDataList.isEmpty();
    }



}
