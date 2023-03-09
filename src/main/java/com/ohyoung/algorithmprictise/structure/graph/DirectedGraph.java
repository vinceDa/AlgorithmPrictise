package com.ohyoung.algorithmprictise.structure.graph;

import com.ohyoung.algorithmprictise.structure.common.UserByLinkList;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * @author ohyoung
 * @date 2023/2/3 17:03
 * 有向图
 */
@Slf4j
@Data
public class DirectedGraph {

    private Map<String, UserByLinkList> directedGraphData;

    public DirectedGraph() {
        this.directedGraphData = new TreeMap<>();
    }

    public DirectedGraph(Map<String, UserByLinkList> directedGraphData) {
        this.directedGraphData = directedGraphData;
    }

    /**
     * 判断用户 A 是否关注了用户 B；
     * 判断用户 A 是否是用户 B 的粉丝；
     * 用户 A 关注用户 B；
     * 用户 A 取消关注用户 B；
     * 根据用户名称的首字母排序，分页获取用户的粉丝列表；
     * 根据用户名称的首字母排序，分页获取用户的关注列表。
     */

    public boolean addUser(UserByLinkList userByLinkList) {
        directedGraphData.put(userByLinkList.getName(), userByLinkList);
        return true;
    }


    /**
     * current是否被target关注
     */
    public boolean isFollow(UserByLinkList current, UserByLinkList target) {
        UserByLinkList userByLinkList = directedGraphData.get(current.getName());
        if (!isExist(userByLinkList)) {
            log.error("current user is not exist");
            return false;
        }
        return userByLinkList.getFans().stream().anyMatch(f -> target.getName().equals(f.getName()));
    }

    /**
     * current是否关注了target
     */
    public boolean isFans(UserByLinkList current, UserByLinkList target) {
        UserByLinkList userByLinkList = directedGraphData.get(current.getName());
        if (!isExist(userByLinkList)) {
            log.error("current user is not exist");
            return false;
        }
        return userByLinkList.getFocusList().stream().anyMatch(f -> target.getName().equals(f.getName()));
    }

    /**
     * current关注target
     */
    public boolean focus(UserByLinkList current, UserByLinkList target) {
        return current.link(target);
    }

    /**
     * current取关target
     */
    public boolean unFocus(UserByLinkList current, UserByLinkList target) {
        return current.unlink(target);
    }

    public boolean isExist(UserByLinkList userByLinkList) {
        return Objects.nonNull(userByLinkList);
    }

    public static void main(String[] args) {
        DirectedGraph directedGraph = new DirectedGraph();
        UserByLinkList a = new UserByLinkList("a");
        directedGraph.addUser(a);
        UserByLinkList b = new UserByLinkList("b");
        directedGraph.addUser(b);
        UserByLinkList c = new UserByLinkList("c");
        directedGraph.addUser(c);
        UserByLinkList d = new UserByLinkList("d");
        directedGraph.addUser(d);
        a.link(b);
        b.link(c);
        c.link(d);
        d.link(a);
        System.out.println(directedGraph.isFans(a, b));
        System.out.println(directedGraph.isFans(b, a));
        System.out.println(directedGraph.isFollow(c, d));
        System.out.println(directedGraph.isFollow(d, c));

    }
}
