package org.prototype.study.job;

public class InputData {

    private final Object value;
    private final DataType type;

    public InputData(DataType type, Object value) {
        this.type = type;
        this.value = value;
    }


    public Object getValue() {
        return this.type.getValue(value);
    }

    public DataType getType() {
        return type;
    }
}
