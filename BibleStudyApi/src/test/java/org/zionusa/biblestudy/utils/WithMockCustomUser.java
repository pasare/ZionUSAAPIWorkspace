package org.zionusa.biblestudy.utils;

public @interface WithMockCustomUser {

    String username() default "tester@test.com";

    String name() default "tester";

    String access();

    String role() default "Member";

    //List<String> applicationRoles default [];

    String churchId() default "1";

    String groupId() default "1";

    String teamId() default "1";

    String id() default "0";

}
