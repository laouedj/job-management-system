package org.prototype.study.job.parameters;

import org.prototype.study.job.parameters.AbstractDataType;

import java.time.LocalDateTime;

public enum DataType implements AbstractDataType {

    LONG,
    DOUBLE,
    STRING,
    DATE {
        @Override
        public LocalDateTime getValue(Object input) {
            return LocalDateTime.parse((String)input);
        }
    };

    @Override
    public Object getValue(Object input) {
        return input;
    }
}
