package view;

import controller.DeadlockAlgorithm;

public class Main {
	
	public static void main(String[] args) {
		
		int qtdProcessos, qtdRecursos;
		qtdProcessos = 5;
		qtdRecursos = 4;
		
		int[][] recursosAlocados = {
									{0, 1, 0, 0},
									{2, 0, 1, 1},
									{0, 1, 0, 2},
									{2, 0, 0, 0},
									{0, 1, 0, 2},
												};

		int[][] recursosNecessarios = {
										{1, 1, 0, 2},
										{0, 2, 0, 1},
										{1, 0, 2, 0},
										{0, 2, 0, 2},
										{2, 0, 1, 0}
													};
		
		DeadlockAlgorithm deadlock = new DeadlockAlgorithm(qtdProcessos, qtdRecursos, recursosAlocados, recursosNecessarios);
		deadlock.mostrarRecursosAlocados();
		deadlock.mostrarRecursosNecessarios();
		//deadlock.realizarAnalise();
		
	}

}
