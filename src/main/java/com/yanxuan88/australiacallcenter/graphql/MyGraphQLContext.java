package com.yanxuan88.australiacallcenter.graphql;

import com.yanxuan88.australiacallcenter.common.UserLoginInfo;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * graphql context entity
 *
 * @author co
 * @since 2023-12-26 16:58:40
 */
@Data
@Accessors(chain = true)
public class MyGraphQLContext {
    private String loginCaptchaKey;
    private UserLoginInfo user;
}
