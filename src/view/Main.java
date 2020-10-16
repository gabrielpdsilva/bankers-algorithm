package view;

import controller.DeadlockAlgorithm;

public class Main {
	
	public static void main(String[] args) {
		
		int qtdProcessos = 5;
		int qtdRecursos = 4;
		
		int[][] recursosAlocados = {
									{3, 0, 1, 1},
									{0, 1, 0, 0},
									{1, 1, 1, 0},
									{1, 1, 0, 1},
									{0, 0, 0, 0},
												};

		int[][] recursosNecessarios = {
										{1, 1, 0, 0},
										{0, 1, 1, 2},
										{3, 1, 0, 0},
										{0, 0, 1, 0},
										{2, 1, 1, 0}
													};

		int[] recursosExistentes = {6, 3, 4, 2};
		
		DeadlockAlgorithm deadlock = new DeadlockAlgorithm(qtdProcessos, qtdRecursos, recursosAlocados, recursosNecessarios, recursosExistentes);
		deadlock.realizarAnalise();
		
	}

}
