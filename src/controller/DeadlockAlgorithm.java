package controller;

public class DeadlockAlgorithm {
	
	public DeadlockAlgorithm(int totalDeProcessos, int totalDeRecursos, int[][] recursosAlocados, int[][] recursosNecessarios) {
		
		this.totalDeProcessos = totalDeProcessos;
		this.totalDeRecursos = totalDeRecursos;
		this.recursosAlocados = recursosAlocados;
		this.recursosNecessarios = recursosNecessarios;
	}
	
	private int totalDeProcessos;
	private int totalDeRecursos;
	
	private boolean[] impasse = {false, false, false, false, false};
	
	private int contadorDeRecursos = 0;
	
	private int[][] recursosAlocados = {
										{0, 1, 0, 0},
										{2, 0, 1, 1},
										{0, 1, 0, 2},
										{2, 0, 0, 0},
										{0, 1, 0, 2},
													};
	
	private int[][] recursosNecessarios = {
										{1, 1, 0, 2},
										{0, 2, 0, 1},
										{1, 0, 2, 0},
										{0, 2, 0, 2},
										{2, 0, 1, 0}
													};
	
	private int[] vezesExecutadasDoProcesso = {0, 0, 0, 0, 0};
	
	private int[] recursosDisponiveis = {0, 0, 0, 0};
	
	private int[] recursosExistentes = {7, 4, 2, 6};
	
	private int[] somatoriaRecursosAlocados = {0, 0, 0, 0};
	
	static boolean[] processoServido = {false, false, false, false, false};
	
	private int[] pegaQtdRecursosEmUso(int[][] recursosAlocados){
		
		for(int processo = 0; processo < 5; processo++){
			for(int recurso = 0; recurso < 4; recurso++){
				somatoriaRecursosAlocados[recurso] += recursosAlocados[processo][recurso];
			}
		}
		
		return somatoriaRecursosAlocados;
	}
	
	//TODO
	private boolean deadlock(){
		
		//se processou mais do que o total de processos e ainda
		//assim nao conseguiu finalizar, deu deadlock
		for(int processo = 0; processo < this.totalDeProcessos; processo++)
			if(vezesExecutadasDoProcesso[processo] >= this.totalDeProcessos)
				return true;
		
		//se os recursos existentes nao satisfazerem
		//os recursos necessarios + recursos alocados
		for(int processo = 0; processo < this.totalDeProcessos; processo++)
			for(int recurso = 0; recurso < this.totalDeRecursos; recurso++)
				
				if(recursosExistentes[recurso] <
				  (recursosNecessarios[processo][recurso] + recursosAlocados[processo][recurso]))
					return true;
		
		//se nao conseguiu rodar nenhum processo
		for(int processo = 0; processo < 5; processo++)
			if(!impasse[processo])
				return false;
		
		return true;
	}
	
	private int[] calcularRecursosDisponiveis(int[] recursosExistentes, int[] somatoriaRecursosAlocados){
		
		for(int recurso = 0; recurso < this.totalDeRecursos; recurso++)
			recursosDisponiveis[recurso] = recursosExistentes[recurso] - somatoriaRecursosAlocados[recurso];
	
		return recursosDisponiveis;
	}
	
	private int[][] criarCopiaDaMatriz(int[][] antigaMatriz){
		int[][] novaMatriz = new int[totalDeProcessos][totalDeRecursos];
		for(int i = 0; i < 5; i++){
			for(int j = 0; j < 4; j++){
				novaMatriz[i][j] = antigaMatriz[i][j];
			}
		}
		return novaMatriz;
	}
	
	public void usarRecurso(int processo){
		System.out.println("Processo[" + processo + "] esta usando recurso.");
		mostrarRecursosAlocados();
		mostrarRecursosDisponiveis();
		
		int[][] recursosAlocadosAntesDaSoma = criarCopiaDaMatriz(recursosAlocados);
		
		for(int recurso = 0; recurso < this.totalDeRecursos; recurso++){
			
			//se tem recursos pra emprestar
			if(recursosDisponiveis[recurso] > 0){
				
				//recursos disponiveis estao sendo emprestados aos processos
				recursosAlocados[processo][recurso] += recursosDisponiveis[recurso];
				
				//recursos disponiveis diminuiram
				recursosDisponiveis[recurso] -= recursosAlocadosAntesDaSoma[processo][recurso];
			}
			
		}
		mostrarRecursosAlocados();
		mostrarRecursosDisponiveis();
	}
	
