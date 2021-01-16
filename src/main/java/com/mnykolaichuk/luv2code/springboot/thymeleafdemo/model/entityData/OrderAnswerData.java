package com.mnykolaichuk.luv2code.springboot.thymeleafdemo.model.entityData;

import com.mnykolaichuk.luv2code.springboot.thymeleafdemo.model.Stan;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class OrderAnswerData {
    private Integer id;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate implementationDate;

    private Double price;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "is required")
    private Stan stan;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getImplementationDate() {
        return implementationDate;
    }

    public void setImplementationDate(LocalDate implementationDate) {
        this.implementationDate = implementationDate;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Stan getStan() {
        return stan;
    }

    public void setStan(Stan stan) {
        this.stan = stan;
    }
}
