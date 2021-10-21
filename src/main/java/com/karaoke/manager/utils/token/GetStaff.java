package com.karaoke.manager.utils.token;

import com.karaoke.manager.entity.Staff;

@FunctionalInterface
public interface GetStaff {
    Staff get(String username);
}
