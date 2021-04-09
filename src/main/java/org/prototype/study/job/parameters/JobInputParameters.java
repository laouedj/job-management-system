package org.prototype.study.job.parameters;

import java.util.LinkedHashMap;
import java.util.Map;

public class JobInputParameters {

    private final Map<String, JobInputParameter> jobInputParameterMap;

    public JobInputParameters() {
        this.jobInputParameterMap = new LinkedHashMap<String, JobInputParameter>();
    }

    public JobInputParameters(Map<String, JobInputParameter> jobInputParameterMap) {
        this.jobInputParameterMap = new LinkedHashMap<String, JobInputParameter>(jobInputParameterMap);
    }

    public void addInputParameter(String key, JobInputParameter param) {
        this.jobInputParameterMap.put(key, param);
    }

    public Object getJobInputParameter(String key) {
        if (!jobInputParameterMap.containsKey(key)){
            return  null;
        }

        return  jobInputParameterMap.get(key).getValue();
    }

    public Map<String, JobInputParameter> getJobInputParameterMap() {
        return new LinkedHashMap<String, JobInputParameter>(jobInputParameterMap);
    }

    public boolean isEmpty(){
        return (jobInputParameterMap == null || jobInputParameterMap.isEmpty());
    }


    @Override
    public String toString() {
        return jobInputParameterMap.toString();
    }



}
