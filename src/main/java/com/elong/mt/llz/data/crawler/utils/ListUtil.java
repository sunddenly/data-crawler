package com.elong.mt.llz.data.crawler.utils;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import java.util.List;

public class ListUtil {

    public static <T> List<List<T>> partitionGroup(List<T> list, int groupSize) {
        Preconditions.checkArgument(!org.springframework.util.CollectionUtils.isEmpty(list));
        Preconditions.checkArgument(groupSize > 0);

        List<List<T>> listGroup = Lists.newArrayList();
        int listSize = list.size();
        if (listSize <= groupSize) {
            for (T obj : list) {
                List<T> childList = Lists.newArrayList();
                childList.add(obj);
                listGroup.add(childList);
            }
            return listGroup;
        }
        int remainder = listSize % groupSize;
        int childSize = listSize / groupSize;
        if (remainder == 0) {
            listGroup.addAll(Lists.partition(list, childSize));
        } else {
            int index = (childSize + 1) * remainder;
            listGroup.addAll(Lists.partition(list.subList(0, index), childSize + 1));
            listGroup.addAll(Lists.partition(list.subList(index, listSize), childSize));
        }
        return listGroup;
    }
}