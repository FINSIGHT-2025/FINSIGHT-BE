package com.finsight.backend.handler;

import com.finsight.backend.enumerate.BaseEnum;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.lang.reflect.Method;
import java.sql.*;

public class GenericEnumTypeHandler<E extends Enum<E> & BaseEnum> extends BaseTypeHandler<E> {

    private Class<E> type;
    private Method fromDbValueMethod;
    public GenericEnumTypeHandler(){

    }
    public GenericEnumTypeHandler(Class<E> type) {
        this.type = type;
        try {
            this.fromDbValueMethod = type.getMethod("fromDbValue", String.class);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException(type.getName() + " must have fromDbValue(String) method");
        }
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter.getDbValue());
    }

    @Override
    public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return convert(rs.getString(columnName));
    }

    @Override
    public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return convert(rs.getString(columnIndex));
    }

    @Override
    public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return convert(cs.getString(columnIndex));
    }

    private E convert(String dbValue) {
        if (dbValue == null) return null;
        try {
            @SuppressWarnings("unchecked")
            E result = (E) fromDbValueMethod.invoke(null, dbValue);
            return result;
        } catch (Exception e) {
            throw new IllegalArgumentException("Error converting '" + dbValue + "' to enum " + type.getSimpleName(), e);
        }
    }
}
