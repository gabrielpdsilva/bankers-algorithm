package controller;

import java.util.Comparator;

public class Deadlock {
	
	static boolean[] impasse = {false, false, false, false, false};
	
	static int contador = 0;
	
	
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
	
	static int[] recursosDisponiveis = {0, 0, 0, 0};
	
	static int[] recursosExistentes = {7, 4, 2, 6};
	
	static int[] somatoriaRecursosAlocados = {4, 3, 1, 5};
	
	static boolean[] processoServido = {false, false, false, false, false};
	
	public static int[] pegaQtdRecursosEmUso(int[][] recursosAlocados){
		
		for(int processo = 0; processo < 5; processo++){
			for(int recurso = 0; recurso < 4; recurso++){
				somatoriaRecursosAlocados[recurso] += recursosAlocados[processo][recurso];
			}
		}
		
		return somatoriaRecursosAlocados;
	}
	
	public static boolean deadlock(){
		
		for(int processo = 0; processo < 5; processo++)
			if(!impasse[processo])
				return false;
		
		return true;
	}
	
	public static int[] calcularRecursosDisponiveis(int[] recursosExistentes, int[] somatoriaRecursosAlocados){
		
		for(int recurso = 0; recurso < 4; recurso++)
			recursosDisponiveis[recurso] = recursosExistentes[recurso] - somatoriaRecursosAlocados[recurso];
	
		return recursosDisponiveis;
	}
	
	//TODO
	public static void usarRecurso(int processo){
		System.out.println("[" + processo + "] esta usando recurso.");
		mostrarRecursosAlocados();
		mostrarRecursosDisponiveis();
		
		int[][] RecursosAlocadosAntesDaSoma = recursosAlocados;
		
		for(int recurso = 0; recurso < 4; recurso++){
			
			if(recursosDisponiveis[recurso] > 0){
				
				//recursos disponiveis estao sendo emprestados aos processos
				recursosAlocados[processo][recurso] += recursosDisponiveis[recurso];
				
				//recursos disponiveis diminuiram
				recursosDisponiveis[recurso] -= RecursosAlocadosAntesDaSoma[processo][recurso];
				
			}
			
		}
		mostrarRecursosAlocados();
		mostrarRecursosDisponiveis();
	}
	
	public static void devolverRecurso(int processo){
		for(int recurso = 0; recurso < 4; recurso++){
			
			//recursos antes usados agora serao devolvidos para os recursos disponiveis
			recursosDisponiveis[recurso] += recursosAlocados[processo][recurso];

			//zerando a quantidade de recursos que aquele processo precisa
			recursosAlocados[processo][recurso] = 0;
			
		}
		processoServido[processo] = true;
		System.out.println("[" + processo + "] devolveu o recurso . Ficou em " + (contador+1) + "o lugar.");
		contador++;
		mostrarRecursosDisponiveis();
	}
	
	public static void compararRecursos(int[][] recursosNecessarios, int[] recursosDisponiveis){
		
		
		//if(deadlock())
		//	return;
		
		for(int processo = 0; processo < 5; processo++){
			
			//se ja foi servido, va servir o proximo processo
			if(processoServido[processo])
				continue;
			
			for(int recurso = 0; recurso < 4; recurso++){
				
				//System.out.println("Recurso -> " + recurso);
				
				//System.out.println("recDisponiveis[recurso] -> " + recursosDisponiveis[recurso]);
				System.out.println();
				System.out.println("===========");
				System.out.println("Analisando o processo " + processo + ", recurso " + recurso);
				System.out.println("Se " + recursosDisponiveis[recurso] + " < "+ recursosNecessarios[processo][recurso] + "; ");
				System.out.println("Impasse do processo [" + processo + "] = " + impasse[processo]);
				
				if(recursosDisponiveis[recurso] < recursosNecessarios[processo][recurso]){
					impasse[processo] = true;
					System.out.println(processo + " t� inseguro.");
					break;
				}
				
				//chegou ate o fim dos recursos e nao houve impasses
				else if(recurso >= 3 && !impasse[processo]){
					
					System.out.println("Processo " + processo + " seguro!!!!!");
					usarRecurso(processo);
					devolverRecurso(processo);
				}
				
			}	
		}
	}
	
	public static void mostrarRecursosDisponiveis(){
		System.out.println("Rec. dispon�veis: ");
		for(int i = 0; i < 4; i++){
			System.out.print(recursosDisponiveis[i] + " ");
		}
		System.out.println();
	}
	
	public static void mostrarRecursosAlocados(){
		System.out.println("Rec. alocados: ");
		for(int i = 0; i < 5; i++){
			for(int j = 0; j < 4; j++){
				System.out.print(recursosAlocados[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println();
	}
	
	public static boolean processosServidos(){
		for(int processo = 0; processo < 5; processo++)
			if(!processoServido[processo])
				return false;
		
		return true;
	}
	
	public static void executar(){
		recursosDisponiveis = calcularRecursosDisponiveis(recursosExistentes, somatoriaRecursosAlocados);
		
		while(!processosServidos() || !deadlock()){
			compararRecursos(recursosNecessarios, recursosDisponiveis);
		}
	}
	
	public static void main(String[] args) {
		
		executar();
	}

}
