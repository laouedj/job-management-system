package org.prototype.study.job.parameters;

public class JobInputParameter {

    private final Object value;
    private final ParameterType type;

    public JobInputParameter(ParameterType type, Object value) {
        this.type = type;
        this.value = value;
    }


    public Object getValue() {
        return this.type.getValue(value);
    }

    public ParameterType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "JobInputParameter{" +
                "value=" + value +
                ", type=" + type +
                '}';
    }
}
