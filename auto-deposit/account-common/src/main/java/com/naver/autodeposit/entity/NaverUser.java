package com.naver.autodeposit.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
@Getter
@NoArgsConstructor
public class NaverUser extends BaseTime implements Serializable {
    @Id
    private long id;

    @Column(name = "naver_id")
    private String naverId;

    private String password;

    @Builder
    public NaverUser(long id, String naverId, String password) {
        this.id = id;
        this.naverId = naverId;
        this.password = password;
    }
}
