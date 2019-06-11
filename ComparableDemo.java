package com.LA;

import java.util.Arrays;

public class ComparableDemo {

	public static void main(String[] args) {
		int [] nums = {324,4567,78,7689,879,123,345,546};
		Arrays.sort(nums);
		System.out.println(Arrays.toString(nums));
		
		Cat[] cats = {new Cat("fenfen",1),new Cat("feifei",4),new Cat("tom",2)};
		Arrays.sort(cats);
		System.out.println(Arrays.toString(cats));
		Dog[] dogs = {new Dog("fenfen",1),new Dog("feifei",4),new Dog("tom",2)};
		Arrays.sort(dogs,new DogComparator());
		System.out.println(Arrays.toString(dogs));
	
	}

}
