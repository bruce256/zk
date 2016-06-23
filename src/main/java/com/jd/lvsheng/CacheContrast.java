package com.jd.lvsheng;

import java.util.Random;

/**
 * Created by cdlvsheng on 2016/6/6.
 */
public class CacheContrast {
	int     ROW    = 1024 * 128;
	int     COL    = 16 * 32;
	int[][] matrix = new int[ROW][COL];
	Random  rand   = new Random();

	static String row = "addByRow";
	static String col = "addByCol";

	void init() {
		for (int i = 0; i < ROW; i++)
			for (int j = 0; j < COL; j++)
				matrix[i][j] = rand.nextInt(100);
	}

	public void addByRow() {
		long sum = 0;
		for (int i = 0; i < ROW; i++)
			for (int j = 0; j < COL; j++)
				sum += matrix[i][j];

		System.out.println(row + " result:\t" + sum);
	}

	public void addByCol() {
		long sum = 0;
		for (int i = 0; i < COL; i++)
			for (int j = 0; j < ROW; j++)
				sum += matrix[j][i];

		System.out.println(col + " result:\t" + sum);
	}

	public static void main(String[] args) {
		CacheContrast cc = new CacheContrast();
		cc.init();
		long start = System.currentTimeMillis();
		cc.addByRow();
		long end1      = System.currentTimeMillis();
		long costOfRow = end1 - start;
		System.out.println(row + " time cost :\t" + costOfRow);
		cc.addByCol();
		long end2      = System.currentTimeMillis();
		long costOfCol = end2 - end1;
		System.out.println(col + " time cost :\t" + costOfCol);
		System.out.println("performance ratio :\t" + (costOfCol * 1.0) / costOfRow);
	}
}
