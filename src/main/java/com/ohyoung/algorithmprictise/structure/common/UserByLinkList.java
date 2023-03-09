package com.ohyoung.algorithmprictise.structure.common;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;

/**
 * @author ohyoung
 * @date 2023/2/3 18:23
 */
@Getter
@Slf4j
public class UserByLinkList {

    @Setter
    private String name;

    private final LinkedList<UserByLinkList> fans;

    private final LinkedList<UserByLinkList> focusList;

    public UserByLinkList() {
        fans = new LinkedList<>();
        focusList = new LinkedList<>();
    }

    public UserByLinkList(String name) {
        this.name = name;
        fans = new LinkedList<>();
        focusList = new LinkedList<>();
    }

    public boolean link(UserByLinkList target) {
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

    public boolean unlink(UserByLinkList target) {
        for (UserByLinkList userByLinkList : focusList) {
            if (userByLinkList.getName().equals(target.getName())) {
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
