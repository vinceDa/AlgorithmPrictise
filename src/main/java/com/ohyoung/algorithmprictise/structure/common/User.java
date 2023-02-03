package com.ohyoung.algorithmprictise.structure.common;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;

/**
 * author: ohyoung
 * date: 2023/2/318:23
 */
@Data
@Slf4j
public class User {

    private String name;

    private LinkedList<User> fans;

    private LinkedList<User> focusList;

    public User() {
        fans = new LinkedList<>();
        focusList = new LinkedList<>();
    }

    public boolean link(User target) {
        boolean targetExist = focusList.stream().anyMatch(u -> u.getName().equals(target.getName()));
        if (targetExist) {
            log.error("target is focused: {}", target);
            return false;
        }
        focusList.addLast(target);
        // 被关注的粉丝列表+1
        target.getFans().addLast(this);
        return true;
    }

    public boolean unlink(User target) {
        for (User user : focusList) {
            if (user.getName().equals(target.getName())) {
                focusList.remove(target);
                // target粉丝列表-1
                target.fans.remove(this);
                return true;
            }
        }
        log.error("target is not exist: {}", target);
        return false;
    }
}
