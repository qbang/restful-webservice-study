package com.example.restfulwebservicestudy.user;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
@AllArgsConstructor
public class User {
    private Integer id;

    @Size(min = 2, message = "Name은 2글자 이상 입력해 주세요.")
    private String name; // @Size는 최소 길이를 2로 지정

    @Past
    private Date joinDate; // @Past는 현재 시점으로부터 과거 데이터만 허용

}
