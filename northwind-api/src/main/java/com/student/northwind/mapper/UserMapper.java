package com.student.northwind.mapper;

import com.student.northwind.domain.Authority;
import com.student.northwind.domain.User;
import com.student.northwind.model.request.AdminUserReq;
import com.student.northwind.model.response.UserLiteResp;
import com.student.northwind.model.response.UserResp;
import org.mapstruct.Mapper;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper
public interface UserMapper {
    List<UserLiteResp> usersToUserReqs(List<User> users);

    UserLiteResp userToUserLiteReq(User user);

    List<AdminUserReq> usersToAdminUserReqs(List<User> users);

    AdminUserReq userToAdminUserReq(User user);

    List<User> adminUserReqsToUsers(List<AdminUserReq> userDTOs);

    User adminUserReqToUser(AdminUserReq adminUserReq);

    UserResp userToUserResp(User user);

    default Set<String> authoritiesToStrings(Set<Authority> authorities) {
        return authorities.stream().map(Authority::getName).collect(Collectors.toSet());
    }

    default Set<Authority> authoritiesFromStrings(Set<String> authoritiesAsString) {
        Set<Authority> authorities = new HashSet<>();

        if (authoritiesAsString != null) {
            authorities =
                    authoritiesAsString
                            .stream()
                            .map(string -> {
                                Authority auth = new Authority();
                                auth.setName(string);
                                return auth;
                            })
                            .collect(Collectors.toSet());
        }

        return authorities;
    }
}
