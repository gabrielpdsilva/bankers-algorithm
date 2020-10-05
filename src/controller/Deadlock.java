package controller;

public class Deadlock {
	
	boolean[] impasse = {false, false, false, false};
	
	static int[][] recursosAlocados = {
										{3, 0, 1, 1},
										{1, 1, 1, 0},
										{1, 1, 0, 1},
										{0, 0, 0, 0},
													};
	
	static int[][] recursosNecessarios = {
										{1, 1, 0, 0},
										{0, 1, 1, 2},
										{3, 1, 0, 0},
										{2, 1, 1, 0},
													};
	
	static int[] recursosExistentes = {7, 4, 2, 6};
	
	static int[] somatoriaRecursosAlocados = {4, 3, 1, 5};
	
	public static int[] pegaQtdRecursosEmUso(int[][] recursosAlocados){
		
		for(int linha = 0; linha < 4; linha++){
			for(int coluna = 0; coluna < 4; coluna++){
				somatoriaRecursosAlocados[coluna] += recursosAlocados[linha][coluna];
			}
		}
		
		return somatoriaRecursosAlocados;
	}
	
	public boolean deadlock(boolean[] impasse){
		
		if(impasse[0] && impasse[1] && impasse[2] && impasse[3])
			return true;
		
		return false;
		
	}
	
	public static int[] calcularRecursosDisponiveis(int[] recursosExistentes, int[] somatoriaRecursosAlocados){
	
		int[] recursosDisponiveis = {0, 0, 0, 0};
		for(int coluna = 0; coluna < 4; coluna++)
			recursosDisponiveis[coluna] = recursosExistentes[coluna] - somatoriaRecursosAlocados[coluna];
	
		return recursosDisponiveis;
	}
	
	public void usarRecurso(){
		//TODO
	}
	
	public void devolverRecurso(){
		//TODO
		
	}
	
	public static void main(String[] args) {
		int[] recursosDisponiveis = calcularRecursosDisponiveis(recursosExistentes, somatoriaRecursosAlocados);
		
		for(int i = 0; i < 4; i++)
			System.out.print(recursosDisponiveis[i] + " ");
	}

}
