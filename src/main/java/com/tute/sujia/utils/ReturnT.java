package com.tute.sujia.utils;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel("返回类型")
@Data
public class ReturnT<T> implements Serializable {
    public static ReturnT<String> successReturn = new ReturnT<>(Constants.SUCCESS, Constants.SUCCESS_MSG);
    public static ReturnT<String> errorReturn = new ReturnT<>(Constants.FAIL, Constants.FAIL_MSG);
    @ApiModelProperty("返回信息")
    private String message;
    @ApiModelProperty("返回结果对象")
    private T obj;
    @ApiModelProperty("返回代码")
    private int code;
    /**
     * 当前页码
     */
    @ApiModelProperty("当前页码")
    private int pageNum;
    /**
     * 每页数量
     */
    @ApiModelProperty("每页数量")
    private int pageSize;
    /**
     * 记录总数
     */
    @ApiModelProperty("记录总数")
    private long totalSize;
    /**
     * 页码总数
     */
    @ApiModelProperty("页码总数")
    private int totalPages;

    public ReturnT(){

    }
    public ReturnT(T obj){
        this.obj = obj;
    }

    public ReturnT(int code, String message){
        this.code = code;
        this.message = message;
    }

    public ReturnT(int code, String message, T obj){
        this.code = code;
        this.message = message;
        this.obj = obj;
    }

    public ReturnT(int code, T obj){
        this.code = code;
        this.obj=obj;
    }

}