	private void resetarImpasses(){
		for(int processo = 0; processo < this.totalDeProcessos; processo++)
			impasse[processo] = false;
	}
	
	private void devolverRecurso(int processo){
		for(int recurso = 0; recurso < this.totalDeRecursos; recurso++){
			
			//recursos antes usados agora serao
			//devolvidos para os recursos disponiveis
			recursosDisponiveis[recurso] = recursosAlocados[processo][recurso];

			//zerando a quantidade de recursos
			//que aquele processo precisa
			recursosAlocados[processo][recurso] = 0;
			
		}
		processoServido[processo] = true;
		System.out.println("Processo[" + processo + "] devolveu o recurso. Ficou em " + (this.contadorDeRecursos+1) + "o lugar.");
		this.contadorDeRecursos++;
		mostrarRecursosDisponiveis();
		
		//serviu o processo, esqueça os antigos
		//impasses e tente novamente, talvez tenha
		//conseguido novos recursos.
		resetarImpasses();
	}
	
	private void compararRecursos(int[][] recursosNecessarios, int[] recursosDisponiveis){
		
		for(int processo = 0; processo < 5; processo++){
			
			//se ja foi servido, va servir o proximo processo
			if(processoServido[processo])
				continue;
			
			//passou pelo processo
			vezesExecutadasDoProcesso[processo]++;
			System.out.println("Executou " + vezesExecutadasDoProcesso[processo] + "x.");
			
			for(int recurso = 0; recurso < 4; recurso++){
				
				System.out.println();
				System.out.println("===========");
				System.out.println("Analisando o processo " + processo + ", recurso " + recurso);
				System.out.println("Se " + recursosDisponiveis[recurso] + " < "+ recursosNecessarios[processo][recurso] + "; ");
				System.out.println("Impasse do processo [" + processo + "] = " + impasse[processo]);
				
				if(recursosDisponiveis[recurso] < recursosNecessarios[processo][recurso]){
					impasse[processo] = true;
					System.out.println(processo + " tá inseguro.");
					break;
				}
				
				//chegou ate o fim dos recursos e nao houve impasses
				else if((recurso >= (this.totalDeRecursos-1)) && (!impasse[processo])){
					
					System.out.println("O processo " + processo + " é seguro!");
					usarRecurso(processo);
					devolverRecurso(processo);
				}
				
			}	
		}
	}
	
	private void mostrarRecursosDisponiveis(){
		System.out.println("Rec. disponíveis: ");
		for(int recurso = 0; recurso < this.totalDeRecursos; recurso++){
			System.out.print(recursosDisponiveis[recurso] + " ");
		}
		System.out.println();
	}
	
	public void mostrarRecursosAlocados(){
		System.out.println("================");
		System.out.println("Rec. alocados: ");
		for(int processo = 0; processo < this.totalDeProcessos; processo++){
			for(int recurso = 0; recurso < this.totalDeRecursos; recurso++){
				System.out.print(recursosAlocados[processo][recurso] + " ");
			}
			System.out.println();
		}
		System.out.println();
	}
	
	public void mostrarRecursosNecessarios(){
		System.out.println("================");
		System.out.println("Rec. necessarios: ");
		for(int processo = 0; processo < this.totalDeProcessos; processo++){
			for(int recurso = 0; recurso < this.totalDeRecursos; recurso++){
				System.out.print(recursosNecessarios[processo][recurso] + " ");
			}
			System.out.println();
		}
		System.out.println();
		
	}
	
	private boolean processosServidos(){
		for(int processo = 0; processo < this.totalDeProcessos; processo++)
			if(!processoServido[processo])
				return false;
		
		return true;
	}
	
	//TODO
	public void realizarAnalise(){
		this.somatoriaRecursosAlocados = pegaQtdRecursosEmUso(recursosAlocados);
		this.recursosDisponiveis = calcularRecursosDisponiveis(recursosExistentes, somatoriaRecursosAlocados);
		
		//recursosDisponiveis[0] = 1;
		//recursosDisponiveis[1] = 1;
		//recursosDisponiveis[2] = 0;
		//recursosDisponiveis[3] = 2;
		while(!processosServidos() && !deadlock()){
			
		//int cont = 1;
		//while(cont < 40){
			compararRecursos(recursosNecessarios, recursosDisponiveis);
		//	cont++;
		}
	}

}
