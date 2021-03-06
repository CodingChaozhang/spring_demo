package com.stream.gkrpc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.ws.Service;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;

/**
 * @author : codingchao
 * @date : 2022-01-20 09:44
 * @Description: 服务描述【服务的key值，包含类，方法，返回值类型，参数类型数组】
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceDescriptor {
    private String clazz; //类名
    private String method; //方法名
    private String returnType; //返回类型
    private String[] parameterTypes; //参数类型

    public static ServiceDescriptor from(Class clazz, Method method){
        ServiceDescriptor sdp = new ServiceDescriptor();
        sdp.setClazz(clazz.getName());
        sdp.setMethod(method.getName());
        sdp.setReturnType(method.getReturnType().getName());

        Class[] parameterClasses = method.getParameterTypes();
        String[] parameterTypes = new String[parameterClasses.length];
        for(int i =0;i<parameterClasses.length;i++){
            parameterTypes[i] = parameterClasses[i].getName();
        }
        sdp.setParameterTypes(parameterTypes);

        return sdp;
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this==obj)return true;
        if (obj==null||getClass()!=obj.getClass())return false;
        ServiceDescriptor that = (ServiceDescriptor) obj;
        return Objects.equals(clazz, that.clazz) &&
                Objects.equals(method, that.method) &&
                Objects.equals(returnType, that.returnType) &&
                Arrays.equals(parameterTypes, that.parameterTypes);
    }

    @Override
    public String toString() {
        return "clazz="+clazz +",method="+method+"，returnType=" +returnType+",parameterTypes="+ Arrays.toString(parameterTypes);
    }
}
