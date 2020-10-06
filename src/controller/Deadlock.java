package controller;

public class Deadlock {
	
	boolean[] impasse = {false, false, false, false};
	
	static int[][] recursosAlocados = {
										{0, 1, 0, 0},
										{2, 0, 1, 1},
										{0, 1, 0, 2},
										{2, 0, 0, 0},
										{0, 1, 0, 2},
													};
	
	static int[][] recursosNecessarios = {
										{1, 1, 0, 2},
										{0, 2, 0, 1},
										{1, 0, 2, 0},
										{0, 2, 0, 2},
										{2, 0, 1, 0}
													};
	
	static int[] recursosExistentes = {7, 4, 2, 6};
	
	static int[] somatoriaRecursosAlocados = {4, 3, 1, 5};
	
	static int[] recursosDisponiveis;
	
	public static int[] pegaQtdRecursosEmUso(int[][] recursosAlocados){
		
		for(int linha = 0; linha < 5; linha++){
			for(int coluna = 0; coluna < 4; coluna++){
				somatoriaRecursosAlocados[coluna] += recursosAlocados[linha][coluna];
			}
		}
		
		return somatoriaRecursosAlocados;
	}
	
	public boolean deadlock(){
		
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
	
	public void usarRecurso(int linha){
		for(int coluna = 0; coluna < 4; coluna++){

			//recursos disponiveis estao sendo emprestados aos processos
			recursosAlocados[linha][coluna] += recursosDisponiveis[coluna];
			
			//recursos disponiveis diminuiram
			recursosDisponiveis[coluna] -= recursosAlocados[linha][coluna];
		}
	}
	
	public void devolverRecurso(int linha){
		for(int coluna = 0; coluna < 4; coluna++){
			
			//recursos antes usados agora serao devolvidos para os recursos disponiveis
			recursosDisponiveis[coluna] += recursosAlocados[linha][coluna];
			
			//zerando a quantidade de recursos que aquele processo precisa
			recursosAlocados[linha][coluna] = 0;
		}
	}
	
	public void compararRecursos(int[] recursosNecessarios, int[] recursosDisponiveis){
		
		if(deadlock())
			return;
		
		for(int linha = 0; linha < 5; linha++){
			for(int coluna = 0; coluna < 4; coluna++){
						
				if(recursosDisponiveis[coluna] < recursosNecessarios[coluna]){
					impasse[linha] = true;
				} else {
					usarRecurso(linha);
					devolverRecurso(linha);
				}
				
			}	
		}
	}
	
	public static void main(String[] args) {
		
		recursosDisponiveis = calcularRecursosDisponiveis(recursosExistentes, somatoriaRecursosAlocados);
		
		for(int i = 0; i < 4; i++)
			System.out.print(recursosDisponiveis[i] + " ");
	}

}
