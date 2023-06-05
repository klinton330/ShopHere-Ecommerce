package com.shophere.admin.controller;

import java.util.HashSet;
import java.util.Set;

public class Test1 {
public static void main(String[] args) {
	
	Set<Integer> set=new HashSet<Integer>();
	set.add(1);
	set.add(2);
	set.add(3);
	set.add(4);
	set.add(5);
	System.out.println(set);
	
	set.remove(3);
	System.out.println(set);
}
}
