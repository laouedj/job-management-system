package org.prototype.study.job.parameters;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

public class JobInputParametersBuilder {

    private final Map<String, JobInputParameter> jobInputParameterMap;


    public JobInputParametersBuilder() {
        this.jobInputParameterMap = new LinkedHashMap<String, JobInputParameter>();
    }


    public JobInputParametersBuilder(JobInputParameters jobInputParameters) {

        assertNotNull(jobInputParameters, "JobInputParameters must not be null");
        this.jobInputParameterMap = new LinkedHashMap<String, JobInputParameter>(jobInputParameters.getJobInputParameterMap());
    }


    public JobInputParametersBuilder addInputParameter(String key, Object value, ParameterType type) {

        assertNotNull(value, "Parameter must not be null");

        JobInputParameter jobInputParameter = new JobInputParameter(type, value);
        this.jobInputParameterMap.put(key, jobInputParameter);
        return this;
    }

    public JobInputParametersBuilder addJobInputParameter(String key, JobInputParameter jobInputParameter) {

        assertNotNull(jobInputParameter, "Parameter must not be null");
        this.jobInputParameterMap.put(key, jobInputParameter);
        return this;
    }

    public JobInputParametersBuilder addJobInputParameters(JobInputParameters jobInputParameters) {

        assertNotNull(jobInputParameters, "Parameter must not be null");

        this.jobInputParameterMap.putAll(jobInputParameters.getJobInputParameterMap());
        return this;
    }


    public JobInputParameters toJobInputParameters() {
        return new JobInputParameters(this.jobInputParameterMap);
    }


    public JobInputParametersBuilder fromProperties(Properties props) {

        if (props == null || props.isEmpty()) {
            return new JobInputParametersBuilder();
        }

        for (Iterator<Map.Entry<Object, Object>> it = props.entrySet().iterator(); it.hasNext(); ) {

            Map.Entry<Object, Object> entry = it.next();
            String key = (String) entry.getKey();
            String value = (String) entry.getValue();

            if (key.contains(".date")) {
                JobInputParameter jobInputParameter = new JobInputParameter(ParameterType.DATE, value);
                this.jobInputParameterMap.put(key, jobInputParameter);
            } else {
                JobInputParameter jobInputParameter = new JobInputParameter(ParameterType.STRING, value);
                this.jobInputParameterMap.put(key, jobInputParameter);
            }

        }

        return this;
    }


    private void assertNotNull(Object input, String message) {
        if (input == null) {
            throw new IllegalArgumentException(message);
        }
    }


}
