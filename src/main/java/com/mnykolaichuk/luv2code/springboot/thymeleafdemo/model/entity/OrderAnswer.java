package com.mnykolaichuk.luv2code.springboot.thymeleafdemo.model.entity;

import com.mnykolaichuk.luv2code.springboot.thymeleafdemo.converter.LocalDateAttributeConverter;
import com.mnykolaichuk.luv2code.springboot.thymeleafdemo.model.Stan;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
    @Table(name = "order_answer")
    public class OrderAnswer {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id")
        private Integer id;

        @Column(name = "implementation_date")
        @Convert(converter = LocalDateAttributeConverter.class)
        private LocalDate implementationDate;

        @Enumerated(EnumType.STRING)
        @Column(name = "stan")
        private Stan stan;

        @Column(name = "price")
        private double price;

        @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
        @JoinColumn(name = "order_id")
        private Order order;

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "workshop_order_answer"
            , joinColumns = @JoinColumn(name = "order_answer_id")
            , inverseJoinColumns = @JoinColumn(name = "workshop_id"))
    private List<Workshop> workshops;

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

    public Stan getStan() {
        return stan;
    }

    public void setStan(Stan stan) {
        this.stan = stan;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public List<Workshop> getWorkshops() {
        return workshops;
    }

    public void setWorkshops(List<Workshop> workshops) {
        this.workshops = workshops;
    }

    @Override
    public String toString() {
        return "OrderAnswer{" +
                "id=" + id +
                ", implementationDate=" + implementationDate +
                ", stan=" + stan +
                ", price=" + price +
                ", order=" + order +
                ", workshops=" + workshops +
                '}';
    }
}
