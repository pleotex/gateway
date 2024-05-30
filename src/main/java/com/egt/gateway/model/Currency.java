package com.egt.gateway.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "currencies")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Currency {

    @Id
    @Column(name = "currency_code", length = 3)
    private String currencyCode;

//    Not unique because Sierra Leonean Leone has 2 currency_codes
    @Column(name = "currency_name")
    private String currencyName;
}
