package org.prototype.study.job.parameters;

import java.time.LocalDateTime;

public enum ParameterType implements AbstractDataType {

    LONG,
    DOUBLE,
    STRING,
    DATE {
        @Override
        public LocalDateTime getValue(Object input) {
            return LocalDateTime.parse(input.toString());
        }
    };

    @Override
    public Object getValue(Object input) {
        return input;
    }
}
