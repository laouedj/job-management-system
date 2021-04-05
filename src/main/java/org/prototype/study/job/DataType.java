package org.prototype.study.job;

import java.util.Date;

public enum DataType implements AbstractDataType{

    LONG,
    DOUBLE,
    STRING,
    DATE {
        @Override
        public Date getValue(Object input) {
            return new Date(((Date) input).getTime());
        }
    };

    @Override
    public Object getValue(Object input) {
        return input;
    }
}
