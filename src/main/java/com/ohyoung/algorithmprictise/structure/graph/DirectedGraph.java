package com.ohyoung.algorithmprictise.structure.graph;

import com.ohyoung.algorithmprictise.structure.common.User;
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

    private Map<String, User> directedGraphData;

    public DirectedGraph() {
        this.directedGraphData = new TreeMap<>();
    }

    public DirectedGraph(Map<String, User> directedGraphData) {
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

    public boolean addUser(User user) {
        directedGraphData.put(user.getName(), user);
        return true;
    }


    /**
     * current是否被target关注
     */
    public boolean isFollow(User current, User target) {
        User user = directedGraphData.get(current.getName());
        if (!isExist(user)) {
            log.error("current user is not exist");
            return false;
        }
        return user.getFans().stream().anyMatch(f -> target.getName().equals(f.getName()));
    }

    /**
     * current是否关注了target
     */
    public boolean isFans(User current, User target) {
        User user = directedGraphData.get(current.getName());
        if (!isExist(user)) {
            log.error("current user is not exist");
            return false;
        }
        return user.getFocusList().stream().anyMatch(f -> target.getName().equals(f.getName()));
    }

    /**
     * current关注target
     */
    public boolean focus(User current, User target) {
        return current.link(target);
    }

    /**
     * current取关target
     */
    public boolean unFocus(User current, User target) {
        return current.unlink(target);
    }

    public boolean isExist(User user) {
        return Objects.nonNull(user);
    }

    public static void main(String[] args) {
        DirectedGraph directedGraph = new DirectedGraph();
        User a = new User("a");
        directedGraph.addUser(a);
        User b = new User("b");
        directedGraph.addUser(b);
        User c = new User("c");
        directedGraph.addUser(c);
        User d = new User("d");
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
