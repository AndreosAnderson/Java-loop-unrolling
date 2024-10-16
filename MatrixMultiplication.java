package org.example;

public class MatrixMultiplication {

	public double[][] multiplyBlock(double[][] A, double[][] B, double[][] C, int N, int blockSize, int rowBlock, int colBlock, int kBlock) {
		for (int i = rowBlock; i < Math.min(rowBlock + blockSize, N); i++) {
			for (int j = colBlock; j < Math.min(colBlock + blockSize, N); j++) {
				double sum = 0;
				for (int k = kBlock; k < Math.min(kBlock + blockSize, N); k++) {
					sum += A[i][k] * B[k][j];
				}
				C[i][j] += sum;
			}
		}
		return C;
	}

	public double[][] execute(double[][] A, double[][] B, double[][] C, int N, int blockSize) {
		if (A[0].length == B.length) {
			for (int i = 0; i < N; i += blockSize) {
				for (int j = 0; j < N; j += blockSize) {
					for (int k = 0; k < N; k += blockSize) {
						C = multiplyBlock(A, B, C, N, blockSize, i, j, k);
					}
				}
			}
			return C;
		} else {
			throw new IllegalArgumentException("Matrices cannot be multiplied due to incompatible dimensions.");
		}
	}
}
