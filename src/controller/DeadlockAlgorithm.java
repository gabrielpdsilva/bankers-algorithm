package controller;

public class DeadlockAlgorithm {
	
	private int totalDeProcessos;
	private int totalDeRecursos;	
	private int posicaoDoProcesso = 0;
	
	private int[][] recursosAlocados;
	private int[][] recursosNecessarios;
	private int[] vezesExecutadasDoProcesso;
	private int[] recursosDisponiveis;
	private int[] recursosExistentes;
	private int[] somatoriaRecursosAlocados;
	private boolean[] processoServido;
	private boolean[] impasse;
	
	public DeadlockAlgorithm(int totalDeProcessos, int totalDeRecursos, int[][] recursosAlocados, int[][] recursosNecessarios, int[] recursosExistentes) {
		
		this.totalDeProcessos = totalDeProcessos;
		this.totalDeRecursos = totalDeRecursos;
		this.recursosAlocados = recursosAlocados;
		this.recursosNecessarios = recursosNecessarios;
		this.recursosExistentes = recursosExistentes;
		
		this.vezesExecutadasDoProcesso = new int[totalDeProcessos];
		this.processoServido = new boolean[totalDeProcessos];
		this.impasse = new boolean[totalDeProcessos];
		
		this.recursosDisponiveis = new int[totalDeRecursos];
		this.somatoriaRecursosAlocados = new int[totalDeRecursos];
		
		//definindo valor padrao pros vetores responsaveis pelos processos
		for(int i = 0; i < totalDeProcessos; i++){
			vezesExecutadasDoProcesso[i] = 0;
			processoServido[i] = false;
			impasse[i] = false;
		}
		
		//definindo valor padrao pros vetores responsaveis pelos recursos
		for(int i = 0; i < totalDeRecursos; i++){
			recursosDisponiveis[i] = 0;
			somatoriaRecursosAlocados[i] = 0;
		}
	}
	
	private int[] pegaQtdRecursosEmUso(int[][] recursosAlocados){
		
		for(int processo = 0; processo < this.totalDeProcessos; processo++){
			for(int recurso = 0; recurso < this.totalDeRecursos; recurso++){
				somatoriaRecursosAlocados[recurso] += recursosAlocados[processo][recurso];
			}
		}
		
		return somatoriaRecursosAlocados;
	}
	
	private boolean qtdValidaDeRecursos(){
		
		//se os recursos existentes nao satisfazerem
		//os recursos necessarios + recursos alocados
		for(int processo = 0; processo < this.totalDeProcessos; processo++)
			for(int recurso = 0; recurso < this.totalDeRecursos; recurso++)

				if(recursosExistentes[recurso] <
				  (recursosNecessarios[processo][recurso] + recursosAlocados[processo][recurso]))
					return false;
		
		return true;
		
	}
	
	//TODO
	private boolean deadlock(){
		
		//se processou mais do que o total de processos e ainda
		//assim nao conseguiu finalizar, deu deadlock
		for(int processo = 0; processo < this.totalDeProcessos; processo++)
			if(vezesExecutadasDoProcesso[processo] >= this.totalDeProcessos)
				return true;
		
		//se nao conseguiu rodar nenhum processo
		for(int processo = 0; processo < this.totalDeProcessos; processo++)
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
		for(int i = 0; i < this.totalDeProcessos; i++){
			for(int j = 0; j < this.totalDeRecursos; j++){
				novaMatriz[i][j] = antigaMatriz[i][j];
			}
		}
		return novaMatriz;
	}
	
	public void usarRecurso(int processo){
		System.out.println("Processo[" + processo + "] esta usando recurso.");
		
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
		System.out.println("Processo[" + processo + "] devolveu o recurso. Ficou em " + (this.posicaoDoProcesso+1) + "o lugar.");
		this.posicaoDoProcesso++;
		mostrarRecursosDisponiveis();
		
		//serviu o processo, esque�a os antigos
		//impasses e tente novamente, talvez tenha
		//conseguido novos recursos.
		resetarImpasses();
	}
	
	private void mostrarAndamentoDaComparacao(int processo, int recurso){
		System.out.println();
		System.out.println("===========");
		System.out.println("Analisando o processo " + processo + ", recurso " + recurso);
		System.out.println("Se " + recursosDisponiveis[recurso] + " < "+ recursosNecessarios[processo][recurso] + "; ");
		System.out.println("Impasse do processo [" + processo + "] = " + impasse[processo]);
	}
	
	private void compararRecursos(int[][] recursosNecessarios, int[] recursosDisponiveis){
		
		for(int processo = 0; processo < this.totalDeProcessos; processo++){
			
			//se ja foi servido, va servir o proximo processo
			if(processoServido[processo])
				continue;
			
			//passou pelo processo
			vezesExecutadasDoProcesso[processo]++;
			
			System.out.println("Executou " + vezesExecutadasDoProcesso[processo] + "x.");
			
			for(int recurso = 0; recurso < this.totalDeRecursos; recurso++){
				
				mostrarAndamentoDaComparacao(processo, recurso);
				
				if(recursosDisponiveis[recurso] < recursosNecessarios[processo][recurso]){
					impasse[processo] = true;
					System.out.println(processo + " t� inseguro.");
					break;
				}
				
				//chegou ate o fim dos recursos e nao houve impasses
				else if((recurso >= (this.totalDeRecursos-1)) && (!impasse[processo])){
					
					System.out.println("O processo " + processo + " � seguro!");
					usarRecurso(processo);
					devolverRecurso(processo);
				}
				
			}	
		}
	}
	
	private void mostrarRecursosDisponiveis(){
		System.out.println("================");
		System.out.println("Rec. dispon�veis: ");
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
		
		if(!qtdValidaDeRecursos()){
			System.out.println("O total de recursos existentes n�o � maior que o total de recursos necess�rios, imposs�vel prosseguir.");
			return;
		}
		//recursosDisponiveis[0] = 1;
		//recursosDisponiveis[1] = 1;
		//recursosDisponiveis[2] = 0;
		//recursosDisponiveis[3] = 2;
		
		while(!processosServidos() && !deadlock()){
			compararRecursos(recursosNecessarios, recursosDisponiveis);
		}
		
		//int cont = 1;
		//while(cont < 40){
		//	compararRecursos(recursosNecessarios, recursosDisponiveis);
		//	cont++;
	}

}
