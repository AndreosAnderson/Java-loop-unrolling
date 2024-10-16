package org.example;

import org.openjdk.jmh.annotations.*;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class MatrixMultiplicationBenchmarking {

	@State(Scope.Thread)
	public static class Operands {
		private final int N = 128;
		private final int blockSize = 64;
		private final double[][] A = new double[N][N];
		private final double[][] B = new double[N][N];
		private final double[][] C = new double[N][N];

		@Setup
		public void setup() {
			Random random = new Random();
			for (int i = 0; i < N; i++) {
				for (int j = 0; j < N; j++) {
					A[i][j] = random.nextDouble();
					B[i][j] = random.nextDouble();
				}
			}
		}
	}

	private double getProcessCpuLoad() {
		OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
		if (osBean instanceof com.sun.management.OperatingSystemMXBean) {
			return ((com.sun.management.OperatingSystemMXBean) osBean).getProcessCpuLoad() * 100;
		}
		return -1;
	}

	private long getUsedMemory() {
		Runtime runtime = Runtime.getRuntime();
		return runtime.totalMemory() - runtime.freeMemory();
	}

	@Benchmark
	@Warmup(iterations = 1, time = 1, timeUnit = TimeUnit.SECONDS)
	@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
	public void multiplication(Operands operands) {
		double cpuBefore = getProcessCpuLoad();
		long memoryBefore = getUsedMemory();

		new MatrixMultiplication().execute(operands.A, operands.B, operands.C, operands.N, operands.blockSize);

		double cpuAfter = getProcessCpuLoad();
		long memoryAfter = getUsedMemory();

		System.out.println("CPU Usage before: " + cpuBefore + "%");
		System.out.println("CPU Usage after: " + cpuAfter + "%");
		System.out.println("Memory Usage before: " + memoryBefore / (1024 * 1024) + " MB");
		System.out.println("Memory Usage after: " + memoryAfter / (1024 * 1024) + " MB");
	}
}
