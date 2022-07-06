package com.example.restfulwebservicestudy.user;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.databind.util.BeanUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminUserController {
    private UserDaoService userDaoService;

    public AdminUserController(UserDaoService userDaoService) {
        this.userDaoService = userDaoService;
    }

    @GetMapping("/users")
    public MappingJacksonValue retrieveAllUsers() {
        List<User> users = userDaoService.findAll();

        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter
                .filterOutAllExcept("id", "name", "joinDate", "password"); // 해당 필드만 가져오도록

        FilterProvider filterProvider = new SimpleFilterProvider().addFilter("UserInfo", filter);
        // 단일 조회랑 마찬가지. 파라미터만 바꿔서 넣으면 됨
        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(users);
        mappingJacksonValue.setFilters(filterProvider);


        return mappingJacksonValue;
    }

//    @GetMapping("v1/users/{id}") -> url을 이용해서 버전 관리
//    @GetMapping(value = "/users/{id}", params = "version=1") -> 파라미터를 이용해서 버전 관리
//    @GetMapping(value = "/users/{id}", headers = "X-API-VERSION=1") -> 헤더를 이용한 버전 관리
    @GetMapping(value = "/users/{id}", produces = "application/vnd.company.appv1+json") // -> mime-type(헤더)를 이용한 버전 관리
    public MappingJacksonValue retrieveUserV1(@PathVariable int id) {
        User user = userDaoService.findOne(id);

        if (user == null) {
            throw new UserNotFoundException(String.format("ID[%s] not found", id));
        }

        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter
                .filterOutAllExcept("id", "name", "joinDate", "ssn"); // 해당 필드만 가져오도록

        FilterProvider filterProvider = new SimpleFilterProvider().addFilter("UserInfo", filter);

        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(user);
        mappingJacksonValue.setFilters(filterProvider);

        return mappingJacksonValue;
    }

//    @GetMapping("v2/users/{id}") -> url을 이용해서 버전 관리
//    @GetMapping(value = "/users/{id}", params = "version=2") -> 파라미터를 이용해서 버전 관리
//    @GetMapping(value = "/users/{id}", headers = "X-API-VERSION=2")  -> 헤더를 이용한 버전 관리
    @GetMapping(value = "/users/{id}", produces = "application/vnd.company.appv1+json") // -> mime-type(헤더)를 이용한 버전 관리
    public MappingJacksonValue retrieveUserV2(@PathVariable int id) {
        User user = userDaoService.findOne(id);

        if (user == null) {
            throw new UserNotFoundException(String.format("ID[%s] not found", id));
        }

        // User -> User2
        UserV2 userV2 = new UserV2();
        BeanUtils.copyProperties(user, userV2);

        userV2.setGrade("VIP");

        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter
                .filterOutAllExcept("id", "name", "joinDate", "ssn"); // 해당 필드만 가져오도록

        FilterProvider filterProvider = new SimpleFilterProvider().addFilter("UserInfo", filter);

        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(userV2);
        mappingJacksonValue.setFilters(filterProvider);

        return mappingJacksonValue;
    }

}
