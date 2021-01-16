package com.mnykolaichuk.luv2code.springboot.thymeleafdemo.model.entityData;

import com.mnykolaichuk.luv2code.springboot.thymeleafdemo.validation.FieldMatch;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@FieldMatch.List({
        @FieldMatch(first = "password", second = "matchingPassword", message = " muszą byc jednakowe")
})
public class OrderData implements Serializable {
    private Integer orderAnswerId;

    @NotNull(message = "is required")
    private String description;

    @NotNull(message = "is required")
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime creationDate;

    @NotNull(message = "is required")
    private String cityName;

    private EmployeeDetailData employeeDetailData;
    private CarData carData;

    public OrderData() {
    }

    public Integer getOrderAnswerId() {
        return orderAnswerId;
    }

    public void setOrderAnswerId(Integer orderAnswerId) {
        this.orderAnswerId = orderAnswerId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public EmployeeDetailData getEmployeeDetailData() {
        return employeeDetailData;
    }

    public void setEmployeeDetailData(EmployeeDetailData employeeDetailData) {
        this.employeeDetailData = employeeDetailData;
    }

    public CarData getCarData() {
        return carData;
    }

    public void setCarData(CarData carData) {
        this.carData = carData;
    }
}
