package com.LA;

import java.util.Random;

public class RandomDemo {
	public static void main(String[] args) {
		Random r = new Random();
		System.out.println(r.nextLong()); 
		System.out.println(r.nextInt(10)); 
	}
}
